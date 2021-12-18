package com.example.landview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.landview.Hotel.Hotel;
import com.example.landview.Restaurant.Restaurant;

public class HotelResDetail extends AppCompatActivity {
    private ImageView background,imgrate;
    private TextView name,numberRate,web,call,email,price,description,address;
    private Button btnSee,btnBook;
    private Bundle bundle;
    private Hotel item1;
    private Restaurant item2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_hotel_res_detail);
        //ánh xạ view
        InitUI();
        //nhận và sét dữ liệu
        bundle = getIntent().getExtras();
        if(bundle == null)
        {
            return;
        }
        else if (bundle.containsKey("Hotel"))
        {
            item1 = (Hotel) bundle.get("Hotel");
            setDataFromBundelHotel();
            //sét sự kiện cho các view
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GotoWebPageHotel();
                }
            };
            web.setOnClickListener(listener);
            call.setOnClickListener(listener);
            email.setOnClickListener(listener);
            btnBook.setOnClickListener(listener);
            btnSee.setOnClickListener(listener);
        }
        else if(bundle.containsKey("Restaurant"))
        {
            item2 = (Restaurant) bundle.get("Restaurant");
            setDataFromBundelRestaurant();
            //sét sự kiện
            View.OnClickListener listener2 = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GotoWebPageRes();
                }
            };
           web.setOnClickListener(listener2);
            call.setOnClickListener(listener2);
            email.setOnClickListener(listener2);
            btnBook.setOnClickListener(listener2);
            btnSee.setOnClickListener(listener2);
        }
    }

    private void GotoWebPageHotel() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
       // intent.setData(Uri.parse(item1.getmLink()));
        startActivity(intent);
    }
    private void GotoWebPageRes() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
      //  intent.setData(Uri.parse(item2.getmLink()));
        startActivity(intent);
    }
    private void setDataFromBundelHotel() {
        /*background.setImageResource(item1.getmBackground());
        imgrate.setImageResource(item1.getmRate());
        name.setText(item1.getmName());
        numberRate.setText(item1.getNumberRate());
        price.setText(item1.getmPrice());
        description.setText(item1.getmIntro());
        address.setText(item1.getmAddress());*/
    }
    private void setDataFromBundelRestaurant() {
       /* background.setImageResource(item2.getmBackground());
        imgrate.setImageResource(item2.getmRate());
        name.setText(item2.getmName());
        numberRate.setText(item2.getNumberRate());
        price.setText(item2.getmPrice());
        description.setText(item2.getmIntro());
        address.setText(item2.getmAddress());*/
    }
    private void InitUI() {
        background = findViewById(R.id.imgView);
        imgrate = findViewById(R.id.imgRate);
        name = findViewById(R.id.txtName);
        numberRate = findViewById(R.id.txtNumberate);
        web = findViewById(R.id.web);
        call = findViewById(R.id.call);
        email = findViewById(R.id.email);
        price = findViewById(R.id.txtPrice);
        description = findViewById(R.id.txtDes);
        address = findViewById(R.id.txtAddress);
        btnSee = findViewById(R.id.btnSeePro);
        btnBook = findViewById(R.id.btnBook);
    }
}