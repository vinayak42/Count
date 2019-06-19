package com.example.count.view;

import android.content.DialogInterface;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    private Button syncButton;
    private Button deleteAllCountersButton;

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
            }
        });
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}