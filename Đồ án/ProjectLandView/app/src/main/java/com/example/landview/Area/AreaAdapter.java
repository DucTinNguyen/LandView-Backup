package com.example.landview.Area;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.landview.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.ViewHolder> {
    private static final String TAG = "FragmentAreaAdapter";
    private ArrayList<Area> areas;
    private Context context;
    private AreaClick listener;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void setAreaClick(AreaClick listener){
        this.listener = listener;
    }
    public AreaAdapter(Context context, ArrayList<Area> areas) {
        this.context = context;
        this.areas = areas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.layout_item_topreview, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Area area = areas.get(position);
        Picasso.get().load(area.getImages().get(0)).placeholder(R.drawable.destination).fit().into(holder.areaIv);
        holder.areaText.setText(area.getAreaName());
        holder.areaLike.setImageResource(R.drawable.tym);
        if(area.getLikes().contains(mAuth.getUid())){
            holder.areaLike.setImageResource(R.drawable.ic_red_tym_24);
        }
    }

    @Override
    public int getItemCount() {
        return areas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView areaIv;
        private TextView areaText;
        private ImageView areaLike;
        private AreaClick listener;
        public ViewHolder(@NonNull View itemView, AreaClick listener) {
            super(itemView);
            areaIv = itemView.findViewById(R.id.imgTop);
            areaText = itemView.findViewById(R.id.textTop);
            areaLike = itemView.findViewById(R.id.iconToptym);
            this.listener = listener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.itemClick(getBindingAdapterPosition());
                }
            });

            areaLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.likeClick(getBindingAdapterPosition());
                }
            });
        }
    }

    public interface AreaClick {
        void itemClick(int position);
        void likeClick(int position);
    }
}
