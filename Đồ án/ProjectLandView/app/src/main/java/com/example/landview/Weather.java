package com.example.landview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.landview.HomeFragmentSection.DetailReview.ImageFragment.Image;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Weather extends AppCompatActivity {
    static final String API_KEY = "57bac8d8bf773db943c0a945fb404977";
    private EditText edtCity;
    private ImageView imgStatus;
    private Button btnFind,btnNextday;
    private TextView txtNameCity,txtNameNation,txtDegree,txtStatus,txtCloud,txtRainy,txtwind,txtNowDegree;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_weather);
        //initUi
        initUI();
        //

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textCity = edtCity.getText().toString().trim();
                checkInput(textCity);
                getJsonWeather(textCity);

            }
        });
        btnNextday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = edtCity.getText().toString().trim();
                checkInput(text);
                Intent intent = new Intent(getApplicationContext(),Weathernext.class);
                intent.putExtra("city",text);
                startActivity(intent);
            }
        });
    }

    public void checkInput(String text)
    {
        if(text.length()==0)
        {
            Toast.makeText(getApplicationContext(),"Please type city",Toast.LENGTH_LONG).show();
        }
    }
    public void getJsonWeather(String city)
    {
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+API_KEY+"&units=metric";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                            Lấy thông tin weather : icon,trạng thái
                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject weatherObj = weatherArray.getJSONObject(0);
                            String icon = weatherObj.getString("icon");
                            String urlIcon = "http://openweathermap.org/img/wn/"+icon+".png";
                            Picasso.get().load(urlIcon).into(imgStatus);

                            String weatherStatus = weatherObj.getString("main");
                            txtStatus.setText(weatherStatus);
//                            lấy thông tin main: nhiệt độ,độ ẩm
                            JSONObject tempObj =response.getJSONObject("main");
                            String temp = tempObj.getString("temp");
                            txtDegree.setText(temp+"°C");
                            String humidity = tempObj.getString("humidity");
                            txtRainy.setText(humidity+"%");
                            // Lấy thông tin wind: tốc độ gió
                            JSONObject wind =response.getJSONObject("wind");
                            String windSpeed = wind.getString("speed");
                            txtwind.setText(windSpeed+"m/s");
                            //lấy thông tin clouds : mây
                            JSONObject clouds = response.getJSONObject("clouds");
                            String cloud = clouds.getString("all");
                            txtCloud.setText(cloud+"%");
                            //lấy ngày nhiệt độ hiện tại
                            String date = response.getString("dt");
                            Date datenow = new Date(Long.parseLong(date)*1000);//chuyển về dạng long
                            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd--MM--yyyy HH::mm::ss");
                            String currentDate = dateFormat.format(datenow);
                            txtNowDegree.setText(currentDate);
                            //lấy thông tin tên thành phố
                            String city = response.getString("name");
                            txtNameCity.setText(city);
                            //lấy thông tin nước
                            JSONObject sys = response.getJSONObject("sys");
                            String nation = sys.getString("country");
                            txtNameNation.setText(nation);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Don't find city!",Toast.LENGTH_LONG).show();
                        edtCity.setText("");
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
    private void initUI() {
        edtCity = findViewById(R.id.edtCountry);
        btnFind = findViewById(R.id.btnFindCity);
        btnNextday = findViewById(R.id.btnMore);
        txtNameCity = findViewById(R.id.txtWname);
        txtNameNation = findViewById(R.id.txtWnation);
        txtDegree = findViewById(R.id.txtDegree);
        txtStatus = findViewById(R.id.txtStatus);
        txtCloud = findViewById(R.id.txtCloud);
        txtRainy = findViewById(R.id.txtRainy);
        txtwind = findViewById(R.id.txtwind);
        txtNowDegree = findViewById(R.id.txtnow);
        imgStatus = findViewById(R.id.imgStatus);
    }
}

