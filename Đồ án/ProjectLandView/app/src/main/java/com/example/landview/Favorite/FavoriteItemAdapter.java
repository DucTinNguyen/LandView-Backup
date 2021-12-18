package com.example.landview.Favorite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.landview.FavoriteFragment;
import com.example.landview.Place.Place;
import com.example.landview.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoriteItemAdapter extends RecyclerView.Adapter<FavoriteItemAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Place> places;
    private FavoriteItemClick listener;
    private int whichActivity;

    public FavoriteItemAdapter(Context context, ArrayList<Place> places, int whichActivity) {
        this.context = context;
        this.places = places;
        this.whichActivity = whichActivity;
    }

    public void setFavoriteItemCLick(FavoriteItemClick listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_favorite_item, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Place place = places.get(position);
        switch (place.getType()){
            case "area":
                holder.ratingBar.setVisibility(View.GONE);
                holder.tvTotalRate.setVisibility(View.GONE);
                holder.tvName.setText(place.getAreaName());
                break;
            default:
                holder.ratingBar.setRating(place.getRating());
                holder.tvTotalRate.setText(String.valueOf(place.getTotalRate()));
                holder.tvName.setText(place.getName());
        }


        Picasso.get()
                .load(place.getImages().get(0))
                .fit()
                .into(holder.ivImage);

        //Kiểm tra xem đang ở search hay favorite
        // define cho nó, nếu là 1 thì là đang ở favorite fragment
        //nếu bằng 2 thì tức là đang ở search activity
        if(whichActivity == 2){ //Activity search sẽ ko cần tym nên sẽ cho nó GONE
            holder.ivTym.setVisibility(View.GONE);
        }else {
        }
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivImage;
        private TextView tvName;
        private RatingBar ratingBar;
        private TextView tvTotalRate;
        private ImageView ivTym;
        private FavoriteItemClick listener;

        public ViewHolder(@NonNull View view, FavoriteItemClick listener) {
            super(view);
            this.ivImage = view.findViewById(R.id.iv_fav_item_image);
            this.tvName = view.findViewById(R.id.tv_fav_item_name);
            this.ratingBar = view.findViewById(R.id.rb_fav_item);
            this.tvTotalRate = view.findViewById(R.id.tv_fav_total_rate);

            this.ivTym = view.findViewById(R.id.iv_fav_item_tym);
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
                    listener.unlikeClick(getBindingAdapterPosition());
                }
            });
        }
    }

    public interface FavoriteItemClick{
        void itemClick(int position);
        void unlikeClick(int position);
    }
}
