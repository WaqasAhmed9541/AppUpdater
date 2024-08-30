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

public class TestMobileAdapter extends RecyclerView.Adapter<TestMobileAdapter.TestHolder> {
    private List<ModelClassForInfo> list;
    private Context context;

    public TestMobileAdapter(Context context, List<ModelClassForInfo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_mobile_item, parent, false);
        return new TestHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TestHolder holder, int position) {
        ModelClassForInfo listitem = list.get(position);
        holder.textView.setText(listitem.getTitle());
        holder.image.setImageDrawable(context.getDrawable(listitem.getIconResId()));
        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,TestingActivity.class);
                intent.putExtra("testType",listitem.getTitle());
                context.startActivity(intent);
            }
        });


    }





    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TestHolder extends RecyclerView.ViewHolder {
        public TextView textView,type;

        public ImageView image;
        ConstraintLayout main;




        public TestHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.title);
//

            image=itemView.findViewById(R.id.icon);
            main=itemView.findViewById(R.id.main);

        }
    }
}
