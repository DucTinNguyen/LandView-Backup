package com.example.landview.chung;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.landview.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.ViewHolder> {

    ArrayList<String> images;

    public SliderAdapter(ArrayList<String> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_slider, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageUrl = images.get(position);
        Picasso.get().load(imageUrl).into(holder.placeImage);
    }

    @Override
    public int getItemCount() {
        if(images == null) return 0;
        return images.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView placeImage;
        public ViewHolder(@NonNull View view) {
            super(view);
            this.placeImage = view.findViewById(R.id.imgPhoto); // layout_item_slider
        }
    }
}