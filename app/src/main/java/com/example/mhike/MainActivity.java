package com.example.mhike;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mhike.adapter.AdapterListHikes;
import com.example.mhike.database.DatabaseHelper;
import com.example.mhike.model.Hike;
import com.example.mhike.utils.Tools;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton btn_create_hike;
    private DrawerLayout drawer;
    private ActionBar actionBar;
    private AdapterListHikes mAdapter;
    private Toolbar toolbar;
    private static final int REQUEST_DELETE_ITEM = 1;
    private static final int REQUEST_INSERT_ITEM = 2;
    private DatabaseHelper db;
    private TextView hike_lat_long;
    private int[] image_array = {R.drawable.no_image_found, R.drawable.hike_image_1, R.drawable.hike_image_2,R.drawable.hike_image_3,R.drawable.hike_image_4,R.drawable.hike_image_5,R.drawable.hike_image_6,R.drawable.hike_image_7,R.drawable.hike_image_8,R.drawable.hike_image_9,R.drawable.hike_image_10}; // Replace with your image resource IDs from drawable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initComponent();
        initNavigationMenu();
        db = new DatabaseHelper(MainActivity.this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        List<Hike> filteredlist = new DatabaseHelper(this).getHikeData("");
        // below line is to get our inflater
        MenuInflater inflater = getMenuInflater();

        // inside inflater we are inflating our menu file.
        inflater.inflate(R.menu.menu_search_setting, menu);

        // below line is to get our menu item.
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);

        // getting search view of our item.
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText,filteredlist);
                return false;
            }
        });
        return true;
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Hikes");
        Tools.setSystemBarColor(this, R.color.grey_10);
        Tools.setSystemBarLight(this);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);
//        toolbar.setNavigationIcon(R.drawable.ic_menu);
//        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.grey_80), PorterDuff.Mode.SRC_ATOP);
//        toolbar.setTitleTextColor(getResources().getColor(R.color.grey_80));
//        getSupportActionBar().setTitle("Hikes");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        Tools.setSystemBarColor(this, R.color.grey_5);
//        Tools.setSystemBarLight(this);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setTitle("Drawer White");
//        Tools.setSystemBarColor(this, R.color.grey_10);
//        Tools.setSystemBarLight(this);
    }

    private void filter(String text,List<Hike> searchList) {
        // creating a new array list to filter our data.
        List<Hike> filteredlist = new ArrayList<>();
        // running a for loop to compare elements.
        for (Hike item : searchList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.size() <1) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            mAdapter.filterList(filteredlist);
        }
    }
    private void initComponent() {
        hike_lat_long = (findViewById(R.id.hike_lat_long));
        drawer = findViewById(R.id.drawer_layout);
        drawer.setScrimColor(getResources().getColor(android.R.color.transparent));

        drawer.closeDrawer(GravityCompat.END);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        String query = getIntent().getStringExtra("query");
        List<Hike> hikes;
        if(query !=null){
             hikes = new DatabaseHelper(this).getHikeData(query);
        }else{
            hikes = new DatabaseHelper(this).getHikeData("");
        }

        //set data and list adapter

        mAdapter = new AdapterListHikes(this, hikes);
        mAdapter.notifyDataSetChanged();
        recyclerView.invalidate();
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdapterListHikes.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Hike obj, int position) {
                drawer.openDrawer(GravityCompat.END);
                ((ImageView) findViewById(R.id.image_main)).setImageResource(image_array[Integer.parseInt(obj.getImage_link())]);
                ((TextView) findViewById(R.id.hike_name)).setText(obj.getName());
                ((TextView) findViewById(R.id.hike_difficulty)).setText("Difficulty " + obj.getLevel_of_difficulty()+ " %");
                ((TextView) findViewById(R.id.hike_location)).setText(obj.getLocation());
                ((TextView) findViewById(R.id.hike_parking_available)).setText("Parking Available " + obj.getParking_available());
                ((TextView) findViewById(R.id.hike_date_time)).setText(obj.getDate());
                hike_lat_long.setText("Opens in map " +obj.getType());
                // Make the link clickable
                hike_lat_long.setMovementMethod(LinkMovementMethod.getInstance());
                hike_lat_long.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // The URL you want to open
                        String url = "https://www.google.com/maps/place/"+obj.getType();

                        // Create an implicit intent to open the browser
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                        startActivity(intent);
                    }
                });
                ((TextView) findViewById(R.id.hike_description)).setText(obj.getDescription());
                ((Button) findViewById(R.id.btn_update_hike)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, UpdateHikeActivity.class);
                        intent.putExtra("name",obj.getName());
                        intent.putExtra("location",obj.getLocation());
                        intent.putExtra("date",obj.getDate());
                        intent.putExtra("description",obj.getDescription());
                        intent.putExtra("length",obj.getLength_of_hike().toString());
                        intent.putExtra("tags",obj.getTags());
                        intent.putExtra("parking_available",obj.getParking_available());
                        intent.putExtra("level_of_difficulty",obj.getLevel_of_difficulty()+"");
                        intent.putExtra("id",obj.getId());
                        intent.putExtra("image_link",obj.getImage_link().toString());
                        startActivity(intent);
                    }
                });
                ((Button) findViewById(R.id.btn_view_observation)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, GetObservationsActivity.class);
                        intent.putExtra("id",obj.getId());
                        startActivity(intent);
                    }
                });
                ((Button) findViewById(R.id.btn_create_observation)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, CreateObservationActivity.class);
                        intent.putExtra("id",obj.getId());
                        startActivity(intent);
                    }
                });
                ((Button) findViewById(R.id.btn_delete_hike)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        deleteHikeConfirmDialog(obj.getId());

                    }
                });
            }
        });
        (findViewById(R.id.lyt_search_hike)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AdvancedSearchHike.class);
                startActivity(intent);
            }
        });
        (findViewById(R.id.lyt_home)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.END);
            }
        });
        (findViewById(R.id.lyt_delete_all_data)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllDataConfirmationDialog();

            }
        });
        (findViewById(R.id.lyt_add_hike)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateHikeActivity.class);
//                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                startActivity(intent);
            }
        });
        (findViewById(R.id.lyt_exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog();
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
    private void initNavigationMenu() {
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view_drawer);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openBracket, R.string.openBracket) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }
    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawers();
        }else {
            exitDialog();
        }
    }
    public void exitDialog(){
        new AlertDialog.Builder(MainActivity.this).setMessage("Do you want to exit ?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    private void deleteHikeConfirmDialog(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning !");
        builder.setMessage("Deleting a hike will delete this hike and it's observations. Are you sure you want to delete this hike ?");
        builder.setPositiveButton(R.string.AGREE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String condition = " WHERE id =  " + Integer.parseInt(id);
                db.deleteHike(condition);
                showSuccessDialog();
            }
        });
        builder.setNegativeButton(R.string.DISAGREE, null);
        builder.show();
    }
    private void deleteAllDataConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning !");
        builder.setMessage("Deleting all data will delete all the hikes and observations. Are you sure you want to delete all data ?");
        builder.setPositiveButton(R.string.AGREE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new DatabaseHelper(MainActivity.this).deleteAllData();
                showSuccessDialog();
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
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}