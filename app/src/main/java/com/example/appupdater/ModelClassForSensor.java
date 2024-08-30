package com.example.appupdater;


public class ModelClassForSensor {
    private String title;
    private String vendor;
    private int type;

    private int intresid;
    public ModelClassForSensor(String title, String vendor, int type , int intresid) {
        this.title = title;
        this.vendor = vendor;
        this.type = type;
        this.intresid=intresid;
    }
    public ModelClassForSensor(String title, String vendor , int intresid) {
        this.title = title;
        this.vendor = vendor;
        this.intresid=intresid;
    }

    public int getIntresid() {
        return intresid;
    }

    public void setIntresid(int intresid) {
        this.intresid = intresid;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return vendor;
    }

    public void setDescription(String vendor) {
        this.vendor = vendor;
    }

    public int gettype() {
        return type;
    }

    public void settype(int type) {
        this.type = type;
    }
}

