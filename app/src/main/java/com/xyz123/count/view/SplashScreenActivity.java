package com.xyz123.count.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.xyz123.count.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        int delayDuration=500;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Class nextActivityClass = getNextClass();
                Intent intent = new Intent(SplashScreenActivity.this, nextActivityClass);
                startActivity(intent);
                finish(); // so that the back button doesn't bring back to the SplashScreen
            }

            private Class getNextClass() {
                return LoginActivity.class;
            }
        },delayDuration);
    }
}
