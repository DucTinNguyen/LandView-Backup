package com.example.landview.Restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.landview.Hotel.Hotel;
import com.example.landview.Hotel.HotelAdapter;
import com.example.landview.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ResAdapter extends RecyclerView.Adapter<ResAdapter.ViewHolder> {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private Context context;
    private ArrayList<Restaurant> restaurants;
    private RestaurantClick listener;

    public void setRestaurantClick(RestaurantClick listener){
        this.listener = listener;
    }

    public ResAdapter(Context context, ArrayList<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_restaurant_item,parent,false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        holder.tvName.setText(restaurant.getName());
        Picasso.get()
                .load(restaurant.getImages().get(0))
                .placeholder(R.drawable.destination)
                .fit()
                .into(holder.ivImage);

        holder.ivTym.setImageResource(R.drawable.tym);
        if(restaurant.getLikesList().contains(mAuth.getUid())){
            holder.ivTym.setImageResource(R.drawable.ic_red_tym_24);
        }

        if(restaurant.getTotalRate() > 0) {
            // set Rating
            holder.ratingBar.setRating(restaurant.getRating());
            // set total Rate
            holder.tvTotalRate.setText(String.valueOf(restaurant.getTotalRate()));
        } else {
            holder.ratingBar.setRating(5f);
            holder.tvTotalRate.setText("0");
        }

    }

    @Override
    public int getItemCount() {
       return restaurants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivImage;
        private TextView tvName;
        private ImageView ivTym;
        private RatingBar ratingBar;
        private TextView tvTotalRate;
        private RestaurantClick listener;
        public ViewHolder(@NonNull View view, RestaurantClick listener) {
            super(view);
            ivImage = view.findViewById(R.id.iv_res_item_image);
            tvName = view.findViewById(R.id.tv_res_item_name);
            ivTym = view.findViewById(R.id.iv_res_item_tym);
            ratingBar = view.findViewById(R.id.rb_res_item_rate);
            tvTotalRate = view.findViewById(R.id.tv_res_item_total_rate);
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

    public interface RestaurantClick{
        void itemClick(int position);
        void likeClick(int position);
    }
}
