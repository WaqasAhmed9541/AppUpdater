package com.example.appupdater;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ModelClassForextraFile {
    private String title;
    private int intresid;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIntresid() {
        return intresid;
    }

    public void setIntresid(int intresid) {
        this.intresid = intresid;
    }

    public List<File> getFilelist() {
        return filelist;
    }

    public void setFilelist(List<File> filelist) {
        this.filelist = filelist;
    }

    List<File> filelist=new ArrayList<>();

    public ModelClassForextraFile(String title, int intresid, List<File> filelist) {
        this.title = title;
        this.intresid = intresid;
        this.filelist = filelist;
    }
}
