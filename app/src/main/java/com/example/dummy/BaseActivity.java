package com.example.dummy;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        String currentLang = LocaleHelper.getLanguage(newBase);
        android.util.Log.d("BaseActivity", "attachBaseContext - Language: " + currentLang);
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ensure locale is applied when activity is created
        LocaleHelper.onAttach(this);
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate a common menu with a Language item for all activities
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_language) {
            showLanguageSelectionDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void showLanguageSelectionDialog() {
        String[] languageNames = LocaleHelper.getSupportedLanguageNames();
        String[] languageCodes = LocaleHelper.getSupportedLanguages();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_language));
        builder.setItems(languageNames, (dialog, which) -> {
            String selectedLanguage = languageCodes[which];
            changeLanguage(selectedLanguage);
        });
        builder.setNegativeButton(getString(R.string.cancel), null);
        builder.show();
    }

    private void changeLanguage(String languageCode) {
        // Debug: Show what language we're changing to
        Toast.makeText(this, "Changing to: " + languageCode, Toast.LENGTH_SHORT).show();
        
        // Update locale using LocaleHelper
        LocaleHelper.setLocale(this, languageCode);
        
        // Debug: Verify the language was saved
        String savedLang = LocaleHelper.getLanguage(this);
        Toast.makeText(this, "Saved language: " + savedLang, Toast.LENGTH_SHORT).show();
        
        Toast.makeText(this, getString(R.string.language_changed), Toast.LENGTH_SHORT).show();

        // Restart the whole app task so all Activities/Fragments pick up new locale
        android.content.SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);

        android.content.Intent intent;
        if (isLoggedIn) {
            intent = new android.content.Intent(this, com.example.dummy.Main.MainActivity.class);
        } else {
            intent = new android.content.Intent(this, com.example.dummy.Autentication.WelcomeActivity.class);
        }
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}


