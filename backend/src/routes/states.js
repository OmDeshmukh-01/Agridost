const express = require('express');
const router = express.Router();
const normalizer = require('../services/normalizer');
const logger = require('../utils/logger');

/**
 * GET /api/states
 * Get list of all available states
 */
router.get('/', async (req, res) => {
  try {
    const states = normalizer.getAllStates();
    
    res.json({
      success: true,
      data: states,
      count: states.length,
      timestamp: new Date().toISOString()
    });
  } catch (error) {
    logger.error('Error fetching states:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch states'
    });
  }
});

/**
 * GET /api/states/:state_id/crops
 * Get crops available for a specific state
 */
router.get('/:state_id/crops', async (req, res) => {
  try {
    const { state_id } = req.params;
    
    // Normalize state ID
    const normalizedState = normalizer.normalizeState(state_id);
    
    if (normalizedState.error) {
      return res.status(400).json({
        success: false,
        error: normalizedState.error,
        suggestions: normalizedState.suggestions
      });
    }

    const crops = normalizer.getCropsForState(normalizedState.id);
    
    res.json({
      success: true,
      data: {
        state: normalizedState,
        crops: crops,
        count: crops.length
      },
      timestamp: new Date().toISOString()
    });
  } catch (error) {
    logger.error('Error fetching crops for state:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch crops for state'
    });
  }
});

/**
 * GET /api/states/:state_id
 * Get specific state information
 */
router.get('/:state_id', async (req, res) => {
  try {
    const { state_id } = req.params;
    
    const normalizedState = normalizer.normalizeState(state_id);
    
    if (normalizedState.error) {
      return res.status(404).json({
        success: false,
        error: normalizedState.error,
        suggestions: normalizedState.suggestions
      });
    }

    res.json({
      success: true,
      data: normalizedState,
      timestamp: new Date().toISOString()
    });
  } catch (error) {
    logger.error('Error fetching state:', error);
    res.status(500).json({
      success: false,
      error: 'Failed to fetch state information'
    });
  }
});

module.exports = router;
