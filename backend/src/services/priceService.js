const cache = require('../utils/cache');
const logger = require('../utils/logger');
const normalizer = require('./normalizer');
const enamConnector = require('../connectors/enamConnector');
const agmarkConnector = require('../connectors/agmarkConnector');
const datagovConnector = require('../connectors/datagovConnector');
const demoData = require('../data/demo.json');

/**
 * Price service for fetching and aggregating market prices
 */
class PriceService {
  constructor() {
    this.connectors = [enamConnector, agmarkConnector, datagovConnector];
  }

  /**
   * Get local market prices for a specific state and crop
   * @param {string} stateId - State ID
   * @param {string} cropId - Crop ID
   * @returns {Promise<Object>} Local market prices
   */
  async getLocalPrices(stateId, cropId) {
    try {
      // Check cache first
      const cacheKey = `local_prices_${stateId}_${cropId}`;
      const cached = await cache.get(cacheKey);
      
      if (cached && !process.env.DEMO_MODE) {
        logger.debug(`Returning cached local prices for ${cropId} in ${stateId}`);
        return cached;
      }

      // Normalize state and crop names
      const normalizedState = normalizer.normalizeState(stateId);
      const normalizedCrop = normalizer.normalizeCrop(cropId);

      if (normalizedState.error || normalizedCrop.error) {
        return {
          error: `Invalid parameters: ${normalizedState.error || normalizedCrop.error}`,
          fallback: true
        };
      }

      let priceData = null;

      if (process.env.DEMO_MODE === 'true') {
        // Use demo data
        priceData = this.getDemoLocalPrices(normalizedState.name, normalizedCrop.name);
      } else {
        // Fetch from live APIs
        priceData = await this.fetchLiveLocalPrices(normalizedState.name, normalizedCrop.name);
      }

      // Cache the result
      if (priceData && !priceData.error) {
        await cache.set(cacheKey, priceData, 3600); // Cache for 1 hour
      }

      return priceData;

    } catch (error) {
      logger.error('Error getting local prices:', error);
      return {
        error: 'Failed to fetch local prices',
        fallback: true
      };
    }
  }

  /**
   * Get government MSP for a specific crop
   * @param {string} cropId - Crop ID
   * @returns {Promise<Object>} Government MSP data
   */
  async getGovernmentPrices(cropId) {
    try {
      // Check cache first
      const cacheKey = `govt_prices_${cropId}`;
      const cached = await cache.get(cacheKey);
      
      if (cached && !process.env.DEMO_MODE) {
        logger.debug(`Returning cached government prices for ${cropId}`);
        return cached;
      }

      // Normalize crop name
      const normalizedCrop = normalizer.normalizeCrop(cropId);

      if (normalizedCrop.error) {
        return {
          error: `Invalid crop: ${normalizedCrop.error}`,
          fallback: true
        };
      }

      let priceData = null;

      if (process.env.DEMO_MODE === 'true') {
        // Use demo data
        priceData = this.getDemoGovernmentPrices(normalizedCrop.name);
      } else {
        // Fetch from live APIs
        priceData = await this.fetchLiveGovernmentPrices(normalizedCrop.name);
      }

      // Cache the result
      if (priceData && !priceData.error) {
        await cache.set(cacheKey, priceData, 3600); // Cache for 1 hour
      }

      return priceData;

    } catch (error) {
      logger.error('Error getting government prices:', error);
      return {
        error: 'Failed to fetch government prices',
        fallback: true
      };
    }
  }

  /**
   * Get comparison data (local + government + differences)
   * @param {string} stateId - State ID
   * @param {string} cropId - Crop ID
   * @returns {Promise<Object>} Comparison data
   */
  async getComparison(stateId, cropId) {
    try {
      const [localPrices, govtPrices] = await Promise.all([
        this.getLocalPrices(stateId, cropId),
        this.getGovernmentPrices(cropId)
      ]);

      const comparison = {
        state: localPrices.state || stateId,
        crop: localPrices.crop || cropId,
        local: localPrices,
        government: govtPrices,
        comparison: null,
        lastUpdated: Date.now()
      };

      // Calculate differences if both prices are available
      if (localPrices.modal && govtPrices.msp && !localPrices.error && !govtPrices.error) {
        const absoluteDiff = localPrices.modal - govtPrices.msp;
        const percentageDiff = ((localPrices.modal - govtPrices.msp) / govtPrices.msp) * 100;

        comparison.comparison = {
          absoluteDifference: absoluteDiff,
          percentageDifference: percentageDiff,
          localHigher: absoluteDiff > 0,
          governmentHigher: absoluteDiff < 0
        };
      }

      return comparison;

    } catch (error) {
      logger.error('Error getting comparison data:', error);
      return {
        error: 'Failed to fetch comparison data',
        fallback: true
      };
    }
  }

