package com.example.landview.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.landview.Place.Place;
import com.example.landview.R;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class NearbyAndMapFragment extends Fragment implements GoogleMap.OnMarkerClickListener {

    private static final String TAG = "NearbyAndMap";
    private static final String LATITUDE= "latitude";
    private static final String LONGITUDE ="longitude";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public NearbyAndMapFragment() {
        // Required empty public constructor
    }
    private double latitude;
    private double longitude;

    private ArrayList<Place> placesOnMap;

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

    private Bitmap blueHotel;
    private Bitmap blueLandscape;
    private Bitmap blueRestaurant;

    private Bitmap redHotel;
    private Bitmap redLandscape;
    private Bitmap redRestaurant;

    private void createBitmap(){
        blueHotel = getBitmapFromVectorDrawable(getContext(), R.drawable.hotel_blue_24);
        blueLandscape = getBitmapFromVectorDrawable(getContext(), R.drawable.landscape_blue_24);
        blueRestaurant = getBitmapFromVectorDrawable(getContext(), R.drawable.restaurant_blue_24);

        redHotel = getBitmapFromVectorDrawable(getContext(), R.drawable.hotel_red_24);
        redLandscape = getBitmapFromVectorDrawable(getContext(), R.drawable.landscape_red_24);
        redRestaurant = getBitmapFromVectorDrawable(getContext(),R.drawable.red_restaurant_24);

    }

    public static NearbyAndMapFragment newInstance(double latitude, double longitude) {
        NearbyAndMapFragment fragment = new NearbyAndMapFragment();
        Bundle args = new Bundle();
        args.putDouble(LATITUDE, latitude);
        args.putDouble(LONGITUDE, longitude);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            latitude = getArguments().getDouble(LATITUDE);
            longitude = getArguments().getDouble(LONGITUDE);
        }
    }

    MapView mapView;
    GoogleMap googleMap;
    ArrayList<Marker> markers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nearby_and_map, container, false);

        createBitmap();
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();

        mapView = view.findViewById(R.id.mp_map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try{
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e){

        }

        placesOnMap = new ArrayList<>();

        findPlaceOnMap();




        return view;
    }

    public OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap map) {
            googleMap = map;
            map.getUiSettings().setScrollGesturesEnabled(false);
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng latLng) {
                    Toast.makeText(getContext(), "Map click detected", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), MapActivity.class);
                    intent.putExtra("places", placesOnMap);
                    startActivity(intent);

                }
            });


            try{
                boolean success = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_json));
                markers = new ArrayList<>();
                for(int i=0 ;i < placesOnMap.size();i++){
                    Place place = placesOnMap.get(i);
                    LatLng placePos = new LatLng(place.getLatitude(), place.getLongitude());
                    Marker marker1 = map.addMarker(new MarkerOptions().position(placePos).title(place.getName()));
                    marker1.setTag(i);
                    markers.add(marker1);
                    if(i==0){
                        setRedIcon(placesOnMap.get(0).getType(),marker1);
                    } else {
                        setBlueIcon(placesOnMap.get(i).getType(), marker1);
                    }

                }

                LatLng currPos = new LatLng(latitude, longitude);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(currPos, 15));


                if(!success){
                    Log.d(TAG, "onMapReady: Style parsing fail" );
                }
            } catch (Exception e){
                Log.d(TAG, "onMapReady: "  + e.getMessage());

            }
        }
    };


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


    private void findPlaceOnMap(){
        int radius = 1000; //1000 m = 1km
        GeoLocation center =new GeoLocation(latitude, longitude);

        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center, radius);
        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();

        for(GeoQueryBounds b : bounds){
            Query q = db.collection("hotels")
                    .orderBy("geohash")
                    .startAt(b.startHash).endAt(b.endHash);
            tasks.add(q.get());
        }
        for(GeoQueryBounds b : bounds){
            Query q = db.collection("landscapes")
                    .orderBy("geohash")
                    .startAt(b.startHash).endAt(b.endHash);
            tasks.add(q.get());
        }
        for(GeoQueryBounds b : bounds){
            Query q = db.collection("restaurants")
                    .orderBy("geohash")
                    .startAt(b.startHash).endAt(b.endHash);
            tasks.add(q.get());
        }

        Tasks.whenAllComplete(tasks).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
            @Override
            public void onComplete(@NonNull Task<List<Task<?>>> t) {

                ArrayList<DocumentSnapshot> matchingDocuments = new ArrayList<>();
                for (Task<QuerySnapshot> task : tasks) {

                    QuerySnapshot snapshot = task.getResult();

                    for (DocumentSnapshot document : snapshot.getDocuments()) {

                        GeoPoint geoPoint = document.getGeoPoint("geopoint");
                        GeoLocation location = new GeoLocation(geoPoint.getLatitude(), geoPoint.getLongitude());

                        double distance = GeoFireUtils.getDistanceBetween(center, location);

                        if (distance <= radius) {
                            matchingDocuments.add(document);
                        }

                    }
                }


                for(DocumentSnapshot document : matchingDocuments){
                    Place place = document.toObject(Place.class);
                    GeoPoint geoPoint = document.getGeoPoint("geopoint");

                    place.setLatitude(geoPoint.getLatitude());
                    place.setLongitude(geoPoint.getLongitude());

                    place.setPath(document.getReference().getPath());
                    Log.d(TAG, "SelectedPlace: " + place.toString());
                    if(place.getLatitude() == latitude && place.getLongitude() == longitude) {
                        placesOnMap.add(0,place);
                    } else {
                        placesOnMap.add(place);
                    }

                }
                mapView.getMapAsync(onMapReadyCallback);


                for(Place place : placesOnMap){
                    Log.d(TAG, "PlaceOnMap: " + place.toString());
                }
            }
        });

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {

        Toast.makeText(getContext(), "Marker: ", Toast.LENGTH_SHORT).show();
        return false;
    }
}