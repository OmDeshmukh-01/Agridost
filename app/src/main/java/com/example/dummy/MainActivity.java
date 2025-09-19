package com.example.dummy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        setupBottomNavigation();
        setupTakePictureButton();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_crops) {
                // Already on main page
                return true;
            } else if (itemId == R.id.nav_community) {
                Intent intent = new Intent(this, CommunityActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_market) {
                // TODO: Implement Market activity
                return true;
            } else if (itemId == R.id.nav_you) {
                // TODO: Implement Profile activity
                return true;
            }
            return false;
        });
    }

    private void setupTakePictureButton() {
        MaterialButton takePictureButton = findViewById(R.id.cta_take_picture);
        if (takePictureButton != null) {
            takePictureButton.setOnClickListener(v -> {
                // TODO: Implement camera functionality
            });
        }
    }
}