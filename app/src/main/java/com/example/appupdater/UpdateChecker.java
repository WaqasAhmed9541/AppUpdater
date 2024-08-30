//package com.example.appupdater;
//
//
//import android.content.Context;
//import android.content.pm.ApplicationInfo;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.select.Elements;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class UpdateChecker {
//
//    ApplicationInfo appInfo;
//    Context context;
//    public UpdateChecker( ApplicationInfo appInfo, Context context) {
//        this.appInfo = appInfo;
//        this.context = context;
//    }
//
//    private static final String PLAY_STORE_BASE_URL = "https://play.google.com/store/apps/details?id=";
//
//    public static boolean isUpdateAvailable(String packageName) throws Exception {
//        String url = PLAY_STORE_BASE_URL + packageName;
//        Document document = Jsoup.connect(url).get();
//
//        Elements versionElements = document.select("div[itemprop=softwareVersion]");
//
//        if (versionElements.isEmpty()) {
//            throw new Exception("Could not find version information on Play Store page.");
//        }
//
//        String currentVersion = ;
//        String playStoreVersion = versionElements.first().text().trim();
//
//        return isNewerVersion(currentVersion, playStoreVersion);
//    }
//
//    private static boolean isNewerVersion(String currentVersion, String playStoreVersion) {
//        // Implement logic to compare version strings (consider versioning scheme)
//        // This example assumes simple version format (e.g., X.Y.Z)
//        String[] currentParts = currentVersion.split("\\.");
//        String[] playStoreParts = playStoreVersion.split("\\.");
//
//        for (int i = 0; i < Math.min(currentParts.length, playStoreParts.length); i++) {
//            int current = Integer.parseInt(currentParts[i]);
//            int playStore = Integer.parseInt(playStoreParts[i]);
//            if (playStore > current) {
//                return true;
//            } else if (playStore < current) {
//                return false;
//            }
//        }
//
//        // If versions are identical up to the compared parts, consider playStore as newer
//        return playStoreParts.length > currentParts.length;
//    }
//}