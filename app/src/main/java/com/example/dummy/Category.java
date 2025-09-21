package com.example.dummy;

public class Category {
    private String name;
    private int iconResource;
    private int backgroundColor;
    private int productCount;

    public Category(String name, int iconResource, int backgroundColor, int productCount) {
        this.name = name;
        this.iconResource = iconResource;
        this.backgroundColor = backgroundColor;
        this.productCount = productCount;
    }

    // Getters
    public String getName() { return name; }
    public int getIconResource() { return iconResource; }
    public int getBackgroundColor() { return backgroundColor; }
    public int getProductCount() { return productCount; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setIconResource(int iconResource) { this.iconResource = iconResource; }
    public void setBackgroundColor(int backgroundColor) { this.backgroundColor = backgroundColor; }
    public void setProductCount(int productCount) { this.productCount = productCount; }
}
