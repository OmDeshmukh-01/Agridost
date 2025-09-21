package com.example.dummy;

import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String name;
    private String brand;
    private String price;
    private String quantity;
    private String category;
    private String imageResource;
    private String description;
    private String rating;
    private String reviews;
    private boolean inStock;
    private String discount;

    public Product(String id, String name, String brand, String price, String quantity, 
                   String category, String imageResource, String description) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.imageResource = imageResource;
        this.description = description;
        this.rating = "4.5";
        this.reviews = "120";
        this.inStock = true;
        this.discount = "0";
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public String getPrice() { return price; }
    public String getQuantity() { return quantity; }
    public String getCategory() { return category; }
    public String getImageResource() { return imageResource; }
    public String getDescription() { return description; }
    public String getRating() { return rating; }
    public String getReviews() { return reviews; }
    public boolean isInStock() { return inStock; }
    public String getDiscount() { return discount; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setPrice(String price) { this.price = price; }
    public void setQuantity(String quantity) { this.quantity = quantity; }
    public void setCategory(String category) { this.category = category; }
    public void setImageResource(String imageResource) { this.imageResource = imageResource; }
    public void setDescription(String description) { this.description = description; }
    public void setRating(String rating) { this.rating = rating; }
    public void setReviews(String reviews) { this.reviews = reviews; }
    public void setInStock(boolean inStock) { this.inStock = inStock; }
    public void setDiscount(String discount) { this.discount = discount; }
}
