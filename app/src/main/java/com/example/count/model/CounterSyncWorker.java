package com.example.count.model;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.count.view.Counter;
import com.example.count.view.CounterRepository;
import com.example.count.view.SettingsActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CounterSyncWorker extends Worker {

    public CounterSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        FirebaseFirestore db = Utils.getInstance().getDb();
        FirebaseUser user = Utils.getInstance().getUser();
        CounterRepository counterRepository = new CounterRepository((Application) getApplicationContext());
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
                        documentReference.update(updationObject);
                    }
                }
            });
        }
        return Result.success();
    }
}
