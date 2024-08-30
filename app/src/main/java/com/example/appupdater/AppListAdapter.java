package com.example.appupdater;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.AppViewHolder> {

    private List<ApplicationInfo> appList=new ArrayList<>();
    private List<ApplicationInfo> updateAvailibleapps = new ArrayList<>();
    Context context;

    public AppListAdapter(List<ApplicationInfo> appList, Context context) {
        this.appList = appList;
        this.context = context;
    }

    public void addApp() {

        notifyItemInserted(appList.size() - 1); // Notify that an item was inserted at the last position
    }


    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.app_list_item, parent, false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        ApplicationInfo appInfo = appList.get(position);
        holder.appName.setText(appInfo.loadLabel(context.getPackageManager()).toString());
        holder.appIcon.setImageDrawable(appInfo.loadIcon(context.getPackageManager()));







    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public static class AppViewHolder extends RecyclerView.ViewHolder {
        TextView appName;
        ImageView appIcon;


        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            appName = itemView.findViewById(R.id.title);

            appIcon = itemView.findViewById(R.id.icon);

        }
    }










}
