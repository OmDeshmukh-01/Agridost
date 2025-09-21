package com.example.dummy;

import java.util.ArrayList;
import java.util.List;

public class CategoryDatabase {
    
    public static List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        
        categories.add(new Category("Pesticides", R.drawable.ic_pesticides, R.color.category_pesticides, 135));
        categories.add(new Category("Fertilizers", R.drawable.ic_fertilizers, R.color.category_fertilizers, 89));
        categories.add(new Category("Seeds", R.drawable.ic_seeds, R.color.category_seeds, 156));
        categories.add(new Category("Organic Crop Protection", R.drawable.ic_organic_protection, R.color.category_organic_protection, 67));
        categories.add(new Category("Organic Crop Nutrition", R.drawable.ic_organic_nutrition, R.color.category_organic_nutrition, 45));
        categories.add(new Category("Cattle Feed", R.drawable.ic_cattle_feed, R.color.category_cattle_feed, 78));
        categories.add(new Category("Tools and Machinery", R.drawable.ic_tools, R.color.category_tools, 92));
        
        return categories;
    }
}
