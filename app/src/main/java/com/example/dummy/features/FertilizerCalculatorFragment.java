package com.example.dummy.features;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dummy.R;

public class FertilizerCalculatorFragment extends Fragment {

    private EditText etFieldArea;
    private Spinner spinnerCropType;
    private Spinner spinnerSoilType;
    private EditText etCurrentNPK;
    private Button btnCalculate;
    private TextView tvResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fertilizer_calculator, container, false);
        initializeViews(view);
        setupClickListeners();
        return view;
    }

    private void initializeViews(View root) {
        etFieldArea = root.findViewById(R.id.etFieldArea);
        spinnerCropType = root.findViewById(R.id.spinnerCropType);
        spinnerSoilType = root.findViewById(R.id.spinnerSoilType);
        etCurrentNPK = root.findViewById(R.id.etCurrentNPK);
        btnCalculate = root.findViewById(R.id.btnCalculate);
        tvResult = root.findViewById(R.id.tvResult);
    }

    private void setupClickListeners() {
        btnCalculate.setOnClickListener(v -> calculateFertilizer());
    }

    private void calculateFertilizer() {
        try {
            String areaText = etFieldArea.getText().toString().trim();
            String npkText = etCurrentNPK.getText().toString().trim();

            if (areaText.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter field area", Toast.LENGTH_SHORT).show();
                return;
            }

            double area = Double.parseDouble(areaText);
            String cropType = spinnerCropType.getSelectedItem().toString();
            String soilType = spinnerSoilType.getSelectedItem().toString();

            FertilizerRecommendation recommendation = calculateFertilizerRequirement(area, cropType, soilType, npkText);
            displayResult(recommendation);

        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }

    private FertilizerRecommendation calculateFertilizerRequirement(double area, String cropType, String soilType, String currentNPK) {
        double baseN, baseP, baseK;

        switch (cropType) {
            case "Rice":
                baseN = 120; baseP = 60; baseK = 60;
                break;
            case "Wheat":
                baseN = 100; baseP = 50; baseK = 50;
                break;
            case "Maize":
                baseN = 150; baseP = 70; baseK = 80;
                break;
            case "Tomato":
                baseN = 200; baseP = 100; baseK = 150;
                break;
            case "Potato":
                baseN = 180; baseP = 80; baseK = 200;
                break;
            case "Cotton":
                baseN = 160; baseP = 60; baseK = 80;
                break;
            default:
                baseN = 120; baseP = 60; baseK = 60;
        }

        double soilMultiplier = 1.0;
        switch (soilType) {
            case "Sandy":
                soilMultiplier = 1.2;
                break;
            case "Clay":
                soilMultiplier = 0.8;
                break;
            case "Loamy":
                soilMultiplier = 1.0;
                break;
        }

        double nitrogen = (baseN * soilMultiplier * area);
        double phosphorus = (baseP * soilMultiplier * area);
        double potassium = (baseK * soilMultiplier * area);

        return new FertilizerRecommendation(nitrogen, phosphorus, potassium, cropType, soilType);
    }

    private void displayResult(FertilizerRecommendation recommendation) {
        StringBuilder result = new StringBuilder();
        result.append("Fertilizer Recommendation for ").append(recommendation.cropType).append("\n");
        result.append("Soil Type: ").append(recommendation.soilType).append("\n\n");
        result.append("Required Fertilizers:\n");
        result.append("• Nitrogen (N): ").append(String.format("%.1f", recommendation.nitrogen)).append(" kg\n");
        result.append("• Phosphorus (P): ").append(String.format("%.1f", recommendation.phosphorus)).append(" kg\n");
        result.append("• Potassium (K): ").append(String.format("%.1f", recommendation.potassium)).append(" kg\n\n");

        result.append("Recommended Fertilizers:\n");
        result.append("• Urea: ").append(String.format("%.1f", recommendation.nitrogen / 0.46)).append(" kg\n");
        result.append("• DAP: ").append(String.format("%.1f", recommendation.phosphorus / 0.46)).append(" kg\n");
        result.append("• MOP: ").append(String.format("%.1f", recommendation.potassium / 0.6)).append(" kg\n\n");

        result.append("Application Tips:\n");
        result.append("• Apply in 2-3 splits during growing season\n");
        result.append("• Mix well with soil during application\n");
        result.append("• Water immediately after application\n");
        result.append("• Test soil every 2-3 years for accuracy");

        tvResult.setText(result.toString());
        tvResult.setVisibility(View.VISIBLE);
    }

    private static class FertilizerRecommendation {
        double nitrogen, phosphorus, potassium;
        String cropType, soilType;

        FertilizerRecommendation(double nitrogen, double phosphorus, double potassium, String cropType, String soilType) {
            this.nitrogen = nitrogen;
            this.phosphorus = phosphorus;
            this.potassium = potassium;
            this.cropType = cropType;
            this.soilType = soilType;
        }
    }
}
