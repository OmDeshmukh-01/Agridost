const statesData = require('../data/mappings/states.json');
const cropsData = require('../data/mappings/crops.json');
const logger = require('../utils/logger');

/**
 * Normalization service for crop and state names across different APIs
 */
class Normalizer {
  constructor() {
    this.states = statesData.states;
    this.crops = cropsData.crops;
  }

  /**
   * Normalize state name to canonical ID
   * @param {string} stateName - State name from any source
   * @returns {Object} Normalized state object
   */
  normalizeState(stateName) {
    if (!stateName) {
      return { id: null, name: null, error: 'State name is required' };
    }

    const normalizedName = stateName.toLowerCase().trim();
    
    // Find exact match first
    let state = this.states.find(s => 
      s.name.toLowerCase() === normalizedName || 
      s.code.toLowerCase() === normalizedName ||
      s.aliases.some(alias => alias.toLowerCase() === normalizedName)
    );

    if (state) {
      return {
        id: state.id,
        name: state.name,
        code: state.code,
        canonical: true
      };
    }

    // Find partial match
    state = this.states.find(s => 
      s.name.toLowerCase().includes(normalizedName) ||
      normalizedName.includes(s.name.toLowerCase()) ||
      s.aliases.some(alias => 
        alias.toLowerCase().includes(normalizedName) ||
        normalizedName.includes(alias.toLowerCase())
      )
    );

    if (state) {
      return {
        id: state.id,
        name: state.name,
        code: state.code,
        canonical: false,
        original: stateName,
        match: 'partial'
      };
    }

    // No match found
    return {
      id: null,
      name: stateName,
      error: 'State not found',
      suggestions: this.states.slice(0, 5).map(s => s.name)
    };
  }

  /**
   * Normalize crop name to canonical ID
   * @param {string} cropName - Crop name from any source
   * @returns {Object} Normalized crop object
   */
  normalizeCrop(cropName) {
    if (!cropName) {
      return { id: null, name: null, error: 'Crop name is required' };
    }

    const normalizedName = cropName.toLowerCase().trim();
    
    // Find exact match first
    let crop = this.crops.find(c => 
      c.name.toLowerCase() === normalizedName ||
      c.aliases.some(alias => alias.toLowerCase() === normalizedName)
    );

    if (crop) {
      return {
        id: crop.id,
        name: crop.name,
        category: crop.category,
        canonical: true,
        apiIds: {
          enam: crop.enam_id,
          agmark: crop.agmark_id,
          datagov: crop.datagov_id
        }
      };
    }

    // Find partial match
    crop = this.crops.find(c => 
      c.name.toLowerCase().includes(normalizedName) ||
      normalizedName.includes(c.name.toLowerCase()) ||
      c.aliases.some(alias => 
        alias.toLowerCase().includes(normalizedName) ||
        normalizedName.includes(alias.toLowerCase())
      )
    );

    if (crop) {
      return {
        id: crop.id,
        name: crop.name,
        category: crop.category,
        canonical: false,
        original: cropName,
        match: 'partial',
        apiIds: {
          enam: crop.enam_id,
          agmark: crop.agmark_id,
          datagov: crop.datagov_id
        }
      };
    }

    // No match found
    return {
      id: null,
      name: cropName,
      error: 'Crop not found',
      suggestions: this.crops.slice(0, 10).map(c => c.name)
    };
  }

  /**
   * Normalize price data from different sources to standard format
   * @param {Object} rawData - Raw price data from API
   * @param {string} source - Source API name
   * @returns {Object} Normalized price data
   */
  normalizePriceData(rawData, source) {
    try {
      const normalized = {
        source: source,
        state: rawData.state || '',
        crop: rawData.crop || '',
        min: parseFloat(rawData.min) || 0,
        modal: parseFloat(rawData.modal) || 0,
        max: parseFloat(rawData.max) || 0,
        unit: rawData.unit || 'kg',
        lastUpdated: rawData.lastUpdated || Date.now(),
        history: rawData.history || [],
        error: rawData.error || null,
        fallback: rawData.fallback || false
      };

      // Validate price data
      if (normalized.min < 0 || normalized.modal < 0 || normalized.max < 0) {
        normalized.error = 'Invalid price data: negative values';
      }

      if (normalized.min > normalized.max) {
        normalized.error = 'Invalid price data: min > max';
      }

      if (normalized.modal < normalized.min || normalized.modal > normalized.max) {
        // Auto-correct modal price if it's outside min-max range
        normalized.modal = (normalized.min + normalized.max) / 2;
      }

      return normalized;

    } catch (error) {
      logger.error('Error normalizing price data:', error);
      return {
        source: source,
        error: 'Failed to normalize price data',
        fallback: true
      };
    }
  }

  /**
   * Merge multiple price sources into a single aggregated result
   * @param {Array} priceSources - Array of normalized price data from different sources
   * @returns {Object} Aggregated price data
   */
  mergePriceSources(priceSources) {
    try {
      const validSources = priceSources.filter(source => 
        !source.error && !source.fallback && source.modal > 0
      );

      if (validSources.length === 0) {
        return {
          error: 'No valid price data available',
          fallback: true,
          sources: priceSources.map(s => s.source)
        };
      }

      // Calculate aggregated values
      const prices = validSources.map(s => s.modal);
      const minPrices = validSources.map(s => s.min);
      const maxPrices = validSources.map(s => s.max);

      const aggregated = {
        state: validSources[0].state,
        crop: validSources[0].crop,
        min: Math.min(...minPrices),
        modal: prices.reduce((sum, price) => sum + price, 0) / prices.length,
        max: Math.max(...maxPrices),
        unit: validSources[0].unit,
        lastUpdated: Math.max(...validSources.map(s => s.lastUpdated)),
        sourceCount: validSources.length,
        sources: validSources.map(s => s.source),
        history: this.mergeHistory(validSources.map(s => s.history || []))
      };

      return aggregated;

    } catch (error) {
      logger.error('Error merging price sources:', error);
      return {
        error: 'Failed to merge price sources',
        fallback: true
      };
    }
  }

  /**
   * Merge historical data from multiple sources
   * @param {Array} historyArrays - Array of history arrays from different sources
   * @returns {Array} Merged and sorted historical data
   */
  mergeHistory(historyArrays) {
    try {
      const allHistory = [];
      
      historyArrays.forEach(history => {
        if (Array.isArray(history)) {
          allHistory.push(...history);
        }
      });

      // Sort by date and remove duplicates
      const uniqueHistory = allHistory.reduce((acc, current) => {
        const existing = acc.find(item => item.date === current.date);
        if (!existing) {
          acc.push(current);
        } else {
          // Average the prices for the same date
          existing.modal = (existing.modal + current.modal) / 2;
        }
        return acc;
      }, []);

      return uniqueHistory.sort((a, b) => new Date(a.date) - new Date(b.date));

    } catch (error) {
      logger.error('Error merging history:', error);
      return [];
    }
  }

  /**
   * Get all available states
   * @returns {Array} List of all states
   */
  getAllStates() {
    return this.states.map(state => ({
      id: state.id,
      name: state.name,
      code: state.code
    }));
  }

  /**
   * Get crops for a specific state
   * @param {string} stateId - State ID
   * @returns {Array} List of crops for the state
   */
  getCropsForState(stateId) {
    // For demo purposes, return all crops
    // In production, this would be state-specific
    return this.crops.map(crop => ({
      id: crop.id,
      name: crop.name,
      category: crop.category
    }));
  }
}

module.exports = new Normalizer();
