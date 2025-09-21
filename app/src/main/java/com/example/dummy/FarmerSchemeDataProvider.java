package com.example.dummy;

import java.util.ArrayList;
import java.util.List;

public class FarmerSchemeDataProvider {
    
    public static List<FarmerScheme> getAllSchemes() {
        List<FarmerScheme> schemes = new ArrayList<>();
        
        // Punjab Schemes
        schemes.add(new FarmerScheme(
            "1", "PM-Kisan Samman Nidhi", 
            "Direct income support scheme providing ₹6000 per year to small and marginal farmers",
            "Punjab", "Financial Support", "₹6000/yr", "Active",
            "Small and marginal farmers with landholding up to 2 hectares",
            "Online application through PM-Kisan portal or visit nearest CSC center",
            "Toll-free: 1800-180-1551", "https://pmkisan.gov.in", "2024-01-15", true
        ));
        
        schemes.add(new FarmerScheme(
            "2", "Fertilizer Subsidy", 
            "50% subsidy on fertilizers for farmers in Punjab",
            "Punjab", "Subsidy", "50% Discount", "Upcoming",
            "All registered farmers in Punjab",
            "Apply through Krishi Vigyan Kendra or online portal",
            "Helpline: 1800-180-1551", "https://fertilizer.gov.in", "2024-02-01", true
        ));
        
        schemes.add(new FarmerScheme(
            "3", "Crop Insurance Scheme", 
            "Comprehensive crop insurance for all major crops",
            "Punjab", "Insurance", "Premium Support", "Active",
            "All farmers growing notified crops",
            "Apply through bank or insurance company",
            "Contact: 1800-425-1551", "https://pmfby.gov.in", "2024-01-10", true
        ));
        
        schemes.add(new FarmerScheme(
            "4", "Kisan Credit Card", 
            "Credit facility for agricultural and allied activities",
            "Punjab", "Credit", "Up to ₹3 Lakh", "Active",
            "Farmers, tenant farmers, oral lessees, sharecroppers",
            "Apply at any commercial bank or cooperative bank",
            "Contact your nearest bank branch", "https://rbi.org.in", "2024-01-05", true
        ));
        
        schemes.add(new FarmerScheme(
            "5", "Solar Pump Scheme", 
            "Subsidy for solar water pumps for irrigation",
            "Punjab", "Infrastructure", "Up to 90% Subsidy", "Active",
            "Farmers with valid land documents",
            "Apply through Punjab Energy Development Agency",
            "PEDA: 0172-2546767", "https://peda.gov.in", "2024-01-20", true
        ));
        
        // Other States Schemes
        schemes.add(new FarmerScheme(
            "6", "Rythu Bandhu", 
            "Investment support scheme for farmers in Telangana",
            "Telangana", "Financial Support", "₹5000/acre", "Active",
            "All farmers owning agricultural land",
            "Automatic credit to bank account",
            "Helpline: 1800-425-1551", "https://rythubandhu.telangana.gov.in", "2024-01-12", true
        ));
        
        schemes.add(new FarmerScheme(
            "7", "KALIA Scheme", 
            "Krushak Assistance for Livelihood and Income Augmentation",
            "Odisha", "Financial Support", "₹10,000/yr", "Active",
            "Small and marginal farmers, landless agricultural households",
            "Apply online or visit block office",
            "Helpline: 1800-345-6770", "https://kalia.odisha.gov.in", "2024-01-08", true
        ));
        
        schemes.add(new FarmerScheme(
            "8", "Mukhya Mantri Krishi Ashirwad Yojana", 
            "Financial assistance to farmers in Jharkhand",
            "Jharkhand", "Financial Support", "₹5000/acre", "Active",
            "All farmers with valid land records",
            "Apply through online portal or block office",
            "Helpline: 1800-345-6770", "https://jharkhand.gov.in", "2024-01-18", true
        ));
        
        schemes.add(new FarmerScheme(
            "9", "Bhavantar Bhugtan Yojana", 
            "Price difference payment scheme for farmers",
            "Madhya Pradesh", "Price Support", "Price Difference", "Active",
            "Farmers growing notified crops",
            "Register on portal during registration period",
            "Helpline: 1800-233-0044", "https://mpkrishi.mp.gov.in", "2024-01-14", true
        ));
        
        schemes.add(new FarmerScheme(
            "10", "Kisan Suryodaya Yojana", 
            "Solar power scheme for farmers in Gujarat",
            "Gujarat", "Infrastructure", "Subsidy Available", "Active",
            "Farmers with agricultural land",
            "Apply through Gujarat Energy Development Agency",
            "GEDA: 079-2325-5555", "https://geda.gujarat.gov.in", "2024-01-22", true
        ));
        
        return schemes;
    }
    
    public static List<String> getAllStates() {
        List<String> states = new ArrayList<>();
        states.add("All States");
        states.add("Punjab");
        states.add("Telangana");
        states.add("Odisha");
        states.add("Jharkhand");
        states.add("Madhya Pradesh");
        states.add("Gujarat");
        states.add("Haryana");
        states.add("Rajasthan");
        states.add("Uttar Pradesh");
        states.add("Maharashtra");
        states.add("Karnataka");
        states.add("Tamil Nadu");
        states.add("West Bengal");
        states.add("Bihar");
        return states;
    }
    
    public static List<String> getAllSchemeTypes() {
        List<String> types = new ArrayList<>();
        types.add("All Types");
        types.add("Financial Support");
        types.add("Subsidy");
        types.add("Insurance");
        types.add("Credit");
        types.add("Infrastructure");
        types.add("Price Support");
        return types;
    }
}
