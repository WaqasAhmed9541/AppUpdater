package com.example.appupdater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.appupdater.databinding.ActivityDetailAppUpdateBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailAppUpdateActivity extends AppCompatActivity {
ActivityDetailAppUpdateBinding binding;
    private DetailAppUpdateAdapter adapter;
    ApplicationInfo appInfo;
List<ModelClassForSensor> data=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDetailAppUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        appInfo = intent.getParcelableExtra("info");
        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(appInfo.packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        long lastUpdateTime = packageInfo.lastUpdateTime;
        Date lastUpdateDate = new Date(lastUpdateTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ", Locale.getDefault());
        binding.recyclerV.setLayoutManager(new LinearLayoutManager(DetailAppUpdateActivity.this));

        binding.name.setText(appInfo.loadLabel(getPackageManager()).toString());
        binding.img.setImageDrawable(appInfo.loadIcon(getPackageManager()));
        binding.version.setText("Version: "+packageInfo.versionName);
        binding.date.setText("Last Update: "+dateFormat.format(lastUpdateDate));

        data.add(new ModelClassForSensor("Open App","open app in playstore",R.drawable.baseline_install_mobile_24));
        data.add(new ModelClassForSensor("Launch App","open app on device",R.drawable.baseline_install_mobile_24));
        data.add(new ModelClassForSensor("Uninstall App","Remove App From Device ",R.drawable.baseline_install_mobile_24));

        adapter=new DetailAppUpdateAdapter(DetailAppUpdateActivity.this,data,appInfo);
        binding.recyclerV.setAdapter(adapter);

        binding.updateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openplaystore();
            }
        });

    }

public void openplaystore(){

    String packageName = appInfo.packageName;
    try {
        // Try to open the app in the Play Store app
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    } catch (android.content.ActivityNotFoundException e) {
        // If Play Store app is not available, open in the browser
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
}