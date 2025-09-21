# Market Price Tracker Backend

A Node.js backend API for agricultural market price tracking with integration to eNAM, AGMARKNET, and Data.gov.in APIs.

## Features

- **Live Price Data**: Integration with eNAM, AGMARKNET, and Data.gov.in APIs
- **Data Normalization**: Unified data format across different sources
- **Caching**: Redis or in-memory LRU cache for performance
- **Rate Limiting**: Configurable delays between API calls
- **Demo Mode**: Fallback to demo data when APIs are unavailable
- **Scheduled Refresh**: Automatic price updates every 24 hours
- **Error Handling**: Graceful fallback to demo data

## API Endpoints

### States
- `GET /api/states` - Get list of all states
- `GET /api/states/:state_id` - Get specific state information
- `GET /api/states/:state_id/crops` - Get crops for a specific state

### Prices
- `GET /api/prices/local?state_id=&crop_id=` - Get local market prices
- `GET /api/prices/government?crop_id=` - Get government MSP
- `GET /api/prices/comparison?state_id=&crop_id=` - Get comparison data
- `POST /api/prices/refresh` - Manually refresh all prices
- `GET /api/prices/status` - Get API connector status

### Demo
- `GET /api/demo` - Get demo data information
- `GET /api/demo/states` - Get demo states
- `GET /api/demo/crops/:state_id` - Get demo crops for state
- `GET /api/demo/prices/local?state_id=&crop_id=` - Get demo local prices
- `GET /api/demo/prices/government?crop_id=` - Get demo government prices

## Setup Instructions

### 1. Install Dependencies
```bash
cd backend
npm install
```

### 2. Environment Configuration
Copy `env.example` to `.env` and configure:

```bash
cp env.example .env
```

Edit `.env` file:
```env
# Server Configuration
PORT=3000
NODE_ENV=development

# Demo Mode (set to true to use mock data)
DEMO_MODE=true

# Redis Configuration (optional)
REDIS_URL=redis://localhost:6379

# API Keys (add your keys here)
ENAM_API_KEY=your_enam_api_key_here
AGMARKNET_API_KEY=your_agmarknet_api_key_here
DATAGOV_API_KEY=your_datagov_api_key_here

# Rate Limiting
RATE_LIMIT_DELAY_MS=1000
CACHE_TTL_SECONDS=3600
```

### 3. API Key Registration

#### eNAM API
1. Visit https://enam.gov.in
2. Register for API access
3. Add your API key to `.env` file

#### AGMARKNET API
1. Visit https://agmarknet.gov.in
2. Register for API access
3. Add your API key to `.env` file

#### Data.gov.in API
1. Visit https://data.gov.in
2. Register for API access
3. Add your API key to `.env` file

### 4. Run the Server

#### Development Mode
```bash
npm run dev
```

#### Production Mode
```bash
npm start
```

The server will start on `http://localhost:3000`

## Testing

### Run Tests
```bash
npm test
```

### Manual Testing with curl

#### Get States
```bash
curl "http://localhost:3000/api/states"
```

#### Get Crops for State
```bash
curl "http://localhost:3000/api/states/maharashtra/crops"
```

#### Get Local Prices
```bash
curl "http://localhost:3000/api/prices/local?state_id=maharashtra&crop_id=tomato"
```

#### Get Government Prices
```bash
curl "http://localhost:3000/api/prices/government?crop_id=tomato"
```

#### Get Comparison Data
```bash
curl "http://localhost:3000/api/prices/comparison?state_id=maharashtra&crop_id=tomato"
```

#### Refresh Prices
```bash
curl -X POST "http://localhost:3000/api/prices/refresh"
```

#### Get API Status
```bash
curl "http://localhost:3000/api/prices/status"
```

## Data Sources

### eNAM (Electronic National Agriculture Market)
- **Purpose**: Local market prices (min, modal, max)
- **Registration**: https://enam.gov.in
- **Rate Limit**: 1 request per second (configurable)

### AGMARKNET (Agricultural Marketing Information Network)
- **Purpose**: Market prices and historical data
- **Registration**: https://agmarknet.gov.in
- **Rate Limit**: 1 request per second (configurable)

### Data.gov.in
- **Purpose**: Government MSP and official prices
- **Registration**: https://data.gov.in
- **Rate Limit**: 1 request per second (configurable)

## Data Normalization

The system normalizes data from different sources into a unified format:

```json
{
  "source": "eNAM|AGMARKNET|Data.gov.in",
  "state": "Maharashtra",
  "crop": "Tomato",
  "min": 12.5,
  "modal": 14.0,
  "max": 18.0,
  "unit": "kg",
  "lastUpdated": 1690000000000,
  "history": [
    {
      "date": "2024-01-01",
      "modal": 13.5
    }
  ]
}
```

## Caching Strategy

- **Primary**: Redis (if available)
- **Fallback**: In-memory LRU cache
- **TTL**: 1 hour (configurable)
- **Refresh**: Every 24 hours via cron job

## Error Handling

- **API Unavailable**: Fallback to demo data
- **Rate Limited**: Exponential backoff
- **Invalid Data**: Graceful error messages
- **Network Issues**: Retry with fallback

## Monitoring

- **Health Check**: `GET /health`
- **API Status**: `GET /api/prices/status`
- **Logs**: Structured logging with different levels
- **Metrics**: Cache hit rates, API response times

## Docker Support

### Using Docker Compose
```bash
docker-compose up -d
```

This will start:
- Node.js API server
- Redis cache
- Scheduled price refresh

### Manual Docker Build
```bash
docker build -t market-price-tracker .
docker run -p 3000:3000 market-price-tracker
```

## Production Deployment

### Environment Variables
- Set `NODE_ENV=production`
- Configure Redis URL
- Add all required API keys
- Set appropriate rate limits

### Security
- Use HTTPS in production
- Implement API authentication
- Rate limiting per IP
- Input validation and sanitization

### Scaling
- Use Redis for shared caching
- Load balancer for multiple instances
- Database for persistent storage
- Monitoring and alerting

## Troubleshooting

### Common Issues

1. **API Keys Not Working**
   - Verify API key format
   - Check registration status
   - Test with curl first

2. **Redis Connection Failed**
   - Check Redis server status
   - Verify connection URL
   - System will fallback to memory cache

3. **Rate Limiting**
   - Increase `RATE_LIMIT_DELAY_MS`
   - Check API provider limits
   - Use demo mode for testing

4. **No Data Available**
   - Check API endpoints
   - Verify state/crop names
   - Enable demo mode for testing

### Debug Mode
Set `LOG_LEVEL=debug` in `.env` for detailed logging.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Add tests for new features
4. Submit a pull request

## License

MIT License - see LICENSE file for details.
