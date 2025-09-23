package com.example.dummy.tools;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.dummy.DiseaseDatabase;
import com.example.dummy.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;

public class CropHealActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    
    private static final String TAG = "CropHealActivity";
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int GALLERY_PERMISSION_CODE = 101;
    private static final int CAMERA_REQUEST = 102;
    private static final int GALLERY_REQUEST = 103;
    
    private ImageView imageView;
    private Button btnTakePicture;
    private Button btnSelectFromGallery;
    private Button btnAnalyze;
    private Button btnSpeak;
    private TextView tvDiagnosis;
    private TextView tvSolution;
    private ProgressBar progressBar;
    
    private Bitmap selectedImage;
    private TextToSpeech textToSpeech;
    private boolean isTTSInitialized = false;
    
    // Disease database
    private DiseaseDatabase diseaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_heal);
        
        initializeViews();
        setupClickListeners();
        initializeTTS();
        initializeDiseaseDatabase();
    }
    
    private void initializeViews() {
        imageView = findViewById(R.id.imageView);
        btnTakePicture = findViewById(R.id.btnTakePicture);
        btnSelectFromGallery = findViewById(R.id.btnSelectFromGallery);
        btnAnalyze = findViewById(R.id.btnAnalyze);
        btnSpeak = findViewById(R.id.btnSpeak);
        tvDiagnosis = findViewById(R.id.tvDiagnosis);
        tvSolution = findViewById(R.id.tvSolution);
        progressBar = findViewById(R.id.progressBar);
        
        // Initially hide analysis results
        tvDiagnosis.setVisibility(View.GONE);
        tvSolution.setVisibility(View.GONE);
        btnSpeak.setVisibility(View.GONE);
    }
    
    private void setupClickListeners() {
        btnTakePicture.setOnClickListener(v -> {
            if (checkCameraPermission()) {
                openCamera();
            } else {
                requestCameraPermission();
            }
        });
        
        btnSelectFromGallery.setOnClickListener(v -> {
            if (checkStoragePermission()) {
                openGallery();
            } else {
                requestStoragePermission();
            }
        });
        
        btnAnalyze.setOnClickListener(v -> {
            if (selectedImage != null) {
                analyzeImage();
            } else {
                Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnSpeak.setOnClickListener(v -> {
            if (isTTSInitialized && tvSolution.getVisibility() == View.VISIBLE) {
                speakSolution();
            } else {
                Toast.makeText(this, "Text-to-speech not ready or no solution available", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void initializeTTS() {
        textToSpeech = new TextToSpeech(this, this);
    }
    
    private void initializeDiseaseDatabase() {
        diseaseDatabase = new DiseaseDatabase();
    }
    
    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
                == PackageManager.PERMISSION_GRANTED;
    }
    
    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) 
                == PackageManager.PERMISSION_GRANTED;
    }
    
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, 
                new String[]{Manifest.permission.CAMERA}, 
                CAMERA_PERMISSION_CODE);
    }
    
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, 
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 
                GALLERY_PERMISSION_CODE);
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission is required to take pictures", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == GALLERY_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Storage permission is required to select images", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    private void openCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }
    
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, 
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST && data != null) {
                // Handle camera result
                Bundle extras = data.getExtras();
                if (extras != null) {
                    selectedImage = (Bitmap) extras.get("data");
                    imageView.setImageBitmap(selectedImage);
                    showAnalysisButton();
                }
            } else if (requestCode == GALLERY_REQUEST && data != null) {
                // Handle gallery result
                Uri imageUri = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    selectedImage = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(selectedImage);
                    showAnalysisButton();
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "Error loading image from gallery", e);
                    Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    
    private void showAnalysisButton() {
        btnAnalyze.setVisibility(View.VISIBLE);
        btnAnalyze.setEnabled(true);
    }
    
    private void analyzeImage() {
        progressBar.setVisibility(View.VISIBLE);
        btnAnalyze.setEnabled(false);
        
        // Simulate image analysis (in real app, you'd use ML model)
        new Thread(() -> {
            try {
                // Simulate processing time
                Thread.sleep(2000);
                
                runOnUiThread(() -> {
                    // Get random disease diagnosis
                    DiseaseDatabase.DiseaseInfo disease = diseaseDatabase.getRandomDisease();
                    
                    tvDiagnosis.setText("Diagnosis: " + disease.getName());
                    tvSolution.setText("Solution: " + disease.getSolution());
                    
                    tvDiagnosis.setVisibility(View.VISIBLE);
                    tvSolution.setVisibility(View.VISIBLE);
                    btnSpeak.setVisibility(View.VISIBLE);
                    
                    progressBar.setVisibility(View.GONE);
                    btnAnalyze.setEnabled(true);
                });
            } catch (InterruptedException e) {
                Log.e(TAG, "Analysis interrupted", e);
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnAnalyze.setEnabled(true);
                    Toast.makeText(this, "Analysis interrupted", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
    
    private void speakSolution() {
        String textToSpeak = tvDiagnosis.getText().toString() + ". " + tvSolution.getText().toString();
        textToSpeech.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
    }
    
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "Language not supported");
                Toast.makeText(this, "Language not supported", Toast.LENGTH_SHORT).show();
            } else {
                isTTSInitialized = true;
                Log.d(TAG, "TextToSpeech initialized successfully");
            }
        } else {
            Log.e(TAG, "TextToSpeech initialization failed");
            Toast.makeText(this, "Text-to-speech initialization failed", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
