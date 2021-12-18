package com.example.landview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class NewsWeb extends AppCompatActivity {
    private WebView web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_web);
//        ánh xạ view
        web = findViewById(R.id.web);

        Intent intent = getIntent();
        String link = intent.getStringExtra("link");
        web.loadUrl(link);
        web.setWebViewClient(new WebViewClient());
    }
}