package com.example.dummy.Autentication;

import android.content.Context;
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

import com.example.dummy.BaseActivity;
import com.example.dummy.Main.MainActivity;
import com.example.dummy.R;
import com.example.dummy.FireBase.UserConn;
import com.example.dummy.LocaleHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends BaseActivity {

    private EditText nameInput, emailInput, passwordInput, confirmPasswordInput;
    private Button signupButton;
    private ProgressBar progressBar;
    private TextView loginText;

    private DatabaseReference dbRef;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Database reference
        dbRef = FirebaseDatabase.getInstance().getReference("Users");

        // Bind views
        nameInput = findViewById(R.id.input_name);
        emailInput = findViewById(R.id.input_email);
        passwordInput = findViewById(R.id.input_password);
        confirmPasswordInput = findViewById(R.id.input_confirm_password);
        signupButton = findViewById(R.id.button_signup);
        progressBar = findViewById(R.id.progress);
        loginText = findViewById(R.id.login_text);

        // Listeners
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

        // Create UserConn object for Firebase
        UserConn user = new UserConn(name, email, password);

        // Push to Firebase
        String userId = dbRef.push().getKey();
        if (userId != null) {
            dbRef.child(userId).setValue(user).addOnCompleteListener(task -> {
                progressBar.setVisibility(View.GONE);
                signupButton.setEnabled(true);

                if (task.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "Signup Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignupActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

