package com.example.count.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.count.R;

public class CounterActivity extends AppCompatActivity {

    // TODO show goal if present

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
    }
}
