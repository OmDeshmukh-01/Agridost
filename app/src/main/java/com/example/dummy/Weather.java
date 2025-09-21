package com.example.dummy;

public class Weather {
    private String location;
    private String condition;
    private double currentTemp;
    private double minTemp;
    private double maxTemp;
    private int humidity;
    private double windSpeed;
    private String windDirection;
    private String lastUpdated;

    public Weather() {}

    public Weather(String location, String condition, double currentTemp, double minTemp, double maxTemp, 
                   int humidity, double windSpeed, String windDirection, String lastUpdated) {
        this.location = location;
        this.condition = condition;
        this.currentTemp = currentTemp;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.lastUpdated = lastUpdated;
    }

    // Getters
    public String getLocation() { return location; }
    public String getCondition() { return condition; }
    public double getCurrentTemp() { return currentTemp; }
    public double getMinTemp() { return minTemp; }
    public double getMaxTemp() { return maxTemp; }
    public int getHumidity() { return humidity; }
    public double getWindSpeed() { return windSpeed; }
    public String getWindDirection() { return windDirection; }
    public String getLastUpdated() { return lastUpdated; }

    // Setters
    public void setLocation(String location) { this.location = location; }
    public void setCondition(String condition) { this.condition = condition; }
    public void setCurrentTemp(double currentTemp) { this.currentTemp = currentTemp; }
    public void setMinTemp(double minTemp) { this.minTemp = minTemp; }
    public void setMaxTemp(double maxTemp) { this.maxTemp = maxTemp; }
    public void setHumidity(int humidity) { this.humidity = humidity; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }
    public void setWindDirection(String windDirection) { this.windDirection = windDirection; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }

    // Helper methods
    public String getFormattedCurrentTemp() {
        return String.format("%.0f°C", currentTemp);
    }

    public String getFormattedTempRange() {
        return String.format("%.0f°C / %.0f°C", minTemp, maxTemp);
    }

    public String getFormattedLocation() {
        if (location != null && location.length() > 15) {
            return location.substring(0, 15) + "...";
        }
        return location;
    }
}
