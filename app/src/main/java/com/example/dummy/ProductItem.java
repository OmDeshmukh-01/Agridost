package com.example.dummy;

public class ProductItem {
    private String name;
    private String brand;
    private String price;
    private String quantity;
    private int imageResource;
    private String category;

    public ProductItem(String name, String brand, String price, String quantity, int imageResource, String category) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.imageResource = imageResource;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getCategory() {
        return category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
