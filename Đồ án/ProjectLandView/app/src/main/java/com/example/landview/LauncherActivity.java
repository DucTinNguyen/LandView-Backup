package com.example.landview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LauncherActivity extends AppCompatActivity {
    private Button buttonSignup,buttonLogin;
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.layout_launcher);
        //ánh xạ view
        buttonSignup = findViewById(R.id.btn_signup);
        buttonLogin = findViewById(R.id.btn_Login);

        //đổi action bar label
//        getSupportActionBar().setTitle("Welcome to LandView.");

        //sự kiện button
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignupActivity();
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginActivity();
            }
        });
    }

    private void showLoginActivity() {
        Intent intent = new Intent(LauncherActivity.this,LoginActivity.class);
        startActivity(intent);

    }

    private void showSignupActivity() {
        Intent intent = new Intent(LauncherActivity.this,SignUpActivity.class);
        startActivity(intent);

    }
}
