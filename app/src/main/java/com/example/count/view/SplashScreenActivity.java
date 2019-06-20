package com.example.count.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.example.count.R;

public class SplashScreenActivity extends AppCompatActivity {

    private final String NOTIFICATION_CHANNEL_ID = "counterNotification";

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = NOTIFICATION_CHANNEL_ID;
            String description = "";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        createNotificationChannel();

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
