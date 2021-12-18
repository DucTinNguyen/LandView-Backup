package com.example.landview.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.landview.Hotel.Hotel;
import com.example.landview.HotelDetail;
import com.example.landview.LandScape.Landscape;
import com.example.landview.LandScapeDetail;
import com.example.landview.Place.Place;
import com.example.landview.R;
import com.example.landview.Restaurant.Restaurant;
import com.example.landview.RestaurantDetail;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private static final String TAG = "MapActivity: ";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Tạo sẵn một số collectionRef để dỡ mỏi tay gõ
    private CollectionReference areaColl = db.collection("areas");
    private CollectionReference landscapeColl = db.collection("landscapes");
    private CollectionReference hotelColl = db.collection("hotels");
    private CollectionReference resColl = db.collection("restaurants");

    ArrayList<Place> placesOnMap;

    private GoogleMap googleMap;
    private Marker currentMarker;
    private ArrayList<Marker> markers;
    private MapItemAdapter adapter;

    private ViewPager2 viewPager2;

    private Bitmap blueHotel;
    private Bitmap blueLandscape;
    private Bitmap blueRestaurant;

    private Bitmap redHotel;
    private Bitmap redLandscape;
    private Bitmap redRestaurant;

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId){
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private void createBitmap(){
        blueHotel = getBitmapFromVectorDrawable(this, R.drawable.hotel_blue_24);
        blueLandscape = getBitmapFromVectorDrawable(this, R.drawable.landscape_blue_24);
        blueRestaurant = getBitmapFromVectorDrawable(this, R.drawable.restaurant_blue_24);

        redHotel = getBitmapFromVectorDrawable(this, R.drawable.hotel_red_24);
        redLandscape = getBitmapFromVectorDrawable(this, R.drawable.landscape_red_24);
        redRestaurant = getBitmapFromVectorDrawable(this,R.drawable.red_restaurant_24);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        viewPager2 = findViewById(R.id.vp2_map);

        Intent intent = getIntent();
        placesOnMap = (ArrayList<Place>) intent.getSerializableExtra("places");
        if(placesOnMap != null){
            for(Place place : placesOnMap){
                Log.d(TAG, "MapActivity: " + place.toString());
            }
        } else {
            Toast.makeText(this, "Can't pass object please use perce", Toast.LENGTH_SHORT).show();
            finish();
        }

        createBitmap();
        adapter = new MapItemAdapter(this, placesOnMap);
        adapter.setMapItemClick(mapItemClickListener);
        for(Place place : placesOnMap){
            getRating(place);
        }

        markers = new ArrayList<>();

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.fcv_map, mapFragment).commit();
        mapFragment.getMapAsync(this);
    }

    private void getRating(Place place){
        String type = place.getType();

        // Ta không lấy rating của area do nó éo có
        // nhớ cái path ở getFavoritePlace hem

        db.document(place.getPath())
                .collection("review") // Ta truy cập vào collection review
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            QuerySnapshot querySnapshot = task.getResult();

                            // Trường hợp không có ai rating hay comment auto cho 5 sao
                            if(querySnapshot.size() == 0){
                                place.setRating(5f);
                                place.setTotalRate(0);
                            } else { // Nếu có nhận xét
                                int count =0; // đếm số lượng document
                                float rate = 0; // tính tổng các rating

                                for(DocumentSnapshot document : querySnapshot){
                                    double rating = document.getDouble("rating");
                                    rate += rating;
                                    count++;
                                }
                                place.setRating(rate/count);
                                place.setTotalRate(count);
                            }
                        } else {
                            place.setRating(5f);
                            place.setTotalRate(0);
                        }

                        // Thông báo adapter cập nhập lại dữ liệu
                        adapter.notifyDataSetChanged();
                    }
                });

    }

    private void setUpViewPager2(MapItemAdapter adapter){
        viewPager2.setAdapter(adapter);
        viewPager2.setClipChildren(false);
        viewPager2.setClipToPadding(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d(TAG, "onPageSelected: " + position);
                Place place = placesOnMap.get(position);
                int currentPos = (int) currentMarker.getTag();

                if( !currentMarker.equals(markers.get(position))){

                    setRedIcon(place.getType(), markers.get(position));
                    setBlueIcon(placesOnMap.get(currentPos).getType(), currentMarker);

                    currentMarker = markers.get(position);

                    LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
                    googleMap.animateCamera(cameraUpdate);
                }

            }

        });

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(16));
        viewPager2.setPageTransformer(compositePageTransformer);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        this.googleMap = map;
        map.setOnMarkerClickListener(this);
        map.addMarker(new MarkerOptions()
                .title("Marker1").position(new LatLng(1,1)));

        try {
            boolean success = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_json));

        } catch (Exception e){

        }

        for(int i=0 ; i < placesOnMap.size();i++){
            Place place = placesOnMap.get(i);

            // Loại bỏ tất cả Area
            if(place.getType() == "area"){
                placesOnMap.remove(i);
                continue;
            }

            LatLng placePos = new LatLng(place.getLatitude(), place.getLongitude());
            Marker marker1 = map.addMarker(new MarkerOptions().position(placePos).title(place.getName()));
            marker1.setTag(i);

            markers.add(marker1);

            if(i==0){
                setRedIcon(placesOnMap.get(0).getType(),marker1);
                currentMarker = marker1;
                LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            } else {
                setBlueIcon(placesOnMap.get(i).getType(), marker1);
            }
        }

        if(placesOnMap.size() ==0){
            viewPager2.setVisibility(View.GONE);
        } else {
            setUpViewPager2(adapter);
        }


    }


    private void setBlueIcon(String type, Marker marker){
        switch (type){
            case "landscape":
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(blueLandscape));
                break;
            case "hotel":
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(blueHotel));
                break;
            case "restaurant":
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(blueRestaurant));
                break;
            default:
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                break;
        }
    }

    private void setRedIcon(String type, Marker marker){
        switch (type){
            case "landscape":
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(redLandscape));
                break;
            case "hotel":
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(redHotel));
                break;
            case "restaurant":
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(redRestaurant));
                break;
            default:
                marker.setIcon(BitmapDescriptorFactory.defaultMarker());
                break;
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        int position = (int) marker.getTag();
        Log.d(TAG, "onMarkerClick: " + position);
        Place place = placesOnMap.get(position);

        int currentPos = (int) currentMarker.getTag();
        if(position != currentPos){

            setRedIcon(place.getType(), marker);
            setBlueIcon(placesOnMap.get(currentPos).getType(), currentMarker);

            currentMarker = marker;
            viewPager2.setCurrentItem(position);
        }

        return false;
    }

    private MapItemAdapter.MapItemClick mapItemClickListener = new MapItemAdapter.MapItemClick() {
        @Override
        public void ItemClick(int position) {
            Place place = placesOnMap.get(position);
            // Do place ta dùng chung cho hotel, landscape, restaurant, area nên ta
            // phân biệt thông qua type
            switch (place.getType()){
                case "landscape":
                    startLandscapeDetail(place.getId());
                    break;
                case "hotel":
                    startHotelDetail(place.getId());
                    break;
                case "restaurant":
                    startResDetail(place.getId());
                    break;
            }
        }
    };



    private void startLandscapeDetail(String id){
        landscapeColl.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists() && document != null){
                        Landscape landscape = document.toObject(Landscape.class);
                        GeoPoint geoPoint = document.getGeoPoint("geopoint");

                        landscape.setLatitude(geoPoint.getLatitude());
                        landscape.setLongitude(geoPoint.getLongitude());

                        Intent intent = new Intent(MapActivity.this, LandScapeDetail.class);
                        intent.putExtra("landscape", landscape);
                        startActivity(intent);
                    }
                }
            }
        });

    }

    private void startHotelDetail(String id){
        hotelColl.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists() && document != null){
                        Hotel hotel = document.toObject(Hotel.class);
                        GeoPoint geoPoint = document.getGeoPoint("geopoint");

                        hotel.setLatitude(geoPoint.getLatitude());
                        hotel.setLongitude(geoPoint.getLongitude());

                        Intent intent = new Intent(MapActivity.this, HotelDetail.class);
                        intent.putExtra("hotel", hotel);
                        startActivity(intent);
                    }
                }
            }
        });

    }

    private void startResDetail(String id){
        resColl.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists() && document != null){
                        Restaurant restaurant = document.toObject(Restaurant.class);
                        GeoPoint geoPoint = document.getGeoPoint("geopoint");

                        restaurant.setLatitude(geoPoint.getLatitude());
                        restaurant.setLongitude(geoPoint.getLongitude());

                        Intent intent = new Intent(MapActivity.this, RestaurantDetail.class);
                        intent.putExtra("restaurant", restaurant);
                        startActivity(intent);
                    }
                }
            }
        });
    }

}