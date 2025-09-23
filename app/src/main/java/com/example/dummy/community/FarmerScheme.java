package com.example.dummy.community;

import java.io.Serializable;

public class FarmerScheme implements Serializable {
    private String id;
    private String title;
    private String description;
    private String state;
    private String schemeType;
    private String amount;
    private String status;
    private String eligibility;
    private String applicationProcess;
    private String contactInfo;
    private String website;
    private String lastUpdated;
    private boolean isActive;

    public FarmerScheme() {}

    public FarmerScheme(String id, String title, String description, String state, String schemeType, 
                       String amount, String status, String eligibility, String applicationProcess, 
                       String contactInfo, String website, String lastUpdated, boolean isActive) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.state = state;
        this.schemeType = schemeType;
        this.amount = amount;
        this.status = status;
        this.eligibility = eligibility;
        this.applicationProcess = applicationProcess;
        this.contactInfo = contactInfo;
        this.website = website;
        this.lastUpdated = lastUpdated;
        this.isActive = isActive;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getSchemeType() { return schemeType; }
    public void setSchemeType(String schemeType) { this.schemeType = schemeType; }

    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getEligibility() { return eligibility; }
    public void setEligibility(String eligibility) { this.eligibility = eligibility; }

    public String getApplicationProcess() { return applicationProcess; }
    public void setApplicationProcess(String applicationProcess) { this.applicationProcess = applicationProcess; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public String getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
