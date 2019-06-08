package com.example.count.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.count.R;
import com.example.count.model.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
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

    private boolean inputValidator() {

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

        Utils.getInstance().init();
       counterTitleEt = ((TextInputLayout)findViewById(R.id.counter_title_text_input_layout)).getEditText();
       counterInitialValueEt = ((TextInputLayout)findViewById(R.id.counter_initial_value_text_input_layout)).getEditText();
       goalValueEt = ((TextInputLayout) findViewById(R.id.goal_value_text_input_layout)).getEditText();
       goalValueTil = (TextInputLayout) findViewById(R.id.goal_value_text_input_layout);
       setGoal = (CheckBox) findViewById(R.id.set_goal_cb);
       db = Utils.getInstance().getDb();
       user = Utils.getInstance().getUser();
       submitButton = (Button) findViewById(R.id.submit_button);

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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!inputValidator()) {
                    return;
                }

                // create a new counter
                String counterTitle = counterTitleEt.getText().toString().trim();
                int counterValue = 0;

                if (!TextUtils.isEmpty(counterInitialValueEt.getText().toString().trim())) {
                    counterValue = Integer.parseInt(counterInitialValueEt.getText().toString().trim());
                }

                Timestamp creationTime = new Timestamp(new Date());
                Timestamp lastUpdationTime = new Timestamp(new Date());

                int goal = -1;

                if (setGoal.isChecked()) {
                    goal = Integer.parseInt(goalValueEt.getText().toString().trim());
                }

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
                        Toast.makeText(AddCounterActivity.this, "Added new counter!", Toast.LENGTH_SHORT).show();
                    }
                });
                
                finish();
            }
        });
    }
}
