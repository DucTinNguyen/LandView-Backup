package com.example.landview.Rating;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.landview.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RatingFragment extends Fragment {


    private static final String RATING_LIST ="rating_list";
    private static final String TYPE ="type";
    private static final String PLACE_ID ="placeId";


    CollectionReference collectionRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    private ArrayList<Float> ratings = new ArrayList<>();
    private String type;
    private String placeId;

    public RatingFragment() {
        // Required empty public constructor
    }



    public static RatingFragment newInstance(String type, String placeId) {
        RatingFragment fragment = new RatingFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        args.putString(PLACE_ID, placeId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ratings = (ArrayList<Float>) getArguments().getSerializable(RATING_LIST);
            placeId = getArguments().getString(PLACE_ID);
            type = getArguments().getString(TYPE);
            createCollectionRef();
        }
    }

    private ProgressBar pbVeryGood, pbGood,pbAvg, pbBad, pbVeryBad;
    private TextView tvFiveStar, tvFourStar, tvThreeStar, tvTwoStar, tvOneStar;
    private RatingBar rbRating;

    private void initUi(View view){
        pbVeryGood = view.findViewById(R.id.pb_very_good);
        pbGood = view.findViewById(R.id.pb_good);
        pbAvg = view.findViewById(R.id.pb_average);
        pbBad = view.findViewById(R.id.pb_bad);
        pbVeryBad = view.findViewById(R.id.pb_very_bad);

        tvFiveStar = view.findViewById(R.id.tv_5_star_count);
        tvFourStar = view.findViewById(R.id.tv_4_star_count);
        tvThreeStar = view.findViewById(R.id.tv_3_star_count);
        tvTwoStar = view.findViewById(R.id.tv_2_star_count);
        tvOneStar = view.findViewById(R.id.tv_1_star_count);

        rbRating = view.findViewById(R.id.rb_raing);
    }

    private void createCollectionRef(){
        switch (type){
            case "landscape":
                collectionRef = db.collection("landscapes").document(placeId).collection("review");
                break;
            case "hotel":
                collectionRef  = db.collection("hotels").document(placeId).collection("review");
                break;
            case "restaurant":
                collectionRef  = db.collection("restaurants").document(placeId).collection("review");
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rating, container, false);
        initUi(view);
        getCommentRating();

        return view;
    }

    private void getCommentRating(){
        collectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error == null){
                    ratings = new ArrayList<>();
                    for(DocumentSnapshot document : value){
                        double rate = document.getDouble("rating");
                        ratings.add((float) rate);
                    }
                    countingStar();
                } else {
                    countingStar();
                }
            }
        });
    }




    private void countingStar(){

        int total = ratings.size();
        float total_rate = 0;

        int fiveStars = 0;
        int fourStars =0 ;
        int threeStars = 0;
        int twoStars = 0;
        int oneStars = 0;

        for(float rating : ratings){
            if(rating > 4) {
                fiveStars ++;
            } else if (rating > 3){
                fourStars ++;
            } else if(rating > 2){
                threeStars ++;
            } else if(rating > 1){
                twoStars ++;
            } else {
                oneStars ++;
            }
            total_rate += rating;
        }

        pbVeryGood.setMax(total);
        pbVeryGood.setProgress(fiveStars);

        pbGood.setMax(total);
        pbGood.setProgress(fourStars);

        pbAvg.setMax(total);
        pbAvg.setProgress(threeStars);

        pbBad.setMax(total);
        pbBad.setProgress(twoStars);

        pbVeryBad.setMax(total);
        pbVeryBad.setProgress(oneStars);

        tvFiveStar.setText(String.valueOf(fiveStars));
        tvFourStar.setText(String.valueOf(fourStars));
        tvThreeStar.setText(String.valueOf(threeStars));
        tvTwoStar.setText(String.valueOf(twoStars));
        tvOneStar.setText(String.valueOf(oneStars));

        if(total > 0){
            rbRating.setRating(total_rate/ total);
        } else {
            rbRating.setRating(5f);
        }
    }
}