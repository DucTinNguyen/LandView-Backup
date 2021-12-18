package com.example.landview.PromotionTab;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PromotionsViewPager2 extends FragmentStateAdapter {
    public PromotionsViewPager2(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position)
        {
            case 0:
                return new AddPromotionFragment();
            case 1:
                return new MyPromotionFragment();
            default:
                return new AddPromotionFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
