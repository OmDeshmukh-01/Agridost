package com.example.dummy.market;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dummy.ProductItem;
import com.example.dummy.R;

import java.util.ArrayList;
import java.util.List;

public class BuyProductsFragment extends Fragment {

    private EditText searchBar;
    private GridView categoryGrid;
    private RecyclerView productGrid;
    private LinearLayout categoryTabs;
    private TextView sectionTitle;
    private TextView productCount;
    
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private List<CategoryItem> categoryList;
    private List<ProductItem> productList;
    private String selectedCategory = "Pesticides";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_products, container, false);
        
        try {
            initializeViews(view);
            setupCategories();
            setupProducts();
            setupSearch();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return view;
    }
    
    private void initializeViews(View view) {
        searchBar = view.findViewById(R.id.search_bar);
        categoryGrid = view.findViewById(R.id.category_grid);
        productGrid = view.findViewById(R.id.product_grid);
        categoryTabs = view.findViewById(R.id.category_tabs);
        sectionTitle = view.findViewById(R.id.section_title);
        productCount = view.findViewById(R.id.product_count);
    }
    
    private void setupCategories() {
        if (getContext() == null) return;
        
        categoryList = new ArrayList<>();
        
        // Add categories matching the exact design from images
        categoryList.add(new CategoryItem("Pesticides", R.drawable.pesticide, "#FFCDD2")); // Pinkish-red
        categoryList.add(new CategoryItem("Fertilizers", R.drawable.fertilizer, "#B2DFDB")); // Light blue-green
        categoryList.add(new CategoryItem("Seeds", R.drawable.seeds, "#C8E6C9")); // Light green
        categoryList.add(new CategoryItem("Organic Crop Protection", R.drawable.neemoil, "#E1BEE7")); // Light purple
        categoryList.add(new CategoryItem("Organic Crop Nutrition", R.drawable.ic_organic, "#FFF9C4")); // Light yellow
        categoryList.add(new CategoryItem("Cattle Feed", R.drawable.ic_cattle_feed, "#E1BEE7")); // Light purple
        categoryList.add(new CategoryItem("Tools and Machinery", R.drawable.ic_tools, "#BBDEFB")); // Light blue
        
        categoryAdapter = new CategoryAdapter(categoryList, new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(CategoryItem category) {
                selectedCategory = category.getName();
                productAdapter.updateProducts(getFilteredProducts(selectedCategory));
                updateSectionTitle(selectedCategory);
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Selected: " + category.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        categoryGrid.setAdapter(categoryAdapter);
        
        // Setup category tabs
        setupCategoryTabs();
    }
    
    private void setupCategoryTabs() {
        if (getContext() == null) return;
        
        String[] tabCategories = {"Pesticides", "Fertilizers", "Seeds", "Organic Crop Protection", "Organic Crop Nutrition", "Cattle Feed", "Tools and Machinery"};
        
        for (String category : tabCategories) {
            TextView tab = new TextView(getContext());
            tab.setText(category);
            tab.setTextSize(14);
            tab.setTextColor(getResources().getColor(android.R.color.black));
            tab.setPadding(24, 12, 24, 12);
            tab.setBackground(getResources().getDrawable(android.R.color.transparent));
            
            if (category.equals(selectedCategory)) {
                tab.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                tab.setBackground(getResources().getDrawable(R.drawable.tab_underline));
            }
            
            tab.setOnClickListener(v -> {
                selectedCategory = category;
                productAdapter.updateProducts(getFilteredProducts(selectedCategory));
                updateSectionTitle(selectedCategory);
                updateTabSelection(tab);
            });
            
            categoryTabs.addView(tab);
        }
    }
    
    private void updateTabSelection(TextView selectedTab) {
        for (int i = 0; i < categoryTabs.getChildCount(); i++) {
            TextView tab = (TextView) categoryTabs.getChildAt(i);
            if (tab == selectedTab) {
                tab.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                tab.setBackground(getResources().getDrawable(R.drawable.tab_underline));
            } else {
                tab.setTextColor(getResources().getColor(android.R.color.black));
                tab.setBackground(getResources().getDrawable(android.R.color.transparent));
            }
        }
    }
    
    private void updateSectionTitle(String category) {
        if (sectionTitle != null) {
            sectionTitle.setText(category);
        }
        if (productCount != null) {
            int count = getProductCountForCategory(category);
            productCount.setText(count + " products");
        }
    }
    
    private int getProductCountForCategory(String category) {
        int count = 0;
        for (ProductItem product : productList) {
            if (product.getCategory().equals(category)) {
                count++;
            }
        }
        return count;
    }
    
    private void setupProducts() {
        if (getContext() == null) return;
        
        productList = new ArrayList<>();
        
        productAdapter = new ProductAdapter(new ArrayList<>(), new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(ProductItem product) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Selected: " + product.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        productGrid.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productGrid.setAdapter(productAdapter);
        
        // Load products after adapter is set
        loadProducts();
        updateSectionTitle(selectedCategory);
        productAdapter.updateProducts(getFilteredProducts(selectedCategory));
    }
    
    private void loadProducts() {
        productList.clear();
        
        // Pesticides - matching the exact products from the images
        productList.add(new ProductItem("ACROBAT", "by BASF", "₹3,990", "1 kilogram", R.drawable.acrobat, "Pesticides"));
        productList.add(new ProductItem("AEROWON", "by GAPL", "₹90", "100 gram", R.drawable.aerowon, "Pesticides"));
        productList.add(new ProductItem("AEROWON", "by GAPL", "₹55", "50 gram", R.drawable.ic_pesticide_3, "Pesticides"));
        productList.add(new ProductItem("AEROWON", "by GAPL", "₹200", "200 gram", R.drawable.ic_pesticide_4, "Pesticides"));
        productList.add(new ProductItem("ACROBAT", "by BASF", "₹1,990", "500 gram", R.drawable.ic_pesticide_5, "Pesticides"));
        productList.add(new ProductItem("AEROWON", "by GAPL", "₹150", "150 gram", R.drawable.ic_pesticide_6, "Pesticides"));

        // Fertilizers
        productList.add(new ProductItem("UREA", "by IFFCO", "₹266", "45kg", R.drawable.urea, "Fertilizers"));
        productList.add(new ProductItem("DAP", "by KRIBHCO", "₹1,350", "50kg", R.drawable.dap, "Fertilizers"));
        productList.add(new ProductItem("POTASH", "by GSFC", "₹900", "25kg", R.drawable.ic_fertilizer_3, "Fertilizers"));
        productList.add(new ProductItem("NPK 19:19:19", "by Zuari Agro", "₹1,200", "25kg", R.drawable.ic_fertilizer_4, "Fertilizers"));

        // Seeds
        productList.add(new ProductItem("Hybrid Maize", "by MAHYCO", "₹800", "5kg", R.drawable.hybridmaze, "Seeds"));
        productList.add(new ProductItem("Paddy Seeds", "by KAAVERI", "₹1,200", "10kg", R.drawable.paddyseeds, "Seeds"));
        productList.add(new ProductItem("Cotton Seeds", "by Nuziveedu", "₹1,000", "1kg", R.drawable.ic_seed_3, "Seeds"));
        productList.add(new ProductItem("Wheat Seeds", "by Shriram", "₹950", "20kg", R.drawable.ic_seed_4, "Seeds"));

        // Organic Crop Protection
        productList.add(new ProductItem("Neem Oil", "by Organic", "₹450", "500ml", R.drawable.neemoil, "Organic Crop Protection"));
        productList.add(new ProductItem("Bio Pesticide", "by Organic", "₹350", "250ml", R.drawable.organiccompost, "Organic Crop Protection"));

        // Organic Crop Nutrition
        productList.add(new ProductItem("Vermicompost", "by Organic", "₹200", "5kg", R.drawable.biopoly, "Organic Crop Nutrition"));
        productList.add(new ProductItem("Bone Meal", "by Organic", "₹300", "2kg", R.drawable.bonemeal, "Organic Crop Nutrition"));

        // Cattle Feed
        productList.add(new ProductItem("Cattle Feed", "by Premium", "₹1,500", "25kg", R.drawable.cattlefeed, "Cattle Feed"));
        productList.add(new ProductItem("Milk Booster", "by Premium", "₹800", "10kg", R.drawable.milkbooster, "Cattle Feed"));

        // Tools and Machinery
        productList.add(new ProductItem("Spray Pump", "by Tools", "₹2,500", "1 piece", R.drawable.spraypump, "Tools and Machinery"));
        productList.add(new ProductItem("Pruning Shears", "by Tools", "₹800", "1 piece", R.drawable.pruning, "Tools and Machinery"));

        // Nothing else here; filtering handled after adapter setup
    }
    
    private List<ProductItem> getFilteredProducts(String category) {
        List<ProductItem> filteredProducts = new ArrayList<>();
        for (ProductItem product : productList) {
            if (product.getCategory().equals(category)) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }
    
    private void setupSearch() {
        searchBar.setOnClickListener(v -> {
            if (getContext() != null) {
                Toast.makeText(getContext(), "Search functionality coming soon!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
