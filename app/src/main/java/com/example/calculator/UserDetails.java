package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserDetails extends AppCompatActivity {
    private EditText nameEditText;
    private DatePicker datePicker;
    private EditText emailEditText;
    private RecyclerView recyclerView;
    private ImageView dynamicImage;
    private ContactAdapter contactAdapter;
    private ContactDatabase contactDatabase;
    private int currentImageIndex = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        nameEditText = findViewById(R.id.nameEditText);
        datePicker = findViewById(R.id.datePicker);
        emailEditText = findViewById(R.id.emailEditText);
        recyclerView = findViewById(R.id.recyclerView);
        dynamicImage = findViewById(R.id.dynamicImage);

        contactAdapter = new ContactAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(contactAdapter);

        contactDatabase = new ContactDatabase(this);

        Button saveButton = findViewById(R.id.saveButton);
        Button viewDetailsButton = findViewById(R.id.viewDetailsButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String dob = getFormattedDate(datePicker.getDayOfMonth(), datePicker.getMonth(), datePicker.getYear());
                String email = emailEditText.getText().toString();

                // Save the contact details to the database
                contactDatabase.saveContact(new Contact(name, dob, email));

                // Clear input fields
                nameEditText.setText("");
                emailEditText.setText("");
            }
        });
        viewDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load contact details from the database and update the RecyclerView
                contactAdapter.setContacts(contactDatabase.getAllContacts());
                contactAdapter.notifyDataSetChanged();

            }
        });

        // Set an initial dynamic image
        updateDynamicImage();



    }
    private void updateDynamicImage() {
        dynamicImage.setImageResource(getResources().getIdentifier("image" + currentImageIndex, "drawable", getPackageName()));
        currentImageIndex = (currentImageIndex % 3) + 1; // Cycle through image resources (1, 2, 3)
    }

    private String getFormattedDate(int day, int month, int year) {
        // Format the date as needed
        // You can customize the formatting according to your requirements
        return day + "/" + (month + 1) + "/" + year;
    }
}