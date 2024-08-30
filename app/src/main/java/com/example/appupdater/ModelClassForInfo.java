package com.example.appupdater;

public class ModelClassForInfo {
    private String title;
    private String description;
    private int iconResId;

    public ModelClassForInfo(String title, String description, int iconResId) {
        this.title = title;
        this.description = description;
        this.iconResId = iconResId;
    }
    public ModelClassForInfo(String title,int iconResId) {
        this.title = title;
        this.iconResId = iconResId;
    }
    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }
}

