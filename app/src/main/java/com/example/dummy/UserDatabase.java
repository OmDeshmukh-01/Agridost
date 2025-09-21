package com.example.dummy;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserDatabase {
    private static final String TAG = "UserDatabase";
    private static final String PREFS_NAME = "user_database";
    private static final String KEY_USERS = "users";
    private static final String KEY_CURRENT_USER = "current_user";
    
    private static UserDatabase instance;
    private SharedPreferences preferences;
    private List<User> users;
    private User currentUser;
    private Gson gson;

    private UserDatabase(Context context) {
        this.preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
        loadUsers();
        loadCurrentUser();
    }

    public static synchronized UserDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new UserDatabase(context);
        }
        return instance;
    }

    private void loadUsers() {
        String usersJson = preferences.getString(KEY_USERS, null);
        if (usersJson != null) {
            Type listType = new TypeToken<List<User>>(){}.getType();
            users = gson.fromJson(usersJson, listType);
        } else {
            users = new ArrayList<>();
            // Add some sample users for testing
            addSampleUsers();
        }
    }

    private void loadCurrentUser() {
        String currentUserJson = preferences.getString(KEY_CURRENT_USER, null);
        if (currentUserJson != null) {
            currentUser = gson.fromJson(currentUserJson, User.class);
        }
    }

    private void saveUsers() {
        String usersJson = gson.toJson(users);
        preferences.edit().putString(KEY_USERS, usersJson).apply();
    }

    private void saveCurrentUser() {
        if (currentUser != null) {
            String currentUserJson = gson.toJson(currentUser);
            preferences.edit().putString(KEY_CURRENT_USER, currentUserJson).apply();
        }
    }

    private void addSampleUsers() {
        // Add sample users for testing
        User user1 = new User("1", "Samir", "samir@example.com");
        user1.setDescription("Passionate farmer and agricultural enthusiast");
        user1.setLocation("Pune, India");
        user1.setPhoneNumber("+91 9876543210");
        
        User user2 = new User("2", "Priya", "priya@example.com");
        user2.setDescription("Organic farming specialist");
        user2.setLocation("Mumbai, India");
        user2.setPhoneNumber("+91 9876543211");
        
        users.add(user1);
        users.add(user2);
        saveUsers();
    }

    // User management methods
    public void addUser(User user) {
        if (user != null && !userExists(user.getEmail())) {
            users.add(user);
            saveUsers();
            Log.d(TAG, "User added: " + user.getEmail());
        }
    }

    public void updateUser(User user) {
        if (user != null) {
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId().equals(user.getId())) {
                    users.set(i, user);
                    saveUsers();
                    Log.d(TAG, "User updated: " + user.getEmail());
                    break;
                }
            }
        }
    }

    public void deleteUser(String userId) {
        users.removeIf(user -> user.getId().equals(userId));
        saveUsers();
        Log.d(TAG, "User deleted: " + userId);
    }

    public User getUserById(String userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    public User getUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    public boolean userExists(String email) {
        return getUserByEmail(email) != null;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    // Current user management
    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (user != null) {
            user.setLastLoginAt(System.currentTimeMillis());
            updateUser(user);
        }
        saveCurrentUser();
        Log.d(TAG, "Current user set: " + (user != null ? user.getEmail() : "null"));
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isUserLoggedIn() {
        return currentUser != null;
    }

    public void logout() {
        currentUser = null;
        preferences.edit().remove(KEY_CURRENT_USER).apply();
        Log.d(TAG, "User logged out");
    }

    // Authentication methods
    public User login(String email, String password) {
        User user = getUserByEmail(email);
        if (user != null) {
            // For demo purposes, accept any password
            // In a real app, you would verify the password hash
            setCurrentUser(user);
            return user;
        }
        return null;
    }

    public User register(String name, String email, String password) {
        if (!userExists(email)) {
            String userId = String.valueOf(System.currentTimeMillis());
            User newUser = new User(userId, name, email);
            addUser(newUser);
            setCurrentUser(newUser);
            return newUser;
        }
        return null;
    }

    // Profile update methods
    public void updateProfile(String name, String description, String location, String phoneNumber) {
        if (currentUser != null) {
            currentUser.setName(name);
            currentUser.setDescription(description);
            currentUser.setLocation(location);
            currentUser.setPhoneNumber(phoneNumber);
            updateUser(currentUser);
            saveCurrentUser();
        }
    }

    public void updateAvatar(String avatarPath) {
        if (currentUser != null) {
            currentUser.setAvatarPath(avatarPath);
            updateUser(currentUser);
            saveCurrentUser();
        }
    }
}
