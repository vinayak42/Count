package com.example.count.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.count.R;
import com.example.count.model.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    // I took help from this guide https://developers.google.com/identity/sign-in/android/sign-in

    public static final String LOG_TAG = LoginActivity.class.getSimpleName();
    private static int RC_SIGN_IN = 100;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;
//    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();

//        mDatabase = FirebaseDatabase.getInstance().getReference();

        db = FirebaseFirestore.getInstance();

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });

        FirebaseUser currentUser = mAuth.getCurrentUser();
        
        if (currentUser != null) {
//            Utils.getInstance().setUser(user);
//            Utils.getInstance().setDb(db);
            Utils.getInstance().init();
            Utils.getInstance().setGoogleSignInClient(googleSignInClient);
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        }
        
        else {
            Toast.makeText(this, "Kindly login to continue", Toast.LENGTH_SHORT).show();
        }
        
        updateUI(currentUser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(LOG_TAG, "Google sign in failed", e);
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        googleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Signed out!", Toast.LENGTH_SHORT).show();
    }

    private void updateUI(FirebaseUser user) {

        this.user = user;

        if (user == null) {
            Toast.makeText(this, "[Firebase] No login detected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "[Firebase] Login detected: " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
            verifyDocumentOnFirebase();
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        }
    }

    private void verifyDocumentOnFirebase() {

        final DocumentReference userRef = db.collection("users").document(user.getUid());

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    if (documentSnapshot.exists()) {
                        // user already exists and his/her document is already there
                        Toast.makeText(LoginActivity.this, "Document exists", Toast.LENGTH_SHORT).show();
                    } else {
                        // new user has logged in so create the document and the counters subcollection too
                        Toast.makeText(LoginActivity.this, "Document not found", Toast.LENGTH_SHORT).show();
                        Map<String, Object> newUser = new HashMap<>();
                        newUser.put("uid", user.getUid());
                        newUser.put("name", user.getDisplayName());
                        newUser.put("email", user.getEmail());
                        newUser.put("counters", 0);

                        // now create a document for the newly created user
                        userRef.set(newUser)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(LoginActivity.this, "Created your document on the database", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                }
                else {
                    Log.e(LOG_TAG, "FIREBASE ERROR: " + task.getException());
                }
            }
        });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(LOG_TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(LOG_TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(LOG_TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
