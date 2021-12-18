package com.example.landview.Hotel;

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
import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHolder>{

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private Context context;
    private ArrayList<Hotel> hotels;
    private HotelClick listener;

    public HotelAdapter(Context context, ArrayList<Hotel> hotels) {
        this.context = context;
        this.hotels = hotels;
    }

    public void setHotelClick(HotelClick listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_hotel_item,parent,false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hotel hotel = hotels.get(position);
        holder.tvHotelName.setText(hotel.getName());
        Picasso.get()
                .load(hotel.getImages().get(0))
                .placeholder(R.drawable.destination)
                .fit()
                .into(holder.ivHotelImage);


        holder.ivHotelTym.setImageResource(R.drawable.tym);
        if(hotel.getLikesList().contains(mAuth.getUid())){
            holder.ivHotelTym.setImageResource(R.drawable.ic_red_tym_24);
        }
        if(hotel.getTotalRate() > 0){
            holder.ratingBar.setRating(hotel.getRating());
            holder.tvTotalRate.setText(String.valueOf(hotel.getTotalRate()));
        } else { // trường
            holder.ratingBar.setRating(5f);
            holder.tvTotalRate.setText("0");
        }

        if(hotel.getPrice() == 0 ) holder.tvHotelPrice.setText("1.000.000 đ");
        else {
            String price = String.valueOf(hotel.getPrice());
            holder.tvHotelPrice.setText(price +"VNĐ");
        }


    }

    @Override
    public int getItemCount() {
        return hotels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvHotelName;
        private ImageView ivHotelImage;
        private ImageView ivHotelTym;
        private RatingBar ratingBar;
        private TextView tvTotalRate;
        private TextView tvHotelPrice;
        private HotelClick listener;
        public ViewHolder(@NonNull View view, HotelClick listener) {
            super(view);
            tvHotelName = view.findViewById(R.id.tv_hotel_item_name);
            ivHotelImage = view.findViewById(R.id.iv_hotel_item_image);
            ivHotelTym = view.findViewById(R.id.iv_hotel_item_tym);
            ratingBar = view.findViewById(R.id.rb_hotel_item_rate);
            tvTotalRate = view.findViewById(R.id.tv_hotel_item_total_rate);
            tvHotelPrice = view.findViewById(R.id.tv_hotel_item_price);
            this.listener = listener;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.itemClick(getBindingAdapterPosition());
                }
            });

            ivHotelTym.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.likeClick(getBindingAdapterPosition());
                }
            });
        }
    }

    public interface HotelClick{
        void itemClick(int position);
        void likeClick(int position);
    }
}
