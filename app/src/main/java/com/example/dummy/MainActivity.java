package com.example.dummy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        setupBottomNavigation();
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
            Intent intent = new Intent(this, CommunityActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.nav_crops) {
            // Already on main activity (crops page)
            return true;
        } else if (itemId == R.id.nav_market) {
            // Handle market navigation
            // You can create a MarketActivity later
            return true;
        } else if (itemId == R.id.nav_you) {
            // Handle profile navigation
            // You can create a ProfileActivity later
            return true;
        }
        
        return false;
    }
}