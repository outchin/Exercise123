package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Image extends AppCompatActivity {
    private ImageView imageView;
    private Button prevButton;
    private Button nextButton;
    private int currentIndex = 0;
    private int[] imageIds = {R.drawable.image1, R.drawable.image2, R.drawable.image3,R.drawable.image4}; // Replace with your image resource IDs from drawable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        imageView = (ImageView) findViewById(R.id.imageView);
        prevButton = (Button) findViewById(R.id.prevButton);
        nextButton = (Button) findViewById(R.id.nextButton);
        updateImage();
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = (currentIndex - 1 + imageIds.length) % imageIds.length;
                updateImage();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = (currentIndex + 1) % imageIds.length;
                updateImage();
            }
        });
    }
    private void updateImage() {
        imageView.setImageResource(imageIds[currentIndex]);
    }
}