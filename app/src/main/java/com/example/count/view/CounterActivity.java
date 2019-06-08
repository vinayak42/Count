package com.example.count.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.count.R;
import com.example.count.model.Utils;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CounterActivity extends AppCompatActivity {

    // TODO show goal if present
    TextView titleTextView, valueTextView, creationDateTextView, lastUpdationTextView;
    Button incrementButton, decrementButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        titleTextView = (TextView) findViewById(R.id.counter_activity_title);
        valueTextView = (TextView) findViewById(R.id.counter_activity_value);
        creationDateTextView = (TextView) findViewById(R.id.counter_activity_creation_date_text_view);
        lastUpdationTextView = (TextView) findViewById(R.id.counter_activity_last_updated_text_view);
        incrementButton = (Button) findViewById(R.id.counter_activity_increment_button);
        decrementButton = (Button) findViewById(R.id.counter_activity_decrement_button);

        final Counter counter = (Counter) getIntent().getExtras().get("counter");

        titleTextView.setText(counter.getTitle());
        valueTextView.setText(String.valueOf(counter.getValue()));
        creationDateTextView.setText(counter.getCreationTimestamp().toString());
        lastUpdationTextView.setText(counter.getLastUpdationTimestamp().toString());

        final String counterId = (String) getIntent().getExtras().get("counterId");
        FirebaseFirestore db = Utils.getInstance().getDb();
        FirebaseUser user = Utils.getInstance().getUser();

        final DocumentReference counterReference = db.collection("users").document(user.getUid()).collection("counters").document(counterId);

        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> newValue = new HashMap<>();
                newValue.put("value", counter.getValue() + 1);
                counterReference.update(newValue);
                valueTextView.setText(String.valueOf(counter.getValue() + 1));
                counter.setValue(counter.getValue() + 1);
            }
        });

        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> newValue = new HashMap<>();
                newValue.put("value", counter.getValue() - 1);
                counterReference.update(newValue);
                valueTextView.setText(String.valueOf(counter.getValue() - 1));
                counter.setValue(counter.getValue() - 1);
            }
        });

    }
}
