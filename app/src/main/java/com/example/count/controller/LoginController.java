package com.example.count.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.count.view.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public final class LoginController extends Notifiable {

    private GoogleSignInClient googleSignInClient;
    private LoginActivity loginActivity;
    private static final int RC_SIGN_IN = 100;

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        loginActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public LoginController(Context context) {
        super(context);
        loginActivity = (LoginActivity)context;
        googleSignInClient = loginActivity.getGoogleSignInClient();
    }

    @Override
    public void notify(int resultCode, String message) {

    }
}
