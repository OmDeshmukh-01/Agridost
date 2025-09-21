package com.example.dummy;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SchemeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_detail);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Get scheme data from intent
        FarmerScheme scheme = (FarmerScheme) getIntent().getSerializableExtra("scheme");
        
        if (scheme != null) {
            populateSchemeDetails(scheme);
        }
    }

    private void populateSchemeDetails(FarmerScheme scheme) {
        TextView title = findViewById(R.id.scheme_title);
        TextView description = findViewById(R.id.scheme_description);
        TextView state = findViewById(R.id.scheme_state);
        TextView schemeType = findViewById(R.id.scheme_type);
        TextView amount = findViewById(R.id.scheme_amount);
        TextView status = findViewById(R.id.scheme_status);
        TextView eligibility = findViewById(R.id.scheme_eligibility);
        TextView applicationProcess = findViewById(R.id.scheme_application_process);
        TextView contactInfo = findViewById(R.id.scheme_contact_info);
        TextView website = findViewById(R.id.scheme_website);
        TextView lastUpdated = findViewById(R.id.scheme_last_updated);

        title.setText(scheme.getTitle());
        description.setText(scheme.getDescription());
        state.setText("State: " + scheme.getState());
        schemeType.setText("Type: " + scheme.getSchemeType());
        amount.setText("Amount: " + scheme.getAmount());
        status.setText("Status: " + scheme.getStatus());
        eligibility.setText("Eligibility: " + scheme.getEligibility());
        applicationProcess.setText("Application Process: " + scheme.getApplicationProcess());
        contactInfo.setText("Contact: " + scheme.getContactInfo());
        website.setText("Website: " + scheme.getWebsite());
        lastUpdated.setText("Last Updated: " + scheme.getLastUpdated());

        // Set toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(scheme.getTitle());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
