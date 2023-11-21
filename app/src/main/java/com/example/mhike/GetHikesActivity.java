package com.example.mhike;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mhike.adapter.AdapterListHikes;
import com.example.mhike.database.DatabaseHelper;
import com.example.mhike.model.Hike;
import com.example.mhike.utils.Tools;

import java.util.List;

public class GetHikesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    private AdapterListHikes mAdapter;
    private static final int REQUEST_DELETE_ITEM = 1;
    private static final int REQUEST_INSERT_ITEM = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_hikes);
        initToolbar();
        initComponent();

    }


    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.grey_80), PorterDuff.Mode.SRC_ATOP);
        toolbar.setTitleTextColor(getResources().getColor(R.color.grey_80));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Hikes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.grey_5);
        Tools.setSystemBarLight(this);
    }

    private void initComponent() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        String query = getIntent().getStringExtra("query");
        List<Hike> hikes = new DatabaseHelper(this).getHikeData(query);
        //set data and list adapter

        mAdapter = new AdapterListHikes(this, hikes);
        mAdapter.notifyDataSetChanged();
        recyclerView.invalidate();
        recyclerView.setAdapter(mAdapter);



        mAdapter.setOnItemClickListener(new AdapterListHikes.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Hike obj, int position) {
                String  id = obj.getId();
                String condition = " WHERE id =  " + Integer.parseInt(id);
                List<Hike> hikes = new DatabaseHelper(getApplicationContext()).getHikeData(condition);
                Hike single_hike = hikes.get(0);
                String name = single_hike.getName();
                String location = single_hike.getLocation();
                String date = single_hike.getDate();
                String description = single_hike.getDescription();
                String length = single_hike.getLength_of_hike().toString();
                String tags = single_hike.getTags();
                String parking_available = single_hike.getParking_available();
                Integer level_of_difficulty = single_hike.getLevel_of_difficulty();

                Intent intent = new Intent(GetHikesActivity.this, UpdateHikeActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("location", location);
                intent.putExtra("date", date);
                intent.putExtra("description", description);
                intent.putExtra("length", length);
                intent.putExtra("tags", tags);
                intent.putExtra("parking_available", parking_available);
                intent.putExtra("level_of_difficulty", level_of_difficulty.toString());
                intent.putExtra("id", id+"");
                startActivityForResult(intent, REQUEST_DELETE_ITEM);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.overridePendingTransition(0, 0);
        finish();
        startActivity(getIntent());
    }
}