  /**
   * Fetch live local prices from all connectors
   * @param {string} state - State name
   * @param {string} crop - Crop name
   * @returns {Promise<Object>} Aggregated local prices
   */
  async fetchLiveLocalPrices(state, crop) {
    const priceSources = [];

    for (const connector of this.connectors) {
      if (connector.isConfigured()) {
        try {
          const prices = await connector.fetchPrices(state, crop);
          const normalized = normalizer.normalizePriceData(prices, connector.constructor.name);
          priceSources.push(normalized);

          // Rate limiting delay
          await new Promise(resolve => setTimeout(resolve, parseInt(process.env.RATE_LIMIT_DELAY_MS || '1000')));
        } catch (error) {
          logger.error(`Error fetching from ${connector.constructor.name}:`, error);
        }
      }
    }

    if (priceSources.length === 0) {
      return {
        error: 'No configured price sources available',
        fallback: true
      };
    }

    return normalizer.mergePriceSources(priceSources);
  }

  /**
   * Fetch live government prices
   * @param {string} crop - Crop name
   * @returns {Promise<Object>} Government prices
   */
  async fetchLiveGovernmentPrices(crop) {
    try {
      if (datagovConnector.isConfigured()) {
        const msp = await datagovConnector.fetchMSP(crop);
        return normalizer.normalizePriceData(msp, 'Data.gov.in');
      } else {
        return {
          error: 'Data.gov.in connector not configured',
          fallback: true
        };
      }
    } catch (error) {
      logger.error('Error fetching government prices:', error);
      return {
        error: 'Failed to fetch government prices',
        fallback: true
      };
    }
  }

  /**
   * Get demo local prices
   * @param {string} state - State name
   * @param {string} crop - Crop name
   * @returns {Object} Demo local prices
   */
  getDemoLocalPrices(state, crop) {
    const key = `${state.toLowerCase()}_${crop.toLowerCase()}`;
    const demoPrice = demoData.prices.local[key];
    
    if (demoPrice) {
      return {
        ...demoPrice,
        source: 'Demo Data',
        fallback: true
      };
    }

    // Return default demo data
    return {
      state: state,
      crop: crop,
      min: 10.0,
      modal: 12.5,
      max: 15.0,
      unit: 'kg',
      lastUpdated: Date.now(),
      source: 'Demo Data',
      fallback: true,
      history: [
        { date: '2024-01-01', modal: 11.0 },
        { date: '2024-01-02', modal: 11.5 },
        { date: '2024-01-03', modal: 12.0 },
        { date: '2024-01-04', modal: 12.5 },
        { date: '2024-01-05', modal: 13.0 }
      ]
    };
  }

  /**
   * Get demo government prices
   * @param {string} crop - Crop name
   * @returns {Object} Demo government prices
   */
  getDemoGovernmentPrices(crop) {
    const demoPrice = demoData.prices.government[crop.toLowerCase()];
    
    if (demoPrice) {
      return {
        ...demoPrice,
        source: 'Demo Data',
        fallback: true
      };
    }

    // Return default demo data
    return {
      crop: crop,
      msp: 10.0,
      unit: 'kg',
      lastUpdated: Date.now(),
      source: 'Demo Data',
      fallback: true
    };
  }

  /**
   * Refresh all cached prices
   * @returns {Promise<Object>} Refresh result
   */
  async refreshAllPrices() {
    try {
      logger.info('Starting price refresh...');
      
      const states = normalizer.getAllStates();
      const crops = normalizer.getCropsForState('all');
      
      let refreshed = 0;
      let errors = 0;

      for (const state of states.slice(0, 3)) { // Limit to first 3 states for demo
        for (const crop of crops.slice(0, 5)) { // Limit to first 5 crops for demo
          try {
            await this.getLocalPrices(state.id, crop.id);
            await this.getGovernmentPrices(crop.id);
            refreshed++;
          } catch (error) {
            logger.error(`Error refreshing ${crop.name} in ${state.name}:`, error);
            errors++;
          }
        }
      }

      logger.info(`Price refresh completed: ${refreshed} successful, ${errors} errors`);
      return { success: true, refreshed, errors };

    } catch (error) {
      logger.error('Error refreshing prices:', error);
      return { success: false, error: error.message };
    }
  }
}

module.exports = new PriceService();
