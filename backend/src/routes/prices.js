const express = require('express');
const router = express.Router();
const priceService = require('../services/priceService');
const { manualRefresh } = require('../utils/scheduler');
const logger = require('../utils/logger');

/**
 * GET /api/prices/local?state_id=&crop_id=
 * Get local market prices for a specific state and crop
 */
router.get('/local', async (req, res) => {
  try {
    const { state_id, crop_id } = req.query;
    
    if (!state_id || !crop_id) {
      return res.status(400).json({
        success: false,
        error: 'Both state_id and crop_id are required'
      });
    }

    const prices = await priceService.getLocalPrices(state_id, crop_id);
    
    // Add fallback header if using demo data
    if (prices.fallback) {
      res.set('X-Data-Fallback', 'demo');
    }

    res.json({
      success: !prices.error,
      data: prices,
      timestamp: new Date().toISOString()
    });
  } catch (error) {
    logger.error('Error fetching local prices:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch local prices'
    });
  }
});

/**
 * GET /api/prices/government?crop_id=
 * Get government MSP for a specific crop
 */
router.get('/government', async (req, res) => {
  try {
    const { crop_id } = req.query;
    
    if (!crop_id) {
      return res.status(400).json({
        success: false,
        error: 'crop_id is required'
      });
    }

    const prices = await priceService.getGovernmentPrices(crop_id);
    
    // Add fallback header if using demo data
    if (prices.fallback) {
      res.set('X-Data-Fallback', 'demo');
    }

    res.json({
      success: !prices.error,
      data: prices,
      timestamp: new Date().toISOString()
    });
  } catch (error) {
    logger.error('Error fetching government prices:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch government prices'
    });
  }
});

/**
 * GET /api/prices/comparison?state_id=&crop_id=
 * Get comparison data (local + government + differences)
 */
router.get('/comparison', async (req, res) => {
  try {
    const { state_id, crop_id } = req.query;
    
    if (!state_id || !crop_id) {
      return res.status(400).json({
        success: false,
        error: 'Both state_id and crop_id are required'
      });
    }

    const comparison = await priceService.getComparison(state_id, crop_id);
    
    // Add fallback header if using demo data
    if (comparison.local?.fallback || comparison.government?.fallback) {
      res.set('X-Data-Fallback', 'demo');
    }

    res.json({
      success: !comparison.error,
      data: comparison,
      timestamp: new Date().toISOString()
    });
  } catch (error) {
    logger.error('Error fetching comparison data:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch comparison data'
    });
  }
});

/**
 * POST /api/prices/refresh
 * Manually refresh all cached prices
 */
router.post('/refresh', async (req, res) => {
  try {
    const result = await manualRefresh();
    
    res.json({
      success: result.success,
      message: result.message,
      data: result,
      timestamp: new Date().toISOString()
    });
  } catch (error) {
    logger.error('Error refreshing prices:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to refresh prices'
    });
  }
});

/**
 * GET /api/prices/status
 * Get status of all price connectors
 */
router.get('/status', async (req, res) => {
  try {
    const enamConnector = require('../connectors/enamConnector');
    const agmarkConnector = require('../connectors/agmarkConnector');
    const datagovConnector = require('../connectors/datagovConnector');
    
    const status = {
      connectors: [
        enamConnector.getStatus(),
        agmarkConnector.getStatus(),
        datagovConnector.getStatus()
      ],
      demoMode: process.env.DEMO_MODE === 'true',
      cache: require('../utils/cache').getStats()
    };

    res.json({
      success: true,
      data: status,
      timestamp: new Date().toISOString()
    });
  } catch (error) {
    logger.error('Error fetching price status:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch price status'
    });
  }
});

module.exports = router;
