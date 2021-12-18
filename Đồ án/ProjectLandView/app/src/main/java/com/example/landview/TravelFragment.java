package com.example.landview;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.landview.Area.Area;
import com.example.landview.Area.AreaAdapter;
import com.example.landview.HomeFragmentSection.Reviews.Review;
import com.example.landview.Hotel.Hotel;
import com.example.landview.Hotel.HotelAdapter;
import com.example.landview.LandScape.Landscape;
import com.example.landview.LandScape.LandscapeAdapter;
import com.example.landview.Restaurant.ResAdapter;
import com.example.landview.Restaurant.Restaurant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class TravelFragment extends Fragment {
    private static final String TAG = "TravelFragment";

    ImageView iconNotify,iconsetting,iconSearch;
    EditText edtSearch;


    // Dành cho area
    private ArrayList<Area> areas; // danh sách area object
    private AreaAdapter areaAdapter;
    private RecyclerView rcvArea; // recycler view hiển thị các area

    // Dành cho landscape
    private ArrayList<Landscape> landscapes;
    private LandscapeAdapter landscapeAdapter;
    private RecyclerView rcvLandscape;

    // Dành cho hotel
    private ArrayList<Hotel> hotels;
    private HotelAdapter hotelAdapter;
    private RecyclerView rcvHotel;

    // Dành cho restaurant
    private ArrayList<Restaurant> restaurants;
    private ResAdapter resAdapter;
    private RecyclerView rcvRestaurant;


    // Firestore
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_travel,container,false);
        //ánh xạ view
        iconNotify = view.findViewById(R.id.imgNotifycations);
        iconsetting = view.findViewById(R.id.imgSetting);
        iconSearch = view.findViewById(R.id.inconSearch);
        edtSearch = view.findViewById(R.id.edtSearch);

        // Init các recycler view
        rcvArea = view.findViewById(R.id.rcv_area);
        rcvHotel = view.findViewById(R.id.rcv_hotel);
        rcvLandscape = view.findViewById(R.id.rcv_landscape);
        rcvRestaurant = view.findViewById(R.id.rcv_restaurant);

        //
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        rcvArea.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        areas = new ArrayList<>();
        getAreaData();

        // Landscape
        rcvLandscape.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        landscapes = new ArrayList<>(); // Khởi tạo danh sách landsacpe
        getLandscapeData(); // Lấy dữ liệu landscape từ database


        // Lấy dữ liệu area từ database

        rcvHotel.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        hotels = new ArrayList<>();
        getHotelData();

        rcvRestaurant.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        restaurants = new ArrayList<>();
        getResData();


        //thêm listener cho thanh search bar
        edtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(getContext(), Review.class));
            }
        });

        return view;
    }

    /******************************** Area ************************************************/


    private void getAreaData(){
        db.collection("areas").limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for(QueryDocumentSnapshot document : task.getResult()){
                        // Tạo từng area object
                        Area area = document.toObject(Area.class);

                        GeoPoint geoPoint = document.getGeoPoint("geopoint");
                        area.setLatitude(geoPoint.getLatitude());
                        area.setLongitude(geoPoint.getLongitude());

                        areas.add(area);
                    }
                    // bind ArrayList<Area> areas vào adapter
                    areaAdapter = new AreaAdapter(getContext(), areas);
                    // Set item click listener cho adapter
                    areaAdapter.setAreaClick(areaClickListener);
                    rcvArea.setAdapter(areaAdapter); // set
                } else {

                }
            }
        });

    }

    private AreaAdapter.AreaClick areaClickListener = new AreaAdapter.AreaClick() {
        @Override
        public void itemClick(int position) {
            Intent intent = new Intent(getContext(), DetailArea.class);
            intent.putExtra("area", areas.get(position));
            startActivity(intent);
        }

        @Override
        public void likeClick(int position) {
            Area area = areas.get(position);
            likeArea(area, position);
        }
    };

    private void likeArea(Area area, int position){
        String userId = mAuth.getCurrentUser().getUid();
        ArrayList<String> likes = area.getLikes();

        DocumentReference areaRef = db.collection("areas").document(area.getId());
        DocumentReference userRef = db.collection("users").document(userId);

        if(likes.contains(userId)){
            areaRef.update("likes", FieldValue.arrayRemove(userId));
            userRef.update("likes", FieldValue.arrayRemove(areaRef));
            likes.remove(userId);
        } else{
            areaRef.update("likes", FieldValue.arrayUnion(userId));
            userRef.update("likes", FieldValue.arrayUnion(areaRef));
            likes.add(userId);
        }
        areaAdapter.notifyItemChanged(position);
    }


    /*********************************************************************************************************************/



    /******************************** Landscape *******************************************************************************/


    private void getLandscapeData(){
        db.collection("landscapes") // truy vấn landscape collection
                .limit(15).get() // giới hạn 15 landscape
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    // Khởi tạo landscape Adapter
                    landscapeAdapter = new LandscapeAdapter(getContext(), landscapes);
                    // Khởi tạo Landscape Click
                    landscapeAdapter.setLandscapeClick(landscapeCLickListener);

                    for(DocumentSnapshot document : task.getResult()){
                        // Tạo một landscape object
                        Landscape landscape = document.toObject(Landscape.class);

                        // Lấy geopoint từ document
                        GeoPoint geoPoint = document.getGeoPoint("geopoint");

                        // set latitude và longitude cho landscape object
                        landscape.setLatitude(geoPoint.getLatitude());
                        landscape.setLongitude(geoPoint.getLongitude());

                        // Thêm landscape vào landscapes ArrayList<Landscape> đã tạo trước đó
                        landscapes.add(landscape);
                        // Lấy rating và total rate
                        getLandscapeRating(document.getReference(), landscape);
                    }
                    rcvLandscape.setAdapter(landscapeAdapter);
                }
            }
        });
   }

    private LandscapeAdapter.LandscapeClick landscapeCLickListener = new LandscapeAdapter.LandscapeClick() {
       @Override
       public void itemClick(int position) { // Click vào cả item
            Landscape landscape = landscapes.get(position);
            Intent intent = new Intent(getContext(), LandScapeDetail.class);
            intent.putExtra("landscape", landscape);

            // Đi tới landscape detail
            startActivity(intent);
       }

       @Override
       public void likeClick(int position) { // click vào icon Tym
            Landscape landscape = landscapes.get(position);
            likeLandscape(landscape, position);
       }
   };

    private void getLandscapeRating(DocumentReference documentRef, Landscape landscape){
        // Mỗi landsacpe sẽ có một review collection chứa các document
        documentRef.collection("review").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            int count =0; // đếm số lượng document
                            float rate = 0; // tính tổng rate
                            for(DocumentSnapshot document : task.getResult()){
                                double rating = document.getDouble("rating");
                                rate += rating;
                                count++;
                            }
                            if(count > 0){
                                // trung bình rate / count
                                landscape.setRating(rate/count);
                                landscape.setTotalRate(count);
                            }
                            // gọi adapter cập nhật lại dữ liệu
                            landscapeAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void likeLandscape(Landscape landscape,int position){
        String userId = mAuth.getUid(); // Lấy user Id
        // Lấy danh sách chứa userId trong likesList của landscape
        ArrayList<String> likesList = landscape.getLikesList();

        DocumentReference landscapeRef = db.collection("landscapes").document(landscape.getId());
        DocumentReference userRef = db.collection("users").document(userId);

        if(likesList.contains(userId)){ // unlike
            landscapeRef.update("likesList", FieldValue.arrayRemove(userId));
            userRef.update("likes", FieldValue.arrayRemove(landscapeRef));
            likesList.remove(userId);
        } else { // like
            landscapeRef.update("likesList", FieldValue.arrayUnion(userId));
            userRef.update("likes", FieldValue.arrayUnion(landscapeRef));
            likesList.add(userId);
        }
        // Cập nhật lại adapter
        landscapeAdapter.notifyItemChanged(position);
    }


    /***************************************************************************************************************************/



    /******************************** Hotel *******************************************************************************/


    private void getHotelData(){
        db.collection("hotels")
                .limit(15).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            hotelAdapter = new HotelAdapter(getContext(), hotels);
                            hotelAdapter.setHotelClick(hotelClickListener);
                            for(DocumentSnapshot document : task.getResult()){
                                Hotel hotel = document.toObject(Hotel.class);

                                GeoPoint geoPoint = document.getGeoPoint("geopoint");
                                hotel.setLatitude(geoPoint.getLatitude());
                                hotel.setLongitude(geoPoint.getLongitude());

                                hotels.add(hotel);
                                getHotelRating(document.getReference(), hotel);

                            }
                            rcvHotel.setAdapter(hotelAdapter);
                        }
                    }
                });
    }

    private HotelAdapter.HotelClick hotelClickListener = new HotelAdapter.HotelClick() {
        @Override
        public void itemClick(int position) {
            Hotel hotel = hotels.get(position);
            Intent intent = new Intent(getContext(), HotelDetail.class);
            intent.putExtra("hotel", hotel);
            startActivity(intent);
        }

        @Override
        public void likeClick(int position) {
            Hotel hotel = hotels.get(position);
            likeHotel(hotel, position);
        }
    };

    private void likeHotel(Hotel hotel, int position){
        String userId = mAuth.getUid();
        ArrayList<String> likesList = hotel.getLikesList();

        DocumentReference hotelRef = db.collection("hotels").document(hotel.getId());
        DocumentReference userRef = db.collection("users").document(userId);

        if(likesList.contains(userId)){
            hotelRef.update("likesList", FieldValue.arrayRemove(userId));
            userRef.update("likes", FieldValue.arrayRemove(hotelRef));
            likesList.remove(userId);
        } else {
            hotelRef.update("likesList", FieldValue.arrayUnion(userId));
            userRef.update("likes", FieldValue.arrayUnion(hotelRef));
            likesList.add(userId);
        }
        hotelAdapter.notifyItemChanged(position);
    }

    private void getHotelRating(DocumentReference documentRef, Hotel hotel){
        documentRef.collection("review").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            int count =0;
                            float rate = 0;
                            for(DocumentSnapshot document : task.getResult()){
                                double rating = document.getDouble("rating");
                                rate += rating;
                                count++;
                            }
                            if(count > 0){
                                hotel.setRating(rate/count);
                                hotel.setTotalRate(count);
                            }
                            hotelAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }


    /**********************************************************************************************************************/



    /******************************** Restaurant *******************************************************************************/


    private void getResData(){
        db.collection("restaurants")
                .limit(15).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            resAdapter = new ResAdapter(getContext(), restaurants);
                            resAdapter.setRestaurantClick(restaurantClickListener);

                            for(DocumentSnapshot document : task.getResult()){
                                Restaurant restaurant = document.toObject(Restaurant.class);

                                GeoPoint geoPoint = document.getGeoPoint("geopoint");
                                restaurant.setLatitude(geoPoint.getLatitude());
                                restaurant.setLongitude(geoPoint.getLongitude());

                                restaurants.add(restaurant);
                                getRestaurantRating(document.getReference(), restaurant);
                            }
                            rcvRestaurant.setAdapter(resAdapter);
                        }
                    }
                });
    }

    private ResAdapter.RestaurantClick restaurantClickListener = new ResAdapter.RestaurantClick() {
        @Override
        public void itemClick(int position) {
            Restaurant restaurant = restaurants.get(position);
            Intent intent = new Intent(getContext(), RestaurantDetail.class);
            intent.putExtra("restaurant", restaurant);
            startActivity(intent);
        }

        @Override
        public void likeClick(int position) {
            Restaurant restaurant = restaurants.get(position);
            likeRestaurant(restaurant, position);
        }
    };

    private void likeRestaurant(Restaurant restaurant, int position){
        String userId = mAuth.getUid();
        ArrayList<String> likesList = restaurant.getLikesList();

        DocumentReference restaurantRef = db.collection("restaurants").document(restaurant.getId());
        DocumentReference userRef = db.collection("users").document(userId);

        if(likesList.contains(userId)){ // unlike
            restaurantRef.update("likesList", FieldValue.arrayRemove(userId));
            userRef.update("likes", FieldValue.arrayRemove(restaurantRef));
            likesList.remove(userId);
        } else { // like
            restaurantRef.update("likesList", FieldValue.arrayUnion(userId));
            userRef.update("likes", FieldValue.arrayUnion(restaurantRef));
            likesList.add(userId);
        }
        resAdapter.notifyItemChanged(position);

    }

    private void getRestaurantRating(DocumentReference documentRef, Restaurant restaurant){
            documentRef.collection("review").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                int count =0;
                                float rate = 0;
                                for(DocumentSnapshot document : task.getResult()){
                                    double rating = document.getDouble("rating");
                                    rate += rating;
                                    count++;
                                }
                                if(count > 0){
                                    restaurant.setRating(rate/count);
                                    restaurant.setTotalRate(count);
                                }
                                resAdapter.notifyDataSetChanged();
                            }
                        }
                    });
    }


    /*******************************************************************************************************************/

}