package com.example.dummy.chat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.dummy.ChatAdapter;
import com.example.dummy.ChatMessage;
import com.example.dummy.R;
import com.example.dummy.network.ApiConfig;
import com.example.dummy.services.RealtimeChatService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewChat;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> messageList;
    private EditText editTextMessage;
    private ImageButton buttonSend;
    private ImageButton buttonMic;
    private FloatingActionButton fabClose;
    private TextView question1, question2, question3, question4;
    private ProgressBar progressBar;
    
    private SpeechRecognizer speechRecognizer;
    private boolean isListening = false;
    private static final int PERMISSION_REQUEST_CODE = 1;
    
    // Realtime Chat Service (Prioritizes API responses)
    private RealtimeChatService realtimeChatService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        initializeViews();
        setupRecyclerView();
        setupSendButton();
        setupMicButton();
        setupCommonQuestions();
        setupCloseFAB();
        addWelcomeMessage();
        checkPermissions();
        initializeRealtimeService();
    }

    private void initializeViews() {
        recyclerViewChat = findViewById(R.id.recycler_view_chat);
        editTextMessage = findViewById(R.id.edit_text_message);
        buttonSend = findViewById(R.id.button_send);
        buttonMic = findViewById(R.id.button_mic);
        fabClose = findViewById(R.id.fab_close);
        question1 = findViewById(R.id.question_1);
        question2 = findViewById(R.id.question_2);
        question3 = findViewById(R.id.question_3);
        question4 = findViewById(R.id.question_4);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void setupRecyclerView() {
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);
    }

    private void setupSendButton() {
        buttonSend.setOnClickListener(v -> sendMessage());
    }

    private void setupMicButton() {
        buttonMic.setOnClickListener(v -> {
            if (isListening) {
                stopListening();
            } else {
                startListening();
            }
        });
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                new String[]{Manifest.permission.RECORD_AUDIO}, 
                PERMISSION_REQUEST_CODE);
        } else {
            initializeSpeechRecognizer();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeSpeechRecognizer();
            } else {
                Toast.makeText(this, "Microphone permission is required for voice input", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    isListening = true;
                    buttonMic.setBackgroundResource(R.drawable.mic_button_listening_bg);
                    Toast.makeText(ChatActivity.this, "Listening...", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onBeginningOfSpeech() {
                    // Speech started
                }

                @Override
                public void onRmsChanged(float rmsdB) {
                    // Volume level changed
                }

                @Override
                public void onBufferReceived(byte[] buffer) {
                    // Audio buffer received
                }

                @Override
                public void onEndOfSpeech() {
                    isListening = false;
                    buttonMic.setBackgroundResource(R.drawable.mic_button_background);
                }

                @Override
                public void onError(int error) {
                    isListening = false;
                    buttonMic.setBackgroundResource(R.drawable.mic_button_background);
                    String errorMessage = getErrorMessage(error);
                    Toast.makeText(ChatActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResults(Bundle results) {
                    isListening = false;
                    buttonMic.setBackgroundResource(R.drawable.mic_button_background);
                    
                    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if (matches != null && !matches.isEmpty()) {
                        String spokenText = matches.get(0);
                        editTextMessage.setText(spokenText);
                    }
                }

                @Override
                public void onPartialResults(Bundle partialResults) {
                    // Partial results received
                }

                @Override
                public void onEvent(int eventType, Bundle params) {
                    // Event occurred
                }
            });
        } else {
            Toast.makeText(this, "Speech recognition not available on this device", Toast.LENGTH_LONG).show();
        }
    }

    private void startListening() {
        if (speechRecognizer != null) {
            android.content.Intent intent = new android.content.Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your agricultural question...");
            speechRecognizer.startListening(intent);
        }
    }

    private void stopListening() {
        if (speechRecognizer != null && isListening) {
            speechRecognizer.stopListening();
            isListening = false;
            buttonMic.setBackgroundResource(R.drawable.mic_button_background);
        }
    }

    private String getErrorMessage(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No speech input recognized";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "Server sends error status";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Unknown error occurred";
                break;
        }
        return message;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }

    private void setupCommonQuestions() {
        question1.setOnClickListener(v -> {
            editTextMessage.setText("I need help identifying plant diseases");
            sendMessage();
        });

        question2.setOnClickListener(v -> {
            editTextMessage.setText("Can you give me advice about crop management?");
            sendMessage();
        });

        question3.setOnClickListener(v -> {
            editTextMessage.setText("What's the weather forecast for farming?");
            sendMessage();
        });

        question4.setOnClickListener(v -> {
            editTextMessage.setText("How can I improve my soil health?");
            sendMessage();
        });
    }

    private void setupCloseFAB() {
        fabClose.setOnClickListener(v -> finish());
    }

    private void addWelcomeMessage() {
        ChatMessage welcomeMessage = new ChatMessage("How can we help you today?", false);
        messageList.add(welcomeMessage);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerViewChat.scrollToPosition(messageList.size() - 1);
    }

    private void sendMessage() {
        String message = editTextMessage.getText().toString().trim();
        if (message.isEmpty()) {
            return;
        }

        // Add user message
        ChatMessage userMessage = new ChatMessage(message, true);
        messageList.add(userMessage);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerViewChat.scrollToPosition(messageList.size() - 1);

        // Clear input
        editTextMessage.setText("");

        // Show loading indicator
        showLoading(true);

        // Send to Realtime Chat Service
        if (realtimeChatService != null) {
            realtimeChatService.sendMessage(message, messageList, new RealtimeChatService.ChatCallback() {
                @Override
                public void onSuccess(String response) {
                    runOnUiThread(() -> {
                        showLoading(false);
                        ChatMessage botMessage = new ChatMessage(response, false);
                        messageList.add(botMessage);
                        chatAdapter.notifyItemInserted(messageList.size() - 1);
                        recyclerViewChat.scrollToPosition(messageList.size() - 1);
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        showLoading(false);
                        String userMessage;
                        if (error.contains("429") || error.contains("Rate limit")) {
                            userMessage = "I'm experiencing high demand right now. Please wait a moment and try again. This usually resolves quickly!";
                        } else {
                            userMessage = "I'm sorry, I'm having trouble connecting right now. Please try again later.";
                        }
                        
                        ChatMessage errorMessage = new ChatMessage(userMessage, false);
                        messageList.add(errorMessage);
                        chatAdapter.notifyItemInserted(messageList.size() - 1);
                        recyclerViewChat.scrollToPosition(messageList.size() - 1);
                        Toast.makeText(ChatActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                    });
                }
            });
        } else {
            showLoading(false);
            Toast.makeText(this, "Realtime chat service not initialized. Please check your configuration.", Toast.LENGTH_LONG).show();
        }
    }

    private void initializeRealtimeService() {
        // Initialize realtime service (prioritizes API responses)
        realtimeChatService = new RealtimeChatService(ApiConfig.HF_API_TOKEN);
    }

    private void showLoading(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        buttonSend.setEnabled(!show);
        buttonMic.setEnabled(!show);
    }
}
