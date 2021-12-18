package com.example.landview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.landview.HomeFragmentSection.News.NewItem;
import com.example.landview.HomeFragmentSection.News.NewItemAdapter;
import com.example.landview.HomeFragmentSection.Reviews.Review;
import com.example.landview.HomeFragmentSection.Services.ItemService;
import com.example.landview.HomeFragmentSection.Services.ItemServiceAdapter;
import com.example.landview.HomeFragmentSection.Slider.Slider;
import com.example.landview.HomeFragmentSection.Slider.SliderAdapter;
import com.example.landview.HomeFragmentSection.trips.Trips;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class HomeFragment extends Fragment {
    private ImageView imgNotify,imgSetting;
    private ViewPager2 viewPager2;
    private CircleIndicator3 circleIndicator3;
    private EditText editOnchange;
    private TextView txtseemore;
    SliderAdapter sliderAdapter;
    List<Slider>list;
    ItemServiceAdapter itemServiceAdapter;
    GridView gridView;
    RecyclerView rcv;
    NewItemAdapter newItemAdapter;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(viewPager2.getCurrentItem() == list.size()-1)
            {
                viewPager2.setCurrentItem(0);
            }
            else
            {
                viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_home,container,false);
       //ánh xạ view
        viewPager2 = view.findViewById(R.id.viewPager2);
        circleIndicator3 = view.findViewById(R.id.indicator3);
        gridView = view.findViewById(R.id.gridView);
        rcv = view.findViewById(R.id.recycleview);
        txtseemore = view.findViewById(R.id.txtReadmore);
        editOnchange = view.findViewById(R.id.frameLayout);

        editOnchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),Review.class);
                getContext().startActivity(intent);
            }
        });

        //đổ dữ liệu slider
        addSlider();
        //auto run slider
        eventAutoRunSlider();
        //đổ dữ liệu vào gridView
        addListitems();
        //gán sự kiện cho gridview
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i)
                {
                    case 0:
                        Intent intent = new Intent(getActivity(), Trips.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getActivity(), Review.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getActivity(),Weather.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(getActivity(),Promotion.class);
                        startActivity(intent3);
                        break;
                }
            }
        });

        //đổ dữ liệu vao recycleview
        newItemAdapter = new NewItemAdapter(this.getContext(),getListNews());
        rcv.setAdapter(newItemAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(),LinearLayoutManager.HORIZONTAL,false);
        rcv.setLayoutManager(linearLayoutManager);
        //xử lí sự kiện đọc thêm
        txtseemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),News.class);
                startActivity(intent);
            }
        });
        return view;
    }


    private void eventAutoRunSlider() {
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable,3000);
            }
        });

    }
    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable,3000);
    }

    private List<NewItem> getListNews() {
        List<NewItem>list = new ArrayList<>();
        list.add(new NewItem(R.drawable.hoian,"Thành phố nào ở nước ta duy nhất giáp biển?","https://vnexpress.net/tinh-nao-co-3-mat-giap-bien-3531889-p2.html"));
        list.add(new NewItem(R.drawable.covid,"Covid 19 tăng cao?","https://vnexpress.net/ca-covid-19-ha-noi-tang-cao-4401477.html"));
        list.add(new NewItem(R.drawable.danhlam,"Top ảnh đẹp du lịch nhất 2021","https://vnexpress.net/top-anh-du-lich-dep-nhat-2021-4399737.html"));
        return list;
    }

    private void addListitems() {
        itemServiceAdapter = new ItemServiceAdapter(this.getContext(),R.layout.layout_service_item_home_fragment);
        itemServiceAdapter.add(new ItemService(R.color.tripColor,R.drawable.icon_trips,"Trips"));
        itemServiceAdapter.add(new ItemService(R.color.reviewColor,R.drawable.img_search_yellow,"Search"));
        itemServiceAdapter.add(new ItemService(R.color.weatherColor,R.drawable.img_weather_purple,"Weather"));
        itemServiceAdapter.add(new ItemService(R.color.promotionColor,R.drawable.icon_pro,"Promotions"));
        gridView.setAdapter(itemServiceAdapter);
    }


    private void addSlider() {
        list = getList();
        sliderAdapter = new SliderAdapter(list);
        viewPager2.setAdapter(sliderAdapter);
        circleIndicator3.setViewPager(viewPager2);
    }

    private List<Slider> getList() {
        List<Slider>list= new ArrayList<>();
        list.add(new Slider(R.drawable.img_slider_1));
        list.add(new Slider(R.drawable.img_slider_2));
        list.add(new Slider(R.drawable.img_slider_3));
        return list;
    }
}