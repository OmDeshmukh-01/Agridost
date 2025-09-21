package com.example.dummy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText nameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private Button signupButton;
    private ProgressBar progressBar;
    private TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        nameInput = findViewById(R.id.input_name);
        emailInput = findViewById(R.id.input_email);
        passwordInput = findViewById(R.id.input_password);
        confirmPasswordInput = findViewById(R.id.input_confirm_password);
        signupButton = findViewById(R.id.button_signup);
        progressBar = findViewById(R.id.progress);
        loginText = findViewById(R.id.login_text);

        signupButton.setOnClickListener(v -> attemptSignup());
        loginText.setOnClickListener(v -> finish());
    }

    private void attemptSignup() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();

        if (TextUtils.isEmpty(name)) {
            nameInput.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Password is required");
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordInput.setError("Please confirm your password");
            return;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordInput.setError("Passwords do not match");
            return;
        }
        if (password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        signupButton.setEnabled(false);

        // Register with UserDatabase
        UserDatabase userDatabase = UserDatabase.getInstance(this);
        User user = userDatabase.register(name, email, password);
        
        nameInput.postDelayed(() -> {
            progressBar.setVisibility(View.GONE);
            signupButton.setEnabled(true);
            
            if (user != null) {
                Toast.makeText(this, "Account created successfully! Welcome " + user.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Email already exists. Please use a different email.", Toast.LENGTH_SHORT).show();
            }
        }, 800);
    }
}