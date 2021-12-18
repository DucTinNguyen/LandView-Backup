package com.example.landview;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.landview.HomeFragmentSection.DetailReview.ImageFragment.Image;
import com.example.landview.HomeFragmentSection.DetailReview.OverviewFragment.Overview;
import com.example.landview.HomeFragmentSection.DetailReview.ReviewFragment.ReviewFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new Overview();
            case 1:
                return new ReviewFragment();
            case 2:
                return new Image();
            default:return new Overview();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
