const priceService = require('../src/services/priceService');

describe('PriceService', () => {
  beforeEach(() => {
    // Reset environment variables
    process.env.DEMO_MODE = 'true';
  });

  describe('getLocalPrices', () => {
    test('should return demo data when DEMO_MODE is true', async () => {
      const result = await priceService.getLocalPrices('maharashtra', 'tomato');
      
      expect(result).toBeDefined();
      expect(result.state).toBe('Maharashtra');
      expect(result.crop).toBe('Tomato');
      expect(result.modal).toBeGreaterThan(0);
      expect(result.fallback).toBe(true);
    });

    test('should handle invalid state', async () => {
      const result = await priceService.getLocalPrices('invalid_state', 'tomato');
      
      expect(result.error).toBeDefined();
      expect(result.fallback).toBe(true);
    });

    test('should handle invalid crop', async () => {
      const result = await priceService.getLocalPrices('maharashtra', 'invalid_crop');
      
      expect(result.error).toBeDefined();
      expect(result.fallback).toBe(true);
    });
  });

  describe('getGovernmentPrices', () => {
    test('should return demo MSP data', async () => {
      const result = await priceService.getGovernmentPrices('tomato');
      
      expect(result).toBeDefined();
      expect(result.crop).toBe('Tomato');
      expect(result.msp).toBeGreaterThan(0);
      expect(result.fallback).toBe(true);
    });

    test('should handle invalid crop', async () => {
      const result = await priceService.getGovernmentPrices('invalid_crop');
      
      expect(result.error).toBeDefined();
      expect(result.fallback).toBe(true);
    });
  });

  describe('getComparison', () => {
    test('should return comparison data with differences', async () => {
      const result = await priceService.getComparison('maharashtra', 'tomato');
      
      expect(result).toBeDefined();
      expect(result.state).toBe('Maharashtra');
      expect(result.crop).toBe('Tomato');
      expect(result.local).toBeDefined();
      expect(result.government).toBeDefined();
      expect(result.comparison).toBeDefined();
    });
  });
});
