package com.example.appupdater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class MobileUsageActivity extends AppCompatActivity {
    private  final int REQUEST_USAGE_ACCESS = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_usage);

        if (!hasUsageStatsPermission(MobileUsageActivity.this)) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivityForResult(intent, REQUEST_USAGE_ACCESS);
        }else {

            getusage();
        }





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_USAGE_ACCESS) {
            // Check if the user has granted the usage stats permission
            if (hasUsageStatsPermission(this)) {
              getusage();

            } else {
                Toast.makeText(MobileUsageActivity.this, "Access Deny", Toast.LENGTH_SHORT).show();

            }
        }
    }


    public boolean hasUsageStatsPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = AppOpsManager.MODE_DEFAULT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mode = appOps.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.getPackageName());
        }
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    public void getusage() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        long startTime = calendar.getTimeInMillis();
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
        long totalUsageTime = 0;
        for (UsageStats usageStats : usageStatsList) {
            totalUsageTime += usageStats.getTotalTimeInForeground();
        }
        long totalTimeInDay = 24 * 60 * 60 * 1000;
        double usagePercentage = ((double) totalUsageTime / totalTimeInDay) * 100;
        int progress = (int) Math.min(usagePercentage, 100);

        ProgressBar circularProgressBar = findViewById(R.id.circularProgressBar);
        circularProgressBar.setMax(100);
        circularProgressBar.setProgress(0);  // Ensure it starts at 0

        TextView progressPercentageTextView = findViewById(R.id.time);

        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(circularProgressBar, "progress", 0, progress);
        progressAnimator.setDuration(1000);  // 1 second animation
        progressAnimator.setInterpolator(new DecelerateInterpolator());
        progressAnimator.start();

        progressPercentageTextView.setText(String.format("%.2f%%", usagePercentage));
    }

}