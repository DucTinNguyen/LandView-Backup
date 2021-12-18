package com.example.landview.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.landview.Place.Place;
import com.example.landview.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MapItemAdapter  extends RecyclerView.Adapter<MapItemAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Place> places;
    private MapItemClick listener;

    public MapItemAdapter(Context context, ArrayList<Place> places) {
        this.context = context;
        this.places = places;
    }

    public void setMapItemClick(MapItemClick listener){
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_map_item, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Place place = places.get(position);
        holder.tvName.setText(place.getName());
        holder.tvAddress.setText(place.getAddress());
        Picasso.get()
                .load(place.getImages().get(0))
                .fit()
                .into(holder.ivImage);
        holder.ratingBar.setRating(place.getRating());
        holder.tvTotalRate.setText(String.valueOf(place.getTotalRate()));



    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivImage;
        private TextView tvName;
        private TextView tvAddress;
        private RatingBar ratingBar;
        private TextView tvTotalRate;
        private MapItemClick listener;
        public ViewHolder(@NonNull View view, MapItemClick listener) {
            super(view);
            ivImage = view.findViewById(R.id.iv_map_item_image);
            tvName = view.findViewById(R.id.tv_map_item_name);
            tvAddress = view.findViewById(R.id.tv_map_item_address);
            ratingBar = view.findViewById(R.id.rb_map_item);
            tvTotalRate = view.findViewById(R.id.tv_map_item_total_rate);


            this.listener = listener;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.ItemClick(getBindingAdapterPosition());
                }
            });

        }

    }

    public interface MapItemClick{
        void ItemClick(int position);

    }
}
