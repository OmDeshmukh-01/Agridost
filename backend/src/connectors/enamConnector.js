const axios = require('axios');
const logger = require('../utils/logger');

/**
 * eNAM (Electronic National Agriculture Market) Connector
 * 
 * API Documentation: https://enam.gov.in
 * Registration: https://enam.gov.in/registration
 * 
 * Note: Replace 'your_enam_api_key_here' with actual API key from eNAM registration
 */
class ENAMConnector {
  constructor() {
    this.baseURL = 'https://enam.gov.in/api/v1';
    this.apiKey = process.env.ENAM_API_KEY || 'your_enam_api_key_here';
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
      logger.debug(`Fetching eNAM prices for ${crop} in ${state}`);
      
      // Example eNAM API call (replace with actual endpoint)
      const response = await axios.get(`${this.baseURL}/prices`, {
        params: {
          state: state,
          commodity: crop,
          api_key: this.apiKey
        },
        timeout: 10000
      });

      // Parse eNAM response format
      const data = response.data;
      
      // Normalize eNAM response to standard format
      return {
        source: 'eNAM',
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
      logger.error(`eNAM API error for ${crop} in ${state}:`, error.message);
      
      // Return fallback data structure
      return {
        source: 'eNAM',
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
      logger.debug(`Fetching eNAM history for ${crop} in ${state} (${days} days)`);
      
      const response = await axios.get(`${this.baseURL}/history`, {
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
      logger.error(`eNAM history API error for ${crop} in ${state}:`, error.message);
      return [];
    }
  }

  /**
   * Check if API key is configured
   * @returns {boolean} True if API key is configured
   */
  isConfigured() {
    return this.apiKey && this.apiKey !== 'your_enam_api_key_here';
  }

  /**
   * Get API status and configuration info
   * @returns {Object} API status information
   */
  getStatus() {
    return {
      name: 'eNAM',
      configured: this.isConfigured(),
      baseURL: this.baseURL,
      rateLimitDelay: this.rateLimitDelay
    };
  }
}

module.exports = new ENAMConnector();
