package com.example.appupdater;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ApkAdapter extends RecyclerView.Adapter<ApkAdapter.ApkViewHolder> {


    private List<ApplicationInfo> appList = new ArrayList<>();
    private Context context;


    public ApkAdapter(List<ApplicationInfo> appList,Context context) {
        this.appList = appList;
        this.context=context;
    }

    @NonNull
    @Override
    public ApkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.apk_item, parent, false);
        return new ApkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApkAdapter.ApkViewHolder holder, int position) {

        ApplicationInfo appInfo = appList.get(position);
        holder.appName.setText(appInfo.loadLabel(context.getPackageManager()).toString());
        holder.appIcon.setImageDrawable(appInfo.loadIcon(context.getPackageManager()));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uninstallApp(appInfo.packageName);
            }
        });

    }
    @Override
    public int getItemCount() {
        return appList.size();
    }

    public static class ApkViewHolder extends RecyclerView.ViewHolder {
        TextView appName;
        ImageView appIcon,delete;


        public ApkViewHolder(@NonNull View itemView) {
            super(itemView);
            appName = itemView.findViewById(R.id.title);
            appIcon = itemView.findViewById(R.id.icon);
            delete = itemView.findViewById(R.id.icon1);

        }
    }


    private void uninstallApp(String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivity(intent);
    }




}
