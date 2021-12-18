package com.example.landview.HomeFragmentSection.Reviews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.landview.HomeFragmentSection.DetailReview.DetailHoguom;
import com.example.landview.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemReviewAdapter extends RecyclerView.Adapter<ItemReviewAdapter.ReviewViewholder>{
    private Context mcontext;
    private List<ItemReview> mlist;
    private ItemReviewClick listener;

    public void setItemReviewClick(ItemReviewClick listener){this.listener=listener;}

    public ItemReviewAdapter(Context mcontext, List<ItemReview> mlist) {
        this.mcontext = mcontext;
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public ReviewViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_review_item,parent,false);
        return new ReviewViewholder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewholder holder, int position) {
        final ItemReview itemReview = mlist.get(position);
        if(itemReview==null)
        {
            return;
        }
//        holder.imgName.setImageResource(itemReview.getImg());
        Picasso.get().load(itemReview.getImg()).placeholder(R.drawable.destination).into(holder.imgName);
        holder.name.setText(itemReview.getName());
        holder.imgRate.setImageResource(itemReview.getRateImg());
        holder.nameAddress.setText(itemReview.getAddress());
        //sự kiện chuyển sang màn hình điện thoại
//        holder.relativeLayout.setOnClickListener(view -> onGotoDetail(itemReview));
    }

//    private void onGotoDetail(ItemReview itemReview) {
//        Intent intent = new Intent(mcontext,DetailHoguom.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("object_ItemReview",itemReview);
//        intent.putExtras(bundle);
//        mcontext.startActivity(intent);
//    }

    @Override
    public int getItemCount() {
        if(mlist!=null)
        {
            return mlist.size();
        }
        return 0;
    }

    public void updateItem(List<ItemReview> itemReviewArrayList){
        mlist.clear();
        mlist.addAll(itemReviewArrayList);
        notifyDataSetChanged();
    }

    public void removeAllItem(){
        mlist.removeAll(mlist);
        notifyDataSetChanged();
    }

    public void replaceNewList(List<ItemReview> itemReviewArrayList){
        // clear old list
        mlist.clear();
        mlist.addAll(itemReviewArrayList);
        // notify adapter
        notifyDataSetChanged();
    }

    public static class ReviewViewholder extends RecyclerView.ViewHolder{
        private ImageView imgName,imgRate;
        private TextView name,nameAddress;
        private RelativeLayout relativeLayout;
        private ItemReviewClick listener;
        public ReviewViewholder(@NonNull View itemView, ItemReviewClick listener) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            imgName = itemView.findViewById(R.id.imgItemReview);
            name = itemView.findViewById(R.id.txtName);
            imgRate = itemView.findViewById(R.id.imgrate);
            nameAddress = itemView.findViewById(R.id.txtAddress);
            this.listener = listener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.itemClick(getBindingAdapterPosition());
                }
            });
        }
    }

    public interface ItemReviewClick {
        void itemClick(int position);
    }
}
