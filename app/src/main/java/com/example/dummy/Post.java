package com.example.dummy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Post implements Serializable {
    private String id;
    private String title;
    private String content;
    private String authorName;
    private String authorLocation;
    private String cropType;
    private String timeAgo;
    private String imageUrl;
    private int likesCount;
    private int dislikesCount;
    private int commentsCount;
    private List<Comment> comments;
    private boolean isLiked;
    private boolean isDisliked;
    
    public Post() {
        this.comments = new ArrayList<>();
        this.likesCount = 0;
        this.dislikesCount = 0;
        this.commentsCount = 0;
        this.isLiked = false;
        this.isDisliked = false;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
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
    
    public String getCropType() {
        return cropType;
    }
    
    public void setCropType(String cropType) {
        this.cropType = cropType;
    }
    
    public String getTimeAgo() {
        return timeAgo;
    }
    
    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public int getLikesCount() {
        return likesCount;
    }
    
    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }
    
    public int getDislikesCount() {
        return dislikesCount;
    }
    
    public void setDislikesCount(int dislikesCount) {
        this.dislikesCount = dislikesCount;
    }
    
    public int getCommentsCount() {
        return commentsCount;
    }
    
    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }
    
    public List<Comment> getComments() {
        return comments;
    }
    
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    
    public boolean isLiked() {
        return isLiked;
    }
    
    public void setLiked(boolean liked) {
        isLiked = liked;
    }
    
    public boolean isDisliked() {
        return isDisliked;
    }
    
    public void setDisliked(boolean disliked) {
        isDisliked = disliked;
    }
    
    public void addComment(Comment comment) {
        this.comments.add(comment);
        this.commentsCount = this.comments.size();
    }
    
    public void removeComment(Comment comment) {
        this.comments.remove(comment);
        this.commentsCount = this.comments.size();
    }
}
