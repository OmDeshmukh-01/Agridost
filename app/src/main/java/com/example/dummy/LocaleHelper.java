package com.example.dummy;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class LocaleHelper {
    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";
    private static final String PREFS_NAME = "language_prefs";

    // Supported languages
    public static final String ENGLISH = "en";
    public static final String HINDI = "hi";
    public static final String MARATHI = "mr";
    public static final String BENGALI = "bn";
    public static final String PUNJABI = "pa";

    /**
     * Set the app's locale to the specified language
     */
    public static Context setLocale(Context context, String language) {
        persist(context, language);
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        }
        
        return updateResourcesLegacy(context, language);
    }

    /**
     * Get the current language from SharedPreferences
     */
    public static String getLanguage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(SELECTED_LANGUAGE, ENGLISH); // Default to English
    }

    /**
     * Persist the selected language to SharedPreferences
     */
    private static void persist(Context context, String language) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(SELECTED_LANGUAGE, language).apply();
    }

    /**
     * Update resources for API 24+
     */
    private static Context updateResources(Context context, String language) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            locale = new Locale.Builder().setLanguage(language).build();
        } else {
            locale = new Locale(language);
        }
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    /**
     * Update resources for API < 24
     */
    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            locale = new Locale.Builder().setLanguage(language).build();
        } else {
            locale = new Locale(language);
        }
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }

    /**
     * Attach the saved locale to the context
     */
    public static Context onAttach(Context context) {
        String language = getLanguage(context);
        return setLocale(context, language);
    }

    /**
     * Get language display name
     */
    public static String getLanguageDisplayName(String languageCode) {
        switch (languageCode) {
            case ENGLISH:
                return "English";
            case HINDI:
                return "हिंदी";
            case MARATHI:
                return "मराठी";
            case BENGALI:
                return "বাংলা";
            case PUNJABI:
                return "ਪੰਜਾਬੀ";
            default:
                return "English";
        }
    }

    /**
     * Get all supported languages
     */
    public static String[] getSupportedLanguages() {
        return new String[]{ENGLISH, HINDI, MARATHI, BENGALI, PUNJABI};
    }

    /**
     * Get all supported language display names
     */
    public static String[] getSupportedLanguageNames() {
        return new String[]{
            getLanguageDisplayName(ENGLISH),
            getLanguageDisplayName(HINDI),
            getLanguageDisplayName(MARATHI),
            getLanguageDisplayName(BENGALI),
            getLanguageDisplayName(PUNJABI)
        };
    }
}
