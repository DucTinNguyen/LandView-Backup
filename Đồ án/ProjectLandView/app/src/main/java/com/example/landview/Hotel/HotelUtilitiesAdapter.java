package com.example.landview.Hotel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.landview.R;

import java.util.List;

public class HotelUtilitiesAdapter extends RecyclerView.Adapter<HotelUtilitiesAdapter.ViewHolder> {

    Context mContext;
    List<String> list;

    public HotelUtilitiesAdapter(Context context, List<String> list){
        this.mContext = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_hotel_utilities_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.utilNameTV.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView utilNameTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            utilNameTV = itemView.findViewById(R.id.utilNameTV);
        }
    }
}
