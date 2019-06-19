package com.example.count.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.count.R;
import com.example.count.model.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CounterActivity extends AppCompatActivity {

    // TODO show goal if present
    private TextView titleTextView, valueTextView, creationDateTextView, lastUpdationTextView;
    private Button incrementButton, decrementButton;
    private Counter counter;
//    private DocumentReference counterReference;
    private ImageView incrementImageView;
    private ImageView decrementImageView;
    private ImageView deleteImageView;
    private CounterRepository counterRepository;
    private DocumentReference counterReference;
    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        counterRepository = new CounterRepository(getApplication());
        db = FirebaseFirestore.getInstance();
        user = Utils.getInstance().getUser();

        titleTextView = (TextView) findViewById(R.id.counter_activity_title);
        valueTextView = (TextView) findViewById(R.id.counter_activity_value);
        creationDateTextView = (TextView) findViewById(R.id.counter_activity_creation_date_text_view);
        lastUpdationTextView = (TextView) findViewById(R.id.counter_activity_last_updated_text_view);
        incrementButton = (Button) findViewById(R.id.counter_activity_increment_button);
        decrementButton = (Button) findViewById(R.id.counter_activity_decrement_button);
        incrementImageView = (ImageView) findViewById(R.id.incrementImageView);
        decrementImageView = (ImageView) findViewById(R.id.decrementImageView);
        deleteImageView = (ImageView) findViewById(R.id.deleteImageView);

        counter = (Counter) getIntent().getExtras().get("counter");

        titleTextView.setText(counter.getTitle());
        valueTextView.setText(String.valueOf(counter.getValue()));
        creationDateTextView.setText(counter.getCreationTimestamp().toString());
        lastUpdationTextView.setText(counter.getLastUpdationTimestamp().toString());

        final String counterId = counter.getId();

        counterReference = db.collection("users").document(user.getUid()).collection("counters").document(counterId);

        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increment();
            }
        });

        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrement();
            }
        });

        incrementImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increment();
            }
        });

        decrementImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrement();
            }
        });

        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                counterRepository.delete(counter);
//                counterReference.delete();
//                finish();

                new AlertDialog.Builder(CounterActivity.this)
                        .setTitle("Delete counter")
                        .setMessage("Are you sure you want to delete this counter?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                counterRepository.delete(counter);
                                counterReference.delete();
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

    }

    private void increment() {
        valueTextView.setText(String.valueOf(counter.getValue() + 1));
        counter.setValue(counter.getValue() + 1);
        counter.setLastUpdationTimestamp(new Date());
        counterRepository.update(counter);
    }

    private void decrement() {
        valueTextView.setText(String.valueOf(counter.getValue() - 1));
        counter.setValue(counter.getValue() - 1);
        counter.setLastUpdationTimestamp(new Date());
        counterRepository.update(counter);
    }
}
