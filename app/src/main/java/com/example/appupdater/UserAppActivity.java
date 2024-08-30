package com.example.appupdater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.appupdater.databinding.ActivityUserAppBinding;

import java.util.ArrayList;
import java.util.List;

public class UserAppActivity extends AppCompatActivity {
ActivityUserAppBinding binding;
RecyclerView recyclerView;
    private UserSystemApkAdapter adapter;
    private List<ApplicationInfo> appList = new ArrayList<>();
    private List<ApplicationInfo> installedAppsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        recyclerView=findViewById(R.id.recyclerview);
        PackageManager packageManager = getPackageManager();
        appList = packageManager.getInstalledApplications(0);
        for (int i = 0; i < appList.size(); i++) {
            if ((appList.get(i).flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                installedAppsList.add(appList.get(i));
            }
        }
        adapter = new UserSystemApkAdapter(installedAppsList,UserAppActivity.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(UserAppActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);


    }
}