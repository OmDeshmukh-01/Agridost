const cron = require('node-cron');
const logger = require('./logger');
const cache = require('./cache');
const { refreshAllPrices } = require('./services/priceService');

/**
 * Schedule price refresh every 24 hours at 2 AM
 */
function schedulePriceRefresh() {
  // Run every day at 2:00 AM
  cron.schedule('0 2 * * *', async () => {
    logger.info('Starting scheduled price refresh...');
    try {
      await refreshAllPrices();
      logger.info('Scheduled price refresh completed successfully');
    } catch (error) {
      logger.error('Scheduled price refresh failed:', error);
    }
  }, {
    scheduled: true,
    timezone: "Asia/Kolkata"
  });

  logger.info('Price refresh scheduler configured for daily 2:00 AM IST');
}

/**
 * Manual price refresh (can be called via API)
 */
async function manualRefresh() {
  logger.info('Starting manual price refresh...');
  try {
    await refreshAllPrices();
    logger.info('Manual price refresh completed successfully');
    return { success: true, message: 'Prices refreshed successfully' };
  } catch (error) {
    logger.error('Manual price refresh failed:', error);
    return { success: false, message: 'Price refresh failed', error: error.message };
  }
}

module.exports = {
  schedulePriceRefresh,
  manualRefresh
};
