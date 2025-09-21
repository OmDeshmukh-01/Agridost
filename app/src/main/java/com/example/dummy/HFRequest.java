package com.example.dummy;

import com.google.gson.annotations.SerializedName;

public class HFRequest {
    @SerializedName("inputs")
    private String inputs;
    
    @SerializedName("parameters")
    private Parameters parameters;

    public HFRequest(String inputs, Parameters parameters) {
        this.inputs = inputs;
        this.parameters = parameters;
    }

    public static class Parameters {
        @SerializedName("max_length")
        private int maxLength;
        
        @SerializedName("temperature")
        private double temperature;
        
        @SerializedName("top_p")
        private double topP;
        
        @SerializedName("top_k")
        private int topK;
        
        @SerializedName("do_sample")
        private boolean doSample;
        
        @SerializedName("pad_token_id")
        private int padTokenId;

        public Parameters(int maxLength, double temperature, double topP, int topK, boolean doSample, int padTokenId) {
            this.maxLength = maxLength;
            this.temperature = temperature;
            this.topP = topP;
            this.topK = topK;
            this.doSample = doSample;
            this.padTokenId = padTokenId;
        }

        // Getters and setters
        public int getMaxLength() {
            return maxLength;
        }

        public void setMaxLength(int maxLength) {
            this.maxLength = maxLength;
        }

        public double getTemperature() {
            return temperature;
        }

        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }

        public double getTopP() {
            return topP;
        }

        public void setTopP(double topP) {
            this.topP = topP;
        }

        public int getTopK() {
            return topK;
        }

        public void setTopK(int topK) {
            this.topK = topK;
        }

        public boolean isDoSample() {
            return doSample;
        }

        public void setDoSample(boolean doSample) {
            this.doSample = doSample;
        }

        public int getPadTokenId() {
            return padTokenId;
        }

        public void setPadTokenId(int padTokenId) {
            this.padTokenId = padTokenId;
        }
    }

    public String getInputs() {
        return inputs;
    }

    public void setInputs(String inputs) {
        this.inputs = inputs;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }
}
