package com.example.mhike;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.example.mhike.database.DatabaseHelper;
import com.example.mhike.model.Hike;
import com.example.mhike.utils.Tools;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class UpdateHikeActivity extends AppCompatActivity {
    private ImageView hike_image;
    private int[] image_array = {R.drawable.no_image_found, R.drawable.hike_image_1, R.drawable.hike_image_2,R.drawable.hike_image_3,R.drawable.hike_image_4,R.drawable.hike_image_5,R.drawable.hike_image_6,R.drawable.hike_image_7,R.drawable.hike_image_8,R.drawable.hike_image_9,R.drawable.hike_image_10}; // Replace with your image resource IDs from drawable
    private TextView radioError;
    boolean seekBarUsed = false;
    private BottomSheetDialog mBottomSheetDialog;
    private int hiking_image = 0;
    private BottomSheetBehavior mBehavior;
    private View bottom_sheet;
    int finalProgress = 0;
    private DatabaseHelper db;
    private EditText hike_name,date_time,length,description,tags;
    private AutoCompleteTextView act_hike_location;
    private EditText hike_length;
    private static final int pic_id = 123;
    private static RadioButton rb_basic_1,rb_basic_2;
    private static SeekBar hike_difficulties;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new DatabaseHelper(UpdateHikeActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_hike);
        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);
        String[] locations = getResources().getStringArray(R.array.location);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.location_item, locations);
        hiking_image = Integer.parseInt(getIntent().getStringExtra("image_link"));

        act_hike_location = (AutoCompleteTextView) findViewById(R.id.hike_location);
        act_hike_location.setAdapter(arrayAdapter);
        act_hike_location.setThreshold(0); // Set threshold to 0 to show suggestions on click

        String defaultText = getIntent().getStringExtra("location");
        act_hike_location.setText(defaultText);
        // Create a list with the default text and the original array
        List<String> fullList = new ArrayList<>(Arrays.asList(defaultText));
        fullList.addAll(Arrays.asList(locations));
        // Create a new ArrayAdapter with the modified list
        ArrayAdapter<String> fullArrayAdapter = new ArrayAdapter<>(this, R.layout.location_item, fullList);
        act_hike_location.setAdapter(fullArrayAdapter);

        initComponent();
        (findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        (findViewById(R.id.hike_date_time)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDatePickerLight(v);

            }
        });
        act_hike_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        (findViewById(R.id.btn_add_observation)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = getIntent().getStringExtra("id");
                Intent intent = new Intent(UpdateHikeActivity.this, CreateObservationActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);

            }
        });
    }
    private void initComponent() {
        hike_image = (ImageView) findViewById(R.id.hike_image);
        radioError = (TextView)findViewById(R.id.radioError);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);
        String h_name = getIntent().getStringExtra("name");
        String h_location = getIntent().getStringExtra("location");
        String h_date = getIntent().getStringExtra("date");
        String h_description = getIntent().getStringExtra("description");
        String h_length = getIntent().getStringExtra("length");
        String h_tags = getIntent().getStringExtra("tags");
        String parking_available = getIntent().getStringExtra("parking_available");
        String level_of_difficulty = getIntent().getStringExtra("level_of_difficulty");
        String image_link = getIntent().getStringExtra("image_link");
        hike_image.setBackground(getResources().getDrawable(image_array[Integer.parseInt(image_link)]));
        rb_basic_1 = (RadioButton) findViewById(R.id.rb_basic_1);
        rb_basic_2 = (RadioButton) findViewById(R.id.rb_basic_2);
        hike_difficulties = (SeekBar) findViewById(R.id.hike_difficulties);
        hike_name = (EditText) findViewById(R.id.hike_name);
        date_time = (EditText) findViewById(R.id.hike_date_time);
        description = (EditText) findViewById(R.id.hike_description);
        hike_length = (EditText) findViewById(R.id.hike_length_1);
        tags = (EditText) findViewById(R.id.hike_tags);
        hike_name.setText(h_name);
        date_time.setText(h_date);
        description.setText(h_description);
        hike_length.setText(h_length);
        tags.setText(h_tags);
        hike_difficulties.setProgress(Integer.parseInt("80"));
        Boolean is_parking_available = false;

        (findViewById(R.id.btn_delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = getIntent().getStringExtra("id");
                String h_name = getIntent().getStringExtra("name");
                String condition = " WHERE id =  " + Integer.parseInt(id);
                db.deleteHike(condition);
                finish();
            }
        });

        (findViewById(R.id.btn_view_observation)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = getIntent().getStringExtra("id");
                Intent intent = new Intent(UpdateHikeActivity.this, GetObservationsActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        if(parking_available.equals("Yes")){
            is_parking_available = true;
            rb_basic_1.setChecked(true);
            rb_basic_2.setChecked(false);

        }else{
            is_parking_available = false;
            rb_basic_1.setChecked(false);
            rb_basic_2.setChecked(true);
        }
        (findViewById(R.id.hike_image)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });
        (findViewById(R.id.btn_update)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = getIntent().getStringExtra("id");
                String hike_name = ((EditText) findViewById(R.id.hike_name)).getText().toString();
                String hike_location = act_hike_location.getText().toString();
                String hike_date_time = ((EditText) findViewById(R.id.hike_date_time)).getText().toString();
                String hike_length = ((EditText) findViewById(R.id.hike_length_1)).getText().toString();
                String hike_description = ((EditText) findViewById(R.id.hike_description)).getText().toString();
                AppCompatSeekBar hike_difficulty = (AppCompatSeekBar) findViewById(R.id.hike_difficulties);
                String hike_difficulty_value = hike_difficulty.getProgress() + "";
                RadioGroup hike_parking_available = (RadioGroup) findViewById(R.id.hike_parking_available);
                // find the radiobutton by returned id
                RadioButton hike_parking_available_value = (RadioButton) findViewById(hike_parking_available.getCheckedRadioButtonId());
                String hike_parking_available_value_string = hike_parking_available_value.getText() + "";
                if(hike_parking_available_value != null){
                    hike_parking_available_value_string = hike_parking_available_value.getText()+"";
                    radioError.setVisibility(View.GONE);
                }else{
                    radioError.setVisibility(View.VISIBLE);
                }
                if(hike_name.trim().isEmpty()){
                    ((EditText) findViewById(R.id.hike_name)).requestFocus();
                    ((EditText) findViewById(R.id.hike_name)).setError("Name is required");
                }
                if(hike_location.trim().isEmpty()){
                    ((EditText) findViewById(R.id.hike_location)).requestFocus();
                    ((EditText) findViewById(R.id.hike_location)).setError("Location is required");
                }
                if(hike_date_time.trim().isEmpty()){
                    ((EditText) findViewById(R.id.hike_date_time)).requestFocus();
                    ((EditText) findViewById(R.id.hike_date_time)).setError("Date is required"); // fix if I get time
                }
                if(hike_length.trim().isEmpty()) {
                    ((EditText) findViewById(R.id.hike_length_1)).requestFocus();
                    ((EditText) findViewById(R.id.hike_length_1)).setError("Length is required"); // fix if I get time
                }
                if( (hike_parking_available_value !=null) && (!hike_name.trim().isEmpty()) && (!hike_location.trim().isEmpty()) && (!hike_date_time.trim().isEmpty()) && (!hike_length.trim().isEmpty())){
                    Hike hike = new Hike();
                    hike.setName(hike_name);
                    hike.setLocation(hike_location);
                    hike.setDate(hike_date_time);
                    hike.setImage_link(hiking_image+"");
                    hike.setLength_of_hike(Integer.parseInt(hike_length));
                    hike.setDescription(hike_description);
                    hike.setLevel_of_difficulty(Integer.parseInt(hike_difficulty_value));
                    hike.setParking_available(hike_parking_available_value_string);
                    hike.setImage_id(0);
                    hike.setId(id);
                    showConfirmDialog(hike);

                }else{
                    Toast.makeText(getApplicationContext() , hike_parking_available_value_string +hike_difficulty_value+ hike_name + hike_location + hike_date_time + hike_length,Toast.LENGTH_SHORT).show();
                }

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
    private void showBottomSheetDialog() {
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        final View view = getLayoutInflater().inflate(R.layout.sheet_list, null);
        ((View) view.findViewById(R.id.hike_image_1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hiking_image = 1;
                hike_image.setBackground(getResources().getDrawable(R.drawable.hike_image_1));
            }
        });
        ((View) view.findViewById(R.id.hike_image_2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hiking_image = 2;
                hike_image.setBackground(getResources().getDrawable(R.drawable.hike_image_2));
            }
        });
        ((View) view.findViewById(R.id.hike_image_3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hiking_image = 3;
                hike_image.setBackground(getResources().getDrawable(R.drawable.hike_image_3));
            }
        });
        ((View) view.findViewById(R.id.hike_image_4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hiking_image = 4;
                hike_image.setBackground(getResources().getDrawable(R.drawable.hike_image_4));
            }
        });
        ((View) view.findViewById(R.id.hike_image_5)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hiking_image = 5;
                hike_image.setBackground(getResources().getDrawable(R.drawable.hike_image_5));
            }
        });
        ((View) view.findViewById(R.id.hike_image_6)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hiking_image = 6;
                hike_image.setBackground(getResources().getDrawable(R.drawable.hike_image_6));
            }
        });
        ((View) view.findViewById(R.id.hike_image_7)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hiking_image = 7;
                hike_image.setBackground(getResources().getDrawable(R.drawable.hike_image_7));
            }
        });
        ((View) view.findViewById(R.id.hike_image_8)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hiking_image = 8;
                hike_image.setBackground(getResources().getDrawable(R.drawable.hike_image_8));
            }
        });
        ((View) view.findViewById(R.id.hike_image_9)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hiking_image = 9;
                hike_image.setBackground(getResources().getDrawable(R.drawable.hike_image_9));
            }
        });
        ((View) view.findViewById(R.id.hike_image_10)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hiking_image = 10;
                hike_image.setBackground(getResources().getDrawable(R.drawable.hike_image_10));
            }
        });


        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });
    }
    private void showConfirmDialog(Hike hike) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please check all the details before saving");
        builder.setMessage("Name : " +hike.getName()+"\n"+ "Location : " +hike.getLocation()+"\n"+ "Date : " +hike.getDate()+"\n"+ "Length : " +hike.getLength_of_hike()+"\n"+ "Description : " +hike.getDescription()+"\n"+ "Difficulty level : " +hike.getLevel_of_difficulty()+"\n"+ "Parking available : " + hike.getParking_available());
        builder.setPositiveButton(R.string.AGREE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper db = new DatabaseHelper(UpdateHikeActivity.this);
                if(db.updateHikeData(hike,Integer.parseInt(hike.getId()))>0){
                    showSuccessDialog();
                    Intent intent = new Intent(UpdateHikeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(UpdateHikeActivity.this, "Hike Update Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.DISAGREE, null);
        builder.show();
    }
    private void showSuccessDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_success);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateHikeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}