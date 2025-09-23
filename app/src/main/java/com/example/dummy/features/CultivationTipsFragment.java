package com.example.dummy.features;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dummy.CultivationTipsAdapter;
import com.example.dummy.tools.CultivationTipsActivity;
import com.example.dummy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CultivationTipsFragment extends Fragment {

    private ExpandableListView expandableListView;
    private CultivationTipsAdapter adapter;
    private List<String> cropGroups;
    private HashMap<String, List<CultivationTipsActivity.CultivationTip>> tipsData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_cultivation_tips, container, false);
        initializeData();
        setupExpandableListView(view);
        return view;
    }

    private void initializeData() {
        cropGroups = new ArrayList<>();
        tipsData = new HashMap<>();

        cropGroups.add("Rice");
        List<CultivationTipsActivity.CultivationTip> riceTips = new ArrayList<>();
        riceTips.add(new CultivationTipsActivity.CultivationTip("Land Preparation", "Prepare land 2-3 weeks before transplanting. Level the field properly for uniform water distribution."));
        riceTips.add(new CultivationTipsActivity.CultivationTip("Seed Treatment", "Soak seeds in water for 12-24 hours before sowing. Treat with fungicide to prevent diseases."));
        riceTips.add(new CultivationTipsActivity.CultivationTip("Water Management", "Maintain 2-3 cm water depth during vegetative stage. Drain water 7-10 days before harvest."));
        riceTips.add(new CultivationTipsActivity.CultivationTip("Fertilizer Application", "Apply NPK in 3 splits: 50% at transplanting, 25% at tillering, 25% at panicle initiation."));
        tipsData.put("Rice", riceTips);

        cropGroups.add("Wheat");
        List<CultivationTipsActivity.CultivationTip> wheatTips = new ArrayList<>();
        wheatTips.add(new CultivationTipsActivity.CultivationTip("Sowing Time", "Sow wheat from mid-October to mid-November for best yield. Avoid late sowing."));
        wheatTips.add(new CultivationTipsActivity.CultivationTip("Seed Rate", "Use 40-50 kg seeds per hectare. Increase seed rate for late sowing."));
        wheatTips.add(new CultivationTipsActivity.CultivationTip("Irrigation", "Give 4-5 irrigations: at crown root initiation, tillering, flowering, and grain filling stages."));
        wheatTips.add(new CultivationTipsActivity.CultivationTip("Weed Control", "Apply pre-emergence herbicide within 3 days of sowing. Hand weed if necessary."));
        tipsData.put("Wheat", wheatTips);

        cropGroups.add("Tomato");
        List<CultivationTipsActivity.CultivationTip> tomatoTips = new ArrayList<>();
        tomatoTips.add(new CultivationTipsActivity.CultivationTip("Nursery Preparation", "Prepare raised beds 1m wide. Sow seeds 2-3 cm apart in rows 10 cm apart."));
        tomatoTips.add(new CultivationTipsActivity.CultivationTip("Transplanting", "Transplant 25-30 days old seedlings. Space plants 60x45 cm apart."));
        tomatoTips.add(new CultivationTipsActivity.CultivationTip("Staking", "Provide bamboo stakes for support. Tie plants loosely to stakes as they grow."));
        tomatoTips.add(new CultivationTipsActivity.CultivationTip("Pruning", "Remove suckers regularly. Pinch growing tips after 4-5 trusses for better fruit quality."));
        tipsData.put("Tomato", tomatoTips);

        cropGroups.add("Maize");
        List<CultivationTipsActivity.CultivationTip> maizeTips = new ArrayList<>();
        maizeTips.add(new CultivationTipsActivity.CultivationTip("Sowing Method", "Sow in rows 60-75 cm apart. Plant 2-3 seeds per hill, thin to 1 plant per hill."));
        maizeTips.add(new CultivationTipsActivity.CultivationTip("Fertilizer", "Apply 120:60:60 kg NPK per hectare. Apply 50% N at sowing, 50% at knee-high stage."));
        maizeTips.add(new CultivationTipsActivity.CultivationTip("Weed Control", "Keep field weed-free for first 45 days. Use pre-emergence herbicides."));
        maizeTips.add(new CultivationTipsActivity.CultivationTip("Harvesting", "Harvest when kernels are hard and moisture content is 20-25%. Dry properly before storage."));
        tipsData.put("Maize", maizeTips);

        cropGroups.add("General Tips");
        List<CultivationTipsActivity.CultivationTip> generalTips = new ArrayList<>();
        generalTips.add(new CultivationTipsActivity.CultivationTip("Soil Testing", "Test soil every 2-3 years to determine nutrient requirements and pH levels."));
        generalTips.add(new CultivationTipsActivity.CultivationTip("Crop Rotation", "Practice crop rotation to maintain soil fertility and break pest cycles."));
        generalTips.add(new CultivationTipsActivity.CultivationTip("Water Conservation", "Use drip irrigation for water efficiency. Mulch soil to retain moisture."));
        generalTips.add(new CultivationTipsActivity.CultivationTip("Pest Management", "Monitor crops regularly. Use integrated pest management (IPM) approach."));
        generalTips.add(new CultivationTipsActivity.CultivationTip("Post-Harvest", "Clean and dry produce properly. Store in cool, dry place to prevent spoilage."));
        tipsData.put("General Tips", generalTips);
    }

    private void setupExpandableListView(View root) {
        expandableListView = root.findViewById(R.id.expandableListView);
        adapter = new CultivationTipsAdapter(requireContext(), cropGroups, tipsData);
        expandableListView.setAdapter(adapter);
        expandableListView.expandGroup(0);
        expandableListView.setOnGroupExpandListener(groupPosition -> {
            Toast.makeText(requireContext(), cropGroups.get(groupPosition) + " tips expanded", Toast.LENGTH_SHORT).show();
        });
    }

    // Removed inner CultivationTip; using CultivationTipsActivity.CultivationTip to match adapter
}
