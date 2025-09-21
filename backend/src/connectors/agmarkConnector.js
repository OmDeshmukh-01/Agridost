const axios = require('axios');
const logger = require('../utils/logger');

/**
 * AGMARKNET (Agricultural Marketing Information Network) Connector
 * 
 * API Documentation: https://agmarknet.gov.in
 * Registration: https://agmarknet.gov.in/registration
 * 
 * Note: Replace 'your_agmarknet_api_key_here' with actual API key from AGMARKNET registration
 */
class AGMARKConnector {
  constructor() {
    this.baseURL = 'https://agmarknet.gov.in/api/v1';
    this.apiKey = process.env.AGMARKNET_API_KEY || 'your_agmarknet_api_key_here';
    this.rateLimitDelay = parseInt(process.env.RATE_LIMIT_DELAY_MS || '1000');
  }

  /**
   * Fetch current prices for a specific state and crop
   * @param {string} state - State name
   * @param {string} crop - Crop name
   * @returns {Promise<Object>} Normalized price data
   */
  async fetchPrices(state, crop) {
    try {
      logger.debug(`Fetching AGMARKNET prices for ${crop} in ${state}`);
      
      // Example AGMARKNET API call (replace with actual endpoint)
      const response = await axios.get(`${this.baseURL}/commodity-prices`, {
        params: {
          state: state,
          commodity: crop,
          api_key: this.apiKey
        },
        timeout: 10000
      });

      // Parse AGMARKNET response format
      const data = response.data;
      
      // Normalize AGMARKNET response to standard format
      return {
        source: 'AGMARKNET',
        state: state,
        crop: crop,
        min: parseFloat(data.min_price) || 0,
        modal: parseFloat(data.modal_price) || 0,
        max: parseFloat(data.max_price) || 0,
        unit: data.unit || 'kg',
        lastUpdated: new Date(data.last_updated || Date.now()).getTime(),
        history: data.history || []
      };

    } catch (error) {
      logger.error(`AGMARKNET API error for ${crop} in ${state}:`, error.message);
      
      // Return fallback data structure
      return {
        source: 'AGMARKNET',
        state: state,
        crop: crop,
        error: error.message,
        fallback: true
      };
    }
  }

  /**
   * Fetch historical prices for a specific state and crop
   * @param {string} state - State name
   * @param {string} crop - Crop name
   * @param {number} days - Number of days of history
   * @returns {Promise<Array>} Historical price data
   */
  async fetchHistory(state, crop, days = 30) {
    try {
      logger.debug(`Fetching AGMARKNET history for ${crop} in ${state} (${days} days)`);
      
      const response = await axios.get(`${this.baseURL}/commodity-history`, {
        params: {
          state: state,
          commodity: crop,
          days: days,
          api_key: this.apiKey
        },
        timeout: 10000
      });

      // Parse and normalize historical data
      return response.data.history.map(item => ({
        date: item.date,
        modal: parseFloat(item.modal_price) || 0,
        min: parseFloat(item.min_price) || 0,
        max: parseFloat(item.max_price) || 0
      }));

    } catch (error) {
      logger.error(`AGMARKNET history API error for ${crop} in ${state}:`, error.message);
      return [];
    }
  }

  /**
   * Check if API key is configured
   * @returns {boolean} True if API key is configured
   */
  isConfigured() {
    return this.apiKey && this.apiKey !== 'your_agmarknet_api_key_here';
  }

  /**
   * Get API status and configuration info
   * @returns {Object} API status information
   */
  getStatus() {
    return {
      name: 'AGMARKNET',
      configured: this.isConfigured(),
      baseURL: this.baseURL,
      rateLimitDelay: this.rateLimitDelay
    };
  }
}

module.exports = new AGMARKConnector();
