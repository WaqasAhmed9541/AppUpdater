package com.example.appupdater;



        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.ApplicationInfo;
        import android.content.pm.PackageManager;
        import android.net.Uri;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.constraintlayout.widget.ConstraintLayout;
        import androidx.recyclerview.widget.RecyclerView;

        import java.util.ArrayList;
        import java.util.List;

public class DetailAppUpdateAdapter extends RecyclerView.Adapter<DetailAppUpdateAdapter.Detailholder> {
    private List<ModelClassForSensor> list;
    private Context context;
    private ApplicationInfo appinfo ;

    public DetailAppUpdateAdapter(Context context, List<ModelClassForSensor> list,ApplicationInfo appinfo ) {
        this.context = context;
        this.list = list;
        this.appinfo = appinfo;
    }

    @NonNull
    @Override
    public Detailholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_detail, parent, false);
        return new Detailholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Detailholder holder, int position) {
        ModelClassForSensor listitem = list.get(position);
        holder.textView.setText(listitem.getTitle());
        holder.textdesc.setText(listitem.getDescription());
        String type= String.valueOf(listitem.gettype());
//        holder.type.setText(type);
        holder.image.setImageDrawable(context.getDrawable(listitem.getIntresid()));

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position==0){
                    openplaystore();
                }
                if (position==1){
                    openapp();
                }
                if (position==2){
                    uninstall();
                }
            }
        });


    }





    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Detailholder extends RecyclerView.ViewHolder {
        public TextView textView,type;
        public TextView textdesc;
        public ImageView image;
        ConstraintLayout constraintLayout;




        public Detailholder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.title);
//            type = itemView.findViewById(R.id.type);
            textdesc = itemView.findViewById(R.id.desc);
            image=itemView.findViewById(R.id.icon);
            constraintLayout=itemView.findViewById(R.id.mainconstraint);

        }
    }

    public void openplaystore(){

        String packageName = appinfo.packageName;
        try {
            // Try to open the app in the Play Store app
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            // If Play Store app is not available, open in the browser
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
    public  void  openapp(){

        String packageName = appinfo.packageName;

        PackageManager packageManager = context.getPackageManager();
        Intent launchIntent = packageManager.getLaunchIntentForPackage(packageName);

        if (launchIntent != null) {
            // Launch the application
            context.startActivity(launchIntent);
        } else {
            // The app doesn't have a launchable activity, handle the error
            Log.e("LaunchApp", "No launchable activity found for package: " + packageName);
            Toast.makeText(context, "The app doesn't have a launchable activity", Toast.LENGTH_SHORT).show();
        }

    }
    public void uninstall(){

        String packageName = appinfo.packageName;

        Uri packageUri = Uri.parse("package:" + packageName);

        Intent intent = new Intent(Intent.ACTION_DELETE, packageUri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  // Launch in a new task
        context.startActivity(intent);

    }
}
