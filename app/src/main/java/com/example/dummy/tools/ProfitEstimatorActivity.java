package com.example.dummy.tools;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dummy.BaseActivity;

import com.example.dummy.R;

public class ProfitEstimatorActivity extends BaseActivity {

    private EditText etFieldArea;
    private Spinner spinnerCropType;
    private EditText etExpectedYield;
    private EditText etMarketPrice;
    private EditText etSeedCost;
    private EditText etFertilizerCost;
    private EditText etLaborCost;
    private EditText etOtherCosts;
    private Button btnCalculate;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit_estimator);
        
        initializeViews();
        setupClickListeners();
    }
    
    private void initializeViews() {
        etFieldArea = findViewById(R.id.etFieldArea);
        spinnerCropType = findViewById(R.id.spinnerCropType);
        etExpectedYield = findViewById(R.id.etExpectedYield);
        etMarketPrice = findViewById(R.id.etMarketPrice);
        etSeedCost = findViewById(R.id.etSeedCost);
        etFertilizerCost = findViewById(R.id.etFertilizerCost);
        etLaborCost = findViewById(R.id.etLaborCost);
        etOtherCosts = findViewById(R.id.etOtherCosts);
        btnCalculate = findViewById(R.id.btnCalculate);
        tvResult = findViewById(R.id.tvResult);
    }
    
    private void setupClickListeners() {
        btnCalculate.setOnClickListener(v -> calculateProfit());
    }
    
    private void calculateProfit() {
        try {
            // Get input values
            double fieldArea = Double.parseDouble(etFieldArea.getText().toString().trim());
            double expectedYield = Double.parseDouble(etExpectedYield.getText().toString().trim());
            double marketPrice = Double.parseDouble(etMarketPrice.getText().toString().trim());
            double seedCost = Double.parseDouble(etSeedCost.getText().toString().trim());
            double fertilizerCost = Double.parseDouble(etFertilizerCost.getText().toString().trim());
            double laborCost = Double.parseDouble(etLaborCost.getText().toString().trim());
            double otherCosts = Double.parseDouble(etOtherCosts.getText().toString().trim());
            
            // Calculate total revenue
            double totalRevenue = expectedYield * marketPrice;
            
            // Calculate total costs
            double totalCosts = seedCost + fertilizerCost + laborCost + otherCosts;
            
            // Calculate profit/loss
            double profit = totalRevenue - totalCosts;
            double profitPerHectare = profit / fieldArea;
            double profitMargin = (profit / totalRevenue) * 100;
            
            // Display results
            displayResults(totalRevenue, totalCosts, profit, profitPerHectare, profitMargin);
            
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers in all fields", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void displayResults(double totalRevenue, double totalCosts, double profit, 
                              double profitPerHectare, double profitMargin) {
        StringBuilder result = new StringBuilder();
        
        result.append("PROFIT ESTIMATION RESULTS\n");
        result.append("========================\n\n");
        
        result.append("REVENUE:\n");
        result.append("• Total Revenue: ₹").append(String.format("%.2f", totalRevenue)).append("\n\n");
        
        result.append("COSTS:\n");
        result.append("• Total Costs: ₹").append(String.format("%.2f", totalCosts)).append("\n");
        result.append("  - Seed Cost: ₹").append(String.format("%.2f", Double.parseDouble(etSeedCost.getText().toString()))).append("\n");
        result.append("  - Fertilizer Cost: ₹").append(String.format("%.2f", Double.parseDouble(etFertilizerCost.getText().toString()))).append("\n");
        result.append("  - Labor Cost: ₹").append(String.format("%.2f", Double.parseDouble(etLaborCost.getText().toString()))).append("\n");
        result.append("  - Other Costs: ₹").append(String.format("%.2f", Double.parseDouble(etOtherCosts.getText().toString()))).append("\n\n");
        
        result.append("PROFIT ANALYSIS:\n");
        if (profit >= 0) {
            result.append("• Net Profit: ₹").append(String.format("%.2f", profit)).append("\n");
            result.append("• Profit per Hectare: ₹").append(String.format("%.2f", profitPerHectare)).append("\n");
            result.append("• Profit Margin: ").append(String.format("%.1f", profitMargin)).append("%\n\n");
        } else {
            result.append("• Net Loss: ₹").append(String.format("%.2f", Math.abs(profit))).append("\n");
            result.append("• Loss per Hectare: ₹").append(String.format("%.2f", Math.abs(profitPerHectare))).append("\n");
            result.append("• Loss Margin: ").append(String.format("%.1f", Math.abs(profitMargin))).append("%\n\n");
        }
        
        result.append("RECOMMENDATIONS:\n");
        if (profitMargin > 20) {
            result.append("✓ Excellent profit margin! Consider expanding production.\n");
        } else if (profitMargin > 10) {
            result.append("✓ Good profit margin. Monitor costs to maintain profitability.\n");
        } else if (profitMargin > 0) {
            result.append("⚠ Low profit margin. Look for ways to reduce costs or increase yield.\n");
        } else {
            result.append("⚠ Loss-making venture. Consider:\n");
            result.append("  - Reducing input costs\n");
            result.append("  - Increasing yield through better practices\n");
            result.append("  - Finding better market prices\n");
            result.append("  - Switching to more profitable crops\n");
        }
        
        tvResult.setText(result.toString());
        tvResult.setVisibility(View.VISIBLE);
    }
}
