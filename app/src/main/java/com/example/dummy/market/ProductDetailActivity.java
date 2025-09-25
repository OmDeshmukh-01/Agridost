package com.example.dummy.market;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dummy.BaseActivity;

import com.example.dummy.Product;
import com.example.dummy.R;

public class ProductDetailActivity extends BaseActivity {

    private Product product;
    private ImageView productImage;
    private TextView productName;
    private TextView productBrand;
    private TextView productPrice;
    private TextView productQuantity;
    private TextView productDescription;
    private TextView productRating;
    private TextView productReviews;
    private Button addToCartButton;
    private Button buyNowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        
        // Get product from intent
        product = (Product) getIntent().getSerializableExtra("product");
        if (product == null) {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        initializeViews();
        setupProductDetails();
        setupClickListeners();
    }
    
    private void initializeViews() {
        productImage = findViewById(R.id.product_image);
        productName = findViewById(R.id.product_name);
        productBrand = findViewById(R.id.product_brand);
        productPrice = findViewById(R.id.product_price);
        productQuantity = findViewById(R.id.product_quantity);
        productDescription = findViewById(R.id.product_description);
        productRating = findViewById(R.id.product_rating);
        productReviews = findViewById(R.id.product_reviews);
        addToCartButton = findViewById(R.id.add_to_cart_button);
        buyNowButton = findViewById(R.id.buy_now_button);
    }
    
    private void setupProductDetails() {
        productName.setText(product.getName());
        productBrand.setText(product.getBrand());
        productPrice.setText(product.getPrice());
        productQuantity.setText(product.getQuantity());
        productDescription.setText(product.getDescription());
        productRating.setText(product.getRating() + " ⭐");
        productReviews.setText("(" + product.getReviews() + " reviews)");
        
        // Set product image
        int imageResource = getImageResource(product.getImageResource());
        productImage.setImageResource(imageResource);
    }
    
    private void setupClickListeners() {
        addToCartButton.setOnClickListener(v -> {
            Toast.makeText(this, "Added to cart: " + product.getName(), Toast.LENGTH_SHORT).show();
        });
        
        buyNowButton.setOnClickListener(v -> {
            Toast.makeText(this, "Buy now: " + product.getName(), Toast.LENGTH_SHORT).show();
        });
    }
    
    private int getImageResource(String imageName) {
        switch (imageName) {
            case "acrobat_pesticide":
                return R.drawable.acrobat_pesticide;
            case "aerowon_pesticide":
                return R.drawable.aerowon_pesticide;
            case "neem_oil":
                return R.drawable.neem_oil;
            case "pyrethrum":
                return R.drawable.pyrethrum;
            case "npk_fertilizer":
                return R.drawable.npk_fertilizer;
            case "urea_fertilizer":
                return R.drawable.urea_fertilizer;
            case "dap_fertilizer":
                return R.drawable.dap_fertilizer;
            case "potash_fertilizer":
                return R.drawable.potash_fertilizer;
            case "compost":
                return R.drawable.compost;
            case "tomato_seeds":
                return R.drawable.tomato_seeds;
            case "rice_seeds":
                return R.drawable.rice_seeds;
            case "wheat_seeds":
                return R.drawable.wheat_seeds;
            case "cotton_seeds":
                return R.drawable.cotton_seeds;
            case "maize_seeds":
                return R.drawable.maize_seeds;
            case "vermicompost":
                return R.drawable.vermicompost;
            case "cow_dung":
                return R.drawable.cow_dung;
            case "bone_meal":
                return R.drawable.bone_meal;
            case "fish_meal":
                return R.drawable.fish_meal;
            case "cattle_feed":
                return R.drawable.cattle_feed;
            case "milk_booster":
                return R.drawable.milk_booster;
            case "calf_feed":
                return R.drawable.calf_feed;
            case "spray_pump":
                return R.drawable.spray_pump;
            case "pruning_shears":
                return R.drawable.pruning_shears;
            case "shovel":
                return R.drawable.shovel;
            case "rake":
                return R.drawable.rake;
            default:
                return R.drawable.product_placeholder;
        }
    }
}
