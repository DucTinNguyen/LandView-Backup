package com.example.landview.HomeFragmentSection.DetailReview.ImageFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.landview.R;

import java.util.ArrayList;


public class Image extends Fragment {

    private GridView gridView;
    ItemImageAdapter adapter;
    ArrayList<ItemImage>images;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image,container,false);
        //ánh xạ view
        gridView = view.findViewById(R.id.gridviewImage);
        //
        adapter = new ItemImageAdapter(this.getContext(),R.layout.layout_review_image_hoguom);
        adapter.add(new ItemImage(R.drawable.hoguom1));
        adapter.add(new ItemImage(R.drawable.hoguom2));
        adapter.add(new ItemImage(R.drawable.hoguom3));
        adapter.add(new ItemImage(R.drawable.hoguom4));
        adapter.add(new ItemImage(R.drawable.hoguom5));
        adapter.add(new ItemImage(R.drawable.hoguom6));
        gridView.setAdapter(adapter);
        return view;
    }
}