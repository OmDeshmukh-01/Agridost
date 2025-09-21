package com.example.dummy;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherService {
    private static final String TAG = "WeatherService";
    private static final String API_KEY = "e78f68a0fe124b60bf7192500252009";
    private static final String BASE_URL = "http://api.weatherapi.com/v1/current.json";
    
    public interface WeatherCallback {
        void onSuccess(Weather weather);
        void onError(String error);
    }

    public static void getCurrentWeather(String location, WeatherCallback callback) {
        new WeatherTask(callback).execute(location);
    }

    private static class WeatherTask extends AsyncTask<String, Void, Weather> {
        private final WeatherCallback callback;
        private String errorMessage = null;

        public WeatherTask(WeatherCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Weather doInBackground(String... params) {
            String location = params[0];
            
            try {
                OkHttpClient client = new OkHttpClient();
                String url = BASE_URL + "?key=" + API_KEY + "&q=" + location + "&aqi=no";
                
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();

                Log.d(TAG, "Weather API Response: " + responseBody);

                if (response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    WeatherApiResponse apiResponse = gson.fromJson(responseBody, WeatherApiResponse.class);
                    
                    if (apiResponse != null && apiResponse.current != null && apiResponse.location != null) {
                        Weather weather = new Weather();
                        weather.setLocation(apiResponse.location.name + ", " + apiResponse.location.region);
                        weather.setCondition(apiResponse.current.condition.text);
                        weather.setCurrentTemp(apiResponse.current.temp_c);
                        weather.setMinTemp(apiResponse.current.temp_c - 3); // Approximate min temp
                        weather.setMaxTemp(apiResponse.current.temp_c + 6); // Approximate max temp
                        weather.setHumidity(apiResponse.current.humidity);
                        weather.setWindSpeed(apiResponse.current.wind_kph);
                        weather.setWindDirection(apiResponse.current.wind_dir);
                        weather.setLastUpdated(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                        
                        return weather;
                    } else {
                        errorMessage = "Invalid weather data received";
                    }
                } else {
                    errorMessage = "Weather API error: " + response.code() + " - " + responseBody;
                }
            } catch (IOException e) {
                Log.e(TAG, "Weather API request failed", e);
                errorMessage = "Network error: " + e.getMessage();
            } catch (Exception e) {
                Log.e(TAG, "Weather parsing failed", e);
                errorMessage = "Data parsing error: " + e.getMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            if (weather != null) {
                callback.onSuccess(weather);
            } else {
                callback.onError(errorMessage != null ? errorMessage : "Unknown error occurred");
            }
        }
    }

    // API Response classes
    private static class WeatherApiResponse {
        public Location location;
        public Current current;
    }

    private static class Location {
        public String name;
        public String region;
        public String country;
        public double lat;
        public double lon;
        public String tz_id;
        public long localtime_epoch;
        public String localtime;
    }

    private static class Current {
        public long last_updated_epoch;
        public String last_updated;
        public double temp_c;
        public double temp_f;
        public int is_day;
        public Condition condition;
        public double wind_mph;
        public double wind_kph;
        public int wind_degree;
        public String wind_dir;
        public double pressure_mb;
        public double pressure_in;
        public double precip_mm;
        public double precip_in;
        public int humidity;
        public int cloud;
        public double feelslike_c;
        public double feelslike_f;
        public double vis_km;
        public double vis_miles;
        public double uv;
        public double gust_mph;
        public double gust_kph;
    }

    private static class Condition {
        public String text;
        public String icon;
        public int code;
    }
}
