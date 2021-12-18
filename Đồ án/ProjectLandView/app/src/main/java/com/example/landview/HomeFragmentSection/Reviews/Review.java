package com.example.landview.HomeFragmentSection.Reviews;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.landview.Area.Area;
import com.example.landview.DetailArea;
import com.example.landview.Favorite.FavoriteItemAdapter;
import com.example.landview.Hotel.Hotel;
import com.example.landview.HotelDetail;
import com.example.landview.LandScape.Landscape;
import com.example.landview.LandScapeDetail;
import com.example.landview.Place.Place;
import com.example.landview.R;
import com.example.landview.Restaurant.Restaurant;
import com.example.landview.RestaurantDetail;
import com.example.landview.Utils.StringUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//class này là phần search nhưng chưa đổi tên
public class Review extends AppCompatActivity {
    EditText edtSearch;
    ImageView iconSearch;
    RecyclerView recyclerView;
    List<ItemReview> list;

    FavoriteItemAdapter adapter;
    ArrayList<Place> placeList;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference areaColl = db.collection("areas");
    private CollectionReference landscapeColl = db.collection("landscapes");
    private CollectionReference hotelColl = db.collection("hotels");
    private CollectionReference resColl = db.collection("restaurants");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_review);
        //ánh xạ view
        edtSearch = findViewById(R.id.edtSearch);
        iconSearch = findViewById(R.id.iconSearch);
        recyclerView = findViewById(R.id.recvSearch);

        list = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();

        placeList = new ArrayList<>();

        placeList = getlistItemReviews();

        //đổ dữ liệu vào recycleview
        adapter = new FavoriteItemAdapter(Review.this, placeList, 2);
        adapter.setFavoriteItemCLick(favoriteItemClickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Review.this, LinearLayoutManager.VERTICAL, false));


        //Hàm bắt các sự kiện search
        searchActivity();
    }
    //Hàm này lấy vài landscape để show trưng
    private ArrayList<Place> getlistItemReviews() {
        db.collection("landscapes").limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot dc : task.getResult()){
                        Place placeTemp = dc.toObject(Place.class);
                        placeTemp.setPath(dc.getReference().getPath());
                        placeList.add(placeTemp);

                        getRating(placeTemp);
                    }
                }else{}
            }
        });

        return placeList;
    }


    //Hàm này query để show những chỗ liên quan đến phần từ được search
    private void searchActivity(){

        iconSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeList.clear();
                placeList.addAll(filter(edtSearch.getText().toString()));
                adapter.notifyDataSetChanged();
            }
        });

        edtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if((keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (keyEvent.getAction() == KeyEvent.ACTION_DOWN)){
                    placeList.clear();
                    placeList.addAll(filter(edtSearch.getText().toString()));
                    adapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });
    }

    private ArrayList<Place> filter(String s){
        String queryText = StringUtils.removeAccent(s.toLowerCase()); //removeAccent để chuyển chuỗi tiếng việt có dấu về không dấu

        //Query bằng condition EQUAL trước, add vào đầu danh sách
        db.collection("landscapes").whereEqualTo("landscapeName", queryText).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        if(document.exists()){
                            Place placeTemp = document.toObject(Place.class);
                            placeTemp.setPath(document.getReference().getPath());
                            placeList.add(placeTemp);

                            getRating(placeTemp);
                        }else{}
                    }
                }
            }
        });
        db.collection("areas").whereEqualTo("name", queryText).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        if(document.exists()) {
                            Place placeTemp = document.toObject(Place.class);
                            placeTemp.setPath(document.getReference().getPath());
                            placeList.add(placeTemp);

                            getRating(placeTemp);
                        }else{}
                    }
                }
            }
        });

        //sau đó thì mới dùng condition less than or equal to để các trường hợp không gõ hết tên địa danh
        // thì firebase có thể show ra những trường hợp có liên quan
        db.collection("landscapes").whereLessThanOrEqualTo("landscapeName", queryText).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        Place placeTemp = document.toObject(Place.class);
                        placeTemp.setPath(document.getReference().getPath());

                        if(listItemDuplicateCheck(placeTemp)){ //check duplicate
                            placeList.add(placeTemp);

                            getRating(placeTemp);
                        }else{}
                    }
                }
            }
        });
        db.collection("areas").whereLessThanOrEqualTo("name", queryText).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        Place placeTemp = document.toObject(Place.class);
                        placeTemp.setPath(document.getReference().getPath());
                        if(listItemDuplicateCheck(placeTemp)){ //check duplicate
                            placeList.add(placeTemp);

                            getRating(placeTemp);
                        }else{}
                    }
                }
            }
        });
        return placeList;
    }

    //Hàm này sẽ kiểm tra xem trong list có trùng phần tử hay không
    //do thực hiện 2 lần query bằng 2 condition khác nhau cho mỗi Collection
    //trường hợp condition equal to nếu đã thêm vào list thì kiểu gì condition less than or equal to cũng sẽ thêm lại phần tử đó vào list
    private boolean listItemDuplicateCheck(Place temp){
        for(Place placeTemp: placeList){
            if(placeTemp.getPath().equals(temp.getPath())){
                return false;
            }
        }
       return true;
    }

    private FavoriteItemAdapter.FavoriteItemClick favoriteItemClickListener = new FavoriteItemAdapter.FavoriteItemClick() {
        @Override
        public void itemClick(int position) {
            Place temp = placeList.get(position);
            switch (temp.getType()){
                case "area":
                    areaColl.document(temp.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists() && document != null){
                                //
                                Area area = document.toObject(Area.class);
                                GeoPoint geoPoint = document.getGeoPoint("geopoint");

                                area.setLatitude(geoPoint.getLatitude());
                                area.setLongitude(geoPoint.getLongitude());

                                Intent intent = new Intent(Review.this, DetailArea.class);
                                intent.putExtra("area", area);
                                startActivity(intent);
                            }
                        }
                    }
                });

                    break;
                case "landscape":
                    landscapeColl.document(temp.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if(document.exists() && document != null){
                                    Landscape landscape = document.toObject(Landscape.class);
                                    GeoPoint geoPoint = document.getGeoPoint("geopoint");

                                    landscape.setLatitude(geoPoint.getLatitude());
                                    landscape.setLongitude(geoPoint.getLongitude());

                                    Intent intent = new Intent(Review.this, LandScapeDetail.class);
                                    intent.putExtra("landscape", landscape);
                                    startActivity(intent);
                                }
                            }
                        }
                    });
                    break;
                case "restaurant":
                    hotelColl.document(temp.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if(document.exists() && document != null){
                                    Hotel hotel = document.toObject(Hotel.class);
                                    GeoPoint geoPoint = document.getGeoPoint("geopoint");

                                    hotel.setLatitude(geoPoint.getLatitude());
                                    hotel.setLongitude(geoPoint.getLongitude());

                                    Intent intent = new Intent(Review.this, HotelDetail.class);
                                    intent.putExtra("hotel", hotel);
                                    startActivity(intent);
                                }
                            }
                        }
                    });
                    break;
                case "hotel":
                    resColl.document(temp.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if(document.exists() && document != null){
                                    Restaurant restaurant = document.toObject(Restaurant.class);
                                    GeoPoint geoPoint = document.getGeoPoint("geopoint");

                                    restaurant.setLatitude(geoPoint.getLatitude());
                                    restaurant.setLongitude(geoPoint.getLongitude());

                                    Intent intent = new Intent(Review.this, RestaurantDetail.class);
                                    intent.putExtra("restaurant", restaurant);
                                    startActivity(intent);
                                }
                            }
                        }
                    });
                    break;
            }

        }

        @Override
        public void unlikeClick(int position) { }
    };

    /************************************** Lấy rating ******************************************/

    private void getRating(Place place){
        String type = place.getType();

        // Ta không lấy rating của area do nó éo có
        if(!(type.equals("area"))){

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

    }

}
