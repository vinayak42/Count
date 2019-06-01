package com.example.count.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.count.R;

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
                // TODO complete this method based on logged in status
                return LoginActivity.class;
            }
        },delayDuration);
    }
}
