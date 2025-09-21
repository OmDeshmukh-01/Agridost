const express = require('express');
const cors = require('cors');
const helmet = require('helmet');
const morgan = require('morgan');
const cron = require('node-cron');
require('dotenv').config();

const cache = require('./utils/cache');
const logger = require('./utils/logger');
const { schedulePriceRefresh } = require('./utils/scheduler');

// Import routes
const statesRoutes = require('./routes/states');
const pricesRoutes = require('./routes/prices');
const demoRoutes = require('./routes/demo');

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(helmet());
app.use(cors());
app.use(morgan('combined'));
app.use(express.json());

// Health check endpoint
app.get('/health', (req, res) => {
  res.json({ 
    status: 'OK', 
    timestamp: new Date().toISOString(),
    demoMode: process.env.DEMO_MODE === 'true'
  });
});

// API Routes
app.use('/api/states', statesRoutes);
app.use('/api/prices', pricesRoutes);
app.use('/api/demo', demoRoutes);

// Error handling middleware
app.use((err, req, res, next) => {
  logger.error('Unhandled error:', err);
  res.status(500).json({ 
    error: 'Internal server error',
    message: process.env.NODE_ENV === 'development' ? err.message : 'Something went wrong'
  });
});

// 404 handler
app.use('*', (req, res) => {
  res.status(404).json({ error: 'Endpoint not found' });
});

// Initialize cache and start scheduled tasks
async function startServer() {
  try {
    // Initialize cache
    await cache.initialize();
    logger.info('Cache initialized successfully');

    // Schedule price refresh every 24 hours
    schedulePriceRefresh();
    logger.info('Price refresh scheduler started');

    // Start server
    app.listen(PORT, () => {
      logger.info(`Server running on port ${PORT}`);
      logger.info(`Demo mode: ${process.env.DEMO_MODE === 'true' ? 'ENABLED' : 'DISABLED'}`);
    });
  } catch (error) {
    logger.error('Failed to start server:', error);
    process.exit(1);
  }
}

// Graceful shutdown
process.on('SIGTERM', async () => {
  logger.info('SIGTERM received, shutting down gracefully');
  await cache.close();
  process.exit(0);
});

process.on('SIGINT', async () => {
  logger.info('SIGINT received, shutting down gracefully');
  await cache.close();
  process.exit(0);
});

startServer();
