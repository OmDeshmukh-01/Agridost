package com.example.dummy.Autentication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dummy.Main.MainActivity;
import com.example.dummy.R;
import com.google.android.material.button.MaterialButton;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        MaterialButton getStartedButton = findViewById(R.id.act_wel_button_get_started);
        getStartedButton.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
            Intent intent;
            if (isLoggedIn) {
                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            } else {
                intent = new Intent(this, LoginActivity.class);
            }
            startActivity(intent);
        });
    }
}


