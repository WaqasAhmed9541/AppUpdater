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

public class SensorInfoAdapter extends RecyclerView.Adapter<SensorInfoAdapter.Sensorholder> {
    private List<ModelClassForSensor> list;
    private Context context;

    public SensorInfoAdapter(Context context, List<ModelClassForSensor> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Sensorholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_sensro, parent, false);
        return new Sensorholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Sensorholder holder, int position) {
        ModelClassForSensor listitem = list.get(position);
        holder.textView.setText(listitem.getTitle());
        holder.textdesc.setText(listitem.getDescription());
        String type= String.valueOf(listitem.gettype());
//        holder.type.setText(type);
        holder.image.setImageDrawable(context.getDrawable(listitem.getIntresid()));


    }





    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Sensorholder extends RecyclerView.ViewHolder {
        public TextView textView,type;
        public TextView textdesc;
        public ImageView image;




        public Sensorholder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.title);
//            type = itemView.findViewById(R.id.type);
            textdesc = itemView.findViewById(R.id.desc);
            image=itemView.findViewById(R.id.icon);

        }
    }
}
