package com.example.appupdater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class SystemAppActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private UserSystemApkAdapter adapter;
    private List<ApplicationInfo> appList = new ArrayList<>();
    private List<ApplicationInfo> installedAppsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_app);

        recyclerView=findViewById(R.id.recycler_v);
        PackageManager packageManager = getPackageManager();
        appList = packageManager.getInstalledApplications(0);
        for (int i = 0; i < appList.size(); i++) {
            if ((appList.get(i).flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                installedAppsList.add(appList.get(i));
            }
        }
        adapter = new UserSystemApkAdapter(installedAppsList,SystemAppActivity.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(SystemAppActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}