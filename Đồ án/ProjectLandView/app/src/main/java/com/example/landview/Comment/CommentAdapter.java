package com.example.landview.Comment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.landview.ExpandableTextView.ExpandableTextView;
import com.example.landview.R;
import com.google.firebase.Timestamp;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<Comment> comments;

    public CommentAdapter(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.tvName.setText(comment.getUsername());
        // holder.tvComment.setText(comment.getComment());
        holder.expandTvComment.setText(comment.getComment());
        Picasso.get().load(comment.getAvatar()).fit().into(holder.ivAvatar);

        if(comment.getDate() ==null ){
        } else {
            String date = convertTimeStamp(comment.getDate());
            holder.tvTime.setText(date);
        }
        holder.ratingBar.setRating(comment.getRating());


    }

    private String convertTimeStamp(Timestamp time){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(time.toDate());
        return date;
    }

    @Override
    public int getItemCount() {
        if(comments.size() < 3)  return comments.size();
        else return 3;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivAvatar;
        private TextView tvName;
        private TextView tvTime;
        private RatingBar ratingBar;
        // private TextView tvComment;
        private ExpandableTextView expandTvComment;

        public ViewHolder(@NonNull View view) {
            super(view);

            ivAvatar = view.findViewById(R.id.iv_avatar);
            tvName = view.findViewById(R.id.tv_user_name);
            tvTime = view.findViewById(R.id.tv_timestamp);
            ratingBar = view.findViewById(R.id.rb_cmt_item);
            // tvComment = view.findViewById(R.id.tv_comment);
            expandTvComment = view.findViewById(R.id.tv_comment);
        }
    }
}