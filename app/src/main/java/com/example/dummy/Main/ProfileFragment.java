package com.example.dummy.Main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.core.os.LocaleListCompat;

import com.example.dummy.Autentication.LoginActivity;
import com.example.dummy.Autentication.WelcomeActivity;
import com.example.dummy.Profile.EditProfileActivity;
import com.example.dummy.R;
import com.example.dummy.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;

import java.io.File;

public class ProfileFragment extends Fragment {

    private static final int EDIT_PROFILE_REQUEST = 100;

    private TextView profileName;
    private TextView profileDescription;
    private MaterialButton editButton;
    private TabLayout profileTabs;
    private MaterialCardView shareCard;
    private MaterialCardView feedbackCard;
    private ImageView profileAvatar;
    private ImageView menuButton;
    private MaterialButton profileLanguageButton;

    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);
        initializeViews(view);
        setupProfileInfo();
        setupTabs();
        setupCards(view);
        return view;
    }

    private void initializeViews(View root) {
        profileName = root.findViewById(R.id.profile_name);
        profileDescription = root.findViewById(R.id.profile_description);
        editButton = root.findViewById(R.id.profile_edit_button);
        profileTabs = root.findViewById(R.id.profile_tabs);
        shareCard = root.findViewById(R.id.share_card);
        feedbackCard = root.findViewById(R.id.feedback_card);
        profileAvatar = root.findViewById(R.id.profile_avatar);
        menuButton = root.findViewById(R.id.profile_menu_button);
        profileLanguageButton = root.findViewById(R.id.profile_language_button);
    }

    private void setupProfileInfo() {
        currentUser = null; // Hook up with real session when available

        // Read one-time login info and saved profile fields
        SharedPreferences prefs = requireContext().getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
        String email = prefs.getString("logged_in_email", null);
        String savedName = prefs.getString("profile_name", null);
        String savedDesc = prefs.getString("profile_description", null);
        String avatarPath = prefs.getString("profile_avatar_path", null);

        if (currentUser != null) {
            profileName.setText(currentUser.getDisplayName());
            profileDescription.setText(currentUser.getDisplayDescription());
            if (currentUser.hasAvatar()) {
                loadAvatar(currentUser.getAvatarPath());
            }
        } else if (isLoggedIn && email != null) {
            // Prefer persisted profile details when available
            profileName.setText(savedName != null && !savedName.isEmpty() ? savedName : email);
            profileDescription.setText(savedDesc != null && !savedDesc.isEmpty() ? savedDesc : "Welcome back!");
            if (avatarPath != null && !avatarPath.isEmpty()) {
                loadAvatar(avatarPath);
            }
        } else {
            profileName.setText(savedName != null && !savedName.isEmpty() ? savedName : "Guest User");
            profileDescription.setText(savedDesc != null && !savedDesc.isEmpty() ? savedDesc : "Please log in to customize your profile");
            if (avatarPath != null && !avatarPath.isEmpty()) {
                loadAvatar(avatarPath);
            }
        }

        editButton.setOnClickListener(v -> {
            boolean canEdit = (currentUser != null) || (isLoggedIn && email != null);
            if (canEdit) {
                Intent intent = new Intent(requireContext(), EditProfileActivity.class);
                startActivityForResult(intent, EDIT_PROFILE_REQUEST);
            } else {
                Toast.makeText(requireContext(), "Please log in to edit your profile", Toast.LENGTH_SHORT).show();
            }
        });

        menuButton.setOnClickListener(v -> showMenuDialog());
        if (profileLanguageButton != null) {
            profileLanguageButton.setOnClickListener(v -> showLanguageSelectionDialog());
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
        String[] menuItems = {"Language", "Help & Support", "About AgriDost", "Logout"};

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Menu");
        builder.setItems(menuItems, (dialog, which) -> {
            switch (which) {
                case 0:
                    showLanguageSelectionDialog();
                    break;
                case 1:
                    showHelpSupport();
                    break;
                case 2:
                    showAbout();
                    break;
                case 3:
                    logout();
                    break;
            }
        });
        builder.show();
    }

    private void showSettings() {
        Toast.makeText(requireContext(), "Settings feature coming soon!", Toast.LENGTH_SHORT).show();
    }

    private void showHelpSupport() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:support@agridost.com"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "AgriDost Support Request");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi AgriDost Support Team,\n\nI need help with:\n\n");
        if (emailIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            startActivity(emailIntent);
        } else {
            Toast.makeText(requireContext(), "No email app found. Please contact us at support@agridost.com", Toast.LENGTH_LONG).show();
        }
    }

    private void showLanguageSelectionDialog() {
        final String[] languageNames = new String[]{
                "हिन्दी (Hindi)",
                "मराठी (Marathi)",
                "ਪੰਜਾਬੀ (Punjabi)",
                "বাংলা (Bengali)",
                "ગુજરાતી (Gujarati)"
        };

        final String[] languageTags = new String[]{
                "hi",
                "mr",
                "pa",
                "bn",
                "gu"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Choose Language");
        builder.setItems(languageNames, (dialog, which) -> {
            String tag = languageTags[which];
            applyAppLanguage(tag);
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void applyAppLanguage(String languageTag) {
        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(languageTag);
        AppCompatDelegate.setApplicationLocales(appLocale);

        SharedPreferences prefs = requireContext().getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE);
        prefs.edit().putString("app_language", languageTag).apply();

        // Recreate host activity if attached to reflect changes
        if (getActivity() != null) {
            getActivity().recreate();
        }
    }

    private void showAbout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("About AgriDost");
        builder.setMessage("AgriDost v1.0\n\nYour agricultural assistant for crop management, plant disease identification, and farming advice.\n\nDeveloped with ❤️ for farmers worldwide.");
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Logout", (dialog, which) -> {
            // Clear one-time login flag
            SharedPreferences prefs = requireContext().getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE);
            prefs.edit()
                .putBoolean("is_logged_in", false)
                .remove("logged_in_email")
                .apply();

            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
            // Navigate to Welcome screen
            Intent intent = new Intent(requireContext(), WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            if (getActivity() != null) getActivity().finish();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void setupTabs() {
        profileTabs.addTab(profileTabs.newTab().setText("General"));
        profileTabs.addTab(profileTabs.newTab().setText("Community"));
        profileTabs.selectTab(profileTabs.getTabAt(0));
        profileTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    shareCard.setVisibility(View.VISIBLE);
                    feedbackCard.setVisibility(View.VISIBLE);
                } else if (position == 1) {
                    shareCard.setVisibility(View.GONE);
                    feedbackCard.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), "Community features coming soon!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupCards(View root) {
        MaterialButton shareButton = root.findViewById(R.id.share_button);
        shareButton.setOnClickListener(v -> shareAgriDost());

        MaterialButton feedbackButton = root.findViewById(R.id.feedback_button);
        feedbackButton.setOnClickListener(v -> giveFeedback());
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
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:feedback@agridost.com"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "AgriDost App Feedback");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi AgriDost Team,\n\nI would like to share my feedback about the app:\n\n");
        if (emailIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            startActivity(emailIntent);
        } else {
            Toast.makeText(requireContext(), "No email app found. Please contact us at feedback@agridost.com", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == Activity.RESULT_OK) {
            setupProfileInfo();
            Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setupProfileInfo();
    }
}
