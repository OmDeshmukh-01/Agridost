package com.example.dummy;

import java.util.ArrayList;
import java.util.List;

public class CommunityDatabase {
    private static List<CommunityPost> posts = new ArrayList<>();
    
    static {
        // Add some sample posts
        addSamplePosts();
    }
    
    private static void addSamplePosts() {
        posts.add(new CommunityPost(
            "Tomato blight treatment",
            "My tomato plants are showing signs of early blight. The leaves have brown spots and are starting to yellow. What's the best treatment?",
            "Tomato",
            null
        ));
        
        posts.add(new CommunityPost(
            "Pea plant spacing",
            "How far apart should I plant my pea seeds? I'm growing them in raised beds.",
            "Pea",
            null
        ));
        
        posts.add(new CommunityPost(
            "Cotton pest control",
            "I've noticed small insects on my cotton plants. They look like aphids. What's the best organic way to control them?",
            "Cotton",
            null
        ));
        
        posts.add(new CommunityPost(
            "Onion harvesting time",
            "When is the right time to harvest onions? The tops are starting to fall over but I'm not sure if they're ready.",
            "Onion",
            null
        ));
    }
    
    public static void addPost(CommunityPost post) {
        posts.add(0, post); // Add to beginning of list (newest first)
    }
    
    public static List<CommunityPost> getAllPosts() {
        return new ArrayList<>(posts);
    }
    
    public static List<CommunityPost> getPostsByCropType(String cropType) {
        List<CommunityPost> filteredPosts = new ArrayList<>();
        for (CommunityPost post : posts) {
            if (post.getCropType().equalsIgnoreCase(cropType)) {
                filteredPosts.add(post);
            }
        }
        return filteredPosts;
    }
    
    public static CommunityPost getPostById(String id) {
        for (CommunityPost post : posts) {
            if (post.getId().equals(id)) {
                return post;
            }
        }
        return null;
    }
    
    public static void likePost(String postId) {
        CommunityPost post = getPostById(postId);
        if (post != null) {
            post.setLikes(post.getLikes() + 1);
        }
    }
    
    public static void dislikePost(String postId) {
        CommunityPost post = getPostById(postId);
        if (post != null) {
            post.setDislikes(post.getDislikes() + 1);
        }
    }
    
    public static void addComment(String postId) {
        CommunityPost post = getPostById(postId);
        if (post != null) {
            post.setComments(post.getComments() + 1);
        }
    }
}
