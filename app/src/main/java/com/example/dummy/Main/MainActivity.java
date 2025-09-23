package com.example.dummy.Main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.example.dummy.ApiTestActivity;
import com.example.dummy.chat.ChatActivity;
import com.example.dummy.community.CommunityActivity;
import com.example.dummy.tools.CropHealActivity;
import com.example.dummy.tools.CultivationTipsActivity;
import com.example.dummy.tools.DiseaseAlertActivity;
import com.example.dummy.community.FarmerScheme;
import com.example.dummy.community.FarmerSchemeAdapter;
import com.example.dummy.community.FarmerSchemeDataProvider;
import com.example.dummy.tools.FertilizerCalculatorActivity;
import com.example.dummy.LocationHelper;
import com.example.dummy.market.MarketActivity;
import com.example.dummy.Profile.ProfileActivity;
import com.example.dummy.tools.ProfitEstimatorActivity;
import com.example.dummy.features.FeatureHostActivity;
import com.example.dummy.R;
import com.example.dummy.Weather;
import com.example.dummy.services.WeatherService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

// New fragments for in-activity navigation
import com.example.dummy.Main.CommunityFragment;
import com.example.dummy.Main.MarketFragment;
import com.example.dummy.Main.ProfileFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final int LOCATION_PERMISSION_REQUEST = 100;
    
    private BottomNavigationView bottomNavigationView;
    private NestedScrollView scrollContent;
    private TextView weatherLocation;
    private TextView weatherCondition;
    private TextView weatherTemperature;
    private TextView weatherLastUpdated;
    private MaterialButton weatherLocationButton;
    
    // Dashboard components
    private RecyclerView recyclerSchemes;
    private TextInputEditText searchSchemes;
    private MaterialButton filterState;
    private MaterialButton filterType;
    private FarmerSchemeAdapter schemeAdapter;
    private List<FarmerScheme> allSchemes;
    private List<FarmerScheme> filteredSchemes;
    private String selectedState = "All States";
    private String selectedType = "All Types";
    
    
    private String currentLocation = "Pune,India";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Grab references for toggling between home content and fragments
        scrollContent = findViewById(R.id.scroll);
        setupBottomNavigation();
        setupTakePictureButton();
        setupChatbotFAB();
        setupCropHealButton();
        setupFeatureCards();
        setupMarketButton();
        setupWeather();
        setupFarmerSchemesDashboard();
    }

    private void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        
        // Set default selected item
        bottomNavigationView.setSelectedItemId(R.id.nav_crops);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        
        if (itemId == R.id.nav_community) {
            showFragment(new CommunityFragment());
            return true;
        } else if (itemId == R.id.nav_crops) {
            showHomeContent();
            return true;
        } else if (itemId == R.id.nav_market) {
            showFragment(new MarketFragment());
            return true;
        } else if (itemId == R.id.nav_you) {
            showFragment(new ProfileFragment());
            return true;
        }
        
        return false;
    }

    private void showFragment(Fragment fragment) {
        // Hide scroll content, show fragment container
        if (scrollContent != null) scrollContent.setVisibility(android.view.View.GONE);
        android.view.View container = findViewById(R.id.fragment_container);
        if (container != null) container.setVisibility(android.view.View.VISIBLE);
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit();
    }

    private void showHomeContent() {
        // Show scroll content, hide fragment container
        if (scrollContent != null) scrollContent.setVisibility(android.view.View.VISIBLE);
        android.view.View container = findViewById(R.id.fragment_container);
        if (container != null) container.setVisibility(android.view.View.GONE);
    }

    private void setupTakePictureButton() {
        MaterialButton takePictureButton = findViewById(R.id.cta_take_picture);
        if (takePictureButton != null) {
            takePictureButton.setOnClickListener(v -> {
                // TODO: Implement camera functionality
            });
        }
    }

    private void setupChatbotFAB() {
        FloatingActionButton chatbotFAB = findViewById(R.id.fab_chatbot);
        if (chatbotFAB != null) {
            chatbotFAB.setOnClickListener(v -> {
                Intent intent = new Intent(this, ChatActivity.class);
                startActivity(intent);
            });
            
            // Add long click for API test
            chatbotFAB.setOnLongClickListener(v -> {
                Intent intent = new Intent(this, ApiTestActivity.class);
                startActivity(intent);
                return true;
            });
        }
    }

    private void setupCropHealButton() {
        MaterialButton cropHealButton = findViewById(R.id.cta_take_picture);
        if (cropHealButton != null) {
            cropHealButton.setOnClickListener(v -> {
                Intent intent = new Intent(this, CropHealActivity.class);
                startActivity(intent);
            });
        }
    }

    private void setupFeatureCards() {
        // Fertilizer Calculator
        findViewById(R.id.card_fertilizer_calculator).setOnClickListener(v -> {
            Intent intent = new Intent(this, FeatureHostActivity.class);
            intent.putExtra(FeatureHostActivity.EXTRA_FEATURE_KEY, FeatureHostActivity.FEATURE_FERTILIZER);
            startActivity(intent);
        });

        // Pests & Diseases (reuse CropHealActivity)
        findViewById(R.id.card_pests_diseases).setOnClickListener(v -> {
            Intent intent = new Intent(this, FeatureHostActivity.class);
            intent.putExtra(FeatureHostActivity.EXTRA_FEATURE_KEY, FeatureHostActivity.FEATURE_CROP_HEAL);
            startActivity(intent);
        });

        // Cultivation Tips
        findViewById(R.id.card_cultivation_tips).setOnClickListener(v -> {
            Intent intent = new Intent(this, FeatureHostActivity.class);
            intent.putExtra(FeatureHostActivity.EXTRA_FEATURE_KEY, FeatureHostActivity.FEATURE_CULTIVATION_TIPS);
            startActivity(intent);
        });

        // Disease Alert
        findViewById(R.id.card_disease_alert).setOnClickListener(v -> {
            Intent intent = new Intent(this, FeatureHostActivity.class);
            intent.putExtra(FeatureHostActivity.EXTRA_FEATURE_KEY, FeatureHostActivity.FEATURE_DISEASE_ALERT);
            startActivity(intent);
        });

        // Profit Estimator
        findViewById(R.id.card_profit_estimator).setOnClickListener(v -> {
            Intent intent = new Intent(this, FeatureHostActivity.class);
            intent.putExtra(FeatureHostActivity.EXTRA_FEATURE_KEY, FeatureHostActivity.FEATURE_PROFIT_ESTIMATOR);
            startActivity(intent);
        });
    }

    private void setupMarketButton() {
        // This will be handled by the bottom navigation
        // The Market button in bottom nav will launch MarketActivity
    }
    
    private void setupWeather() {
        weatherLocation = findViewById(R.id.weather_location);
        weatherCondition = findViewById(R.id.weather_condition);
        weatherTemperature = findViewById(R.id.weather_temperature);
        weatherLastUpdated = findViewById(R.id.weather_last_updated);
        weatherLocationButton = findViewById(R.id.weather_location_button);
        
        weatherLocationButton.setOnClickListener(v -> showLocationSelectionDialog());
        
        // Load initial weather data
        loadWeatherData();
    }
    
    private void loadWeatherData() {
        WeatherService.getCurrentWeather(currentLocation, new WeatherService.WeatherCallback() {
            @Override
            public void onSuccess(Weather weather) {
                runOnUiThread(() -> {
                    weatherLocation.setText(weather.getFormattedLocation());
                    weatherCondition.setText(weather.getCondition());
                    weatherTemperature.setText(weather.getFormattedTempRange());
                    weatherLastUpdated.setText("Updated: " + weather.getLastUpdated());
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Weather update failed: " + error, Toast.LENGTH_SHORT).show();
                    // Keep default values on error
                });
            }
        });
    }
    
    private void showLocationSelectionDialog() {
        String[] cities = LocationHelper.getPopularCities();
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Location");
        builder.setItems(cities, (dialog, which) -> {
            currentLocation = cities[which];
            loadWeatherData();
        });
        builder.setNegativeButton("Use Current Location", (dialog, which) -> {
            requestLocationPermission();
        });
        builder.show();
    }
    
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 
                LOCATION_PERMISSION_REQUEST);
        } else {
            getCurrentLocation();
        }
    }
    
    private void getCurrentLocation() {
        LocationHelper.getCurrentLocation(this, new LocationHelper.LocationCallback() {
            @Override
            public void onLocationReceived(String location) {
                // Convert coordinates to city name (simplified)
                currentLocation = "Pune,India"; // Default for now
                loadWeatherData();
            }
            
            @Override
            public void onLocationError(String error) {
                Toast.makeText(MainActivity.this, "Location error: " + error, Toast.LENGTH_SHORT).show();
                currentLocation = LocationHelper.getDefaultLocation();
                loadWeatherData();
            }
        });
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied. Using default location.", Toast.LENGTH_SHORT).show();
                currentLocation = LocationHelper.getDefaultLocation();
                loadWeatherData();
            }
        }
    }
    
    private void setupFarmerSchemesDashboard() {
        // Initialize dashboard components
        recyclerSchemes = findViewById(R.id.recycler_schemes);
        searchSchemes = findViewById(R.id.search_schemes);
        filterState = findViewById(R.id.filter_state);
        filterType = findViewById(R.id.filter_type);
        
        // Load all schemes
        allSchemes = FarmerSchemeDataProvider.getAllSchemes();
        filteredSchemes = new ArrayList<>();
        
        // Initially show only first 3 schemes
        for (int i = 0; i < Math.min(3, allSchemes.size()); i++) {
            filteredSchemes.add(allSchemes.get(i));
        }
        
        // Setup RecyclerView
        schemeAdapter = new FarmerSchemeAdapter(this, filteredSchemes);
        recyclerSchemes.setLayoutManager(new LinearLayoutManager(this));
        recyclerSchemes.setAdapter(schemeAdapter);
        
        // Enable scrollbars
        recyclerSchemes.setVerticalScrollBarEnabled(true);
        
        // Setup search functionality
        searchSchemes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterSchemes();
            }
            
            @Override
            public void afterTextChanged(Editable s) {}
        });
        
        // Setup filter buttons
        filterState.setOnClickListener(v -> showStateFilterDialog());
        filterType.setOnClickListener(v -> showTypeFilterDialog());
    }

    private void showStateFilterDialog() {
        String[] states = FarmerSchemeDataProvider.getAllStates().toArray(new String[0]);
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select State");
        builder.setItems(states, (dialog, which) -> {
            selectedState = states[which];
            filterState.setText(selectedState + " ▼");
            filterSchemes();
        });
        builder.show();
    }
    
    private void showTypeFilterDialog() {
        String[] types = FarmerSchemeDataProvider.getAllSchemeTypes().toArray(new String[0]);
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Scheme Type");
        builder.setItems(types, (dialog, which) -> {
            selectedType = types[which];
            filterType.setText(selectedType + " ▼");
            filterSchemes();
        });
        builder.show();
    }
    
    private void filterSchemes() {
        String searchQuery = searchSchemes.getText().toString().toLowerCase().trim();
        
        filteredSchemes.clear();
        
        // Check if any filters are applied
        boolean hasFilters = !searchQuery.isEmpty() || 
                           !selectedState.equals("All States") || 
                           !selectedType.equals("All Types");
        
        for (FarmerScheme scheme : allSchemes) {
            boolean matchesSearch = searchQuery.isEmpty() || 
                scheme.getTitle().toLowerCase().contains(searchQuery) ||
                scheme.getDescription().toLowerCase().contains(searchQuery) ||
                scheme.getState().toLowerCase().contains(searchQuery);
            
            boolean matchesState = selectedState.equals("All States") || 
                scheme.getState().equals(selectedState);
            
            boolean matchesType = selectedType.equals("All Types") || 
                scheme.getSchemeType().equals(selectedType);
            
            if (matchesSearch && matchesState && matchesType) {
                filteredSchemes.add(scheme);
            }
        }
        
        // If no filters are applied, limit to first 3 schemes
        if (!hasFilters && filteredSchemes.size() > 3) {
            List<FarmerScheme> limitedSchemes = new ArrayList<>();
            for (int i = 0; i < Math.min(3, filteredSchemes.size()); i++) {
                limitedSchemes.add(filteredSchemes.get(i));
            }
            schemeAdapter.updateSchemes(limitedSchemes);
        } else {
            schemeAdapter.updateSchemes(filteredSchemes);
        }
    }
}