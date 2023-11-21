package com.example.mhike;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mhike.utils.Tools;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

public class AdvancedSearchHike extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search_hike);

        (findViewById(R.id.hike_date_time)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDatePickerLight(v);

            }
        });
        (findViewById(R.id.btn_add_observation)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hike_name = ((EditText) findViewById(R.id.hike_name)).getText().toString();
                String hike_location = ((EditText) findViewById(R.id.hike_location)).getText().toString();
                String hike_date_time = ((EditText) findViewById(R.id.hike_date_time)).getText().toString();
                String hike_length = ((EditText) findViewById(R.id.hike_length)).getText().toString();
                String condition = "";

                if (!hike_name.isEmpty()) {
                    condition += "name LIKE '%" + hike_name + "%'";
                }

                if (!hike_location.isEmpty()) {
                    if (!condition.isEmpty()) {
                        condition += " OR ";
                    }
                    condition += "location LIKE '%" + hike_location + "%'";
                }

                if (!hike_date_time.isEmpty()) {
                    if (!condition.isEmpty()) {
                        condition += " OR ";
                    }
                    condition += "date LIKE '%" + hike_date_time + "%'";
                }

                if (!hike_length.isEmpty()) {
                    if (!condition.isEmpty()) {
                        condition += " OR ";
                    }
                    condition += "length LIKE '%" + hike_length + "%'";
                }

// Check if any conditions were added
                if (!condition.isEmpty()) {
                    condition = " WHERE " + condition;
                } else {
                    // Handle the case where no conditions are provided
                    condition = "";
                }

                Intent intent = new Intent(AdvancedSearchHike.this, MainActivity.class);
                intent.putExtra("query", condition);
                startActivity(intent);
                finish();
            }
        });
        (findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
        datePicker.show(getFragmentManager(), "Expiration Date");
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
}