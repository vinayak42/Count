package com.example.count.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.count.R;
import com.example.count.model.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddCounterActivity extends AppCompatActivity {

    private EditText counterTitleEt, counterInitialValueEt, goalValueEt;
    private TextInputLayout goalValueTil;
    private Button submitButton;
    private CheckBox setGoal;
    FirebaseFirestore db;
    FirebaseUser user;
    boolean customGoalRequired;
    private CounterRepository counterRepository;
    private boolean edit_mode = false;
    Counter receivedCounter;

    private boolean inputValidator() {

        // TODO check for negative values and various other edge cases

        boolean valid = true;

        if (TextUtils.isEmpty(counterTitleEt.getText())) {
            Toast.makeText(this, "Fill in a title name!", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (setGoal.isChecked()) {
            if (TextUtils.isEmpty(goalValueEt.getText())) {
                Toast.makeText(this, "Enter a number for goal or uncheck the option", Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }
        return valid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_counter);
        final Intent receivedIntent = getIntent();
        if (receivedIntent.hasExtra("edit_mode")) {
            edit_mode = true;
            receivedCounter = (Counter) receivedIntent.getSerializableExtra("counter");
        }

        Utils.getInstance().init(getApplication());
       counterTitleEt = ((TextInputLayout)findViewById(R.id.counter_title_text_input_layout)).getEditText();
       counterInitialValueEt = ((TextInputLayout)findViewById(R.id.counter_initial_value_text_input_layout)).getEditText();
       goalValueEt = ((TextInputLayout) findViewById(R.id.goal_value_text_input_layout)).getEditText();
       goalValueTil = (TextInputLayout) findViewById(R.id.goal_value_text_input_layout);
       setGoal = (CheckBox) findViewById(R.id.set_goal_cb);
       db = Utils.getInstance().getDb();
       user = Utils.getInstance().getUser();
       submitButton = (Button) findViewById(R.id.submit_button);
       counterRepository = new CounterRepository(getApplication());

        goalValueTil.setVisibility(View.INVISIBLE);

        setGoal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    goalValueTil.setVisibility(View.VISIBLE);
                }
                else {
                    goalValueTil.setVisibility(View.INVISIBLE);
                }
            }
        });

        if (edit_mode) {
            counterTitleEt.setText(receivedCounter.getTitle());
            counterInitialValueEt.setText(receivedCounter.getValue() + "");

            if (receivedCounter.getGoal() > -1) {
                setGoal.setChecked(true);
                goalValueEt.setText(receivedCounter.getGoal());
                goalValueEt.setVisibility(View.VISIBLE);
            }
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!inputValidator()) {
                    return;
                }

                // create a new counter
                final String counterTitle = counterTitleEt.getText().toString().trim();
                int counterValue = 0;

                if (!TextUtils.isEmpty(counterInitialValueEt.getText().toString().trim())) {
                    counterValue = Integer.parseInt(counterInitialValueEt.getText().toString().trim());
                }

                final Timestamp creationTime = edit_mode? new Timestamp(receivedCounter.getCreationTimestamp()) : new Timestamp(new Date());
                final Timestamp lastUpdationTime = new Timestamp(new Date());

                int goal = -1;

                if (setGoal.isChecked()) {
                    goal = Integer.parseInt(goalValueEt.getText().toString().trim());
                }

                final int goal2 = goal;
                final int counterValue2 = counterValue;

                if (!edit_mode) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("title", counterTitle);
                    data.put("value", counterValue);
                    data.put("creationTimestamp", creationTime);
                    data.put("lastUpdationTimestamp", lastUpdationTime);
                    data.put("goal", goal);

                    CollectionReference countersReference = db.collection("users").document(user.getUid()).collection("counters");
                    countersReference.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String documentId = documentReference.getId();
                            Map<String, Object> idUpdationdata = new HashMap<>();
                            idUpdationdata.put("id", documentId);
                            documentReference.update(idUpdationdata);
                            Counter counter = new Counter(documentId, counterTitle, creationTime.toDate(), lastUpdationTime.toDate(), goal2, counterValue2);
                            counterRepository.insert(counter);
                            Toast.makeText(AddCounterActivity.this, "Added new counter!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                else {
                    receivedCounter.setTitle(counterTitle);
                    receivedCounter.setValue(counterValue2);
                    receivedCounter.setLastUpdationTimestamp(new Date());
                    receivedCounter.setGoal(goal2);
                    counterRepository.update(receivedCounter);
                    Toast.makeText(AddCounterActivity.this, "Updated the counter!", Toast.LENGTH_SHORT).show();
                }


                finish();
            }
        });
    }
}
