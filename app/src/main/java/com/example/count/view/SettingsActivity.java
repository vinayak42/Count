package com.example.count.view;

import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceFragmentCompat;

import com.example.count.R;
import com.example.count.model.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    private static final long MILLISECONDS_IN_A_DAY = 86400000;
    private static final long MILLISECONDS_IN_AN_HOUR = 3600000;
    private Button syncButton;
    private Button deleteAllCountersButton;
    private boolean allowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        deleteAllCountersButton = (Button) findViewById(R.id.delete_counters_button);
        syncButton = (Button) findViewById(R.id.sync_button);

        deleteAllCountersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle("Delete all counters")
                        .setMessage("Are you sure to delete all counters?")
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .setPositiveButton("Yes, continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                CounterRepository counterRepository = new CounterRepository(getApplication());
                                counterRepository.deleteAllCounters();
                                FirebaseFirestore db = Utils.getInstance().getDb();
                                FirebaseUser user = Utils.getInstance().getUser();
                                db.collection("users").document(user.getUid()).collection("counters").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot documentSnapshots : task.getResult()) {
                                                documentSnapshots.getReference().delete();
                                            }
                                        }
                                    }
                                });
                                Toast.makeText(SettingsActivity.this, "Deleted all your counters!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No, go back", null).show();
            }
        });

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean connected = checkInternetConnection();
                boolean syncAllowed = checkLastSyncTimestamp();

                if (!connected) {
                    Toast.makeText(SettingsActivity.this, "Please connect to internet first.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!syncAllowed) {
                    return;
                }

                FirebaseFirestore db = Utils.getInstance().getDb();
                FirebaseUser user = Utils.getInstance().getUser();
                CounterRepository counterRepository = new CounterRepository(getApplication());
                Toast.makeText(SettingsActivity.this, "Please wait", Toast.LENGTH_SHORT).show();
                List<Counter> counterList = counterRepository.getAllCountersList();

                for (final Counter counter: counterList) {
                    final DocumentReference documentReference = db.collection("users").document(user.getUid()).collection("counters").document(counter.getId());
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Counter counterServer = documentSnapshot.toObject(Counter.class);
                            if (!counter.equals(counterServer)) {
                                Map<String, Object> updationObject = new HashMap<>();
                                updationObject.put("value", counter.getValue());
                                updationObject.put("title", counter.getTitle());
                                updationObject.put("goal", counter.getGoal());
                                updationObject.put("lastUpdationTimestamp", counter.getLastUpdationTimestamp());
                                documentReference.update(updationObject).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SettingsActivity.this, "Sync successful!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }

                updateLastSyncTimeOnServer();
            }
        });
    }

    private void updateLastSyncTimeOnServer() {
        FirebaseFirestore db = Utils.getInstance().getDb();
        FirebaseUser user = Utils.getInstance().getUser();
        DocumentReference userDocumentReference = db.collection("users").document(user.getUid());
        Map<String, Object> syncTimeUpdationObject = new HashMap<>();
        syncTimeUpdationObject.put("last_sync_timestamp", new Timestamp(new Date()));
        userDocumentReference.update(syncTimeUpdationObject);
    }

    private boolean checkLastSyncTimestamp() {
        FirebaseFirestore db = Utils.getInstance().getDb();
        FirebaseUser user = Utils.getInstance().getUser();
        DocumentReference syntTimestampReference = db.collection("users").document(user.getUid());
        syntTimestampReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        HashMap<String, Object> retreivedData = (HashMap<String, Object>) documentSnapshot.getData();
                        Timestamp lastSyncTimestamp = (Timestamp) retreivedData.get("last_sync_timestamp");
                        Date lastSyncDate = lastSyncTimestamp.toDate();
                        Date currentDate = new Date();
                        long diff = currentDate.getTime() - lastSyncDate.getTime();

                        if (diff < MILLISECONDS_IN_A_DAY) {
                            allowed = false;
                            long hoursToWait = (long)(Math.ceil(diff / (double) MILLISECONDS_IN_AN_HOUR));
                            Toast.makeText(SettingsActivity.this, "Please wait " + hoursToWait + " hour(s) before syncing again.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            allowed = true;
                        }
                    }
                }
            }
        });

        return allowed;
    }

    private boolean checkInternetConnection() {
        // reference: https://stackoverflow.com/a/4239019/5394180
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        connected = (networkInfo != null && networkInfo.isConnected());
        return connected;
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}