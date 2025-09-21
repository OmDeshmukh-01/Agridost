package com.example.dummy;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class HFResponse {
    @SerializedName("generated_text")
    private String generatedText;
    
    @SerializedName("error")
    private String error;
    
    @SerializedName("estimated_time")
    private Double estimatedTime;
    
    @SerializedName("warnings")
    private List<String> warnings;

    public String getGeneratedText() {
        return generatedText;
    }

    public void setGeneratedText(String generatedText) {
        this.generatedText = generatedText;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Double getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Double estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }
}
