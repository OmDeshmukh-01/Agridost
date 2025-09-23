package com.example.dummy.market;

public class CategoryItem {
    private String name;
    private int iconResource;
    private String backgroundColor;

    public CategoryItem(String name, int iconResource, String backgroundColor) {
        this.name = name;
        this.iconResource = iconResource;
        this.backgroundColor = backgroundColor;
    }

    public String getName() {
        return name;
    }

    public int getIconResource() {
        return iconResource;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
