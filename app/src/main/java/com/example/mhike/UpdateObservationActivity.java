package com.example.mhike;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mhike.database.DatabaseHelper;
import com.example.mhike.model.Observation;
import com.example.mhike.utils.Tools;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

public class UpdateObservationActivity extends AppCompatActivity {
    private EditText observation_name,observation_date_time,observation_additional_comment;
    private static final int pic_id = 123;
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_observation);
        db = new DatabaseHelper(this);
        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);

        initComponent();
        (findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        (findViewById(R.id.observation_date_time)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDatePickerLight(v);

            }
        });
    }
    private void initComponent() {

        observation_name = (EditText) findViewById(R.id.observation_name);
        observation_date_time = (EditText) findViewById(R.id.observation_date_time);
        observation_additional_comment = (EditText) findViewById(R.id.observation_additional_comment);


        observation_name.setText(getIntent().getStringExtra("observation_name"));
        observation_date_time.setText(getIntent().getStringExtra("observation_date"));
        observation_additional_comment.setText(getIntent().getStringExtra("observation_additional_comment"));

        (findViewById(R.id.hike_image)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the camera_intent ACTION_IMAGE_CAPTURE it will open the camera for capture the image
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Start the activity with camera_intent, and request pic id
                startActivityForResult(camera_intent, pic_id);
            }
        });
        (findViewById(R.id.btn_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UpdateObservationActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                String observation_name = ((EditText) findViewById(R.id.observation_name)).getText().toString();
                String observation_date_time = ((EditText) findViewById(R.id.observation_date_time)).getText().toString();
                String observation_additional_comment = ((EditText) findViewById(R.id.observation_additional_comment)).getText().toString();



                Observation observation = new Observation();
                observation.setTitle(observation_name);
                observation.setDatetime(observation_date_time);
                observation.setAdditionalcomment(observation_additional_comment);

                String observation_id = getIntent().getStringExtra("observation_id");
                DatabaseHelper db = new DatabaseHelper(UpdateObservationActivity.this);
                if(db.updateObservationData(observation,Integer.parseInt(observation_id))>0){
                    Toast.makeText(UpdateObservationActivity.this, "Observation Updated  Successfully", Toast.LENGTH_SHORT).show();

                }
            }
        });

        (findViewById(R.id.btn_delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = getIntent().getStringExtra("observation_id");
                String condition = " WHERE id =  " + Integer.parseInt(id);
                db.deleteObservation(condition);
                finish();
                Toast.makeText(UpdateObservationActivity.this, "Delete " + id + " Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void dialogDatePickerLight(final View v) {
        Calendar cur_calender = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        long date = calendar.getTimeInMillis();
                        ((EditText) v).setText(Tools.getFormattedDateShort(date));
                    }
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        //set dark light
        datePicker.setThemeDark(false);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.show(getFragmentManager(), "Observation Date");
    }
}