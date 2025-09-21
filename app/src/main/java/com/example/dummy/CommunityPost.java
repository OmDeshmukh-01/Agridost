package com.example.dummy;

import java.util.Date;

public class CommunityPost {
    private String id;
    private String title;
    private String description;
    private String cropType;
    private String imageUri;
    private String authorName;
    private String authorLocation;
    private Date timestamp;
    private int likes;
    private int dislikes;
    private int comments;

    public CommunityPost(String title, String description, String cropType, String imageUri) {
        this.id = String.valueOf(System.currentTimeMillis());
        this.title = title;
        this.description = description;
        this.cropType = cropType;
        this.imageUri = imageUri;
        this.authorName = "Farmer User";
        this.authorLocation = "India";
        this.timestamp = new Date();
        this.likes = 0;
        this.dislikes = 0;
        this.comments = 0;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCropType() { return cropType; }
    public String getImageUri() { return imageUri; }
    public String getAuthorName() { return authorName; }
    public String getAuthorLocation() { return authorLocation; }
    public Date getTimestamp() { return timestamp; }
    public int getLikes() { return likes; }
    public int getDislikes() { return dislikes; }
    public int getComments() { return comments; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCropType(String cropType) { this.cropType = cropType; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public void setAuthorLocation(String authorLocation) { this.authorLocation = authorLocation; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
    public void setLikes(int likes) { this.likes = likes; }
    public void setDislikes(int dislikes) { this.dislikes = dislikes; }
    public void setComments(int comments) { this.comments = comments; }
}
