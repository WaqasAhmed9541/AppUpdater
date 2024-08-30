package com.example.appupdater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appupdater.databinding.ActivityScanningAppBinding;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ScanningAppActivity extends AppCompatActivity {
    ActivityScanningAppBinding binding;
    String currentVersion = "";

    String temp="";
    String packageName = "";
    private ProgressBar progressBar;
    private int remainingTasks;
    PackageManager packageManager;
    private static final String PLAY_STORE_BASE_URL = "https://play.google.com/store/apps/details?id=";
    private AppListAdapter adapter;
    private List<ApplicationInfo> appList = new ArrayList<>();
    private List<ApplicationInfo> installedAppsList = new ArrayList<>();
    private List<ApplicationInfo> updateAvailibleapps = new ArrayList<>();
    boolean isTaskRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanningAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        packageManager = getPackageManager();
        appList = packageManager.getInstalledApplications(0);
        for (int i = 0; i < appList.size(); i++) {
            if ((appList.get(i).flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                installedAppsList.add(appList.get(i));

            }
        }
        if (isInternetConnected(ScanningAppActivity.this)) {
            progressBar = binding.progressBar;
            progressBar.setVisibility(View.VISIBLE);
            remainingTasks = installedAppsList.size();
            for (int i = 0; i < installedAppsList.size(); i++) {
                ApplicationInfo appInfo = installedAppsList.get(i);
                packageName = appInfo.packageName;
                MyAsyncTask myAsyncTask = new MyAsyncTask(packageName, ScanningAppActivity.this, appInfo);
                isTaskRunning = true;
                myAsyncTask.execute();
                if (i == installedAppsList.size() - 1) {
                    isTaskRunning = false;
                }


            }
            adapter = new AppListAdapter(updateAvailibleapps, ScanningAppActivity.this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
            binding.recycler.setLayoutManager(gridLayoutManager);
            binding.recycler.setAdapter(adapter);

        }else{

            View dialogView = getLayoutInflater().inflate(R.layout.internet_dialog, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(ScanningAppActivity.this, R.style.CustomDialogTheme);
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();
            Button btn=dialogView.findViewById(R.id.btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =new Intent(ScanningAppActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });


        }

    }



    public  String getPlayStoreVersion(String packageName) throws IOException {
//        try {
//            Connection connection = Jsoup.connect("https://play.google.com/store/apps/details?id=" + packageName + "&hl=en").timeout(30000);
//            connection.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6");
//            connection.referrer("http://www.google.com");
//            Document document = connection.get();
//            if (document != null) { Elements elements = document.getElementsContainingOwnText("Version");
//                for (Element element : elements) {
//                    Elements siblingElements = element.siblingElements();
//                    if (siblingElements != null) {
//                        for (Element siblingElement : siblingElements) {
//                            PackageInfo packageInfo = null;
//                            try {
//                                packageInfo = packageManager.getPackageInfo(packageName, 0);
//                            } catch (PackageManager.NameNotFoundException e) {
//                                throw new RuntimeException(e);
//                            }
//                            currentVersion = packageInfo.versionName;
//                            currentVersion = siblingElement.text();
//                            return currentVersion;
//                        }
//                    }
//                }
//
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return "null";

//        String newVersion = null;
//        try {
//            newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" +packageName )
//                    .timeout(30000)
//                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//                    .referrer("http://www.google.com")
//                    .get()
//                    .select(".q078ud .reAt0")
//                    .get(7)
//                    .ownText();
//            return newVersion;

        try{
        String url = PLAY_STORE_BASE_URL + packageName+ "&hl=en";
        Document doc = Jsoup.connect(url).get();
        // Select the elements containing the version information
        Elements versionElements = doc.select("div.sMUprd div.q078ud div.reAt0");
        for (Element element : versionElements) {
            if (element.text().equals("Version")) {
                Element versionInfo = element.nextElementSibling();
                return versionInfo.text();
            }
        }
        } catch (Exception e) {
            return "null";
        }
        return "null";
    }


        public boolean isUpdateAvailable(String packageName) throws Exception {
        String url = PLAY_STORE_BASE_URL + packageName;
        Document document = Jsoup.connect(url).get();

        Elements versionElements = document.select("div[itemprop=sMUprd]");

//        Elements versionElements = document.getElementsContainingOwnText("Version");

        if (versionElements.isEmpty()) {
            throw new Exception("Could not find version information on Play Store page.");
        }
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        currentVersion = packageInfo.versionName;
        String playStoreVersion = versionElements.first().text().trim();

        return isNewerVersion(currentVersion, playStoreVersion);
    }

    private boolean isNewerVersion(String currentVersion, String playStoreVersion) {
        // Implement logic to compare version strings (consider versioning scheme)
        // This example assumes simple version format (e.g., X.Y.Z)
        String[] currentParts = currentVersion.split("\\.");
        String[] playStoreParts = playStoreVersion.split("\\.");

        for (int i = 0; i < Math.min(currentParts.length, playStoreParts.length); i++) {
            int current = Integer.parseInt(currentParts[i]);
            int playStore = Integer.parseInt(playStoreParts[i]);
            if (playStore > current) {
                return true;
            } else if (playStore < current) {
                return false;
            }
        }

        // If versions are identical up to the compared parts, consider playStore as newer
        return playStoreParts.length > currentParts.length;
    }

    public static boolean isAppAvailable(String packageName) {
        String url = PLAY_STORE_BASE_URL + packageName;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public class MyAsyncTask extends AsyncTask<Void, Void, Boolean> {

        Context context;
        private String packageName;
        ApplicationInfo appInfo;

        public MyAsyncTask(String packageName, Context context, ApplicationInfo appInfo) {
            this.packageName = packageName;
            this.context = context;
            this.appInfo = appInfo;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.imageIcon.setImageDrawable(appInfo.loadIcon(context.getPackageManager()));
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                temp=getPlayStoreVersion(packageName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if(isAppAvailable(packageName)) {

                try {
                    return isUpdateAvailable(packageName);
                } catch (Exception e) {
                    e.printStackTrace(); // Log the exception for debugging
                    return false; // Return false in case of error
                }

            }else {return false;}
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            binding.imageIcon.setImageDrawable(appInfo.loadIcon(context.getPackageManager()));
            Toast.makeText(ScanningAppActivity.this, currentVersion, Toast.LENGTH_SHORT).show();
            synchronized (ScanningAppActivity.this) {
                remainingTasks--; // Decrease the counter as the task is done


                if (remainingTasks == 0) {
                    progressBar.setVisibility(View.GONE); // Hide the ProgressBar when all tasks are done
                }

//                updateAvailibleapps.add(appInfo);
//                adapter.addApp();
                if (result) {
//                    updateAvailibleapps.add(appInfo);
//                    adapter.addApp();
                } else {

                }
            }
        }

    }

    @Override
    public void onBackPressed() {


        if (isTaskRunning) {

        } else {
            Intent intent = new Intent(ScanningAppActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}



