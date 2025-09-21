const { createClient } = require('redis');
const { LRUCache } = require('lru-cache');
const logger = require('./logger');

let redisClient = null;
let memoryCache = null;

/**
 * Initialize cache - try Redis first, fallback to in-memory LRU cache
 */
async function initialize() {
  try {
    // Try to connect to Redis
    if (process.env.REDIS_URL) {
      redisClient = createClient({
        url: process.env.REDIS_URL,
        password: process.env.REDIS_PASSWORD || undefined
      });

      redisClient.on('error', (err) => {
        logger.warn('Redis connection error, falling back to memory cache:', err.message);
        redisClient = null;
      });

      await redisClient.connect();
      logger.info('Connected to Redis cache');
      return;
    }
  } catch (error) {
    logger.warn('Redis connection failed, using memory cache:', error.message);
  }

  // Fallback to in-memory cache
  memoryCache = new LRUCache({
    max: 1000, // Maximum number of items
    ttl: parseInt(process.env.CACHE_TTL_SECONDS || '3600') * 1000, // TTL in milliseconds
    updateAgeOnGet: true,
    updateAgeOnHas: true
  });
  logger.info('Using in-memory LRU cache');
}

/**
 * Get value from cache
 */
async function get(key) {
  try {
    if (redisClient) {
      const value = await redisClient.get(key);
      return value ? JSON.parse(value) : null;
    } else if (memoryCache) {
      return memoryCache.get(key) || null;
    }
    return null;
  } catch (error) {
    logger.error('Cache get error:', error);
    return null;
  }
}

/**
 * Set value in cache
 */
async function set(key, value, ttlSeconds = null) {
  try {
    const ttl = ttlSeconds || parseInt(process.env.CACHE_TTL_SECONDS || '3600');
    
    if (redisClient) {
      await redisClient.setEx(key, ttl, JSON.stringify(value));
    } else if (memoryCache) {
      memoryCache.set(key, value, { ttl: ttl * 1000 });
    }
  } catch (error) {
    logger.error('Cache set error:', error);
  }
}

/**
 * Delete value from cache
 */
async function del(key) {
  try {
    if (redisClient) {
      await redisClient.del(key);
    } else if (memoryCache) {
      memoryCache.delete(key);
    }
  } catch (error) {
    logger.error('Cache delete error:', error);
  }
}

/**
 * Clear all cache
 */
async function clear() {
  try {
    if (redisClient) {
      await redisClient.flushAll();
    } else if (memoryCache) {
      memoryCache.clear();
    }
  } catch (error) {
    logger.error('Cache clear error:', error);
  }
}

/**
 * Close cache connection
 */
async function close() {
  try {
    if (redisClient) {
      await redisClient.quit();
    }
  } catch (error) {
    logger.error('Cache close error:', error);
  }
}

/**
 * Get cache statistics
 */
function getStats() {
  if (memoryCache) {
    return {
      type: 'memory',
      size: memoryCache.size,
      max: memoryCache.max,
      ttl: memoryCache.ttl
    };
  } else if (redisClient) {
    return {
      type: 'redis',
      status: redisClient.isReady ? 'ready' : 'connecting'
    };
  }
  return { type: 'none' };
}

module.exports = {
  initialize,
  get,
  set,
  del,
  clear,
  close,
  getStats
};
