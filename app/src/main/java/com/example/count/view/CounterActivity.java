package com.example.count.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.count.R;
import com.example.count.model.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CounterActivity extends AppCompatActivity {

    // TODO show goal if present
    // TODO do not allow negative values
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
    private ImageView editImageView;
    private TextView goalTextView, goalHeaderTextView, goalStatusTextView;

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
        editImageView  = (ImageView) findViewById(R.id.edit_image_button);
        goalTextView = (TextView) findViewById(R.id.goalTextView);
        goalHeaderTextView = (TextView) findViewById(R.id.goalHeaderTv);
        goalStatusTextView = (TextView) findViewById(R.id.goalStatusTextView);

        counter = (Counter) getIntent().getExtras().get("counter");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm:ss");

        String creationTimestampString = simpleDateFormat.format(counter.getCreationTimestamp()) + " at " + simpleTimeFormat.format(counter.getCreationTimestamp());
        String lastUpdationTimestampString = simpleDateFormat.format(counter.getLastUpdationTimestamp()) + " at " + simpleTimeFormat.format(counter.getLastUpdationTimestamp());

        titleTextView.setText(counter.getTitle());
        valueTextView.setText(String.valueOf(counter.getValue()));
        creationDateTextView.setText(creationTimestampString);
        lastUpdationTextView.setText(lastUpdationTimestampString);

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

//        incrementImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                increment();
//            }
//        });

//        decrementImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                decrement();
//            }
//        });

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
                                Toast.makeText(CounterActivity.this, "Counter deleted!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CounterActivity.this, AddCounterActivity.class);
                intent.putExtra("counter", counter);
                intent.putExtra("edit_mode", true);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (counter != null) {
            counter = counterRepository.getCounter(counter.getId());
            titleTextView.setText(counter.getTitle());
            valueTextView.setText(counter.getValue() + "");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm:ss");
            String lastUpdationTimestampString = simpleDateFormat.format(counter.getLastUpdationTimestamp()) + " at " + simpleTimeFormat.format(counter.getLastUpdationTimestamp());
            lastUpdationTextView.setText(lastUpdationTimestampString);
            if (counter.getGoal() == -1) {
                goalTextView.setVisibility(View.GONE);
                goalHeaderTextView.setVisibility(View.GONE);
                goalStatusTextView.setVisibility(View.GONE);

            }
            else {
                goalTextView.setVisibility(View.VISIBLE);
                goalHeaderTextView.setVisibility(View.VISIBLE);
                goalStatusTextView.setVisibility(View.VISIBLE);
                goalTextView.setText(counter.getGoal() + "");
            }
            updateGoal();
        }
    }

    private void increment() {
        valueTextView.setText(String.valueOf(counter.getValue() + 1));
        counter.setValue(counter.getValue() + 1);
        counter.setLastUpdationTimestamp(new Date());
        counterRepository.update(counter);

        if (counter.getGoal() != -1) {
            updateGoal();
        }
    }

    private void updateGoal() {
        if (counter.getValue() < counter.getGoal()) {
            goalTextView.setText(counter.getGoal() + "");
            goalStatusTextView.setText(counter.getGoal() - counter.getValue() + " more to go!");
        }

        else if (counter.getValue() == counter.getGoal()){
            goalTextView.setText(counter.getGoal() + "");
            goalStatusTextView.setText("You have reached your goal!");
        }

        else {
            goalTextView.setText(counter.getGoal() + "");
            goalStatusTextView.setText("You are already past your goal (+" + (counter.getValue() - counter.getGoal()) +")");
        }
    }

    private void decrement() {

        if (counter.getValue() - 1 < 0) {
            Toast.makeText(this, "Cannot go below zero!", Toast.LENGTH_SHORT).show();
            return;
        }

        valueTextView.setText(String.valueOf(counter.getValue() - 1));
        counter.setValue(counter.getValue() - 1);
        counter.setLastUpdationTimestamp(new Date());
        counterRepository.update(counter);

        if (counter.getGoal() != -1) {
            updateGoal();
        }
    }
}
