package com.example.studentapp;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_1_DURATION = 6000;
    private static final int SPLASH_SCREEN_2_DURATION = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_1);
        new Handler().postDelayed(() -> {
            setContentView(R.layout.activity_splash_2);

            new Handler().postDelayed(() -> {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }, SPLASH_SCREEN_2_DURATION);

        }, SPLASH_SCREEN_1_DURATION);
    }
}
