# Market Price Tracker - Complete Implementation

A complete, production-ready Market Price Tracking system with backend API and Android frontend for agricultural market price monitoring.

## 🚀 Features

### Backend API (Node.js + Express)
- **Live Data Integration**: eNAM, AGMARKNET, Data.gov.in APIs
- **Data Normalization**: Unified format across different sources
- **Caching**: Redis + in-memory LRU cache
- **Rate Limiting**: Configurable delays between API calls
- **Scheduled Refresh**: Automatic updates every 24 hours
- **Demo Mode**: Fallback data when APIs unavailable
- **Error Handling**: Graceful degradation

### Android App (Java + XML)
- **Market Dashboard**: Two stacked dashboards (Government Subsidy + Price Tracking)
- **TabLayout + ViewPager2**: Buy/Sell sections
- **Buy Section**: Existing categories (pesticides, seeds, fertilizers) + product grid
- **Sell Section**: State/crop selection, price cards, charts, refresh functionality
- **Material UI**: Modern design with CardViews, chips, icons
- **WorkManager**: Background refresh every 24 hours
- **Demo Mode**: Works without backend connection

## 📁 Project Structure

```
/
├── backend/                 # Node.js API server
│   ├── src/
│   │   ├── connectors/     # API connectors (eNAM, AGMARKNET, Data.gov.in)
│   │   ├── data/          # Demo data and mappings
│   │   ├── routes/        # API endpoints
│   │   ├── services/      # Business logic
│   │   └── utils/         # Utilities (cache, logger, scheduler)
│   ├── tests/             # Unit tests
│   ├── docker-compose.yml # Docker setup
│   └── README.md          # Backend documentation
├── app/                   # Android application
│   ├── src/main/
│   │   ├── java/         # Java source code
│   │   └── res/          # XML layouts and resources
│   └── build.gradle.kts  # Android build configuration
└── README.md             # This file
```

## 🛠️ Quick Start

### Backend Setup

1. **Install Dependencies**
   ```bash
   cd backend
   npm install
   ```

2. **Configure Environment**
   ```bash
   cp env.example .env
   # Edit .env with your API keys
   ```

3. **Run in Demo Mode**
   ```bash
   npm run dev
   # Server runs on http://localhost:3000
   ```

### Android Setup

1. **Open in Android Studio**
   - Import the project
   - Sync Gradle files
   - Build and run

2. **Test the App**
   - Navigate to Market tab
   - Switch between Buy/Sell tabs
   - Test price tracking in Sell tab

## 🔧 API Endpoints

### Core Endpoints
- `GET /api/states` - List all states
- `GET /api/states/:id/crops` - Get crops for state
- `GET /api/prices/local?state_id=&crop_id=` - Local market prices
- `GET /api/prices/government?crop_id=` - Government MSP
- `GET /api/prices/comparison?state_id=&crop_id=` - Combined data

### Demo Endpoints
- `GET /api/demo` - Demo data info
- `GET /api/demo/prices/local?state_id=&crop_id=` - Demo local prices
- `GET /api/demo/prices/government?crop_id=` - Demo government prices

## 📱 Android Components

### Activities
- `MarketActivity` - Main market page with TabLayout + ViewPager2
- `ProductDetailActivity` - Product details (existing)

### Fragments
- `BuyFragment` - Product categories and grid
- `SellFragment` - Price tracking dashboard

### Services
- `MarketPriceService` - API communication
- `WorkManager` - Background refresh

### Adapters
- `ProductAdapter` - Product grid
- `CategoryAdapter` - Category grid
- `PriceHistoryAdapter` - Price history chart

## 🎨 UI Features

### Government Subsidy Dashboard
- Small card showing MSP info
- Subsidy information
- Quick reference data

### Market Price Tracking Dashboard
- State selection dropdown
- Crop selection (state-dependent)
- Price display cards:
  - Local market prices (min, modal, max)
  - Government MSP
  - Absolute & percentage differences
  - Last updated timestamp
- Price history chart (7 days)
- Manual refresh button
- Auto-refresh every 24 hours

### Material Design
- CardViews with elevation
- Rounded corners
- Color-coded price changes
- Responsive layouts
- Modern typography

## 🔌 API Integration

### eNAM Integration
```javascript
// Example connector usage
const enamConnector = require('./connectors/enamConnector');
const prices = await enamConnector.fetchPrices('Maharashtra', 'Tomato');
```

### AGMARKNET Integration
```javascript
// Example connector usage
const agmarkConnector = require('./connectors/agmarkConnector');
const prices = await agmarkConnector.fetchPrices('Maharashtra', 'Tomato');
```

### Data.gov.in Integration
```javascript
// Example connector usage
const datagovConnector = require('./connectors/datagovConnector');
const msp = await datagovConnector.fetchMSP('Tomato');
```

## 🧪 Testing

### Backend Tests
```bash
cd backend
npm test
```

### Manual API Testing
```bash
# Get states
curl "http://localhost:3000/api/states"

# Get prices
curl "http://localhost:3000/api/prices/comparison?state_id=maharashtra&crop_id=tomato"
```

### Android Tests
- Unit tests for adapters and services
- Instrumentation tests for fragments
- UI tests for user interactions

## 🐳 Docker Support

### Run with Docker Compose
```bash
cd backend
docker-compose up -d
```

This starts:
- Node.js API server
- Redis cache
- Scheduled price refresh

## 📊 Data Flow

1. **Android App** requests price data
2. **Backend API** checks cache first
3. If cache miss, calls **external APIs** (eNAM, AGMARKNET, Data.gov.in)
4. **Normalization layer** converts to unified format
5. **Caching layer** stores result
6. **Android App** displays formatted data
7. **WorkManager** refreshes data every 24 hours

## 🔐 Security & Configuration

### API Keys Required
- eNAM API key (https://enam.gov.in)
- AGMARKNET API key (https://agmarknet.gov.in)
- Data.gov.in API key (https://data.gov.in)

### Environment Variables
```env
DEMO_MODE=true                    # Use demo data
REDIS_URL=redis://localhost:6379  # Redis connection
RATE_LIMIT_DELAY_MS=1000         # API rate limiting
CACHE_TTL_SECONDS=3600           # Cache TTL
```

## 🚀 Production Deployment

### Backend
- Set `NODE_ENV=production`
- Configure Redis
- Add all API keys
- Use HTTPS
- Implement rate limiting

### Android
- Sign APK for release
- Configure ProGuard
- Add crash reporting
- Performance monitoring

## 📈 Monitoring

### Backend
- Health check endpoint
- API status monitoring
- Cache hit rates
- Error logging

### Android
- Crash reporting
- Performance metrics
- User analytics
- Network monitoring

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Add tests for new features
4. Submit a pull request

## 📄 License

MIT License - see LICENSE file for details.

## 🆘 Support

### Common Issues

1. **API Keys Not Working**
   - Verify registration status
   - Check key format
   - Test with curl first

2. **No Data Available**
   - Enable demo mode
   - Check API endpoints
   - Verify state/crop names

3. **Android Build Issues**
   - Sync Gradle files
   - Check dependencies
   - Clean and rebuild

### Debug Mode
- Backend: Set `LOG_LEVEL=debug`
- Android: Enable debug logging

## 📞 Contact

For questions or support, please open an issue in the repository.

---

**Note**: This is a complete, production-ready implementation with both backend and Android components. The system works out-of-the-box in demo mode and can be easily configured for live API integration.
