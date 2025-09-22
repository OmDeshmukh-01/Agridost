package com.example.dummy;

/**
 * Configuration class for API keys and settings
 * 
 * IMPORTANT: Replace "YOUR_HUGGING_FACE_TOKEN" with your actual Hugging Face token
 * You can get your token from: https://huggingface.co/settings/tokens
 */
public class ApiConfig {
    // Hugging Face API token
    public static final String HF_API_TOKEN = "YOUR_HUGGING_FACE_TOKEN";
    
    // Hugging Face API settings
    public static final String HF_API_URL = "https://api-inference.huggingface.co/models/microsoft/DialoGPT-medium";
    public static final String HF_API_URL_LARGE = "https://api-inference.huggingface.co/models/microsoft/DialoGPT-large";
    public static final String HF_API_URL_AGRICULTURE = "https://api-inference.huggingface.co/models/facebook/blenderbot-400M-distill";
    public static final String HF_API_URL_FALLBACK = "https://api-inference.huggingface.co/models/gpt2";
    
    // Model parameters
    public static final int MAX_LENGTH = 200;
    public static final double TEMPERATURE = 0.7;
    public static final double TOP_P = 0.9;
    public static final int TOP_K = 50;
    
    // System prompt for agricultural context
    public static final String SYSTEM_PROMPT = 
        "You are AgriDost, a helpful agricultural assistant. " +
        "You provide expert advice on farming, crop management, plant diseases, " +
        "soil health, weather patterns, and agricultural best practices. " +
        "Keep your responses concise, practical, and focused on agriculture. " +
        "If asked about non-agricultural topics, politely redirect to farming-related questions. " +
        "Always provide actionable advice that farmers can implement.";
}
