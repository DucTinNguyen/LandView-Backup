package com.example.landview.PromotionTab;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.landview.R;

import java.util.ArrayList;
import java.util.List;


public class MyPromotionFragment extends Fragment {
    private RecyclerView rcv;
    private View view;
    MyPromotionAdapter promotionsAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_promotion, container, false);
        rcv = view.findViewById(R.id.rcvMypromotions);
        //sét data cho recycle view
        setDataforMy();
        return view;
    }
    private void setDataforMy() {
        promotionsAdapter = new MyPromotionAdapter(this.getContext(),getList());
        rcv.setAdapter(promotionsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false);
        rcv.setLayoutManager(linearLayoutManager);
    }

    private List<PromotionModel> getList() {
        List<PromotionModel>list = new ArrayList<>();
        list.add(new PromotionModel(R.drawable.sale2,"Sale 15%","Can use for book ticket and hotel","EXP:31/12/2021"));
        return list;
    }
}