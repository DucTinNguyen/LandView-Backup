package com.example.landview.HomeFragmentSection.DetailReview;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.landview.R;
import com.example.landview.HomeFragmentSection.Reviews.ItemReview;
import com.example.landview.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class DetailHoguom extends AppCompatActivity {
    private TabLayout mtablayout;
    private ViewPager2 mviewpager;
    private ImageView img;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_review_detail_hoguom);

        Bundle bundle = getIntent().getExtras();
        if(bundle==null)
        {
            return;
        }
        ItemReview itemReview = (ItemReview) bundle.get("object_ItemReview");
        //ánh xạ view
        mtablayout = findViewById(R.id.tablayout);
        mviewpager = findViewById(R.id.viewPager2);
        img = findViewById(R.id.imgView);
//        img.setImageResource(itemReview.getImg());
        viewPagerAdapter = new ViewPagerAdapter(this);
        mviewpager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(mtablayout,mviewpager,(tab, position) ->{
            switch (position)
            {
                case 0:
                    tab.setText("Overview");
                    break;
                case 1:
                    tab.setText("Review");
                    break;
                case 2:
                    tab.setText("Image");
                    break;
                default:tab.setText("Overview");
            }
        }).attach();
    }


}
