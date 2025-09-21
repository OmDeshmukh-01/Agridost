package com.example.dummy;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HybridChatService {
    private static final String TAG = "HybridChatService";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    
    private final String apiToken;
    private final OkHttpClient client;
    private final Gson gson;
    private final Random random;

    public interface ChatCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    public HybridChatService(String apiToken) {
        this.apiToken = apiToken;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        this.gson = new GsonBuilder().create();
        this.random = new Random();
    }

    public void sendMessage(String userMessage, List<ChatMessage> chatHistory, ChatCallback callback) {
        new ChatTask(callback).execute(userMessage, chatHistory);
    }

    private class ChatTask extends AsyncTask<Object, Void, String> {
        private final ChatCallback callback;
        private String errorMessage = null;

        public ChatTask(ChatCallback callback) {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Object... params) {
            String userMessage = (String) params[0];
            @SuppressWarnings("unchecked")
            List<ChatMessage> chatHistory = (List<ChatMessage>) params[1];

            // First try Hugging Face API with multiple models
            String hfResponse = tryHuggingFaceAPI(userMessage);
            if (hfResponse != null && !hfResponse.trim().isEmpty()) {
                Log.d(TAG, "Successfully got HF API response: " + hfResponse);
                return hfResponse;
            }

            // Try alternative HF models
            hfResponse = tryAlternativeHFAPI(userMessage);
            if (hfResponse != null && !hfResponse.trim().isEmpty()) {
                Log.d(TAG, "Successfully got alternative HF API response: " + hfResponse);
                return hfResponse;
            }

            // If all HF attempts fail, use intelligent fallback responses
            Log.d(TAG, "Using fallback response for: " + userMessage);
            return generateIntelligentResponse(userMessage);
        }

        private String tryHuggingFaceAPI(String userMessage) {
            try {
                // Enhanced prompt for better agricultural responses
                String prompt = "You are AgriDost, a helpful agricultural assistant. " +
                        "Provide expert advice on farming, crop management, plant diseases, soil health, and agricultural best practices. " +
                        "Keep responses concise and practical. " +
                        "Human: " + userMessage + "\nAgriDost:";

                HFRequest.Parameters parameters = new HFRequest.Parameters(
                        150, // Increased response length
                        0.7, // Balanced creativity
                        0.9,
                        50,
                        true,
                        50256
                );

                HFRequest request = new HFRequest(prompt, parameters);
                String jsonRequest = gson.toJson(request);

                RequestBody body = RequestBody.create(jsonRequest, JSON);
                Request httpRequest = new Request.Builder()
                        .url(ApiConfig.HF_API_URL)
                        .addHeader("Authorization", "Bearer " + apiToken)
                        .addHeader("Content-Type", "application/json")
                        .post(body)
                        .build();

                Response response = client.newCall(httpRequest).execute();
                String responseBody = response.body().string();

                Log.d(TAG, "HF API Response Code: " + response.code());
                Log.d(TAG, "HF API Response Body: " + responseBody);

                if (response.isSuccessful()) {
                    String apiResponse = null;
                    
                    // Try to parse response
                    if (responseBody.trim().startsWith("[")) {
                        HFResponse[] responses = gson.fromJson(responseBody, HFResponse[].class);
                        if (responses.length > 0 && responses[0].getGeneratedText() != null) {
                            apiResponse = responses[0].getGeneratedText();
                        }
                    } else {
                        HFResponse hfResponse = gson.fromJson(responseBody, HFResponse.class);
                        if (hfResponse.getGeneratedText() != null) {
                            apiResponse = hfResponse.getGeneratedText();
                        }
                    }
                    
                    if (apiResponse != null) {
                        String cleanedResponse = cleanResponse(apiResponse);
                        if (cleanedResponse != null && !cleanedResponse.trim().isEmpty() && 
                            !cleanedResponse.equals("I'm here to help with your agricultural questions. What would you like to know?")) {
                            Log.d(TAG, "Using HF API response: " + cleanedResponse);
                            return cleanedResponse;
                        }
                    }
                } else {
                    Log.w(TAG, "HF API failed with code: " + response.code() + ", body: " + responseBody);
                }
            } catch (Exception e) {
                Log.e(TAG, "HF API exception, using fallback", e);
            }
            
            return null;
        }

        private String tryAlternativeHFAPI(String userMessage) {
            try {
                // Try with a simpler model
                String prompt = userMessage + " Please provide agricultural advice.";

                HFRequest.Parameters parameters = new HFRequest.Parameters(
                        100, // Shorter response
                        0.6, // Lower temperature for more focused responses
                        0.9,
                        50,
                        true,
                        50256
                );

                HFRequest request = new HFRequest(prompt, parameters);
                String jsonRequest = gson.toJson(request);

                RequestBody body = RequestBody.create(jsonRequest, JSON);
                Request httpRequest = new Request.Builder()
                        .url(ApiConfig.HF_API_URL_FALLBACK)
                        .addHeader("Authorization", "Bearer " + apiToken)
                        .addHeader("Content-Type", "application/json")
                        .post(body)
                        .build();

                Response response = client.newCall(httpRequest).execute();
                String responseBody = response.body().string();

                Log.d(TAG, "Alternative HF API Response Code: " + response.code());
                Log.d(TAG, "Alternative HF API Response Body: " + responseBody);

                if (response.isSuccessful()) {
                    String apiResponse = null;
                    
                    // Try to parse response
                    if (responseBody.trim().startsWith("[")) {
                        HFResponse[] responses = gson.fromJson(responseBody, HFResponse[].class);
                        if (responses.length > 0 && responses[0].getGeneratedText() != null) {
                            apiResponse = responses[0].getGeneratedText();
                        }
                    } else {
                        HFResponse hfResponse = gson.fromJson(responseBody, HFResponse.class);
                        if (hfResponse.getGeneratedText() != null) {
                            apiResponse = hfResponse.getGeneratedText();
                        }
                    }
                    
                    if (apiResponse != null) {
                        String cleanedResponse = cleanResponse(apiResponse);
                        if (cleanedResponse != null && !cleanedResponse.trim().isEmpty()) {
                            Log.d(TAG, "Using alternative HF API response: " + cleanedResponse);
                            return cleanedResponse;
                        }
                    }
                } else {
                    Log.w(TAG, "Alternative HF API failed with code: " + response.code() + ", body: " + responseBody);
                }
            } catch (Exception e) {
                Log.e(TAG, "Alternative HF API exception", e);
            }
            
            return null;
        }

        private String cleanResponse(String response) {
            if (response == null) return null;
            
            String cleaned = response.trim();
            
            // Remove input context
            if (cleaned.contains("Human:")) {
                String[] parts = cleaned.split("Human:");
                if (parts.length > 0) {
                    cleaned = parts[0].trim();
                }
            }
            
            // Remove "AgriDost:" prefix
            if (cleaned.startsWith("AgriDost:")) {
                cleaned = cleaned.substring("AgriDost:".length()).trim();
            }
            
            // Remove any remaining prompt text
            if (cleaned.contains("You are AgriDost")) {
                int agriDostIndex = cleaned.indexOf("You are AgriDost");
                if (agriDostIndex > 0) {
                    cleaned = cleaned.substring(0, agriDostIndex).trim();
                }
            }
            
            // Remove any system instructions
            if (cleaned.contains("Provide expert advice")) {
                int adviceIndex = cleaned.indexOf("Provide expert advice");
                if (adviceIndex > 0) {
                    cleaned = cleaned.substring(0, adviceIndex).trim();
                }
            }
            
            // Clean up any remaining artifacts
            cleaned = cleaned.replaceAll("^[^a-zA-Z]*", ""); // Remove leading non-letters
            cleaned = cleaned.replaceAll("\\s+", " "); // Normalize whitespace
            
            return cleaned.isEmpty() ? null : cleaned;
        }

        private String generateIntelligentResponse(String userMessage) {
            String lowerMessage = userMessage.toLowerCase();
            
            // Agricultural responses based on keywords
            if (lowerMessage.contains("hello") || lowerMessage.contains("hi") || lowerMessage.contains("hey")) {
                return getRandomResponse(new String[]{
                    "Hello! I'm AgriDost, your agricultural assistant. How can I help you with your farming needs today?",
                    "Hi there! I'm here to help with all your agricultural questions. What would you like to know?",
                    "Greetings! I'm AgriDost, ready to assist with crop management, plant health, and farming advice."
                });
            }
            
            if (lowerMessage.contains("plant") && lowerMessage.contains("disease")) {
                return getRandomResponse(new String[]{
                    "I can help you identify plant diseases! Please describe the symptoms you're seeing - like yellowing leaves, spots, wilting, or unusual growth patterns.",
                    "Plant disease identification is crucial for healthy crops. Tell me about the affected plants and their symptoms for accurate diagnosis.",
                    "I'm here to help with plant diseases. Describe the visual symptoms and I'll guide you on potential causes and treatments."
                });
            }
            
            if (lowerMessage.contains("crop") || lowerMessage.contains("farming")) {
                return getRandomResponse(new String[]{
                    "I'd be happy to help with crop management! What specific crop are you growing and what challenges are you facing?",
                    "Crop management is key to successful farming. Tell me about your crops and I'll provide expert advice.",
                    "I'm here to assist with all farming questions. What crops are you working with and what do you need help with?"
                });
            }
            
            if (lowerMessage.contains("soil") || lowerMessage.contains("fertilizer")) {
                return getRandomResponse(new String[]{
                    "Soil health is fundamental to good farming! I can help with soil testing, fertilization, and improvement strategies.",
                    "Great question about soil! Tell me about your soil type and current condition for personalized advice.",
                    "Soil management is crucial for crop success. What specific soil issues are you dealing with?"
                });
            }
            
            if (lowerMessage.contains("weather") || lowerMessage.contains("season")) {
                return getRandomResponse(new String[]{
                    "Weather and seasonal planning are essential for farming success. I can help with planting schedules and weather-related advice.",
                    "Seasonal farming advice is my specialty! What's your location and what crops are you planning for this season?",
                    "Weather patterns greatly affect farming. Tell me about your region and I'll provide seasonal guidance."
                });
            }
            
            if (lowerMessage.contains("water") || lowerMessage.contains("irrigation")) {
                return getRandomResponse(new String[]{
                    "Proper watering is crucial for plant health! I can help with irrigation schedules and water management strategies.",
                    "Water management is key to successful farming. Tell me about your irrigation system and I'll provide guidance.",
                    "I can help with watering schedules and irrigation advice. What type of plants are you watering?"
                });
            }
            
            if (lowerMessage.contains("pest") || lowerMessage.contains("insect")) {
                return getRandomResponse(new String[]{
                    "Pest management is important for healthy crops! Describe the pests you're seeing and I'll suggest control methods.",
                    "I can help identify and control agricultural pests. What insects or damage are you observing?",
                    "Pest problems can be challenging. Tell me about the specific pests and I'll recommend solutions."
                });
            }
            
            // General agricultural response
            return getRandomResponse(new String[]{
                "I'm AgriDost, your agricultural assistant! I can help with crop management, plant diseases, soil health, pest control, and farming best practices. What would you like to know?",
                "Hello! I specialize in agricultural advice including crop care, plant health, soil management, and farming techniques. How can I assist you today?",
                "I'm here to help with all your farming questions! Whether it's about crops, soil, pests, diseases, or general agriculture - I'm ready to assist."
            });
        }

        private String getRandomResponse(String[] responses) {
            return responses[random.nextInt(responses.length)];
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null && !result.trim().isEmpty()) {
                callback.onSuccess(result);
            } else {
                callback.onError("Unable to generate response. Please try again.");
            }
        }
    }
}
