package com.example.landview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.landview.Comment.CommentFragment;
import com.example.landview.Map.NearbyAndMapFragment;
import com.example.landview.Rating.RatingFragment;
import com.example.landview.Restaurant.Restaurant;
import com.example.landview.chung.SliderAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class RestaurantDetail extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Restaurant restaurant;

    private ViewPager2 viewPager2;
    private SliderAdapter sliderAdapter;
    private Handler sliderHandler = new Handler();

    private TextView tvImageCount;
    private TextView tvName;
    private RatingBar ratingBar;
    private TextView tvTotalRate;
    //    private TextView tvPrice;
//    private TextView tvDescription;
    private TextView tvAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        // Lấy dữ liệu từ intent
        getRestaurantData();
        // Khởi tạo view
        initUi();

        tvImageCount.setText(1 + "/" + restaurant.getImages().size());
        tvName.setText(restaurant.getName());
        tvAddress.setText(restaurant.getAddress());

        sliderAdapter = new SliderAdapter(restaurant.getImages());
        setUpViewPager2(sliderAdapter);

        // getRating();

        getRating();


        createCommentFragment();

        createRatingFragment();

        createMapFragment();
    }

    private void createMapFragment(){
        NearbyAndMapFragment nearbyAndMapFragment = NearbyAndMapFragment
                .newInstance(restaurant.getLatitude(), restaurant.getLongitude());

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fcv_map, nearbyAndMapFragment)
                .commit();
    }


    // Khởi tạo các Ui
    private void initUi() {
        viewPager2 = findViewById(R.id.vp2_res);
        tvImageCount = findViewById(R.id.tv_res_image_count);
        tvName = findViewById(R.id.tv_res_name);
        ratingBar = findViewById(R.id.rb_res);
        tvTotalRate = findViewById(R.id.tv_res_total_rate);
        tvAddress = findViewById(R.id.tv_res_address);

    }

    // Lấy restaurant object từ intent
    private void getRestaurantData() {
        Intent intent = getIntent();
        restaurant = (Restaurant) intent.getSerializableExtra("restaurant");
        if (restaurant == null) {
            Toast.makeText(RestaurantDetail.this, "Restaurant object is null", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /***************** Xử lý ảnh tự động trượt*****************/
    private void setUpViewPager2(SliderAdapter sliderAdapter) {
        viewPager2.setAdapter(sliderAdapter);
        viewPager2.setClipChildren(false);
        viewPager2.setClipToPadding(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tvImageCount.setText((position + 1) + "/" + restaurant.getImages().size());
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

    /**********************************************************************************************/


    /********************************* Xử lý lấy rating ****************************************************/
    private void getRating() {
        db.collection("restaurants")
                .document(restaurant.getId()).collection("review")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
                        if (error == null) {
                            if (querySnapshot.size() == 0) {
                                ratingBar.setRating(5f);
                                tvTotalRate.setText("0 đánh giá");
                            } else {
                                float rate = 0;
                                int count = 0;
                                for (DocumentSnapshot document : querySnapshot) {
                                    double rating = document.getDouble("rating");
                                    rate += rating;
                                    count++;
                                }
                                ratingBar.setRating(rate / count);
                                tvTotalRate.setText(String.valueOf(count) + " đánh giá");
                            }
                        } else {
                            ratingBar.setRating(5f);
                            tvTotalRate.setText("0 đánh giá");
                        }
                    }
                });
    }

    /**********************************************************************************************/

    private void createCommentFragment() {
        // Tạo fragment object và truyền vào 2 tham số
        CommentFragment commentFragment = CommentFragment.newInstance(restaurant.getType(), restaurant.getId());
        // Gọi fragment
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fcv_comment, commentFragment)
                .commit();
    }

    private void createRatingFragment() {
        RatingFragment ratingFragment = RatingFragment.newInstance(restaurant.getType(), restaurant.getId());
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fcv_rating, ratingFragment)
                .commit();
    }

/***** lEGACY CODE: maybe we can reuse it
    private void getRating() {
        db.collection("restaurants")
                .document(restaurant.getId()).collection("review")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot.size() == 0) {
                                ratingBar.setRating(5f);
                                tvTotalRate.setText("0 đánh giá");
                            } else {
                                float rate = 0;
                                int count = 0;
                                for (DocumentSnapshot document : querySnapshot) {
                                    double rating = document.getDouble("rating");
                                    rate += rating;
                                    count++;
                                }
                                ratingBar.setRating(rate / count);
                                tvTotalRate.setText(String.valueOf(count) + " đánh giá");
                            }

                        }
                    }
                });
    }
*/

}