package com.example.dummy;

import java.io.Serializable;

public class Comment implements Serializable {
    private String id;
    private String postId;
    private String authorName;
    private String authorLocation;
    private String content;
    private String timeAgo;
    private int likesCount;
    private boolean isLiked;
    
    public Comment() {
        this.likesCount = 0;
        this.isLiked = false;
    }
    
    public Comment(String postId, String authorName, String authorLocation, String content, String timeAgo) {
        this();
        this.postId = postId;
        this.authorName = authorName;
        this.authorLocation = authorLocation;
        this.content = content;
        this.timeAgo = timeAgo;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getPostId() {
        return postId;
    }
    
    public void setPostId(String postId) {
        this.postId = postId;
    }
    
    public String getAuthorName() {
        return authorName;
    }
    
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    
    public String getAuthorLocation() {
        return authorLocation;
    }
    
    public void setAuthorLocation(String authorLocation) {
        this.authorLocation = authorLocation;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getTimeAgo() {
        return timeAgo;
    }
    
    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }
    
    public int getLikesCount() {
        return likesCount;
    }
    
    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }
    
    public boolean isLiked() {
        return isLiked;
    }
    
    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
