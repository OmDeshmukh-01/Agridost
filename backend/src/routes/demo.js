const express = require('express');
const router = express.Router();
const demoData = require('../data/demo.json');
const logger = require('../utils/logger');

/**
 * GET /api/demo
 * Get demo data information
 */
router.get('/', (req, res) => {
  try {
    res.json({
      success: true,
      data: {
        message: 'Demo mode is active',
        availableStates: demoData.states.length,
        availableCrops: Object.keys(demoData.crops).length,
        sampleData: {
          states: demoData.states.slice(0, 3),
          crops: demoData.crops.maharashtra?.slice(0, 3) || [],
          prices: {
            local: Object.keys(demoData.prices.local).slice(0, 2),
            government: Object.keys(demoData.prices.government).slice(0, 2)
          }
        }
      },
      timestamp: new Date().toISOString()
    });
  } catch (error) {
    logger.error('Error fetching demo data:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch demo data'
    });
  }
});

/**
 * GET /api/demo/states
 * Get demo states data
 */
router.get('/states', (req, res) => {
  try {
    res.json({
      success: true,
      data: demoData.states,
      count: demoData.states.length,
      timestamp: new Date().toISOString()
    });
  } catch (error) {
    logger.error('Error fetching demo states:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch demo states'
    });
  }
});

/**
 * GET /api/demo/crops/:state_id
 * Get demo crops for a specific state
 */
router.get('/crops/:state_id', (req, res) => {
  try {
    const { state_id } = req.params;
    const crops = demoData.crops[state_id] || [];
    
    res.json({
      success: true,
      data: {
        state: state_id,
        crops: crops,
        count: crops.length
      },
      timestamp: new Date().toISOString()
    });
  } catch (error) {
    logger.error('Error fetching demo crops:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch demo crops'
    });
  }
});

/**
 * GET /api/demo/prices/local?state_id=&crop_id=
 * Get demo local prices
 */
router.get('/prices/local', (req, res) => {
  try {
    const { state_id, crop_id } = req.query;
    
    if (!state_id || !crop_id) {
      return res.status(400).json({
        success: false,
        error: 'Both state_id and crop_id are required'
      });
    }

    const key = `${state_id.toLowerCase()}_${crop_id.toLowerCase()}`;
    const prices = demoData.prices.local[key];
    
    if (!prices) {
      return res.status(404).json({
        success: false,
        error: 'No demo data available for this state and crop combination'
      });
    }

    res.json({
      success: true,
      data: {
        ...prices,
        source: 'Demo Data',
        fallback: true
      },
      timestamp: new Date().toISOString()
    });
  } catch (error) {
    logger.error('Error fetching demo local prices:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch demo local prices'
    });
  }
});

/**
 * GET /api/demo/prices/government?crop_id=
 * Get demo government prices
 */
router.get('/prices/government', (req, res) => {
  try {
    const { crop_id } = req.query;
    
    if (!crop_id) {
      return res.status(400).json({
        success: false,
        error: 'crop_id is required'
      });
    }

    const prices = demoData.prices.government[crop_id.toLowerCase()];
    
    if (!prices) {
      return res.status(404).json({
        success: false,
        error: 'No demo government data available for this crop'
      });
    }

    res.json({
      success: true,
      data: {
        ...prices,
        source: 'Demo Data',
        fallback: true
      },
      timestamp: new Date().toISOString()
    });
  } catch (error) {
    logger.error('Error fetching demo government prices:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch demo government prices'
    });
  }
});

module.exports = router;
