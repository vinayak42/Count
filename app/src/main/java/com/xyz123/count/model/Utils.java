package com.xyz123.count.model;

import android.app.Application;
import android.widget.Toast;

import com.xyz123.count.view.CounterRepository;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Utils {

    private static final Utils ourInstance = new Utils();
//    private ArrayList<Counter> counterArrayList;

    public static Utils getInstance() {
        return ourInstance;
    }

    private Utils() {
    }

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private GoogleSignInClient googleSignInClient;
    private CounterRepository counterRepository;
    private Application application;

    public void setGoogleSignInClient(GoogleSignInClient googleSignInClient) {
        this.googleSignInClient = googleSignInClient;
    }

//    public void setCounterList(ArrayList<Counter> counterArrayList) {
//        this.counterArrayList = counterArrayList;
//    }



    public FirebaseFirestore getDb() {
        return db;
    }

    public void setDb(FirebaseFirestore db) {
        this.db = db;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    public void init(Application application) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        counterRepository = new CounterRepository(application);
        this.application = application;
    }

    public void signOut() {
        counterRepository.deleteAllCounters();
        googleSignInClient.signOut();
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(application, "Logged out!", Toast.LENGTH_SHORT).show();
    }

//    public ArrayList<Counter> getCounterList() {
//        return this.counterArrayList;
//    }
}
