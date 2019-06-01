package com.example.count.controller;

import android.content.Context;
import android.widget.Toast;

public abstract class Notifiable {
    protected Context context;

    public Notifiable(Context context) {
        this.context = context;
    }

    public void makeToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public abstract void notify(int resultCode, String message);

    public void requestLogin() {

    }
}
