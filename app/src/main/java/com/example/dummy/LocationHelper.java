package com.example.dummy;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class LocationHelper {
    private static final String TAG = "LocationHelper";
    private static final long MIN_TIME_BW_UPDATES = 10000; // 10 seconds
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    public interface LocationCallback {
        void onLocationReceived(String location);
        void onLocationError(String error);
    }

    public static void getCurrentLocation(Context context, LocationCallback callback) {
        android.location.LocationManager locationManager = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) 
            != PackageManager.PERMISSION_GRANTED && 
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) 
            != PackageManager.PERMISSION_GRANTED) {
            callback.onLocationError("Location permission not granted");
            return;
        }

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String locationString = location.getLatitude() + "," + location.getLongitude();
                callback.onLocationReceived(locationString);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {
                callback.onLocationError("Location provider disabled");
            }
        };

        try {
            // Try GPS first
            if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                    android.location.LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    locationListener
                );
            }
            // Try Network provider as fallback
            else if (locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(
                    android.location.LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    locationListener
                );
            } else {
                callback.onLocationError("No location provider available");
            }
        } catch (SecurityException e) {
            Log.e(TAG, "Location permission error", e);
            callback.onLocationError("Location permission error");
        }
    }

    // Get a default location (Pune, India) if location services are not available
    public static String getDefaultLocation() {
        return "Pune,India"; // Default to Pune, India
    }

    // Popular Indian cities for weather
    public static String[] getPopularCities() {
        return new String[]{
            "Mumbai,India",
            "Delhi,India", 
            "Bangalore,India",
            "Hyderabad,India",
            "Chennai,India",
            "Kolkata,India",
            "Pune,India",
            "Ahmedabad,India",
            "Jaipur,India",
            "Surat,India"
        };
    }
}