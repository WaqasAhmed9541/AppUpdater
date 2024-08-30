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
        import androidx.cardview.widget.CardView;
        import androidx.constraintlayout.widget.ConstraintLayout;
        import androidx.core.content.FileProvider;
        import androidx.recyclerview.widget.RecyclerView;

        import java.io.File;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;
        import java.util.Locale;

public class UserSystemApkAdapter extends RecyclerView.Adapter<UserSystemApkAdapter.ApkUserViewHolder> {


    private List<ApplicationInfo> appList = new ArrayList<>();
    private Context context;


    public UserSystemApkAdapter(List<ApplicationInfo> appList,Context context) {
        this.appList = appList;
        this.context=context;
    }

    @NonNull
    @Override
    public ApkUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_system_apk_item, parent, false);
        return new ApkUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserSystemApkAdapter.ApkUserViewHolder holder, int position) {

        ApplicationInfo appInfo = appList.get(position);
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(appInfo.packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        long lastUpdateTime = packageInfo.lastUpdateTime;
        Date lastUpdateDate = new Date(lastUpdateTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ", Locale.getDefault());

        holder.appName.setText(appInfo.loadLabel(context.getPackageManager()).toString());
        holder.appIcon.setImageDrawable(appInfo.loadIcon(context.getPackageManager()));
        holder.version.setText("Version: "+packageInfo.versionName);
        holder.date.setText("Last Update: "+dateFormat.format(lastUpdateDate));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,DetailAppUpdateActivity.class);
                intent.putExtra("info",appInfo);
                context.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return appList.size();
    }

    public static class ApkUserViewHolder extends RecyclerView.ViewHolder {
        TextView appName,date,version;
        ImageView appIcon;
        CardView cardView;


        public ApkUserViewHolder(@NonNull View itemView) {
            super(itemView);
            appName = itemView.findViewById(R.id.name);
            appIcon = itemView.findViewById(R.id.img);
            date = itemView.findViewById(R.id.date);
            version = itemView.findViewById(R.id.version);
            cardView = itemView.findViewById(R.id.card_btn);




        }
    }


    private void uninstallApp(String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivity(intent);
    }




}
