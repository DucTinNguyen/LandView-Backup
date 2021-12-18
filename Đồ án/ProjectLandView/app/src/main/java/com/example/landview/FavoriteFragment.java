package com.example.landview;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.landview.Area.Area;
import com.example.landview.Favorite.FavoriteItemAdapter;
import com.example.landview.HomeFragmentSection.Reviews.ItemReview;
import com.example.landview.HomeFragmentSection.Reviews.ItemReviewAdapter;
import com.example.landview.HomeFragmentSection.Reviews.Review;
import com.example.landview.Hotel.Hotel;
import com.example.landview.LandScape.Landscape;
import com.example.landview.Place.Place;
import com.example.landview.Restaurant.Restaurant;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavoriteFragment extends Fragment {
    private static final String TAG = "FavoriteFragment";

    RecyclerView favoriteRecyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    LinearLayout emptyList;
    EditText edt;
    Button goBtn;

    FavoriteItemAdapter favAdapter;
    ArrayList<Place> placeList;

    // Tạo sẵn một số collectionRef để dỡ mỏi tay gõ
    private CollectionReference areaColl = db.collection("areas");
    private CollectionReference landscapeColl = db.collection("landscapes");
    private CollectionReference hotelColl = db.collection("hotels");
    private CollectionReference resColl = db.collection("restaurants");

    private LinearLayout lnlFavContainer;

    private void initUi(View view){
        emptyList = view.findViewById(R.id.emptyList);

        edt = view.findViewById(R.id.edtSearch);
        goBtn = view.findViewById(R.id.goBtn);

        lnlFavContainer = view.findViewById(R.id.lnl_fav_container);
        favoriteRecyclerView = view.findViewById(R.id.recvFav);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite,container,false);

        initUi(view);
        favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        placeList = new ArrayList<>();

        //Chuyển ->
        edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Review.class);
                getContext().startActivity(intent);
            }
        });

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new TravelFragment();
                loadFragment(fragment);
                Toast.makeText(getContext(), "Go click", Toast.LENGTH_SHORT).show();
            }
        });

        //setup 1 recycler view cho favorite fragment
       getFavoriteList();

       return view;
    }

    /****************************************Lấy các DocumentReferent*********************************************/
    private void getFavoriteList(){
        db.collection("users")
                .document(getCurrentUserId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    DocumentSnapshot document = task.getResult();

                    if(document.exists() && document != null){

                        List<DocumentReference> group = (List<DocumentReference>) document.get("likes");

                        if(group.size() == 0){

                            emptyList.setVisibility(View.VISIBLE);
                            lnlFavContainer.setVisibility(View.GONE);
                        } else {

                            emptyList.setVisibility(View.GONE);
                            lnlFavContainer.setVisibility(View.VISIBLE);

                            getFavoritePlace(group);
                        }
                    }
                } else {

                    emptyList.setVisibility(View.VISIBLE);
                    lnlFavContainer.setVisibility(View.GONE);
                }
            }
        });
    }

    /************************************************************************************************/

    /****************************** Lấy DocumentSnapshot ********************************************/

    private void getFavoritePlace(List<DocumentReference> group){

        favAdapter = new FavoriteItemAdapter(getContext(), placeList, 1);
        favAdapter.setFavoriteItemCLick(favoriteItemClickListener);
        favoriteRecyclerView.setAdapter(favAdapter);

        for(DocumentReference docRef : group){
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){

                        DocumentSnapshot document = task.getResult();

                        if(document!= null && document.exists()){
                            Place place = document.toObject(Place.class);

                            // document.getReference() trả về một DocumentReference
                            // DocumentReference thật ra là một string
                            // ví dụ: DocumentReference <=> "hotels/6m8FHGCCD9DnYlomxJHm"

                            // getPath(): sẽ trả về một string như trên
                            // Ta lưu path này dể dùng cho getRating()
                            place.setPath(document.getReference().getPath());
                            placeList.add(place);

                            getRating(place);
                        }
                        for(Place place : placeList){
                            Log.d(TAG, "GetPlace: " + place.toString());
                        }

                    }
                }
            });
        }
    }

    /************************************************************************************************/


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
                            favAdapter.notifyDataSetChanged();
                        }
                    });
        }

    }

    /************************************************************************************************/


    // Favorite Item click

    private FavoriteItemAdapter.FavoriteItemClick favoriteItemClickListener = new FavoriteItemAdapter.FavoriteItemClick() {

        // Khi người dùng click vào item
        @Override
        public void itemClick(int position) {

            Place place = placeList.get(position);

            // Do place ta dùng chung cho hotel, landscape, restaurant, area nên ta
            // phân biệt thông qua type
            switch (place.getType()){
                case "area":
                    startAreaDetail(place.getId());
                    break;
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

        // Khi người dùng click vào tym
        @Override
        public void unlikeClick(int position) {
            Place place = placeList.get(position);
            unlikePlace(place, position);
        }
    };

    /*************************** UNLIKE **********************************************88******************/

    private void unlikePlace(Place place, int position){
        // Lấy userId, và placeId
        String userId = mAuth.getUid();
        String placeId = place.getId();

        DocumentReference userRef = db.collection("users").document(userId);

        // Do ta tạo một DocumentReference đúng với từng loại
        DocumentReference placeRef = null;
        switch (place.getType()){
            case "area":
                placeRef = areaColl.document(placeId);
                break;
            case "landscape":
                placeRef = landscapeColl.document(placeId);
                break;
            case "hotel":
                placeRef = hotelColl.document(placeId);
                break;
            case "restaurant":
                placeRef = resColl.document(placeId);
                break;
                //  Trường hợp người quản trị data base cố tình thêm 1 type khác trên database
            default:
                Toast.makeText(getContext(), "Lỗi không thể unlike", Toast.LENGTH_SHORT).show();
                break;
        }

        // Xóa userId trên danh sách like của place
        // Do ngu nên nên đặt area là likes
        if(place.getType().equals("area")) {
            placeRef.update("likes", FieldValue.arrayRemove(userId));
        } else {
            placeRef.update("likesList", FieldValue.arrayRemove(userId));
        }

        // xóa DocumentReference trên danh sánh của user
        userRef.update("likes", FieldValue.arrayRemove(placeRef));

        // Xóa place khỏi list hiện tại.
        placeList.remove(place);

        // Cập nhật lại favAdapter
        favAdapter.notifyItemRemoved(position);

        // Trường hợp unlike hết thì cho sad girl xuất hiện.
        if(placeList.isEmpty()){
            lnlFavContainer.setVisibility(View.GONE);
            emptyList.setVisibility(View.VISIBLE);
        }
    }

    /************************************************************************************************/

    /*************************** startDetail Activity ***************************************************************/

    private void startAreaDetail(String id){
        areaColl.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

                        Intent intent = new Intent(getContext(), DetailArea.class);
                        intent.putExtra("area", area);
                        startActivity(intent);
                    }
                }
            }
        });

    }

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

                        Intent intent = new Intent(getContext(), LandScapeDetail.class);
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

                        Intent intent = new Intent(getContext(), HotelDetail.class);
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

                        Intent intent = new Intent(getContext(), RestaurantDetail.class);
                        intent.putExtra("restaurant", restaurant);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    /************************************************************************************************/



    private void loadFragment(Fragment fragment){
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = getParentFragmentManager();

        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) {

            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.layoutFrame, fragment, fragmentTag);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.addToBackStack(backStateName);
            fragmentTransaction.commit();
        }
    }


    private String getCurrentUserId(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String id = currentUser.getUid();
        return id;
    }
}