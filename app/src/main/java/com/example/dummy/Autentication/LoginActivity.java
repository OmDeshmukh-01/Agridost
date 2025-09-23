package com.example.dummy.Autentication;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.dummy.Main.MainActivity;
import com.example.dummy.R;
import com.example.dummy.FireBase.UserConn;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
// Auth implemented using Firebase Realtime Database

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

        // Query Firebase Realtime Database: Users by email, then verify password
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        loginButton.setEnabled(true);

                        if (!dataSnapshot.exists()) {
                            Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        boolean authenticated = false;
                        String fullName = null;

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            UserConn user = child.getValue(UserConn.class);
                            if (user != null && user.password != null && user.password.equals(password)) {
                                authenticated = true;
                                fullName = user.fullName;
                                break;
                            }
                        }

                        if (authenticated) {
                            SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
                            prefs.edit()
                                    .putBoolean("is_logged_in", true)
                                    .putString("logged_in_email", email)
                                    .putString("profile_name", fullName != null ? fullName : email)
                                    .apply();

                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                        loginButton.setEnabled(true);
                        Toast.makeText(LoginActivity.this, "Login failed: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}



