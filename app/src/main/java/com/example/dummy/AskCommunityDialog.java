package com.example.dummy;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class AskCommunityDialog extends DialogFragment {
    
    private EditText editTextTitle;
    private EditText editTextContent;
    private AutoCompleteTextView autoCompleteCropType;
    private ImageView imageViewPhoto;
    private Button buttonPost;
    private OnPostCreatedListener listener;
    
    public interface OnPostCreatedListener {
        void onPostCreated(Post post);
    }
    
    public static AskCommunityDialog newInstance() {
        return new AskCommunityDialog();
    }
    
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnPostCreatedListener) {
            listener = (OnPostCreatedListener) context;
        }
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_ask_community, container, false);
        
        initializeViews(view);
        setupToolbar(view);
        setupCropTypeDropdown();
        setupClickListeners();
        
        return view;
    }
    
    private void initializeViews(View view) {
        editTextTitle = view.findViewById(R.id.edit_text_title);
        editTextContent = view.findViewById(R.id.edit_text_content);
        autoCompleteCropType = view.findViewById(R.id.auto_complete_crop_type);
        imageViewPhoto = view.findViewById(R.id.image_view_photo);
        buttonPost = view.findViewById(R.id.button_post);
    }
    
    private void setupToolbar(View view) {
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        
        Button buttonCancel = view.findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(v -> dismiss());
    }
    
    private void setupCropTypeDropdown() {
        String[] cropTypes = {"Tomato", "Pea", "Cotton", "Onion", "Wheat", "Rice", "Sugarcane", "Potato", "Corn", "Soybean"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), 
                android.R.layout.simple_dropdown_item_1line, cropTypes);
        autoCompleteCropType.setAdapter(adapter);
    }
    
    private void setupClickListeners() {
        imageViewPhoto.setOnClickListener(v -> {
            // Handle photo selection
            Toast.makeText(getContext(), "Photo selection not implemented yet", Toast.LENGTH_SHORT).show();
        });
        
        buttonPost.setOnClickListener(v -> {
            if (validateInput()) {
                createPost();
            }
        });
    }
    
    private boolean validateInput() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();
        String cropType = autoCompleteCropType.getText().toString().trim();
        
        if (title.isEmpty()) {
            editTextTitle.setError("Please enter a title");
            editTextTitle.requestFocus();
            return false;
        }
        
        if (content.isEmpty()) {
            editTextContent.setError("Please enter your question");
            editTextContent.requestFocus();
            return false;
        }
        
        if (cropType.isEmpty()) {
            autoCompleteCropType.setError("Please select a crop type");
            autoCompleteCropType.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void createPost() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();
        String cropType = autoCompleteCropType.getText().toString().trim();
        
        Post post = new Post();
        post.setId(String.valueOf(System.currentTimeMillis()));
        post.setTitle(title);
        post.setContent(content);
        post.setCropType(cropType);
        post.setAuthorName("Current User"); // This should come from user session
        post.setAuthorLocation("Your Location"); // This should come from user session
        post.setTimeAgo("now");
        post.setImageUrl(""); // Handle image upload later
        post.setLikesCount(0);
        post.setDislikesCount(0);
        post.setCommentsCount(0);
        
        if (listener != null) {
            listener.onPostCreated(post);
        }
        
        Toast.makeText(getContext(), "Post created successfully!", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}
