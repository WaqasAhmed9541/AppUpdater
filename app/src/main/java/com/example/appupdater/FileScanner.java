package com.example.appupdater;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileScanner {

    private Context context;
    private List<File> systemExtraFiles = new ArrayList<>();
    private List<File> appExtraFiles = new ArrayList<>();
    private List<File> emptyFolders = new ArrayList<>();
    private List<File> apkFiles = new ArrayList<>();
    private List<ApplicationInfo> appList = new ArrayList<>();

    // Listener interface to notify when a file is scanned
    public interface FileScanListener {
        void onFileScanned(File file);
    }

    private FileScanListener fileScanListener;

    // Constructor to initialize the context
    public FileScanner(Context context) {
        this.context = context;
    }

    // Setter for the file scan listener
    public void setFileScanListener(FileScanListener listener) {
        this.fileScanListener = listener;
    }

    // Method to start scanning the external storage
    public void scanExternalStorage() {
        File externalStorageDir = Environment.getExternalStorageDirectory();


        scanDirectory(externalStorageDir);
    }



// Now `allCacheFiles` contains all the cache files from the device's applications.


    // Recursive method to scan directories and categorize files
    private void scanDirectory(File directory) {
        if (directory == null || !directory.exists()) return;

        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                if (isEmptyFolder(file)) {
                    emptyFolders.add(file);

                    if (fileScanListener != null) {
                        fileScanListener.onFileScanned(file);
                    }
                } else {
                    scanDirectory(file); // Recursively scan subdirectories
                }
            } else {
                categorizeFile(file);
                // Notify about the file
                if (fileScanListener != null) {
                    fileScanListener.onFileScanned(file);
                }
            }
        }
    }

    // Method to check if a folder is empty
    private boolean isEmptyFolder(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            return (files == null || files.length == 0);
        }
        return false;
    }

    // Method to categorize a file
    private void categorizeFile(File file) {
        String fileName = file.getName();

        // Detect APK files
        if (fileName.endsWith(".apk")) {
            apkFiles.add(file);
        }

        // Detect app extra files (e.g., residual files, known app folders)
        if (isAppExtraFile(file)) {
            appExtraFiles.add(file);
        }

        // Detect system extra files (e.g., log files, temporary files)
        if (isSystemExtraFile(file)) {
            systemExtraFiles.add(file);
        }
    }

    // Method to determine if a file is an app extra file
    private boolean isAppExtraFile(File file) {

        String filePath = file.getAbsolutePath();
        List<String> pkglist=new ArrayList<>();
        pkglist=getapp();

//        if(file.isDirectory()){
            String dirName=file.getName().toString();
            for(int i=0;i<=pkglist.size()-1;i++){
                String pakageName=pkglist.get(i);
                if(pakageName.contains(dirName)){
                    if(containsCacheDirectory(file)) {
                    return true;
                    }
                }
            }

//        }

      return false;
    }

    private boolean containsCacheDirectory(File dir) {
        File[] subFiles = dir.listFiles();
        if (subFiles != null) {
            for (File subFile : subFiles) {
                if (subFile.isDirectory()) {
                    if (subFile.getName().equalsIgnoreCase("cache")) {
                        return true;
                    }

                    if (containsCacheDirectory(subFile)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Method to determine if a file is a system extra file
    private boolean isSystemExtraFile(File file) {
        if(file.isDirectory()){
        String fileName = file.getName();
        return fileName.endsWith(".log") || fileName.endsWith(".tmp")  ;
        }
            return false;


    }
public List<String> getapp(){
    PackageManager packageManager = context.getPackageManager();
    appList = packageManager.getInstalledApplications(0);

    List<String> applist=new ArrayList<>();
    for (int i=0;i<=appList.size()-1;i++){
        ApplicationInfo appInfo = appList.get(i);
        String pkgname=appInfo.packageName;
        applist.add(pkgname);

    }
    return  applist;
}


    public List<File> getSystemExtraFiles() {
        return systemExtraFiles;
    }

    public List<File> getAppExtraFiles() {
        return appExtraFiles;
    }

    public List<File> getEmptyFolders() {
        return emptyFolders;
    }

    public List<File> getApkFiles() {
        return apkFiles;
    }
}
