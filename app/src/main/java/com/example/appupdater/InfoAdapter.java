package com.example.appupdater;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.InfoHolder> {
    private List<ModelClassForInfo> list;
    private Context context;

    public InfoAdapter(Context context, List<ModelClassForInfo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_info, parent, false);
        return new InfoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoHolder holder, int position) {
        ModelClassForInfo listitem = list.get(position);
        holder.textView.setText(list.get(position).getTitle());
        holder.textdesc.setText(list.get(position).getDescription());
        holder.image.setImageDrawable(context.getDrawable(list.get(position).getIconResId()));


    }





    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class InfoHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextView textdesc;
        public ImageView image;


        public InfoHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.title);
            textdesc = itemView.findViewById(R.id.desc);
            image=itemView.findViewById(R.id.icon);
        }
    }
}
