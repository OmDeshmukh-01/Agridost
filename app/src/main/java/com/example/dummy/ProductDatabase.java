package com.example.dummy;

import java.util.ArrayList;
import java.util.List;

public class ProductDatabase {
    
    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        
        // Pesticides
        products.add(new Product("1", "ACROBAT", "by BASF", "₹3,990", "1 kilogram", 
            "Pesticides", "acrobat_pesticide", "Effective fungicide for crop protection"));
        products.add(new Product("2", "AEROWON", "by GAPL", "₹90", "100 gram", 
            "Pesticides", "aerowon_pesticide", "Insecticide for pest control"));
        products.add(new Product("3", "AEROWON", "by GAPL", "₹55", "50 gram", 
            "Pesticides", "aerowon_pesticide", "Insecticide for pest control"));
        products.add(new Product("4", "AEROWON", "by GAPL", "₹200", "250 gram", 
            "Pesticides", "aerowon_pesticide", "Insecticide for pest control"));
        products.add(new Product("5", "NEEM OIL", "by Organic", "₹150", "500 ml", 
            "Pesticides", "neem_oil", "Natural pest repellent"));
        products.add(new Product("6", "PYRETHRUM", "by Natural", "₹300", "1 liter", 
            "Pesticides", "pyrethrum", "Organic insecticide"));
        
        // Fertilizers
        products.add(new Product("7", "NPK 19-19-19", "by IFFCO", "₹800", "5 kg", 
            "Fertilizers", "npk_fertilizer", "Balanced NPK fertilizer"));
        products.add(new Product("8", "UREA", "by NFL", "₹500", "50 kg", 
            "Fertilizers", "urea_fertilizer", "Nitrogen fertilizer"));
        products.add(new Product("9", "DAP", "by IFFCO", "₹1,200", "50 kg", 
            "Fertilizers", "dap_fertilizer", "Phosphorus fertilizer"));
        products.add(new Product("10", "POTASH", "by KRIBHCO", "₹900", "50 kg", 
            "Fertilizers", "potash_fertilizer", "Potassium fertilizer"));
        products.add(new Product("11", "COMPOST", "by Organic", "₹200", "25 kg", 
            "Fertilizers", "compost", "Organic compost manure"));
        
        // Seeds
        products.add(new Product("12", "TOMATO SEEDS", "by Syngenta", "₹150", "100 seeds", 
            "Seeds", "tomato_seeds", "High yield tomato seeds"));
        products.add(new Product("13", "RICE SEEDS", "by Bayer", "₹300", "1 kg", 
            "Seeds", "rice_seeds", "Premium rice seeds"));
        products.add(new Product("14", "WHEAT SEEDS", "by Pioneer", "₹250", "1 kg", 
            "Seeds", "wheat_seeds", "High quality wheat seeds"));
        products.add(new Product("15", "COTTON SEEDS", "by Mahyco", "₹400", "1 kg", 
            "Seeds", "cotton_seeds", "BT cotton seeds"));
        products.add(new Product("16", "MAIZE SEEDS", "by Monsanto", "₹350", "1 kg", 
            "Seeds", "maize_seeds", "Hybrid maize seeds"));
        
        // Organic
        products.add(new Product("17", "VERMICOMPOST", "by Organic", "₹300", "25 kg", 
            "Organic", "vermicompost", "Organic vermicompost"));
        products.add(new Product("18", "COW DUNG", "by Natural", "₹100", "50 kg", 
            "Organic", "cow_dung", "Natural cow dung manure"));
        products.add(new Product("19", "BONE MEAL", "by Organic", "₹200", "10 kg", 
            "Organic", "bone_meal", "Organic bone meal fertilizer"));
        products.add(new Product("20", "FISH MEAL", "by Organic", "₹400", "10 kg", 
            "Organic", "fish_meal", "Organic fish meal fertilizer"));
        
        // Cattle Feed
        products.add(new Product("21", "CATTLE FEED", "by Godrej", "₹800", "50 kg", 
            "Cattle Feed", "cattle_feed", "Nutritious cattle feed"));
        products.add(new Product("22", "MILK BOOSTER", "by Amul", "₹600", "25 kg", 
            "Cattle Feed", "milk_booster", "Milk production booster"));
        products.add(new Product("23", "CALF FEED", "by Wockhardt", "₹500", "20 kg", 
            "Cattle Feed", "calf_feed", "Special calf nutrition"));
        
        // Tools
        products.add(new Product("24", "SPRAY PUMP", "by Fieldking", "₹2,500", "1 piece", 
            "Tools", "spray_pump", "Manual spray pump"));
        products.add(new Product("25", "PRUNING SHEARS", "by Fiskars", "₹800", "1 piece", 
            "Tools", "pruning_shears", "Garden pruning shears"));
        products.add(new Product("26", "SHOVEL", "by Spear & Jackson", "₹1,200", "1 piece", 
            "Tools", "shovel", "Heavy duty garden shovel"));
        products.add(new Product("27", "RAKE", "by Gardena", "₹600", "1 piece", 
            "Tools", "rake", "Garden rake for soil preparation"));
        
        return products;
    }
    
    public static List<Product> getProductsByCategory(String category) {
        List<Product> allProducts = getAllProducts();
        List<Product> filteredProducts = new ArrayList<>();
        
        for (Product product : allProducts) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                filteredProducts.add(product);
            }
        }
        
        return filteredProducts;
    }
}
