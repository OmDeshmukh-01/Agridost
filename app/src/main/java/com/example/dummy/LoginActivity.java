package com.example.dummy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private ProgressBar progressBar;
    private TextView signupText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.input_email);
        passwordInput = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.button_login);
        progressBar = findViewById(R.id.progress);
        signupText = findViewById(R.id.signup_text);

        loginButton.setOnClickListener(v -> attemptLogin());
        signupText.setOnClickListener(v -> startActivity(new Intent(this, SignupActivity.class)));
    }

    private void attemptLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();

        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Password is required");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        // Authenticate with UserDatabase
        UserDatabase userDatabase = UserDatabase.getInstance(this);
        User user = userDatabase.login(email, password);
        
        emailInput.postDelayed(() -> {
            progressBar.setVisibility(View.GONE);
            loginButton.setEnabled(true);
            
            if (user != null) {
                Toast.makeText(this, "Login successful! Welcome " + user.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        }, 800);
    }
}


