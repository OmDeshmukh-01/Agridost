package com.example.dummy;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.IOException;

public class AskCommunityActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private static final int GALLERY_PERMISSION_REQUEST = 101;
    
    private EditText questionTitle;
    private EditText questionDescription;
    private MaterialButton postButton;
    private MaterialButton takePictureButton;
    private MaterialButton chooseGalleryButton;
    private ImageView selectedImageView;
    private MaterialButton removeImageButton;
    private ChipGroup cropTypeGroup;
    
    private Uri selectedImageUri;
    private Bitmap selectedImageBitmap;
    
    // Activity result launchers
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ask_community);

        setupToolbar();
        setupViews();
        setupActivityResultLaunchers();
        setupImageButtons();
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
        takePictureButton = findViewById(R.id.act_ask_take_picture_button);
        chooseGalleryButton = findViewById(R.id.act_ask_choose_gallery_button);
        selectedImageView = findViewById(R.id.act_ask_selected_image);
        removeImageButton = findViewById(R.id.act_ask_remove_image_button);
        cropTypeGroup = findViewById(R.id.act_ask_crop_type_group);
        
        postButton.setOnClickListener(v -> postQuestion());
    }
    
    private void setupActivityResultLaunchers() {
        // Camera launcher
        cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            selectedImageUri = data.getData();
                            if (selectedImageUri != null) {
                                try {
                                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                                        getContentResolver(), selectedImageUri);
                                    displaySelectedImage();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(AskCommunityActivity.this, 
                                        "Error loading image", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
            }
        );
        
        // Gallery launcher
        galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            selectedImageUri = data.getData();
                            if (selectedImageUri != null) {
                                try {
                                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                                        getContentResolver(), selectedImageUri);
                                    displaySelectedImage();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(AskCommunityActivity.this, 
                                        "Error loading image", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
            }
        );
    }
    
    private void setupImageButtons() {
        takePictureButton.setOnClickListener(v -> openCamera());
        chooseGalleryButton.setOnClickListener(v -> openGallery());
        removeImageButton.setOnClickListener(v -> removeSelectedImage());
    }
    
    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
            return;
        }
        
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            cameraLauncher.launch(cameraIntent);
        } else {
            Toast.makeText(this, "Camera not available", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void openGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) 
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_REQUEST);
            return;
        }
        
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }
    
    private void displaySelectedImage() {
        if (selectedImageBitmap != null) {
            selectedImageView.setImageBitmap(selectedImageBitmap);
            selectedImageView.setVisibility(View.VISIBLE);
            removeImageButton.setVisibility(View.VISIBLE);
        }
    }
    
    private void removeSelectedImage() {
        selectedImageUri = null;
        selectedImageBitmap = null;
        selectedImageView.setVisibility(View.GONE);
        removeImageButton.setVisibility(View.GONE);
    }
    
    private void postQuestion() {
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
        
        // Get selected crop type
        String selectedCropType = "";
        int selectedChipId = cropTypeGroup.getCheckedChipId();
        if (selectedChipId != View.NO_ID) {
            Chip selectedChip = findViewById(selectedChipId);
            selectedCropType = selectedChip.getText().toString();
        }
        
        // Create community post
        CommunityPost post = new CommunityPost(
            title,
            description,
            selectedCropType,
            selectedImageUri != null ? selectedImageUri.toString() : null
        );
        
        // Add to community posts (you can implement a database or API call here)
        CommunityDatabase.addPost(post);
        
        Toast.makeText(this, "Question posted successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission is required to take photos", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == GALLERY_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Storage permission is required to access gallery", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
