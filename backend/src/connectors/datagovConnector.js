const axios = require('axios');
const logger = require('../utils/logger');

/**
 * Data.gov.in Connector for Government MSP and Official Prices
 * 
 * API Documentation: https://data.gov.in
 * Registration: https://data.gov.in/user/register
 * 
 * Note: Replace 'your_datagov_api_key_here' with actual API key from Data.gov.in registration
 */
class DataGovConnector {
  constructor() {
    this.baseURL = 'https://api.data.gov.in/resource';
    this.apiKey = process.env.DATAGOV_API_KEY || 'your_datagov_api_key_here';
    this.rateLimitDelay = parseInt(process.env.RATE_LIMIT_DELAY_MS || '1000');
  }

  /**
   * Fetch government MSP (Minimum Support Price) for a specific crop
   * @param {string} crop - Crop name
   * @returns {Promise<Object>} Government MSP data
   */
  async fetchMSP(crop) {
    try {
      logger.debug(`Fetching Data.gov.in MSP for ${crop}`);
      
      // Example Data.gov.in API call for MSP data
      const response = await axios.get(`${this.baseURL}/msp-data`, {
        params: {
          'api-key': this.apiKey,
          'format': 'json',
          'filters[commodity]': crop,
          'limit': 1
        },
        timeout: 10000
      });

      // Parse Data.gov.in response format
      const data = response.data;
      
      if (data.records && data.records.length > 0) {
        const record = data.records[0];
        return {
          source: 'Data.gov.in',
          crop: crop,
          msp: parseFloat(record.msp) || 0,
          unit: record.unit || 'kg',
          lastUpdated: new Date(record.last_updated || Date.now()).getTime(),
          season: record.season || 'Kharif'
        };
      }

      return {
        source: 'Data.gov.in',
        crop: crop,
        msp: 0,
        unit: 'kg',
        lastUpdated: Date.now(),
        error: 'No MSP data found'
      };

    } catch (error) {
      logger.error(`Data.gov.in MSP API error for ${crop}:`, error.message);
      
      return {
        source: 'Data.gov.in',
        crop: crop,
        msp: 0,
        unit: 'kg',
        lastUpdated: Date.now(),
        error: error.message,
        fallback: true
      };
    }
  }

  /**
   * Fetch government official prices for a specific state and crop
   * @param {string} state - State name
   * @param {string} crop - Crop name
   * @returns {Promise<Object>} Government official price data
   */
  async fetchOfficialPrices(state, crop) {
    try {
      logger.debug(`Fetching Data.gov.in official prices for ${crop} in ${state}`);
      
      const response = await axios.get(`${this.baseURL}/official-prices`, {
        params: {
          'api-key': this.apiKey,
          'format': 'json',
          'filters[state]': state,
          'filters[commodity]': crop,
          'limit': 10
        },
        timeout: 10000
      });

      const data = response.data;
      
      if (data.records && data.records.length > 0) {
        // Calculate average prices from multiple records
        const prices = data.records.map(record => parseFloat(record.price) || 0).filter(p => p > 0);
        
        if (prices.length > 0) {
          const avgPrice = prices.reduce((sum, price) => sum + price, 0) / prices.length;
          const minPrice = Math.min(...prices);
          const maxPrice = Math.max(...prices);
          
          return {
            source: 'Data.gov.in',
            state: state,
            crop: crop,
            min: minPrice,
            modal: avgPrice,
            max: maxPrice,
            unit: 'kg',
            lastUpdated: new Date().getTime(),
            recordCount: prices.length
          };
        }
      }

      return {
        source: 'Data.gov.in',
        state: state,
        crop: crop,
        error: 'No official price data found'
      };

    } catch (error) {
      logger.error(`Data.gov.in official prices API error for ${crop} in ${state}:`, error.message);
      
      return {
        source: 'Data.gov.in',
        state: state,
        crop: crop,
        error: error.message,
        fallback: true
      };
    }
  }

  /**
   * Check if API key is configured
   * @returns {boolean} True if API key is configured
   */
  isConfigured() {
    return this.apiKey && this.apiKey !== 'your_datagov_api_key_here';
  }

  /**
   * Get API status and configuration info
   * @returns {Object} API status information
   */
  getStatus() {
    return {
      name: 'Data.gov.in',
      configured: this.isConfigured(),
      baseURL: this.baseURL,
      rateLimitDelay: this.rateLimitDelay
    };
  }
}

module.exports = new DataGovConnector();
