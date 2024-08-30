package com.example.appupdater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ActivityManager;
import android.content.Context;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.example.appupdater.databinding.ActivityDeviceInfoBinding;
import com.example.appupdater.databinding.ActivityScanningAppBinding;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeviceInfoActivity extends AppCompatActivity {
ActivityDeviceInfoBinding binding;

    private InfoAdapter adapter;
    List<ModelClassForInfo> itemList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDeviceInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.deviceName.setText(getDeviceName());
        binding.brandName.setText(getDeviceBrand());
        binding.recycler.setLayoutManager(new LinearLayoutManager(DeviceInfoActivity.this));
        itemList.add(new ModelClassForInfo("Ram", getTotalRAM(DeviceInfoActivity.this), R.drawable.baseline_install_mobile_24));
        itemList.add(new ModelClassForInfo("Rom", getTotalInternalStorage(), R.drawable.baseline_install_mobile_24));
        itemList.add(new ModelClassForInfo("Battery", getBatteryCapacity(DeviceInfoActivity.this)+"%", R.drawable.baseline_install_mobile_24));
        itemList.add(new ModelClassForInfo("Processor", getProcessorName(), R.drawable.baseline_install_mobile_24));
        itemList.add(new ModelClassForInfo("Display", getDisplaySize(DeviceInfoActivity.this), R.drawable.baseline_install_mobile_24));
        itemList.add(new ModelClassForInfo("Display Dpi", getDisplayDpi(DeviceInfoActivity.this), R.drawable.baseline_install_mobile_24));

        adapter = new InfoAdapter(DeviceInfoActivity.this,itemList);
        binding.recycler.setAdapter(adapter);





    }
    public  String getDisplaySize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;
        String display = widthPixels+"/"+heightPixels;
       return display;
    }
    public  String getDisplayDpi(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        int densityDpi = displayMetrics.densityDpi;
        String display = String.valueOf(densityDpi);
        return display;
    }
    public  String getProcessorName() {
        String processorName = "";
        String filePath = "/proc/cpuinfo";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Hardware")) {
                    String[] parts = line.split(":\\s+", 2);
                    if (parts.length == 2) {
                        processorName = parts[1];
                        break;
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return processorName;
    }
    public  String getBatteryCapacity(Context context) {
        BatteryManager batteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        if (batteryManager != null) {
            String capacity = String.valueOf(batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY));
            return capacity;
        }
        return "-1"; // Indicating that the capacity could not be determined
    }

    public  String getTotalInternalStorage() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        long totalBytes = (long) statFs.getBlockSizeLong() * (long) statFs.getBlockCountLong();
        return convertFileSize(totalBytes);
    }
    public  String getTotalRAM(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return convertFileSize(memoryInfo.totalMem);
    }
    public  String convertFileSize(long sizeInBytes) {
        if (sizeInBytes <= 0) return "0 KB";

        final String[] units = new String[]{"KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(sizeInBytes) / Math.log10(1024)) - 1;

        if (digitGroups < 0) digitGroups = 0;

        return String.format("%.2f %s", sizeInBytes / Math.pow(1024, digitGroups + 1), units[digitGroups]);
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        if (model != null && model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }
    public  String getDeviceBrand() {
        return Build.BRAND;
    }
    private  String capitalize(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        char firstChar = str.charAt(0);
        if (Character.isUpperCase(firstChar)) {
            return str;
        } else {
            return Character.toUpperCase(firstChar) + str.substring(1);
        }
    }
}