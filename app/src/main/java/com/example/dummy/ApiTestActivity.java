package com.example.dummy;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ApiTestActivity extends AppCompatActivity {
    private static final String TAG = "ApiTestActivity";
    private TextView resultText;
    private Button testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_test);
        
        resultText = findViewById(R.id.result_text);
        testButton = findViewById(R.id.test_button);
        
        testButton.setOnClickListener(v -> testApiConnection());
    }
    
    private void testApiConnection() {
        resultText.setText("Testing Realtime Chat Service...\n\nThis service prioritizes real-time API responses from multiple Hugging Face models.\n\nToken: " + ApiConfig.HF_API_TOKEN.substring(0, 10) + "...");
        
        // Test with a simple message
        RealtimeChatService realtimeService = new RealtimeChatService(ApiConfig.HF_API_TOKEN);
        
        realtimeService.sendMessage("Hello", new java.util.ArrayList<>(), new RealtimeChatService.ChatCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    resultText.setText("SUCCESS!\n\nResponse: " + response + "\n\nThis means the API is working correctly!");
                    Toast.makeText(ApiTestActivity.this, "API connection successful!", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    resultText.setText("ERROR!\n\n" + error + "\n\nThis is the exact error from the API. Please share this with the developer.");
                    Toast.makeText(ApiTestActivity.this, "API connection failed: " + error, Toast.LENGTH_LONG).show();
                    Log.e(TAG, "API Error: " + error);
                });
            }
        });
    }
}
