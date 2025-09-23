package com.example.dummy.features;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dummy.DiseaseAlertAdapter;
import com.example.dummy.tools.DiseaseAlertActivity;
import com.example.dummy.R;

import java.util.ArrayList;
import java.util.List;

public class DiseaseAlertFragment extends Fragment {

    private Spinner spinnerRegion;
    private Spinner spinnerCrop;
    private ListView listViewAlerts;
    private TextView tvNoAlerts;
    private List<DiseaseAlertActivity.DiseaseAlert> alerts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_disease_alert, container, false);
        initializeViews(view);
        setupSpinners();
        loadAlerts();
        return view;
    }

    private void initializeViews(View root) {
        spinnerRegion = root.findViewById(R.id.spinnerRegion);
        spinnerCrop = root.findViewById(R.id.spinnerCrop);
        listViewAlerts = root.findViewById(R.id.listViewAlerts);
        tvNoAlerts = root.findViewById(R.id.tvNoAlerts);
    }

    private void setupSpinners() {
        String[] regions = {"All Regions", "North India", "South India", "East India", "West India", "Central India"};
        ArrayAdapter<String> regionAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, regions);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegion.setAdapter(regionAdapter);

        String[] crops = {"All Crops", "Rice", "Wheat", "Maize", "Tomato", "Potato", "Cotton", "Sugarcane"};
        ArrayAdapter<String> cropAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, crops);
        cropAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCrop.setAdapter(cropAdapter);
    }

    private void loadAlerts() {
        alerts = new ArrayList<>();

        alerts.add(new DiseaseAlertActivity.DiseaseAlert("High", "Rice", "North India", "Blast Disease Alert",
                "High humidity and temperature conditions favor blast disease. Apply fungicide immediately.", "2 days ago"));

        alerts.add(new DiseaseAlertActivity.DiseaseAlert("Medium", "Tomato", "South India", "Late Blight Warning",
                "Cool and wet conditions detected. Monitor plants closely for late blight symptoms.", "1 week ago"));

        alerts.add(new DiseaseAlertActivity.DiseaseAlert("High", "Wheat", "Punjab", "Rust Disease Outbreak",
                "Yellow rust detected in nearby fields. Apply preventive fungicide treatment.", "3 days ago"));

        alerts.add(new DiseaseAlertActivity.DiseaseAlert("Low", "Maize", "Central India", "Downy Mildew Alert",
                "Humid conditions may cause downy mildew. Ensure proper drainage and ventilation.", "5 days ago"));

        alerts.add(new DiseaseAlertActivity.DiseaseAlert("Medium", "Potato", "West Bengal", "Early Blight Warning",
                "Alternating wet and dry conditions favor early blight. Apply copper-based fungicide.", "1 week ago"));

        alerts.add(new DiseaseAlertActivity.DiseaseAlert("High", "Cotton", "Gujarat", "Bollworm Infestation",
                "High bollworm activity reported. Apply appropriate insecticide immediately.", "1 day ago"));

        alerts.add(new DiseaseAlertActivity.DiseaseAlert("Low", "Sugarcane", "Maharashtra", "Red Rot Alert",
                "Red rot symptoms detected in some fields. Remove infected plants and treat soil.", "4 days ago"));

        displayAlerts();
    }

    private void displayAlerts() {
        if (alerts == null || alerts.isEmpty()) {
            tvNoAlerts.setVisibility(View.VISIBLE);
            listViewAlerts.setVisibility(View.GONE);
        } else {
            tvNoAlerts.setVisibility(View.GONE);
            listViewAlerts.setVisibility(View.VISIBLE);
            DiseaseAlertAdapter adapter = new DiseaseAlertAdapter(requireContext(), alerts);
            listViewAlerts.setAdapter(adapter);
        }
    }

    // Removed inner DiseaseAlert class; using DiseaseAlertActivity.DiseaseAlert to match adapter
}
