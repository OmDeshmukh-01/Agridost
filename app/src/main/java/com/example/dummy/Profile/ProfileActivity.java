package com.example.dummy.Profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import com.example.dummy.BaseActivity;

import com.example.dummy.Autentication.LoginActivity;
import com.example.dummy.Autentication.WelcomeActivity;
import com.example.dummy.community.CommunityActivity;
import com.example.dummy.Main.MainActivity;
import com.example.dummy.market.MarketActivity;
import com.example.dummy.R;
import com.example.dummy.User;
import com.example.dummy.LocaleHelper;
// Session/auth will be added later with Firebase
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;

import java.io.File;

public class ProfileActivity extends BaseActivity {

    private static final int EDIT_PROFILE_REQUEST = 100;
    
    private TextView profileName;
    private TextView profileDescription;
    private MaterialButton editButton;
    private TabLayout profileTabs;
    private MaterialCardView shareCard;
    private MaterialCardView feedbackCard;
    private BottomNavigationView bottomNavigationView;
    private ImageView profileAvatar;
    private ImageView menuButton;
    private Button btnLanguage;
    
    
    private User currentUser;
    private Object unused;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        
        initializeViews();
        setupProfileInfo();
        setupTabs();
        setupCards();
        setupBottomNavigation();
    }

    private void initializeViews() {
        profileName = findViewById(R.id.profile_name);
        profileDescription = findViewById(R.id.profile_description);
        editButton = findViewById(R.id.profile_edit_button);
        profileTabs = findViewById(R.id.profile_tabs);
        shareCard = findViewById(R.id.share_card);
        feedbackCard = findViewById(R.id.feedback_card);
        bottomNavigationView = findViewById(R.id.profile_bottom_nav);
        profileAvatar = findViewById(R.id.profile_avatar);
        menuButton = findViewById(R.id.profile_menu_button);
        btnLanguage = findViewById(R.id.btnLanguage);
        
        
        // Debug: Check if buttons are found
        if (btnLanguage == null) {
            Toast.makeText(this, "Language button 1 NOT found!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Language button 1 found!", Toast.LENGTH_SHORT).show();
        }
        
        
        
        unused = null;
    }

    private void setupProfileInfo() {
        // Load current user
        currentUser = null;
        
        // One-time login info from SharedPreferences
        android.content.SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
        String email = prefs.getString("logged_in_email", null);

        if (currentUser != null) {
            // Display current user information
            profileName.setText(currentUser.getDisplayName());
            profileDescription.setText(currentUser.getDisplayDescription());
            if (currentUser.hasAvatar()) {
                loadAvatar(currentUser.getAvatarPath());
            }
        } else if (isLoggedIn && email != null) {
            // Prefer persisted profile details when available
            String savedName = prefs.getString("profile_name", null);
            String savedDesc = prefs.getString("profile_description", null);
            String avatarPath = prefs.getString("profile_avatar_path", null);

            profileName.setText(savedName != null && !savedName.isEmpty() ? savedName : email);
            profileDescription.setText(savedDesc != null && !savedDesc.isEmpty() ? savedDesc : "Welcome back!");
            if (avatarPath != null && !avatarPath.isEmpty()) {
                loadAvatar(avatarPath);
            }
        } else {
            // Default values if no user is logged in
            String savedName = prefs.getString("profile_name", null);
            String savedDesc = prefs.getString("profile_description", null);
            String avatarPath = prefs.getString("profile_avatar_path", null);
            profileName.setText(savedName != null && !savedName.isEmpty() ? savedName : "Guest User");
            profileDescription.setText(savedDesc != null && !savedDesc.isEmpty() ? savedDesc : "Please log in to customize your profile");
            if (avatarPath != null && !avatarPath.isEmpty()) {
                loadAvatar(avatarPath);
            }
        }
        
        editButton.setOnClickListener(v -> {
            if (isLoggedIn || currentUser != null) {
                Intent intent = new Intent(this, EditProfileActivity.class);
                startActivityForResult(intent, EDIT_PROFILE_REQUEST);
            } else {
                Toast.makeText(this, "Please log in to edit your profile", Toast.LENGTH_SHORT).show();
            }
        });
        
        // Setup menu button
        menuButton.setOnClickListener(v -> showMenuDialog());

        // Setup language button
        if (btnLanguage != null) {
            btnLanguage.setClickable(true);
            btnLanguage.setEnabled(true);
            btnLanguage.setOnClickListener(v -> {
                // Debug: Show current language
                String currentLang = LocaleHelper.getLanguage(this);
                Toast.makeText(this, "Current language: " + currentLang, Toast.LENGTH_SHORT).show();
                showLanguageSelectionDialog();
            });
        }

    }
    
    private void loadAvatar(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                profileAvatar.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showMenuDialog() {
        String[] menuItems = {"Test Dialog", "Language", "Settings", "Help & Support", "About AgriDost", "Logout"};
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Menu");
        builder.setItems(menuItems, (dialog, which) -> {
            switch (which) {
                case 0:
                    showSimpleTestDialog();
                    break;
                case 1:
                    showLanguageSelectionDialog();
                    break;
                case 2:
                    showSettings();
                    break;
                case 3:
                    showHelpSupport();
                    break;
                case 4:
                    showAbout();
                    break;
                case 5:
                    logout();
                    break;
            }
        });
        builder.show();
    }
    
    private void showSettings() {
        Toast.makeText(this, getString(R.string.settings_coming_soon), Toast.LENGTH_SHORT).show();
    }

    private void showSimpleTestDialog() {
        Toast.makeText(this, "Creating simple test dialog...", Toast.LENGTH_SHORT).show();
        
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Test Dialog");
            builder.setMessage("This is a simple test dialog. If you can see this, the button click is working!");
            builder.setPositiveButton("OK", (dialog, which) -> {
                Toast.makeText(this, "Test dialog OK clicked!", Toast.LENGTH_SHORT).show();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                Toast.makeText(this, "Test dialog Cancel clicked!", Toast.LENGTH_SHORT).show();
            });
            
            AlertDialog dialog = builder.create();
            dialog.show();
            Toast.makeText(this, "Simple test dialog shown!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error in simple dialog: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }



    
    private void showHelpSupport() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:support@agridost.com"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "AgriDost Support Request");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi AgriDost Support Team,\n\nI need help with:\n\n");
        
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        } else {
            Toast.makeText(this, "No email app found. Please contact us at support@agridost.com", Toast.LENGTH_LONG).show();
        }
    }
    
    private void showAbout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About AgriDost");
        builder.setMessage("AgriDost v1.0\n\nYour agricultural assistant for crop management, plant disease identification, and farming advice.\n\nDeveloped with ❤️ for farmers worldwide.");
        builder.setPositiveButton("OK", null);
        builder.show();
    }
    
    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Logout", (dialog, which) -> {
            // Clear one-time login flag
            SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
            prefs.edit()
                .putBoolean("is_logged_in", false)
                .remove("logged_in_email")
                .apply();

            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            // Navigate to Welcome screen
            Intent intent = new Intent(this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void setupTabs() {
        // Add tabs
        profileTabs.addTab(profileTabs.newTab().setText("General"));
        profileTabs.addTab(profileTabs.newTab().setText("Community"));
        
        // Set default selection
        profileTabs.selectTab(profileTabs.getTabAt(0));
        
        // Handle tab selection
        profileTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    // Show General content
                    shareCard.setVisibility(View.VISIBLE);
                    feedbackCard.setVisibility(View.VISIBLE);
                } else if (position == 1) {
                    // Show Community content (for now, hide general cards)
                    shareCard.setVisibility(View.GONE);
                    feedbackCard.setVisibility(View.GONE);
                    Toast.makeText(ProfileActivity.this, "Community features coming soon!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupCards() {
        // Share AgriDost card
        MaterialButton shareButton = findViewById(R.id.share_button);
        shareButton.setOnClickListener(v -> shareAgriDost());

        // Feedback card
        MaterialButton feedbackButton = findViewById(R.id.feedback_button);
        feedbackButton.setOnClickListener(v -> giveFeedback());
    }

    // XML onClick handler for language button (btnLanguage)
    public void onLanguageClick(View view) {
        showLanguageSelectionDialog();
    }

    private void shareAgriDost() {
        String shareText = "Check out AgriDost - Your agricultural assistant! " +
                "Get expert advice on farming, crop management, plant diseases, and more. " +
                "Download now and grow smart together! 🌱";
        
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "AgriDost - Agricultural Assistant");
        
        startActivity(Intent.createChooser(shareIntent, "Share AgriDost"));
    }

    private void giveFeedback() {
        // Open email client for feedback
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:feedback@agridost.com"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "AgriDost App Feedback");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi AgriDost Team,\n\nI would like to share my feedback about the app:\n\n");
        
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        } else {
            Toast.makeText(this, "No email app found. Please contact us at feedback@agridost.com", Toast.LENGTH_LONG).show();
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                
                if (itemId == R.id.nav_crops) {
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_community) {
                    Intent intent = new Intent(ProfileActivity.this, CommunityActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_market) {
                    Intent intent = new Intent(ProfileActivity.this, MarketActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_you) {
                    // Already on profile page
                    return true;
                }
                
                return false;
            }
        });
        
        // Set Profile as selected
        bottomNavigationView.setSelectedItemId(R.id.nav_you);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == Activity.RESULT_OK) {
            // Profile was updated, refresh the display
            setupProfileInfo();
            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh profile info when returning to this activity
        setupProfileInfo();
    }
    
    @Override
    public void onBackPressed() {
        // Navigate to home page instead of welcome page
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
