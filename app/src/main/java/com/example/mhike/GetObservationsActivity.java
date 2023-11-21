package com.example.mhike;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mhike.adapter.AdapterListObservations;
import com.example.mhike.database.DatabaseHelper;
import com.example.mhike.model.Observation;
import com.example.mhike.utils.Tools;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class GetObservationsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton btn_save;
    private AdapterListObservations mAdapter;
    private static final int REQUEST_DELETE_ITEM = 1;
    private static final int REQUEST_INSERT_ITEM = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_observations);
        initToolbar();
        initComponent();

    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.grey_80), PorterDuff.Mode.SRC_ATOP);
        toolbar.setTitleTextColor(getResources().getColor(R.color.grey_80));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Observations");
        Tools.setSystemBarColor(this, R.color.grey_5);
        Tools.setSystemBarLight(this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle the Up button click
                onBackPressed();
                return true;
            // Add other cases as needed
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void initComponent() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        String id = getIntent().getStringExtra("id");
        String condition = " WHERE hike_id = " + Integer.parseInt(id);
        List<Observation> observations = new DatabaseHelper(this).getObservationData(condition);
        //set data and list adapter

        mAdapter = new AdapterListObservations(this, observations);
        mAdapter.notifyDataSetChanged();
        recyclerView.invalidate();
        recyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new AdapterListObservations.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Observation obj, int position) {
                String id = obj.getId().toString();
                String condition = " WHERE id =  " + Integer.parseInt(id);
                List<Observation> observations = new DatabaseHelper(getApplicationContext()).getObservationData(condition);
                Observation observation = observations.get(0);
                String observation_name = observation.getTitle();
                String observation_date = observation.getDatetime();
                String observation_additional_comment= observation.getAdditionalcomment();
                String observation_id= observation.getId().toString();

                Intent intent = new Intent(GetObservationsActivity.this, UpdateObservationActivity.class);
                intent.putExtra("observation_name", observation_name);
                intent.putExtra("observation_date", observation_date);
                intent.putExtra("observation_additional_comment", observation_additional_comment);
                intent.putExtra("observation_id", observation_id + "");
                startActivityForResult(intent, REQUEST_DELETE_ITEM);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
        startActivity(getIntent());
        if (requestCode == REQUEST_DELETE_ITEM) {

        } else {
            List<Observation> observations = new DatabaseHelper(getApplicationContext()).getObservationData("");
            //set data and list adapter
            mAdapter = new AdapterListObservations(getApplicationContext(), observations);
            mAdapter.notifyDataSetChanged();
            recyclerView.invalidate();
            recyclerView.setAdapter(mAdapter);
            finish();
            startActivity(getIntent());
        }
    }
}