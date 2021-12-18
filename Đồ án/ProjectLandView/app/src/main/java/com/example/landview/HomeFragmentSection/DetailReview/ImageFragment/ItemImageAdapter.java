package com.example.landview.HomeFragmentSection.DetailReview.ImageFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.landview.R;

public class ItemImageAdapter extends ArrayAdapter<ItemImage> {


    public ItemImageAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_review_image_hoguom,parent,false);
        ImageView imageView = convertView.findViewById(R.id.itemImage);
        ItemImage itemImage = getItem(position);
        imageView.setImageResource(itemImage.getImage());
        return convertView;
    }
}
