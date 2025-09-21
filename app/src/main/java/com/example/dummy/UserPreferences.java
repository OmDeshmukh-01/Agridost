package com.example.dummy;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {
    private static final String PREFS_NAME = "user_preferences";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_REMEMBER_ME = "remember_me";
    private static final String KEY_LAST_LOGIN = "last_login";
    
    private static UserPreferences instance;
    private SharedPreferences preferences;

    private UserPreferences(Context context) {
        this.preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized UserPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new UserPreferences(context);
        }
        return instance;
    }

    // Login state management
    public void setLoggedIn(boolean isLoggedIn) {
        preferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // User info management
    public void setUserId(String userId) {
        preferences.edit().putString(KEY_USER_ID, userId).apply();
    }

    public String getUserId() {
        return preferences.getString(KEY_USER_ID, null);
    }

    public void setUserEmail(String email) {
        preferences.edit().putString(KEY_USER_EMAIL, email).apply();
    }

    public String getUserEmail() {
        return preferences.getString(KEY_USER_EMAIL, null);
    }

    public void setUserName(String name) {
        preferences.edit().putString(KEY_USER_NAME, name).apply();
    }

    public String getUserName() {
        return preferences.getString(KEY_USER_NAME, null);
    }

    // Remember me functionality
    public void setRememberMe(boolean remember) {
        preferences.edit().putBoolean(KEY_REMEMBER_ME, remember).apply();
    }

    public boolean getRememberMe() {
        return preferences.getBoolean(KEY_REMEMBER_ME, false);
    }

    // Last login tracking
    public void setLastLogin(long timestamp) {
        preferences.edit().putLong(KEY_LAST_LOGIN, timestamp).apply();
    }

    public long getLastLogin() {
        return preferences.getLong(KEY_LAST_LOGIN, 0);
    }

    // Clear all user data
    public void clearUserData() {
        preferences.edit()
                .remove(KEY_IS_LOGGED_IN)
                .remove(KEY_USER_ID)
                .remove(KEY_USER_EMAIL)
                .remove(KEY_USER_NAME)
                .remove(KEY_REMEMBER_ME)
                .remove(KEY_LAST_LOGIN)
                .apply();
    }

    // Save user session
    public void saveUserSession(User user) {
        if (user != null) {
            setLoggedIn(true);
            setUserId(user.getId());
            setUserEmail(user.getEmail());
            setUserName(user.getName());
            setLastLogin(System.currentTimeMillis());
        }
    }

    // Load user session
    public User loadUserSession() {
        if (isLoggedIn() && getUserId() != null) {
            User user = new User();
            user.setId(getUserId());
            user.setEmail(getUserEmail());
            user.setName(getUserName());
            user.setLastLoginAt(getLastLogin());
            return user;
        }
        return null;
    }
}
