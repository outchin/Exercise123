package com.example.mhike;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewHikesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_hikes);
        initComponent();
    }

    private void initComponent() {
        String h_name = getIntent().getStringExtra("name");
        String h_location = getIntent().getStringExtra("location");
        String h_date = getIntent().getStringExtra("date");
        String h_description = getIntent().getStringExtra("description");
        String h_length = getIntent().getStringExtra("length");
        String h_tags = getIntent().getStringExtra("tags");
        String parking_available = getIntent().getStringExtra("parking_available");
        String level_of_difficulty = getIntent().getStringExtra("level_of_difficulty");
        ((TextView) findViewById(R.id.hike_name)).setText(h_name);
        ((TextView) findViewById(R.id.hike_length)).setText(h_length + " Km");
        ((TextView) findViewById(R.id.hike_difficulty)).setText(level_of_difficulty);
        ((TextView) findViewById(R.id.hike_location)).setText(h_location);
        ((TextView) findViewById(R.id.hike_description)).setText(h_description);
        ((TextView) findViewById(R.id.hike_date)).setText(h_date);


    }
}