package com.example.dummy;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class AskCommunityActivity extends AppCompatActivity {

    private EditText questionTitle;
    private EditText questionDescription;
    private MaterialButton postButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ask_community);

        setupToolbar();
        setupViews();
    }

    private void setupToolbar() {
        ImageView backButton = findViewById(R.id.act_ask_back_button);
        TextView toolbarTitle = findViewById(R.id.act_ask_toolbar_title);
        
        backButton.setOnClickListener(v -> finish());
        toolbarTitle.setText("Ask Community");
    }

    private void setupViews() {
        questionTitle = findViewById(R.id.act_ask_title_input);
        questionDescription = findViewById(R.id.act_ask_description_input);
        postButton = findViewById(R.id.act_ask_post_button);
        
        postButton.setOnClickListener(v -> {
            String title = questionTitle.getText().toString().trim();
            String description = questionDescription.getText().toString().trim();
            
            if (title.isEmpty()) {
                questionTitle.setError("Please enter a question title");
                return;
            }
            
            if (description.isEmpty()) {
                questionDescription.setError("Please enter question description");
                return;
            }
            
            // TODO: Post to community
            finish();
        });
    }
}
