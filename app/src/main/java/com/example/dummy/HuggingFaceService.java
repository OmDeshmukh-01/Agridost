package com.example.dummy;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HuggingFaceService {
    private static final String TAG = "HuggingFaceService";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    
    private final String apiToken;
    private final OkHttpClient client;
    private final Gson gson;

    public interface ChatCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    public HuggingFaceService(String apiToken) {
        this.apiToken = apiToken;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        this.gson = new GsonBuilder().create();
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

            try {
                // Build conversation context - simplified for better compatibility
                StringBuilder conversationContext = new StringBuilder();
                conversationContext.append("Human: ").append(userMessage).append("\n");
                conversationContext.append("AgriDost: I'm AgriDost, your agricultural assistant. ");

                // Create request
                HFRequest.Parameters parameters = new HFRequest.Parameters(
                        ApiConfig.MAX_LENGTH,
                        ApiConfig.TEMPERATURE,
                        ApiConfig.TOP_P,
                        ApiConfig.TOP_K,
                        true,
                        50256 // pad_token_id for DialoGPT
                );

                HFRequest request = new HFRequest(conversationContext.toString(), parameters);

                String jsonRequest = gson.toJson(request);
                Log.d(TAG, "Request: " + jsonRequest);

                RequestBody body = RequestBody.create(jsonRequest, JSON);
                Request httpRequest = new Request.Builder()
                        .url(ApiConfig.HF_API_URL)
                        .addHeader("Authorization", "Bearer " + apiToken)
                        .addHeader("Content-Type", "application/json")
                        .post(body)
                        .build();

                Response response = client.newCall(httpRequest).execute();
                String responseBody = response.body().string();

                if (response.isSuccessful()) {
                    Log.d(TAG, "Response: " + responseBody);
                    
                    // Handle different response formats
                    if (responseBody.trim().startsWith("[")) {
                        // Array response format
                        HFResponse[] responses = gson.fromJson(responseBody, HFResponse[].class);
                        if (responses.length > 0 && responses[0].getGeneratedText() != null) {
                            String generatedText = responses[0].getGeneratedText();
                            String botResponse = extractBotResponse(generatedText);
                            return botResponse;
                        }
                    } else {
                        // Single object response format
                        HFResponse hfResponse = gson.fromJson(responseBody, HFResponse.class);
                        
                        if (hfResponse.getGeneratedText() != null && !hfResponse.getGeneratedText().trim().isEmpty()) {
                            String generatedText = hfResponse.getGeneratedText();
                            String botResponse = extractBotResponse(generatedText);
                            return botResponse;
                        } else if (hfResponse.getError() != null) {
                            errorMessage = "HF API Error: " + hfResponse.getError();
                            return null;
                        }
                    }
                    
                    // If we get here, no valid response was found
                    return "I'm here to help with your agricultural questions. What would you like to know?";
                } else {
                    Log.e(TAG, "API Error: " + response.code() + " - " + responseBody);
                    errorMessage = "API Error " + response.code() + ": " + responseBody;
                    return null;
                }

            } catch (IOException e) {
                Log.e(TAG, "Network error", e);
                errorMessage = "Network error: " + e.getMessage();
                return null;
            } catch (Exception e) {
                Log.e(TAG, "Unexpected error", e);
                errorMessage = "Unexpected error: " + e.getMessage();
                return null;
            }
        }

        private String extractBotResponse(String generatedText) {
            // Simple extraction - just clean up the response
            String result = generatedText.trim();
            
            // Remove the input context if it's still there
            if (result.contains("Human:")) {
                String[] parts = result.split("Human:");
                if (parts.length > 0) {
                    result = parts[0].trim();
                }
            }
            
            // Remove "AgriDost:" prefix if present
            if (result.startsWith("AgriDost:")) {
                result = result.substring("AgriDost:".length()).trim();
            }
            
            // Return a default message if empty
            if (result.isEmpty()) {
                return "I'm here to help with your agricultural questions. What would you like to know?";
            }
            
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                callback.onSuccess(result);
            } else {
                callback.onError(errorMessage != null ? errorMessage : "Unknown error occurred");
            }
        }
    }
}
