package com.example.dummy.tools;

import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.dummy.BaseActivity;

import com.example.dummy.CultivationTipsAdapter;
import com.example.dummy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CultivationTipsActivity extends BaseActivity {

    private ExpandableListView expandableListView;
    private CultivationTipsAdapter adapter;
    private List<String> cropGroups;
    private HashMap<String, List<CultivationTip>> tipsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cultivation_tips);
        
        initializeData();
        setupExpandableListView();
    }
    
    private void initializeData() {
        cropGroups = new ArrayList<>();
        tipsData = new HashMap<>();
        
        // Rice Tips
        cropGroups.add("Rice");
        List<CultivationTip> riceTips = new ArrayList<>();
        riceTips.add(new CultivationTip("Land Preparation", "Prepare land 2-3 weeks before transplanting. Level the field properly for uniform water distribution."));
        riceTips.add(new CultivationTip("Seed Treatment", "Soak seeds in water for 12-24 hours before sowing. Treat with fungicide to prevent diseases."));
        riceTips.add(new CultivationTip("Water Management", "Maintain 2-3 cm water depth during vegetative stage. Drain water 7-10 days before harvest."));
        riceTips.add(new CultivationTip("Fertilizer Application", "Apply NPK in 3 splits: 50% at transplanting, 25% at tillering, 25% at panicle initiation."));
        tipsData.put("Rice", riceTips);
        
        // Wheat Tips
        cropGroups.add("Wheat");
        List<CultivationTip> wheatTips = new ArrayList<>();
        wheatTips.add(new CultivationTip("Sowing Time", "Sow wheat from mid-October to mid-November for best yield. Avoid late sowing."));
        wheatTips.add(new CultivationTip("Seed Rate", "Use 40-50 kg seeds per hectare. Increase seed rate for late sowing."));
        wheatTips.add(new CultivationTip("Irrigation", "Give 4-5 irrigations: at crown root initiation, tillering, flowering, and grain filling stages."));
        wheatTips.add(new CultivationTip("Weed Control", "Apply pre-emergence herbicide within 3 days of sowing. Hand weed if necessary."));
        tipsData.put("Wheat", wheatTips);
        
        // Tomato Tips
        cropGroups.add("Tomato");
        List<CultivationTip> tomatoTips = new ArrayList<>();
        tomatoTips.add(new CultivationTip("Nursery Preparation", "Prepare raised beds 1m wide. Sow seeds 2-3 cm apart in rows 10 cm apart."));
        tomatoTips.add(new CultivationTip("Transplanting", "Transplant 25-30 days old seedlings. Space plants 60x45 cm apart."));
        tomatoTips.add(new CultivationTip("Staking", "Provide bamboo stakes for support. Tie plants loosely to stakes as they grow."));
        tomatoTips.add(new CultivationTip("Pruning", "Remove suckers regularly. Pinch growing tips after 4-5 trusses for better fruit quality."));
        tipsData.put("Tomato", tomatoTips);
        
        // Maize Tips
        cropGroups.add("Maize");
        List<CultivationTip> maizeTips = new ArrayList<>();
        maizeTips.add(new CultivationTip("Sowing Method", "Sow in rows 60-75 cm apart. Plant 2-3 seeds per hill, thin to 1 plant per hill."));
        maizeTips.add(new CultivationTip("Fertilizer", "Apply 120:60:60 kg NPK per hectare. Apply 50% N at sowing, 50% at knee-high stage."));
        maizeTips.add(new CultivationTip("Weed Control", "Keep field weed-free for first 45 days. Use pre-emergence herbicides."));
        maizeTips.add(new CultivationTip("Harvesting", "Harvest when kernels are hard and moisture content is 20-25%. Dry properly before storage."));
        tipsData.put("Maize", maizeTips);
        
        // General Tips
        cropGroups.add("General Tips");
        List<CultivationTip> generalTips = new ArrayList<>();
        generalTips.add(new CultivationTip("Soil Testing", "Test soil every 2-3 years to determine nutrient requirements and pH levels."));
        generalTips.add(new CultivationTip("Crop Rotation", "Practice crop rotation to maintain soil fertility and break pest cycles."));
        generalTips.add(new CultivationTip("Water Conservation", "Use drip irrigation for water efficiency. Mulch soil to retain moisture."));
        generalTips.add(new CultivationTip("Pest Management", "Monitor crops regularly. Use integrated pest management (IPM) approach."));
        generalTips.add(new CultivationTip("Post-Harvest", "Clean and dry produce properly. Store in cool, dry place to prevent spoilage."));
        tipsData.put("General Tips", generalTips);
    }
    
    private void setupExpandableListView() {
        expandableListView = findViewById(R.id.expandableListView);
        adapter = new CultivationTipsAdapter(this, cropGroups, tipsData);
        expandableListView.setAdapter(adapter);
        
        // Expand first group by default
        expandableListView.expandGroup(0);
        
        expandableListView.setOnGroupExpandListener(groupPosition -> {
            Toast.makeText(this, cropGroups.get(groupPosition) + " tips expanded", Toast.LENGTH_SHORT).show();
        });
    }
    
    public static class CultivationTip {
        String title;
        String description;
        
        public CultivationTip(String title, String description) {
            this.title = title;
            this.description = description;
        }
        
        public String getTitle() {
            return title;
        }
        
        public String getDescription() {
            return description;
        }
    }
}
