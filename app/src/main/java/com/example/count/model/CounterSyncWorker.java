package com.example.count.model;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.count.R;
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

    private final String NOTIFICATION_CHANNEL_ID = "counterNotification";
    private final int NOTIFICATION_ID = 100;

    private void showNotification(boolean success) {

        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_plus_one_black_24dp)
                .setContentTitle("Sync successful")
                .setContentText("Your data was successfully uploaded to server")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

    }

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
