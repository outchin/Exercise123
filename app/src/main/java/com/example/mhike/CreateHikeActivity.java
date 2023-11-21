package com.example.mhike;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.core.app.ActivityCompat;

import com.example.mhike.database.DatabaseHelper;
import com.example.mhike.model.Hike;
import com.example.mhike.utils.Tools;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

public class CreateHikeActivity extends AppCompatActivity {
    private ImageView hike_image;
    private View bottom_sheet;
    private  int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    private BottomSheetBehavior mBehavior;
    private EditText edt_hike_name, edt_hike_date_time, edt_hike_length;
    private AutoCompleteTextView act_hike_location;
    private int hiking_image = 0;
    private BottomSheetDialog mBottomSheetDialog;
    private TextView radioError,txt_current_location_lat_long;

    private AppCompatSeekBar seekBar;
    boolean seekBarUsed = false;

    int finalProgress = 0;
    String hike_parking_available_value_string = "No";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_hike);
        String[] locations = getResources().getStringArray(R.array.location);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.location_item, locations);
        act_hike_location = (AutoCompleteTextView) findViewById(R.id.hike_location);
        act_hike_location.setAdapter(arrayAdapter);
        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);
        initComponent();
        txt_current_location_lat_long = (TextView)findViewById(R.id.txt_current_location_lat_long);
        (findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edt_hike_date_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDatePickerLight(v);

            }
        });
    }

    private void initComponent() {
        edt_hike_name = findViewById(R.id.hike_name);
        edt_hike_date_time = findViewById(R.id.hike_date_time);
        edt_hike_length = findViewById(R.id.hike_length);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);
        hike_image = (ImageView) findViewById(R.id.hike_image);
        radioError = (TextView) findViewById(R.id.radioError);
        seekBar = (AppCompatSeekBar) findViewById(R.id.hike_difficulties);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // method to get the location
        getLastLocation();
        (findViewById(R.id.hike_image)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });
        (findViewById(R.id.btn_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hike_name = edt_hike_name.getText().toString();
                String hike_location = act_hike_location.getText().toString();
                String hike_date_time = edt_hike_date_time.getText().toString();
                String hike_length = edt_hike_length.getText().toString();
                String hike_description = ((EditText) findViewById(R.id.hike_description)).getText().toString();
                String hike_tags = ((EditText) findViewById(R.id.hike_tags)).getText().toString();
                String hike_lat_long = ((TextView) findViewById(R.id.txt_current_location_lat_long)).getText().toString();
                AppCompatSeekBar hike_difficulty = (AppCompatSeekBar) findViewById(R.id.hike_difficulties);
                String hike_difficulty_value = hike_difficulty.getProgress() + "";
                RadioGroup hike_parking_available = (RadioGroup) findViewById(R.id.hike_parking_available);
                // find the radiobutton by returned id
                RadioButton hike_parking_available_value = (RadioButton) findViewById(hike_parking_available.getCheckedRadioButtonId());
                if (hike_parking_available_value != null) {
                    hike_parking_available_value_string = hike_parking_available_value.getText() + "";
                    radioError.setVisibility(View.GONE);
                } else {
                    radioError.setVisibility(View.VISIBLE);
                }
                if (hike_name.trim().isEmpty()) {
                    edt_hike_name.requestFocus();
                    edt_hike_name.setError("Name is required");
                }
                if (hike_location.trim().isEmpty()) {
                    act_hike_location.requestFocus();
                    act_hike_location.setError("Location is required");
                }
                if (hike_date_time.trim().isEmpty()) {
                    edt_hike_date_time.requestFocus();
                    edt_hike_date_time.setError("Date is required"); // fix if I get time
                }
                if (hike_length.trim().isEmpty()) {
                    edt_hike_length.requestFocus();
                    edt_hike_length.setError("Length is required"); // fix if I get time
                }
                if (!seekBarUsed) {
                    Toast.makeText(getApplicationContext(), "Seek bar need to use", Toast.LENGTH_SHORT).show();
                }

                if ((hike_parking_available_value != null) && (!hike_name.trim().isEmpty()) && (!hike_location.trim().isEmpty()) && (!hike_date_time.trim().isEmpty()) && (!hike_length.trim().isEmpty()) && (seekBarUsed)) {


                    Hike hike = new Hike();
                    hike.setName(hike_name);
                    hike.setImage_link(hiking_image + "");
                    hike.setLocation(hike_location);
                    hike.setDate(hike_date_time);
                    hike.setLength_of_hike(Integer.parseInt(hike_length));
                    hike.setDescription(hike_description);
                    hike.setLevel_of_difficulty(Integer.parseInt(hike_difficulty_value));
                    hike.setParking_available(hike_parking_available_value_string);
                    hike.setTags(hike_tags);
                    hike.setType(hike_lat_long);
                    hike.setImage_id(0);
                    showConfirmDialog(hike);
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill all required values", Toast.LENGTH_SHORT).show();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Set the flag to true since the user has used the seek bar
                seekBarUsed = true;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (seekBarUsed) {

                    finalProgress = seekBar.getProgress();

                } else {

                }
            }
        });


    }
    // This method will help to retrieve the image



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
        builder.setMessage("Name : " +hike.getName()+"\n"+ "Location : " +hike.getLocation()+"\n"+ "Date : " +hike.getDate()+"\n"+ "Length : " +hike.getLength_of_hike() + " Km"+"\n"+ "Description : " +hike.getDescription()+"\n"+ "Difficulty level : " +hike.getLevel_of_difficulty()+"\n"+ "Parking available : " + hike.getParking_available());
        builder.setPositiveButton(R.string.AGREE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper db = new DatabaseHelper(CreateHikeActivity.this);
                if (db.insertHikeData(hike) > 0) {
                    showSuccessDialog();

                } else {
                    Toast.makeText(CreateHikeActivity.this, "Hike Creation Failed", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(CreateHikeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }
    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
        }
    };
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            txt_current_location_lat_long.setText(location.getLatitude() + ", " + location.getLongitude() + "");
//                            latitudeTextView.setText(location.getLatitude() + "");
//                            longitTextView.setText(location.getLongitude() + "");
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

}