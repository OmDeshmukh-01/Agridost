package com.example.dummy.services;

import android.os.AsyncTask;
import android.util.Log;

import com.example.dummy.network.ApiConfig;
import com.example.dummy.ChatMessage;
import com.example.dummy.HFRequest;
import com.example.dummy.HFResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RealtimeChatService {
    private static final String TAG = "RealtimeChatService";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    
    private final String apiToken;
    private final OkHttpClient client;
    private final Gson gson;

    public interface ChatCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    public RealtimeChatService(String apiToken) {
        this.apiToken = apiToken;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();
        this.gson = new GsonBuilder().create();
    }

    public void sendMessage(String userMessage, List<ChatMessage> chatHistory, ChatCallback callback) {
        new RealtimeChatTask(callback).execute(userMessage, chatHistory);
    }

    private class RealtimeChatTask extends AsyncTask<Object, Void, String> {
        private final ChatCallback callback;
        private String errorMessage = null;

        public RealtimeChatTask(ChatCallback callback) {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Object... params) {
            String userMessage = (String) params[0];
            @SuppressWarnings("unchecked")
            List<ChatMessage> chatHistory = (List<ChatMessage>) params[1];

            // Try multiple API endpoints in order of preference
            String[] apiUrls = {
                ApiConfig.HF_API_URL,
                ApiConfig.HF_API_URL_LARGE,
                ApiConfig.HF_API_URL_AGRICULTURE,
                ApiConfig.HF_API_URL_FALLBACK
            };

            for (String apiUrl : apiUrls) {
                String response = tryAPI(apiUrl, userMessage);
                if (response != null && !response.trim().isEmpty()) {
                    Log.d(TAG, "Success with API: " + apiUrl);
                    return response;
                }
            }

            // If all APIs fail, return a simple fallback
            return "I'm experiencing technical difficulties right now. Please try again in a moment, or ask me about specific agricultural topics like crop diseases, soil health, or farming techniques.";
        }

        private String tryAPI(String apiUrl, String userMessage) {
            try {
                // Create a context-aware prompt
                String prompt = buildContextualPrompt(userMessage);

                HFRequest.Parameters parameters = new HFRequest.Parameters(
                        200, // Longer responses
                        0.8, // Higher creativity
                        0.9,
                        50,
                        true,
                        50256
                );

                HFRequest request = new HFRequest(prompt, parameters);
                String jsonRequest = gson.toJson(request);

                RequestBody body = RequestBody.create(jsonRequest, JSON);
                Request httpRequest = new Request.Builder()
                        .url(apiUrl)
                        .addHeader("Authorization", "Bearer " + apiToken)
                        .addHeader("Content-Type", "application/json")
                        .post(body)
                        .build();

                Response response = client.newCall(httpRequest).execute();
                String responseBody = response.body().string();

                Log.d(TAG, "API: " + apiUrl + " | Code: " + response.code());
                Log.d(TAG, "Response: " + responseBody);

                if (response.isSuccessful()) {
                    String apiResponse = parseResponse(responseBody);
                    if (apiResponse != null) {
                        String cleanedResponse = cleanResponse(apiResponse);
                        if (cleanedResponse != null && !cleanedResponse.trim().isEmpty() && 
                            cleanedResponse.length() > 10) { // Ensure meaningful response
                            return cleanedResponse;
                        }
                    }
                } else {
                    Log.w(TAG, "API failed: " + apiUrl + " | Code: " + response.code());
                }
            } catch (Exception e) {
                Log.e(TAG, "API exception for " + apiUrl, e);
            }
            
            return null;
        }

        private String buildContextualPrompt(String userMessage) {
            return "You are AgriDost, an expert agricultural assistant. " +
                    "You provide practical, actionable advice on farming, crop management, plant diseases, soil health, pest control, and agricultural best practices. " +
                    "Your responses are helpful, accurate, and focused on agriculture. " +
                    "If asked about non-agricultural topics, politely redirect to farming-related questions. " +
                    "Always provide specific, implementable advice that farmers can use. " +
                    "\n\nUser Question: " + userMessage + "\n\nAgriDost Response:";
        }

        private String parseResponse(String responseBody) {
            try {
                if (responseBody.trim().startsWith("[")) {
                    HFResponse[] responses = gson.fromJson(responseBody, HFResponse[].class);
                    if (responses.length > 0 && responses[0].getGeneratedText() != null) {
                        return responses[0].getGeneratedText();
                    }
                } else {
                    HFResponse hfResponse = gson.fromJson(responseBody, HFResponse.class);
                    if (hfResponse.getGeneratedText() != null) {
                        return hfResponse.getGeneratedText();
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Failed to parse response", e);
            }
            return null;
        }

        private String cleanResponse(String response) {
            if (response == null) return null;
            
            String cleaned = response.trim();
            
            // Remove input context
            if (cleaned.contains("User Question:")) {
                String[] parts = cleaned.split("User Question:");
                if (parts.length > 0) {
                    cleaned = parts[0].trim();
                }
            }
            
            // Remove "AgriDost Response:" prefix
            if (cleaned.startsWith("AgriDost Response:")) {
                cleaned = cleaned.substring("AgriDost Response:".length()).trim();
            }
            
            // Remove any remaining system instructions
            if (cleaned.contains("You are AgriDost")) {
                int agriDostIndex = cleaned.indexOf("You are AgriDost");
                if (agriDostIndex > 0) {
                    cleaned = cleaned.substring(0, agriDostIndex).trim();
                }
            }
            
            // Clean up artifacts
            cleaned = cleaned.replaceAll("^[^a-zA-Z]*", ""); // Remove leading non-letters
            cleaned = cleaned.replaceAll("\\s+", " "); // Normalize whitespace
            
            return cleaned.isEmpty() ? null : cleaned;
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
