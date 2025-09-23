package com.example.dummy.features;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.dummy.R;

public class FeatureHostActivity extends AppCompatActivity {

    public static final String EXTRA_FEATURE_KEY = "feature_key";

    public static final String FEATURE_FERTILIZER = "fertilizer_calculator";
    public static final String FEATURE_CROP_HEAL = "crop_heal";
    public static final String FEATURE_CULTIVATION_TIPS = "cultivation_tips";
    public static final String FEATURE_DISEASE_ALERT = "disease_alert";
    public static final String FEATURE_PROFIT_ESTIMATOR = "profit_estimator";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_host);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            String feature = intent != null ? intent.getStringExtra(EXTRA_FEATURE_KEY) : null;
            Fragment fragment = createFragmentForFeature(feature);
            if (fragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.feature_container, fragment)
                        .commit();
            }
        }
    }

    private Fragment createFragmentForFeature(@Nullable String feature) {
        if (FEATURE_FERTILIZER.equals(feature)) {
            setTitle("Fertilizer Calculator");
            return new FertilizerCalculatorFragment();
        } else if (FEATURE_CROP_HEAL.equals(feature)) {
            setTitle("Crop Heal");
            return new CropHealFragment();
        } else if (FEATURE_CULTIVATION_TIPS.equals(feature)) {
            setTitle("Cultivation Tips");
            return new CultivationTipsFragment();
        } else if (FEATURE_DISEASE_ALERT.equals(feature)) {
            setTitle("Disease Alert");
            return new DiseaseAlertFragment();
        } else if (FEATURE_PROFIT_ESTIMATOR.equals(feature)) {
            setTitle("Profit Estimator");
            return new ProfitEstimatorFragment();
        }
        return null;
    }
}
