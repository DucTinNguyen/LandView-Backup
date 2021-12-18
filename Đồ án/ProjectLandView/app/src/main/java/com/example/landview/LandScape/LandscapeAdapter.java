package com.example.landview.LandScape;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.landview.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LandscapeAdapter extends RecyclerView.Adapter<LandscapeAdapter.ViewHolder> {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private Context context;
    private ArrayList<Landscape> landscapes;
    private LandscapeClick listener;

    public void setLandscapeClick(LandscapeClick listener){
        this.listener = listener;
    }

    public LandscapeAdapter(Context context, ArrayList<Landscape> landscapes) {
        this.context = context;
        this.landscapes = landscapes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_landscape_item, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Landscape landscape = landscapes.get(position);

        holder.tvName.setText(landscape.getName());

        // load ảnh thứ 1 trong danh sách ảnh
        Picasso.get()
                .load(landscape.getImages().get(0))
                .placeholder(R.drawable.destination)
                .fit()
                .into(holder.ivImage);

        // Cập nhật like hay unlike
        holder.ivTym.setImageResource(R.drawable.tym);
        if(landscape.getLikesList().contains(mAuth.getUid())) {
            holder.ivTym.setImageResource(R.drawable.ic_red_tym_24);
        }


        // Cập nhật rating và total rate
        if(landscape.getTotalRate() > 0){
            holder.ratingBar.setRating(landscape.getRating());
            holder.tvTotalRate.setText(String.valueOf(landscape.getTotalRate()));
        } else {
            holder.ratingBar.setRating(5f);
            holder.tvTotalRate.setText("0");
        }


    }

    @Override
    public int getItemCount() {
        return landscapes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        // R.layout.layout_landscape_item
        private ImageView ivImage;
        private TextView tvName;
        private ImageView ivTym;
        private RatingBar ratingBar;
        private TextView tvTotalRate;
        private LandscapeClick listener;
        public ViewHolder(@NonNull View view, LandscapeClick listener) {
            super(view);
            ivImage = view.findViewById(R.id.iv_landscape_item_image);
            tvName = view.findViewById(R.id.tv_landscape_item_name);
            ivTym = view.findViewById(R.id.iv_landscape_item_tym);
            ratingBar = view.findViewById(R.id.rb_landscape_item_rating);
            tvTotalRate = view.findViewById(R.id.tv_landscape_item_total_rate);
            this.listener = listener;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.itemClick(getBindingAdapterPosition());
                }
            });

            ivTym.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.likeClick(getBindingAdapterPosition());
                }
            });
        }
    }
    public interface LandscapeClick{
        void itemClick(int position);
        void likeClick(int position);
    }
}
