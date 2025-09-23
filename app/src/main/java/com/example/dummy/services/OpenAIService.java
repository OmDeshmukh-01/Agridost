package com.example.dummy.services;

import android.os.AsyncTask;
import android.util.Log;

import com.example.dummy.network.ApiConfig;
import com.example.dummy.ChatMessage;
import com.example.dummy.OpenAIRequest;
import com.example.dummy.OpenAIResponse;
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

public class OpenAIService {
    private static final String TAG = "OpenAIService";
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    
    private final String apiKey;
    private final OkHttpClient client;
    private final Gson gson;

    public interface ChatCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    public OpenAIService(String apiKey) {
        this.apiKey = apiKey;
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

    public void sendMessageWithRetry(String userMessage, List<ChatMessage> chatHistory, ChatCallback callback) {
        new ChatTaskWithRetry(callback).execute(userMessage, chatHistory);
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
                // Build messages list
                List<OpenAIRequest.Message> messages = new ArrayList<>();
                messages.add(new OpenAIRequest.Message("system", ApiConfig.SYSTEM_PROMPT));

                // Add recent chat history (last 10 messages to stay within token limits)
                int startIndex = Math.max(0, chatHistory.size() - 10);
                for (int i = startIndex; i < chatHistory.size(); i++) {
                    ChatMessage chatMsg = chatHistory.get(i);
                    String role = chatMsg.isUser() ? "user" : "assistant";
                    messages.add(new OpenAIRequest.Message(role, chatMsg.getMessage()));
                }

                // Add current user message
                messages.add(new OpenAIRequest.Message("user", userMessage));

                // Create request
                OpenAIRequest request = new OpenAIRequest(
                        "gpt-3.5-turbo", // Default model for OpenAI
                        messages,
                        500, // Default max tokens
                        ApiConfig.TEMPERATURE
                );

                String jsonRequest = gson.toJson(request);
                Log.d(TAG, "Request: " + jsonRequest);

                RequestBody body = RequestBody.create(jsonRequest, JSON);
                Request httpRequest = new Request.Builder()
                        .url(OPENAI_API_URL)
                        .addHeader("Authorization", "Bearer " + apiKey)
                        .addHeader("Content-Type", "application/json")
                        .post(body)
                        .build();

                Response response = client.newCall(httpRequest).execute();
                String responseBody = response.body().string();

                if (response.isSuccessful()) {
                    Log.d(TAG, "Response: " + responseBody);
                    OpenAIResponse openAIResponse = gson.fromJson(responseBody, OpenAIResponse.class);
                    
                    if (openAIResponse.getChoices() != null && !openAIResponse.getChoices().isEmpty()) {
                        return openAIResponse.getChoices().get(0).getMessage().getContent();
                    } else {
                        return "I'm sorry, I couldn't generate a response. Please try again.";
                    }
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

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                callback.onSuccess(result);
            } else {
                callback.onError(errorMessage != null ? errorMessage : "Unknown error occurred");
            }
        }
    }

    private class ChatTaskWithRetry extends AsyncTask<Object, Void, String> {
        private final ChatCallback callback;
        private String errorMessage = null;
        private static final int MAX_RETRIES = 3;
        private static final long BASE_DELAY_MS = 1000; // 1 second

        public ChatTaskWithRetry(ChatCallback callback) {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Object... params) {
            String userMessage = (String) params[0];
            @SuppressWarnings("unchecked")
            List<ChatMessage> chatHistory = (List<ChatMessage>) params[1];

            for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
                try {
                    String result = makeApiCall(userMessage, chatHistory);
                    if (result != null) {
                        return result;
                    }
                    
                    // If we get here, there was an error
                    if (attempt < MAX_RETRIES - 1) {
                        // Wait before retrying (exponential backoff)
                        long delay = BASE_DELAY_MS * (1L << attempt); // 1s, 2s, 4s
                        Log.d(TAG, "Retrying in " + delay + "ms (attempt " + (attempt + 1) + "/" + MAX_RETRIES + ")");
                        Thread.sleep(delay);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    errorMessage = "Request interrupted";
                    return null;
                } catch (Exception e) {
                    Log.e(TAG, "Attempt " + (attempt + 1) + " failed", e);
                    if (attempt == MAX_RETRIES - 1) {
                        errorMessage = "All retry attempts failed: " + e.getMessage();
                    }
                }
            }
            
            return null;
        }

        private String makeApiCall(String userMessage, List<ChatMessage> chatHistory) throws Exception {
            // Build messages list
            List<OpenAIRequest.Message> messages = new ArrayList<>();
            messages.add(new OpenAIRequest.Message("system", ApiConfig.SYSTEM_PROMPT));

            // Add recent chat history (last 10 messages to stay within token limits)
            int startIndex = Math.max(0, chatHistory.size() - 10);
            for (int i = startIndex; i < chatHistory.size(); i++) {
                ChatMessage chatMsg = chatHistory.get(i);
                String role = chatMsg.isUser() ? "user" : "assistant";
                messages.add(new OpenAIRequest.Message(role, chatMsg.getMessage()));
            }

            // Add current user message
            messages.add(new OpenAIRequest.Message("user", userMessage));

            // Create request
            OpenAIRequest request = new OpenAIRequest(
                    "gpt-3.5-turbo", // Default model for OpenAI
                    messages,
                    500, // Default max tokens
                    ApiConfig.TEMPERATURE
            );

            String jsonRequest = gson.toJson(request);
            Log.d(TAG, "Request: " + jsonRequest);

            RequestBody body = RequestBody.create(jsonRequest, JSON);
            Request httpRequest = new Request.Builder()
                    .url(OPENAI_API_URL)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            Response response = client.newCall(httpRequest).execute();
            String responseBody = response.body().string();

            if (response.isSuccessful()) {
                Log.d(TAG, "Response: " + responseBody);
                OpenAIResponse openAIResponse = gson.fromJson(responseBody, OpenAIResponse.class);
                
                if (openAIResponse.getChoices() != null && !openAIResponse.getChoices().isEmpty()) {
                    return openAIResponse.getChoices().get(0).getMessage().getContent();
                } else {
                    throw new Exception("No response choices available");
                }
            } else {
                String errorMsg = "API Error " + response.code() + ": " + responseBody;
                Log.e(TAG, errorMsg);
                
                // If it's a rate limit error, throw a specific exception for retry
                if (response.code() == 429) {
                    throw new Exception("Rate limit exceeded (429) - will retry");
                } else {
                    errorMessage = errorMsg;
                    throw new Exception(errorMsg);
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                callback.onSuccess(result);
            } else {
                callback.onError(errorMessage != null ? errorMessage : "All retry attempts failed");
            }
        }
    }
}
