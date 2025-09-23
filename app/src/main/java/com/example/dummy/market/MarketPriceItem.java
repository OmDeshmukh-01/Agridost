package com.example.dummy.market;

public class MarketPriceItem {
    private int imageResource;
    private String cropName;
    private String marketLocation;
    private String price;
    private boolean isPriceUp;
    private String priceChange;

    public MarketPriceItem(int imageResource, String cropName, String marketLocation, 
                          String price, boolean isPriceUp, String priceChange) {
        this.imageResource = imageResource;
        this.cropName = cropName;
        this.marketLocation = marketLocation;
        this.price = price;
        this.isPriceUp = isPriceUp;
        this.priceChange = priceChange;
    }

    // Getters
    public int getImageResource() {
        return imageResource;
    }

    public String getCropName() {
        return cropName;
    }

    public String getMarketLocation() {
        return marketLocation;
    }

    public String getPrice() {
        return price;
    }

    public boolean isPriceUp() {
        return isPriceUp;
    }

    public String getPriceChange() {
        return priceChange;
    }

    // Setters
    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public void setMarketLocation(String marketLocation) {
        this.marketLocation = marketLocation;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setPriceUp(boolean priceUp) {
        isPriceUp = priceUp;
    }

    public void setPriceChange(String priceChange) {
        this.priceChange = priceChange;
    }
}
