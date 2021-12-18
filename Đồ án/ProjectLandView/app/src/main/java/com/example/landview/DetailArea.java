package com.example.landview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.landview.Area.Area;
import com.example.landview.Area.TopItem;
import com.example.landview.chung.SliderAdapter;

import java.util.ArrayList;

public class DetailArea extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private TextView txtName,txtDesCription;
    private Handler sliderHandler = new Handler();
    private TextView imagesCountTv;
    private LinearLayout layout,btnSeemap;
    private Bundle bundle;
    private TopItem item;
    private Area area;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detail_area);
        //ánh xạ view
        initUI();
        //nhận dữ liệu và sét sự kiện từ bundel Top ItemAdapter
//        bundle = getIntent().getExtras();
//        if(bundle == null)
//        {
//            return;
//        }
//        item = (TopItem) bundle.get("area");
//        txtName.setText(item.getName());
//        txtDesCription.setText(item.getTextDescription());
        //sử lí sự kiện readmore

        Intent intent = getIntent();
        area = (Area) intent.getSerializableExtra("area");

        txtName.setText(area.getAreaName());
        txtDesCription.setText(area.getDescription());
        imagesCountTv.setText((1 + "/" + area.getImages().size()));
        setUpViewPager2(area.getImages());

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processReadmore();
            }
        });
        //sử lí sự kiện button xem bản đồ
        btnSeemap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(item.getmLink()));
                startActivity(intent);
            }
        });
    }

    private void processReadmore() {
        txtDesCription.setMaxLines(txtDesCription.getLineCount());
        txtDesCription.setEllipsize(null);
    }

    private void initUI() {
        viewPager2 = findViewById(R.id.viewpager2);
        imagesCountTv = findViewById(R.id.images_count_tv);
        txtName = findViewById(R.id.name);
        txtDesCription = findViewById(R.id.description);
        layout = findViewById(R.id.readmore);
        btnSeemap = findViewById(R.id.btnSeeMap);
    }

    private void setUpViewPager2(ArrayList<String> images){
        SliderAdapter adapter = new SliderAdapter(images);
        viewPager2.setAdapter(adapter);
        viewPager2.setClipChildren(false);
        viewPager2.setClipToPadding(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                imagesCountTv.setText((position + 1) + "/" + area.getImages().size());
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
}