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

public class ScanExtraFileAdapter extends RecyclerView.Adapter<ScanExtraFileAdapter.ScanViewHolder> {

    public  OnItemClickListener listener;
    private List<ModelClassForextraFile> fileList = new ArrayList<>();
    private Context context;


    public ScanExtraFileAdapter(List<ModelClassForextraFile> fileList,Context context,OnItemClickListener listener) {
        this.fileList = fileList;
        this.context=context;
        this.listener = listener;

    }
    public void updateList(List<ModelClassForextraFile> fileList) {
        this.fileList = fileList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.extrafile_item, parent, false);
        return new ScanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanExtraFileAdapter.ScanViewHolder holder, int position) {
                ModelClassForextraFile data=fileList.get(position);
                holder.name.setText(data.getTitle());
                holder.appIcon.setImageDrawable(context.getDrawable(data.getIntresid()));
                holder.counter.setText(String.valueOf(data.getFilelist().size()));
                holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onItemClick(position);
                        }
                    }
                });

    }
    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public static class ScanViewHolder extends RecyclerView.ViewHolder {
        TextView name,counter;
        ImageView appIcon;
        ConstraintLayout constraintLayout;


        public ScanViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.title);
            appIcon = itemView.findViewById(R.id.icon);
            counter = itemView.findViewById(R.id.counter);
            constraintLayout = itemView.findViewById(R.id.main_cons);
        }
    }







}
