package com.example.dummy;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
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
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private static final int GALLERY_PERMISSION_REQUEST = 101;
    
    private ImageView profileAvatar;
    private TextInputEditText editName;
    private TextInputEditText editDescription;
    private TextInputEditText editLocation;
    private TextInputEditText editPhoneNumber;
    private MaterialButton saveButton;
    private MaterialButton cancelButton;
    
    private User currentUser;
    private UserDatabase userDatabase;
    private String selectedImagePath;
    
    // Activity result launchers
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        initializeViews();
        setupActivityResultLaunchers();
        loadCurrentUser();
        setupClickListeners();
    }

    private void initializeViews() {
        profileAvatar = findViewById(R.id.edit_profile_avatar);
        editName = findViewById(R.id.edit_profile_name);
        editDescription = findViewById(R.id.edit_profile_description);
        editLocation = findViewById(R.id.edit_profile_location);
        editPhoneNumber = findViewById(R.id.edit_profile_phone);
        saveButton = findViewById(R.id.edit_profile_save_button);
        cancelButton = findViewById(R.id.edit_profile_cancel_button);
        
        userDatabase = UserDatabase.getInstance(this);
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
                            Bundle extras = data.getExtras();
                            if (extras != null) {
                                Bitmap imageBitmap = (Bitmap) extras.get("data");
                                if (imageBitmap != null) {
                                    selectedImagePath = saveImageToInternalStorage(imageBitmap);
                                    profileAvatar.setImageBitmap(imageBitmap);
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
                            Uri selectedImageUri = data.getData();
                            if (selectedImageUri != null) {
                                try {
                                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(
                                        getContentResolver(), selectedImageUri);
                                    selectedImagePath = saveImageToInternalStorage(imageBitmap);
                                    profileAvatar.setImageBitmap(imageBitmap);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(EditProfileActivity.this, 
                                        "Error loading image", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
            }
        );
    }

    private void loadCurrentUser() {
        currentUser = userDatabase.getCurrentUser();
        if (currentUser != null) {
            editName.setText(currentUser.getName());
            editDescription.setText(currentUser.getDescription());
            editLocation.setText(currentUser.getLocation());
            editPhoneNumber.setText(currentUser.getPhoneNumber());
            
            // Load avatar if exists
            if (currentUser.hasAvatar()) {
                loadAvatar(currentUser.getAvatarPath());
            }
        }
    }

    private void loadAvatar(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                profileAvatar.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupClickListeners() {
        profileAvatar.setOnClickListener(v -> showImageSelectionDialog());
        
        saveButton.setOnClickListener(v -> saveProfile());
        
        cancelButton.setOnClickListener(v -> finish());
    }

    private void showImageSelectionDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Select Profile Photo");
        builder.setItems(new String[]{"Take Photo", "Choose from Gallery", "Remove Photo"}, (dialog, which) -> {
            switch (which) {
                case 0:
                    openCamera();
                    break;
                case 1:
                    openGallery();
                    break;
                case 2:
                    removePhoto();
                    break;
            }
        });
        builder.show();
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

    private void removePhoto() {
        selectedImagePath = null;
        profileAvatar.setImageResource(R.drawable.ic_user_avatar);
    }

    private String saveImageToInternalStorage(Bitmap bitmap) {
        try {
            // Create directory if it doesn't exist
            File directory = new File(getFilesDir(), "profile_images");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            // Create unique filename
            String filename = "profile_" + System.currentTimeMillis() + ".jpg";
            File imageFile = new File(directory, filename);
            
            // Compress and save bitmap
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();
            
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveProfile() {
        String name = editName.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String location = editLocation.getText().toString().trim();
        String phoneNumber = editPhoneNumber.getText().toString().trim();
        
        if (name.isEmpty()) {
            editName.setError("Name is required");
            return;
        }
        
        if (currentUser != null) {
            // Update user profile
            userDatabase.updateProfile(name, description, location, phoneNumber);
            
            // Update avatar if changed
            if (selectedImagePath != null) {
                userDatabase.updateAvatar(selectedImagePath);
            }
            
            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        }
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
    
    @Override
    public void onBackPressed() {
        // Go back to profile page
        setResult(RESULT_CANCELED);
        finish();
    }
}
