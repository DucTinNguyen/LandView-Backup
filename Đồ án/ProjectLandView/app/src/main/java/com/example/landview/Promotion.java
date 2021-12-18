package com.example.landview;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.landview.PromotionTab.PromotionsViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Promotion extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    PromotionsViewPager2 promotionsViewPager2;
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.layout_promotion);
        //ánh xạ aview
        tabLayout = findViewById(R.id.tablayout);
        viewPager2 = findViewById(R.id.viewPager2);
        //
        promotionsViewPager2 = new PromotionsViewPager2(this);
        viewPager2.setAdapter(promotionsViewPager2);
        new TabLayoutMediator(tabLayout,viewPager2,((tab, position) -> {
            switch (position)
            {
                case 0:
                    tab.setText("ADD PROMOTIONS");
                    break;
                case 1:
                    tab.setText("MY PROMOTIONS");
                    break;
            }
        })).attach();
    }
}
