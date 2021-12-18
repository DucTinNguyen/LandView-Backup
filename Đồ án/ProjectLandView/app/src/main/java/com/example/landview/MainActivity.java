package com.example.landview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.landview.AppUser.AppUser;
import com.example.landview.Area.Area;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    //    private ActionBar actionBar;
    private BottomNavigationView bottomNavigationView;
    private static final String TAG = "MainActivity";

    // Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // Khởi tạo singleton AppUser
    private AppUser appUser = AppUser.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ánh xạ
        bottomNavigationView = findViewById(R.id.bottomNav);

        //
        createAppUser();

//        actionBar = getSupportActionBar();
        //đánh dấu mặc định
//        actionBar.setTitle("Home");




        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
               Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.layoutFrame);
               if(fragment != null){
                updateIcon(fragment);
               }
            }
        });

        loadFragment(new HomeFragment());

        //sét sự kiện item bottom
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.home:
//                        actionBar.setTitle("Home");
                        fragment = new HomeFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.travel:
//                        actionBar.setTitle("Travel");
                        fragment = new TravelFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.favorite:
//                        actionBar.setTitle("Favorite");
                        fragment = new FavoriteFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.user:
//                        actionBar.setTitle("User");
                        fragment = new UserFragment();
                        loadFragment(fragment);
                        return true;
                }
                return false;
            }
        });


    }

    private void updateIcon(Fragment fragment){
        String fragClassName = fragment.getClass().getName();
        if(fragClassName.equals(HomeFragment.class.getName())){
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
        }
        if(fragClassName.equals(TravelFragment.class.getName())){
            bottomNavigationView.getMenu().getItem(1).setChecked(true);
        }
        if(fragClassName.equals(FavoriteFragment.class.getName())){
            bottomNavigationView.getMenu().getItem(2).setChecked(true);
        }
        if(fragClassName.equals(UserFragment.class.getName())){
            bottomNavigationView.getMenu().getItem(3).setChecked(true);
        }

    }


    // Bấm back 2 lần để thoát
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        // Kiểm tra xem backstack có còn 1 fragment
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {

            // Ktra đã ấn back 2 lần chưa
            if (doubleBackToExitPressedOnce) {
                finish();
            } else {
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Click BACK again to exit", Toast.LENGTH_SHORT).show();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);

            }
        } else {
            super.onBackPressed();
        }
    }


    private void loadFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = getSupportFragmentManager();
        // Nếu fragment có trong backstack, ta tái sử dụng lại fragment đó
        //
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);
        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.layoutFrame, fragment, fragmentTag);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.addToBackStack(backStateName);
            fragmentTransaction.commit();
        }

    }

    private void createAppUser(){
        db.collection("users").document(mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists() && document != null){
                                appUser.setUserName(document.getString("username"));
                                appUser.setUID(mAuth.getUid());
                                appUser.setAvatar(document.getString("avatar"));
                            }
                        }
                    }
                });
    }
}