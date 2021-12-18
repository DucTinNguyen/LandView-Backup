package com.example.landview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.landview.WeatherAdapter;
import com.example.landview.WeatherModels;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Weathernext extends AppCompatActivity {
    private String city;
    private ListView lv;
    private WeatherModels weatherModel;
    private WeatherAdapter weatherAdapter;
    private List<WeatherModels>weatherList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_weather_next);

        //
        lv = findViewById(R.id.lvWeathers);

        Intent intent = getIntent();
        city = intent.getStringExtra("city");

        weatherList = new ArrayList<>();
        getJSONNextWeather(city);
    }

    private void getJSONNextWeather(String city) {
        //xử lí api
        String url = "https://api.openweathermap.org/data/2.5/forecast?q="+city+"&appid=57bac8d8bf773db943c0a945fb404977&units=metric";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray list = response.getJSONArray("list");
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject listItem = list.getJSONObject(i);
                                //lấy ngày
                                String date = listItem.getString("dt");
                                Long day = Long.parseLong(date);
                                Date datenow = new Date(day*1000);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE,dd/MM/yyyy HH:mm:ss");
                                String currentDay= simpleDateFormat.format(datenow);
                                //lấy nhiệt độ
                                JSONObject main = listItem.getJSONObject("main");
                                String temp = main.getString("temp");
                                //lấy tình hình thời tiết
                                JSONArray weather = listItem.getJSONArray("weather");
                                JSONObject weatherItem = weather.getJSONObject(0);
                                String description = weatherItem.getString("description");
                                String icon = weatherItem.getString("icon");
                                String urlIcon = "http://openweathermap.org/img/wn/"+icon+".png";

                                //add list
                                weatherList.add(new WeatherModels(currentDay,temp,description,urlIcon));

                            }
                            weatherAdapter = new WeatherAdapter(getApplicationContext(),R.layout.layout_weather_next_item,weatherList);
                            lv.setAdapter(weatherAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(jsonObjectRequest);

    }
}