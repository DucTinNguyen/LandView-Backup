package com.example.landview.HomeFragmentSection.Slider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.landview.R;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewholder> {
    List<Slider> mlist;

    public SliderAdapter(List<Slider> mlist) {
        this.mlist = mlist;
    }

    public SliderViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_slider,parent,false);
        return new SliderViewholder(view);
    }

    @Override
    public void onBindViewHolder( SliderViewholder holder, int position) {
        Slider slider = mlist.get(position);
        if(slider==null)
        {
            return;
        }
        holder.imagePhoto.setImageResource(slider.getResID());
    }

    @Override
    public int getItemCount() {
        if(mlist!=null)
        {
            return mlist.size();
        }
        return 0;
    }

    public class SliderViewholder extends RecyclerView.ViewHolder{
        ImageView imagePhoto;
        public SliderViewholder( View itemView) {
            super(itemView);
            imagePhoto = itemView.findViewById(R.id.imgPhoto);
        }
    }
}
