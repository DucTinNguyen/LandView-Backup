package com.example.landview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.landview.Comment.CommentFragment;
import com.example.landview.ExpandableTextView.ExpandableTextView;
import com.example.landview.LandScape.Landscape;
import com.example.landview.Map.NearbyAndMapFragment;
import com.example.landview.Rating.RatingFragment;
import com.example.landview.chung.SliderAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class LandScapeDetail extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private boolean isExpand = false;
    private Landscape landscape;
    private TextView tvDescription;
    private TextView tvName;
    private ViewPager2 viewPager2;
    private TextView tvImageCount;
    private TextView tvAddress;

    private RatingBar ratingBar;
    private TextView tvTotalRates;

    private ExpandableTextView etvDescription;

    private SliderAdapter sliderAdapter;
    private Handler sliderHandler = new Handler();

    private void initUI() {
        tvName = findViewById(R.id.tv_landscape_name);
        tvAddress = findViewById(R.id.tv_landscape_address);
        viewPager2 = findViewById(R.id.vp2_landscape);
        tvImageCount = findViewById(R.id.tv_landscape_image_count);
        ratingBar = findViewById(R.id.rb_landscape);
        tvTotalRates = findViewById(R.id.tv_landscape_total_rate);
        etvDescription = findViewById(R.id.etv_landscape_description);

    }

    // Lấy Landscape object từ activity trc đó đã gọi
    private void getLandscape(){
        Intent intent = getIntent();
        landscape = (Landscape) intent.getSerializableExtra("landscape");
        if(landscape == null){
            Toast.makeText(LandScapeDetail.this, "Landscape is null", Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_landscape_detail);
        //ánh xạ view
        getLandscape();
        initUI();

        tvName.setText(landscape.getName());

        etvDescription.setText(landscape.getDescription());
        tvAddress.setText(landscape.getAddress());

        tvImageCount.setText(1+"/"+landscape.getImages());
        sliderAdapter = new SliderAdapter(landscape.getImages());
        setUpViewPager2(sliderAdapter);

        getRating();
        createRatingFragment();
        createCommentFragment();
        createMapFragment();

    }

    private void createMapFragment(){
        NearbyAndMapFragment nearbyAndMapFragment = NearbyAndMapFragment
                .newInstance(landscape.getLatitude(), landscape.getLongitude());

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fcv_map, nearbyAndMapFragment)
                .commit();
    }



    /***************** Xử lý ảnh tự động trượt **********************************************/
    private void setUpViewPager2(SliderAdapter sliderAdapter){
        viewPager2.setAdapter(sliderAdapter);
        viewPager2.setClipChildren(false);
        viewPager2.setClipToPadding(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tvImageCount.setText((position + 1) + "/" + landscape.getImages().size());
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    /*******************************************************************************************************/


    /********************************* Xử lý lấy rating ****************************************************/

    private void getRating(){
        db.collection("landscapes")
                .document(landscape.getId()).collection("review").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
                if(error == null){
                    if(querySnapshot.size() == 0){
                        ratingBar.setRating(5f);
                        tvTotalRates.setText("0 đánh giá");
                    } else {
                        float rate = 0;
                        int count =0;
                        for(DocumentSnapshot document : querySnapshot){
                            double rating = document.getDouble("rating");
                            rate += rating;
                            count ++;
                        }
                        ratingBar.setRating(rate/count);
                        tvTotalRates.setText(String.valueOf(count) + " đánh giá");
                    }
                }

                if(error != null){
                    ratingBar.setRating(5f);
                    tvTotalRates.setText("0 đánh giá");
                }
            }
        });
    }

    /**********************************************************************************************/



    private void createRatingFragment(){
        // Tạo fragment object và truyền vào 2 tham số
        RatingFragment ratingFragment = RatingFragment.newInstance(landscape.getType(), landscape.getId());
        // Gọi fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fcv_rating, ratingFragment)
                .commit();
    }

    private void createCommentFragment(){
        // Tạo fragment object và truyền vào 2 tham số
        CommentFragment commentFragment = CommentFragment.newInstance(landscape.getType(), landscape.getId());
        // Gọi fragment
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fcv_comment, commentFragment)
                .commit();
    }

}
