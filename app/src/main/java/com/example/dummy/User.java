package com.example.dummy;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String name;
    private String email;
    private String description;
    private String avatarPath;
    private String location;
    private String phoneNumber;
    private long createdAt;
    private long lastLoginAt;

    // Default constructor
    public User() {
        this.createdAt = System.currentTimeMillis();
        this.lastLoginAt = System.currentTimeMillis();
    }

    // Constructor with basic info
    public User(String id, String name, String email) {
        this();
        this.id = id;
        this.name = name;
        this.email = email;
        this.description = "Describe yourself to others";
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(long lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    // Helper methods
    public boolean hasAvatar() {
        return avatarPath != null && !avatarPath.isEmpty();
    }

    public String getDisplayName() {
        return name != null && !name.isEmpty() ? name : "User";
    }

    public String getDisplayDescription() {
        return description != null && !description.isEmpty() ? description : "Describe yourself to others";
    }
}
