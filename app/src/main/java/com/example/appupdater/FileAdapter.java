package com.example.appupdater;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
    private List<File> fileList;
    private Context context;

    public FileAdapter(List<File> fileList, Context context) {
        this.fileList = fileList;
        this.context = context;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout for each file
        View view = LayoutInflater.from(context).inflate(R.layout.item_file, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        // Bind the file data to the views
        File file = fileList.get(position);
        holder.fileName.setText(file.getName());
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFileFromExternalStorage(file,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public class FileViewHolder extends RecyclerView.ViewHolder {
        TextView fileName;
        ImageView del;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the TextViews from your item layout
            fileName = itemView.findViewById(R.id.fileName);
            del = itemView.findViewById(R.id.delete);

        }
    }

    private void deleteFileFromExternalStorage(File file, int position) {
        if (file.exists()) {
            boolean deleted = file.delete();

            if (deleted) {
                fileList.remove(position);
                notifyItemRemoved(position);
            } else {
                Toast.makeText(context, "File could not be deleted", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show();
        }
    }
}
