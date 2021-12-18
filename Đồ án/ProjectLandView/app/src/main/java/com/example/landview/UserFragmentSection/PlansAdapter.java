package com.example.landview.UserFragmentSection;

import android.content.Context;
import android.hardware.lights.LightState;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.landview.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.ViewHolder> {
    Context mContext;
    List<PlanItem> list;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    public PlansAdapter(Context context, List<PlanItem> list) {
        this.mContext = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_for_user_plans, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlanItem item = list.get(position);
        holder.planName.setText(item.getName());
        holder.planDes.setText(item.getDescription());
        String dateRange = item.getDateStart() + " - " + item.getDateEnd();
        holder.planDate.setText(dateRange);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView planName, planDes, planDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            planName = itemView.findViewById(R.id.plansNameTV);
            planDes = itemView.findViewById(R.id.plansDescriptionTV);
            planDate = itemView.findViewById(R.id.dateTV);
        }
    }
}
