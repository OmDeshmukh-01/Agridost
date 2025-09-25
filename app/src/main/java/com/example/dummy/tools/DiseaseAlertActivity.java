package com.example.dummy.tools;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dummy.BaseActivity;

import com.example.dummy.DiseaseAlertAdapter;
import com.example.dummy.R;

import java.util.ArrayList;
import java.util.List;

public class DiseaseAlertActivity extends BaseActivity {

    private Spinner spinnerRegion;
    private Spinner spinnerCrop;
    private ListView listViewAlerts;
    private TextView tvNoAlerts;
    private List<DiseaseAlert> alerts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_alert);
        
        initializeViews();
        setupSpinners();
        loadAlerts();
    }
    
    private void initializeViews() {
        spinnerRegion = findViewById(R.id.spinnerRegion);
        spinnerCrop = findViewById(R.id.spinnerCrop);
        listViewAlerts = findViewById(R.id.listViewAlerts);
        tvNoAlerts = findViewById(R.id.tvNoAlerts);
    }
    
    private void setupSpinners() {
        // Region spinner
        String[] regions = {"All Regions", "North India", "South India", "East India", "West India", "Central India"};
        ArrayAdapter<String> regionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, regions);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegion.setAdapter(regionAdapter);
        
        // Crop spinner
        String[] crops = {"All Crops", "Rice", "Wheat", "Maize", "Tomato", "Potato", "Cotton", "Sugarcane"};
        ArrayAdapter<String> cropAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, crops);
        cropAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCrop.setAdapter(cropAdapter);
    }
    
    private void loadAlerts() {
        alerts = new ArrayList<>();
        
        // Sample disease alerts
        alerts.add(new DiseaseAlert("High", "Rice", "North India", "Blast Disease Alert", 
            "High humidity and temperature conditions favor blast disease. Apply fungicide immediately.", "2 days ago"));
        
        alerts.add(new DiseaseAlert("Medium", "Tomato", "South India", "Late Blight Warning", 
            "Cool and wet conditions detected. Monitor plants closely for late blight symptoms.", "1 week ago"));
        
        alerts.add(new DiseaseAlert("High", "Wheat", "Punjab", "Rust Disease Outbreak", 
            "Yellow rust detected in nearby fields. Apply preventive fungicide treatment.", "3 days ago"));
        
        alerts.add(new DiseaseAlert("Low", "Maize", "Central India", "Downy Mildew Alert", 
            "Humid conditions may cause downy mildew. Ensure proper drainage and ventilation.", "5 days ago"));
        
        alerts.add(new DiseaseAlert("Medium", "Potato", "West Bengal", "Early Blight Warning", 
            "Alternating wet and dry conditions favor early blight. Apply copper-based fungicide.", "1 week ago"));
        
        alerts.add(new DiseaseAlert("High", "Cotton", "Gujarat", "Bollworm Infestation", 
            "High bollworm activity reported. Apply appropriate insecticide immediately.", "1 day ago"));
        
        alerts.add(new DiseaseAlert("Low", "Sugarcane", "Maharashtra", "Red Rot Alert", 
            "Red rot symptoms detected in some fields. Remove infected plants and treat soil.", "4 days ago"));
        
        displayAlerts();
    }
    
    private void displayAlerts() {
        if (alerts.isEmpty()) {
            tvNoAlerts.setVisibility(View.VISIBLE);
            listViewAlerts.setVisibility(View.GONE);
        } else {
            tvNoAlerts.setVisibility(View.GONE);
            listViewAlerts.setVisibility(View.VISIBLE);
            
            DiseaseAlertAdapter adapter = new DiseaseAlertAdapter(this, alerts);
            listViewAlerts.setAdapter(adapter);
        }
    }
    
    public static class DiseaseAlert {
        String severity;
        String crop;
        String region;
        String title;
        String description;
        String timeAgo;
        
        public DiseaseAlert(String severity, String crop, String region, String title, String description, String timeAgo) {
            this.severity = severity;
            this.crop = crop;
            this.region = region;
            this.title = title;
            this.description = description;
            this.timeAgo = timeAgo;
        }
        
        public String getSeverity() { return severity; }
        public String getCrop() { return crop; }
        public String getRegion() { return region; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getTimeAgo() { return timeAgo; }
    }
}
