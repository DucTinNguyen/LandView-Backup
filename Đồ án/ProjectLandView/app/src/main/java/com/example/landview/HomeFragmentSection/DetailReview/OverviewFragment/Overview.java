package com.example.landview.HomeFragmentSection.DetailReview.OverviewFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.landview.R;


public class Overview extends Fragment {
    private TextView textViewAddress,textViewDescriptions;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview,container,false);

        textViewAddress = view.findViewById(R.id.txtDestination);
        textViewDescriptions = view.findViewById(R.id.textDescription);
        return view;
    }}