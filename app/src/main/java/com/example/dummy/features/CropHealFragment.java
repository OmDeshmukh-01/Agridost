package com.example.dummy.features;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.dummy.DiseaseDatabase;
import com.example.dummy.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;

public class CropHealFragment extends Fragment implements TextToSpeech.OnInitListener {

    private static final String TAG = "CropHealFragment";

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

    private DiseaseDatabase diseaseDatabase;

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<String> requestCameraPermissionLauncher;
    private ActivityResultLauncher<String> requestStoragePermissionLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_crop_heal, container, false);
        initializeViews(view);
        setupActivityResultLaunchers();
        setupClickListeners();
        initializeTTS();
        initializeDiseaseDatabase();
        return view;
    }

    private void initializeViews(View root) {
        imageView = root.findViewById(R.id.imageView);
        btnTakePicture = root.findViewById(R.id.btnTakePicture);
        btnSelectFromGallery = root.findViewById(R.id.btnSelectFromGallery);
        btnAnalyze = root.findViewById(R.id.btnAnalyze);
        btnSpeak = root.findViewById(R.id.btnSpeak);
        tvDiagnosis = root.findViewById(R.id.tvDiagnosis);
        tvSolution = root.findViewById(R.id.tvSolution);
        progressBar = root.findViewById(R.id.progressBar);

        tvDiagnosis.setVisibility(View.GONE);
        tvSolution.setVisibility(View.GONE);
        btnSpeak.setVisibility(View.GONE);
    }

    private void setupActivityResultLaunchers() {
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    if (extras != null) {
                        selectedImage = (Bitmap) extras.get("data");
                        imageView.setImageBitmap(selectedImage);
                        showAnalysisButton();
                    }
                }
            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(inputStream);
                        imageView.setImageBitmap(selectedImage);
                        showAnalysisButton();
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, "Error loading image from gallery", e);
                        Toast.makeText(requireContext(), "Error loading image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        requestCameraPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                openCamera();
            } else {
                Toast.makeText(requireContext(), "Camera permission is required to take pictures", Toast.LENGTH_LONG).show();
            }
        });

        requestStoragePermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                openGallery();
            } else {
                Toast.makeText(requireContext(), "Storage permission is required to select images", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupClickListeners() {
        btnTakePicture.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
        });

        btnSelectFromGallery.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                // Try READ_MEDIA_IMAGES for newer APIs first
                if (android.os.Build.VERSION.SDK_INT >= 33) {
                    requestStoragePermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                } else {
                    requestStoragePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }
        });

        btnAnalyze.setOnClickListener(v -> {
            if (selectedImage != null) {
                analyzeImage();
            } else {
                Toast.makeText(requireContext(), "Please select an image first", Toast.LENGTH_SHORT).show();
            }
        });

        btnSpeak.setOnClickListener(v -> {
            if (isTTSInitialized && tvSolution.getVisibility() == View.VISIBLE) {
                speakSolution();
            } else {
                Toast.makeText(requireContext(), "Text-to-speech not ready or no solution available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeTTS() {
        textToSpeech = new TextToSpeech(requireContext(), this);
    }

    private void initializeDiseaseDatabase() {
        diseaseDatabase = new DiseaseDatabase();
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(cameraIntent);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

    private void showAnalysisButton() {
        btnAnalyze.setVisibility(View.VISIBLE);
        btnAnalyze.setEnabled(true);
    }

    private void analyzeImage() {
        progressBar.setVisibility(View.VISIBLE);
        btnAnalyze.setEnabled(false);

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
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
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnAnalyze.setEnabled(true);
                    Toast.makeText(requireContext(), "Analysis interrupted", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(requireContext(), "Language not supported", Toast.LENGTH_SHORT).show();
            } else {
                isTTSInitialized = true;
                Log.d(TAG, "TextToSpeech initialized successfully");
            }
        } else {
            Log.e(TAG, "TextToSpeech initialization failed");
            Toast.makeText(requireContext(), "Text-to-speech initialization failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
