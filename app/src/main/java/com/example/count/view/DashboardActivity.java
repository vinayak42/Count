package com.example.count.view;

import android.content.Intent;
import android.os.Bundle;

import com.example.count.R;
import com.example.count.model.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// TODO need to tap back twice to close Dashboard activity, fix it
public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = "DashboardTag";
    private static final int NEW_COUNTER_ACTIVITY_REQUEST_CODE = 1;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
//    private CollectionReference counterCollectionReference;
    private CounterAdapter counterAdapter;
    private TextView emptyTextView;
    private CircleImageView profileImageView;
    private TextView nameTextView;
    private TextView emailTextView;
//    private List<Counter> counterArrayList;
    private CounterViewModel counterViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        emptyTextView = (TextView) findViewById(R.id.empty_text_view);
        emptyTextView.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, AddCounterActivity.class);
                startActivityForResult(intent, NEW_COUNTER_ACTIVITY_REQUEST_CODE);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
//        counterCollectionReference = db.collection("users").document(user.getUid()).collection("counters");
        getSupportActionBar().setTitle(user.getDisplayName().split(" ")[0] + "'s Dashboard");

        View headerLayout = navigationView.getHeaderView(0);

        profileImageView = (CircleImageView) headerLayout.findViewById(R.id.profile_image_view);
        nameTextView = (TextView) headerLayout.findViewById(R.id.dashboard_name_text_view);
        emailTextView = (TextView) headerLayout.findViewById(R.id.dashboard_email_text_view);

        Picasso.get().load(user.getPhotoUrl().toString()).into(profileImageView);
        nameTextView.setText(user.getDisplayName());
        emailTextView.setText(user.getEmail());


        // Here work starts

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        counterAdapter = new CounterAdapter();
        recyclerView.setAdapter(counterAdapter);

        counterViewModel = ViewModelProviders.of(this).get(CounterViewModel.class);
        counterViewModel.getAllCounters().observe(this, new Observer<List<Counter>>() {
            @Override
            public void onChanged(List<Counter> counters) {
                // isnt this same as changing the counterArrayList in this file?
                counterAdapter.setCounterList(counters);
            }
        });

    }

//    private void setupRecyclerView() {
//        Query query = counterCollectionReference;
//        FirestoreRecyclerOptions<Counter> options = new FirestoreRecyclerOptions.Builder<Counter>()
//                .setQuery(query, Counter.class)
//                .build();
//
//        counterAdapter = new CounterAdapter(emptyTextView);
//
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_view);
//        recyclerView.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(counterAdapter);
//
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ((LinearLayoutManager) layoutManager).getOrientation());
//        recyclerView.addItemDecoration(dividerItemDecoration);
//
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        else if (id == R.id.action_logout) {
            Utils.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            // open settings activity
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
