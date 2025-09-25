package com.example.dummy.market;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dummy.BaseActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.dummy.ProductItem;
import com.example.dummy.R;
import com.google.android.material.button.MaterialButton;

public class BuyActivity extends BaseActivity {

    private ImageView productImage;
    private TextView productName;
    private TextView productBrand;
    private TextView productPrice;
    private TextView productQuantity;
    private TextView productDescription;
    private TextView totalPrice;
    private NumberPicker quantityPicker;
    private MaterialButton addToCartButton;
    private MaterialButton buyNowButton;
    private LinearLayout quantityContainer;
    private LinearLayout priceContainer;
    private LinearLayout descriptionContainer;
    
    private ProductItem selectedProduct;
    private int currentQuantity = 1;
    private double basePrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        
        try {
            initializeViews();
            setupToolbar();
            getProductData();
            setupQuantityPicker();
            setupButtons();
            updateUI();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading product details", Toast.LENGTH_LONG).show();
            finish();
        }
    }
    
    private void initializeViews() {
        productImage = findViewById(R.id.product_image);
        productName = findViewById(R.id.product_name);
        productBrand = findViewById(R.id.product_brand);
        productPrice = findViewById(R.id.product_price);
        productQuantity = findViewById(R.id.product_quantity);
        productDescription = findViewById(R.id.product_description);
        totalPrice = findViewById(R.id.total_price);
        quantityPicker = findViewById(R.id.quantity_picker);
        addToCartButton = findViewById(R.id.add_to_cart_button);
        buyNowButton = findViewById(R.id.buy_now_button);
        quantityContainer = findViewById(R.id.quantity_container);
        priceContainer = findViewById(R.id.price_container);
        descriptionContainer = findViewById(R.id.description_container);
    }
    
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Product Details");
        }
        
        toolbar.setNavigationOnClickListener(v -> finish());
    }
    
    private void getProductData() {
        // Get product data from intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("product")) {
            selectedProduct = (ProductItem) intent.getSerializableExtra("product");
        } else {
            // Default product for testing
            selectedProduct = new ProductItem(
                "ACROBAT", 
                "by BASF", 
                "₹3,990", 
                "1 kilogram", 
                R.drawable.ic_pesticide_1, 
                "Pesticides"
            );
        }
        
        // Parse base price
        if (selectedProduct != null) {
            String priceStr = selectedProduct.getPrice().replace("₹", "").replace(",", "");
            try {
                basePrice = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                basePrice = 3990.0; // Default price
            }
        }
    }
    
    private void setupQuantityPicker() {
        quantityPicker.setMinValue(1);
        quantityPicker.setMaxValue(100);
        quantityPicker.setValue(1);
        quantityPicker.setWrapSelectorWheel(false);
        
        quantityPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            currentQuantity = newVal;
            updateTotalPrice();
        });
    }
    
    private void setupButtons() {
        addToCartButton.setOnClickListener(v -> {
            addToCart();
        });
        
        buyNowButton.setOnClickListener(v -> {
            buyNow();
        });
    }
    
    private void updateUI() {
        if (selectedProduct == null) return;
        
        // Set product details
        productName.setText(selectedProduct.getName());
        productBrand.setText(selectedProduct.getBrand());
        productPrice.setText(selectedProduct.getPrice());
        productQuantity.setText(selectedProduct.getQuantity());
        
        // Set product image
        if (selectedProduct.getImageResource() != 0) {
            productImage.setImageResource(selectedProduct.getImageResource());
        }
        
        // Set product description based on category
        String description = getProductDescription(selectedProduct);
        productDescription.setText(description);
        
        // Update total price
        updateTotalPrice();
    }
    
    private String getProductDescription(ProductItem product) {
        String category = product.getCategory();
        String name = product.getName();
        
        switch (category) {
            case "Pesticides":
                if (name.contains("ACROBAT")) {
                    return "ACROBAT is a systemic fungicide that provides excellent protection against late blight and other fungal diseases. It's highly effective for potato and tomato crops with long-lasting protection.";
                } else if (name.contains("AEROWON")) {
                    return "AEROWON is a broad-spectrum insecticide that controls a wide range of pests including aphids, whiteflies, and thrips. Safe for use on vegetables and fruits.";
                }
                return "High-quality pesticide for effective crop protection against various pests and diseases.";
                
            case "Fertilizers":
                if (name.contains("UREA")) {
                    return "UREA is a nitrogen-rich fertilizer that promotes healthy plant growth and increases yield. Essential for leafy vegetables and cereal crops.";
                } else if (name.contains("DAP")) {
                    return "DAP (Diammonium Phosphate) provides essential phosphorus and nitrogen for root development and flowering. Ideal for all crops.";
                }
                return "Premium quality fertilizer to enhance soil fertility and crop productivity.";
                
            case "Seeds":
                return "High-yielding, disease-resistant seeds with excellent germination rate. Perfect for modern farming practices.";
                
            case "Organic Crop Protection":
                return "100% organic and natural crop protection solution. Safe for environment and beneficial insects.";
                
            case "Organic Crop Nutrition":
                return "Organic nutrition products that improve soil health and provide essential nutrients naturally.";
                
            case "Cattle Feed":
                return "Nutritionally balanced cattle feed that improves milk production and overall health of livestock.";
                
            case "Tools and Machinery":
                return "Durable and efficient farming tools designed for modern agricultural practices.";
                
            default:
                return "High-quality agricultural product for better farming results.";
        }
    }
    
    private void updateTotalPrice() {
        double total = basePrice * currentQuantity;
        String formattedTotal = String.format("₹%,.0f", total);
        totalPrice.setText(formattedTotal);
    }
    
    private void addToCart() {
        // Add to cart functionality
        CartItem cartItem = new CartItem(selectedProduct, currentQuantity, basePrice * currentQuantity);
        
        // Here you would typically save to a cart database or shared preferences
        // For now, we'll just show a success message
        Toast.makeText(this, 
            "Added " + currentQuantity + " x " + selectedProduct.getName() + " to cart", 
            Toast.LENGTH_LONG).show();
        
        // You could also navigate to cart activity
        // Intent intent = new Intent(this, CartActivity.class);
        // startActivity(intent);
    }
    
    private void buyNow() {
        // Buy now functionality
        double totalAmount = basePrice * currentQuantity;
        
        // Create order summary
        String orderSummary = "Order Summary:\n" +
            "Product: " + selectedProduct.getName() + "\n" +
            "Brand: " + selectedProduct.getBrand() + "\n" +
            "Quantity: " + currentQuantity + " x " + selectedProduct.getQuantity() + "\n" +
            "Total: ₹" + String.format("%,.0f", totalAmount);
        
        // Show order confirmation dialog or navigate to checkout
        Toast.makeText(this, "Proceeding to checkout...", Toast.LENGTH_SHORT).show();
        
        // You could navigate to checkout activity
        // Intent intent = new Intent(this, CheckoutActivity.class);
        // intent.putExtra("order_summary", orderSummary);
        // intent.putExtra("total_amount", totalAmount);
        // startActivity(intent);
    }
    
    // Cart item class for cart functionality
    public static class CartItem {
        private ProductItem product;
        private int quantity;
        private double totalPrice;
        
        public CartItem(ProductItem product, int quantity, double totalPrice) {
            this.product = product;
            this.quantity = quantity;
            this.totalPrice = totalPrice;
        }
        
        public ProductItem getProduct() { return product; }
        public int getQuantity() { return quantity; }
        public double getTotalPrice() { return totalPrice; }
    }
}
