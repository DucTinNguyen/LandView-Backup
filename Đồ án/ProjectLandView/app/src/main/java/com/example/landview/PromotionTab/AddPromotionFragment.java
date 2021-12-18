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


public class AddPromotionFragment extends Fragment {

    private View view;
    private RecyclerView rcv;
    PromotionAdapter promotionsAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add, container, false);
        rcv = view.findViewById(R.id.rcvAddpromotions);

        //sét dữ liệu lên recycleview
        setDataForAdd();
        return view;
    }


    private void setDataForAdd() {
        promotionsAdapter = new PromotionAdapter(this.getContext(),getList());
        rcv.setAdapter(promotionsAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false);
        rcv.setLayoutManager(linearLayoutManager);
    }

    private List<PromotionModel> getList() {
        List<PromotionModel>list= new ArrayList<>();
        list.add(new PromotionModel(R.drawable.sale1,"Sale 25%","Can use for book ticket or hotel","EXP:31/11/2022"));
        list.add(new PromotionModel(R.drawable.sale2,"Sale 15%","Can use for book ticket or hotel","EXP:31/11/2022"));
        return list;
    }
}