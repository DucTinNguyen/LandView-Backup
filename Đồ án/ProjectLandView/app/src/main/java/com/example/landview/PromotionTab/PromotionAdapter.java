package com.example.landview.PromotionTab;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.landview.HomeFragmentSection.DetailReview.ImageFragment.Image;
import com.example.landview.R;
import com.example.landview.ViewPagerAdapter;

import org.w3c.dom.Text;

import java.util.List;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.ViewHolder> {

    private Context mcontext;
    private List<PromotionModel>mList;

    public PromotionAdapter(Context mcontext, List<PromotionModel> mList) {
        this.mcontext = mcontext;
        this.mList = mList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_promotion_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PromotionModel item = mList.get(position);
        if(item == null)
        {
            return;
        }
        holder.imgSale.setImageResource(item.getmImgSale());
        holder.txtSale.setText(item.getmTextsale());
        holder.txtSaleDes.setText(item.getmTextSaledes());
        holder.txtEXP.setText(item.getmExp());

    }

    @Override
    public int getItemCount() {
        if(mList!=null)
        {
            return mList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgSale;
        private TextView txtSale,txtSaleDes,txtEXP;
        private Button btnSale;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSale = itemView.findViewById(R.id.promotionImg);
            txtSale = itemView.findViewById(R.id.txtTextSale);
            txtSaleDes = itemView.findViewById(R.id.txtTextSaleDes);
            txtEXP = itemView.findViewById(R.id.txtEXP);
            btnSale = itemView.findViewById(R.id.btnSale);
        }
    }
}
