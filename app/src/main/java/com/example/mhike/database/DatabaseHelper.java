package com.example.mhike.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mhike.model.Hike;
import com.example.mhike.model.Observation;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "2.db";
    private static final String CONTACT_TABLE_NAME = "contact";
    private static final String CONTACT_COLUMN_ID = "id";
    private static final String CONTACT_COLUMN_NAME = "name";
    private static final String CONTACT_COLUMN_EMAIL = "email";
    private static final String CONTACT_COLUMN_DATE_OF_BIRTH = "date_of_birth";
    private static final String CONTACT_COLUMN_IMAGE_ID = "image_id";


    private static final String HIKE_TABLE_NAME = "hike";
    private static final String HIKE_COLUMN_ID = "id";
    private static final String HIKE_COLUMN_NAME = "name";
    private static final String HIKE_COLUMN_IMAGE_LINK = "image_link";
    private static final String HIKE_COLUMN_LOCATION = "location";
    private static final String HIKE_COLUMN_DATE = "date";
    private static final String HIKE_COLUMN_IS_PARKING_AVAILABLE = "is_parking_available";
    private static final String HIKE_COLUMN_LENGTH = "length";
    private static final String HIKE_COLUMN_LEVEL_OF_DIFFICULTIES = "lv_of_difficulties";
    private static final String HIKE_COLUMN_DESCRIPTION = "description";
    private static final String HIKE_COLUMN_RATING = "rating";
    private static final String HIKE_COLUMN_TYPE = "type";
    private static final String HIKE_COLUMN_TAGS = "tags";

    private static final String OBSERVATION_TABLE_NAME = "observation";
    private static final String OBSERVATION_COLUMN_ID = "id";
    private static final String OBSERVATION_COLUMN_HIKE_ID = "hike_id";
    private static final String OBSERVATION_COLUMN_TITLE = "title";
    private static final String OBSERVATION_COLUMN_DATE_TIME = "created_date";
    private static final String OBSERVATION_COLUMN_ADDITIONAL_COMMENT = "additional_comment";
    private static final String OBSERVATION_COLUMN_TYPE = "type";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CONFIG_TABLE());
        sqLiteDatabase.execSQL(CREATE_HIKE_TABLE());
        sqLiteDatabase.execSQL(CREATE_OBSERVATION_TABLE());
    }


    private String CREATE_CONFIG_TABLE() {
        return "CREATE TABLE IF NOT EXISTS " + CONTACT_TABLE_NAME +
                " (" + CONTACT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CONTACT_COLUMN_NAME + " TEXT NOT NULL," +
                CONTACT_COLUMN_EMAIL + " TEXT NOT NULL," +
                CONTACT_COLUMN_DATE_OF_BIRTH + " DATETIME NOT NULL," +
                CONTACT_COLUMN_IMAGE_ID + " INTEGER NOT NULL DEFAULT 0" + ")";
    }

    private String CREATE_HIKE_TABLE() {
        return "CREATE TABLE IF NOT EXISTS " + HIKE_TABLE_NAME +
                " (" + HIKE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                HIKE_COLUMN_NAME + " TEXT NOT NULL," +
                HIKE_COLUMN_IMAGE_LINK + " TEXT DEFAULT NULL," +
                HIKE_COLUMN_LOCATION + " TEXT DEFAULT NULL," +
                HIKE_COLUMN_DATE + " DATETIME NOT NULL," +
                HIKE_COLUMN_IS_PARKING_AVAILABLE + " TEXT DEFAULT NULL," +
                HIKE_COLUMN_LENGTH + " TEXT DEFAULT NULL," +
                HIKE_COLUMN_LEVEL_OF_DIFFICULTIES + " INTEGER NOT NULL DEFAULT 0," +
                HIKE_COLUMN_DESCRIPTION + " TEXT DEFAULT NULL," +
                HIKE_COLUMN_RATING + " TEXT DEFAULT NULL," +
                HIKE_COLUMN_TYPE + " TEXT DEFAULT NULL," +
                HIKE_COLUMN_TAGS + " TEXT DEFAULT NULL " + ")";
    }

    private String CREATE_OBSERVATION_TABLE() {
        return "CREATE TABLE IF NOT EXISTS " + OBSERVATION_TABLE_NAME +
                " (" + OBSERVATION_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                OBSERVATION_COLUMN_HIKE_ID + " INTEGER NOT NULL," +
                OBSERVATION_COLUMN_TITLE + " TEXT NOT NULL," +
                OBSERVATION_COLUMN_DATE_TIME + " DATETIME NOT NULL," +
                OBSERVATION_COLUMN_ADDITIONAL_COMMENT + " TEXT DEFAULT NULL," +
                OBSERVATION_COLUMN_TYPE + " TEXT DEFAULT NULL " + ")";
    }

    public void deleteHike(String condition) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + HIKE_TABLE_NAME +  condition);
        db.close();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



    public long insertObservationData(Observation observation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OBSERVATION_COLUMN_HIKE_ID, observation.getHike_id());
        contentValues.put(OBSERVATION_COLUMN_TITLE, observation.getTitle());
        contentValues.put(OBSERVATION_COLUMN_DATE_TIME, observation.getDatetime());
        contentValues.put(OBSERVATION_COLUMN_ADDITIONAL_COMMENT, observation.getAdditionalcomment());
        contentValues.put(OBSERVATION_COLUMN_TYPE, observation.getType());

        long id = db.insert(OBSERVATION_TABLE_NAME, null, contentValues);
        db.close();

        return id;
    }


    public int updateObservationData(Observation observation, long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(OBSERVATION_COLUMN_TITLE, observation.getTitle());
        contentValues.put(OBSERVATION_COLUMN_DATE_TIME, observation.getDatetime());
        contentValues.put(OBSERVATION_COLUMN_ADDITIONAL_COMMENT, observation.getAdditionalcomment());

        // updating row
        return db.update(OBSERVATION_TABLE_NAME, contentValues, OBSERVATION_COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public void deleteObservation(String condition) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + OBSERVATION_TABLE_NAME +  condition);
        db.close();
    }
    public long insertHikeData(Hike hike){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HIKE_COLUMN_NAME, hike.getName());
        contentValues.put(HIKE_COLUMN_IMAGE_LINK, hike.getImage_link());
        contentValues.put(HIKE_COLUMN_LOCATION, hike.getLocation());
        contentValues.put(HIKE_COLUMN_DATE, hike.getDate());
        contentValues.put(HIKE_COLUMN_IS_PARKING_AVAILABLE, hike.getParking_available());
        contentValues.put(HIKE_COLUMN_LENGTH, hike.getLength_of_hike());
        contentValues.put(HIKE_COLUMN_LEVEL_OF_DIFFICULTIES, hike.getLevel_of_difficulty());
        contentValues.put(HIKE_COLUMN_DESCRIPTION, hike.getDescription());
        contentValues.put(HIKE_COLUMN_RATING, hike.getRating());
        contentValues.put(HIKE_COLUMN_TYPE, hike.getType());
        contentValues.put(HIKE_COLUMN_TAGS, hike.getTags());

        long id = db.insert(HIKE_TABLE_NAME, null, contentValues);
        db.close();

        return id;
    }

    public int updateHikeData(Hike hike, long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(HIKE_COLUMN_NAME, hike.getName());
        contentValues.put(HIKE_COLUMN_IMAGE_LINK, hike.getImage_link());
        contentValues.put(HIKE_COLUMN_LOCATION, hike.getLocation());
        contentValues.put(HIKE_COLUMN_DATE, hike.getDate());
        contentValues.put(HIKE_COLUMN_IS_PARKING_AVAILABLE, hike.getParking_available());
        contentValues.put(HIKE_COLUMN_LENGTH, hike.getLength_of_hike());
        contentValues.put(HIKE_COLUMN_LEVEL_OF_DIFFICULTIES, hike.getLevel_of_difficulty());
        contentValues.put(HIKE_COLUMN_DESCRIPTION, hike.getDescription());
        contentValues.put(HIKE_COLUMN_RATING, hike.getRating());
        contentValues.put(HIKE_COLUMN_TYPE, hike.getType());
        contentValues.put(HIKE_COLUMN_TAGS, hike.getTags());

        // updating row
        return db.update(HIKE_TABLE_NAME, contentValues, HIKE_COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    @SuppressLint("Range")
    public List<Hike> getHikeData(String condition) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Hike> hikes = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + HIKE_TABLE_NAME + condition, null);
        if (cursor != null)
            if (cursor.moveToFirst()) {
                do {
                    Hike hike = new Hike();
                    hike.setName(cursor.getString(cursor.getColumnIndex(HIKE_COLUMN_NAME)));
                    hike.setId(cursor.getString(cursor.getColumnIndex(HIKE_COLUMN_ID)));
                    hike.setImage_link(cursor.getString(cursor.getColumnIndex(HIKE_COLUMN_IMAGE_LINK)));
                    hike.setLocation(cursor.getString(cursor.getColumnIndex(HIKE_COLUMN_LOCATION)));
                    hike.setDate(cursor.getString(cursor.getColumnIndex(HIKE_COLUMN_DATE)));
                    hike.setParking_available(cursor.getString(cursor.getColumnIndex(HIKE_COLUMN_IS_PARKING_AVAILABLE)));
                    hike.setLength_of_hike(cursor.getInt(cursor.getColumnIndex(HIKE_COLUMN_LENGTH)));
                    hike.setLevel_of_difficulty(cursor.getInt(cursor.getColumnIndex(HIKE_COLUMN_LEVEL_OF_DIFFICULTIES)));
                    hike.setDescription(cursor.getString(cursor.getColumnIndex(HIKE_COLUMN_DESCRIPTION)));
                    hike.setRating(cursor.getString(cursor.getColumnIndex(HIKE_COLUMN_RATING)));
                    hike.setType(cursor.getString(cursor.getColumnIndex(HIKE_COLUMN_TYPE)));
                    hike.setTags(cursor.getString(cursor.getColumnIndex(HIKE_COLUMN_TAGS)));
                    hikes.add(hike);
                } while (cursor.moveToNext());
            }
        db.close();
        return hikes;
    }

    @SuppressLint("Range")
    public List<Observation> getObservationData(String condition) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Observation> observations = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + OBSERVATION_TABLE_NAME + condition, null);
        if (cursor != null)
            if (cursor.moveToFirst()) {
                do {
                    Observation observation = new Observation();
                    observation.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(OBSERVATION_COLUMN_ID))));
                    observation.setHike_id(Integer.valueOf(cursor.getString(cursor.getColumnIndex(OBSERVATION_COLUMN_HIKE_ID))));
                    observation.setTitle(cursor.getString(cursor.getColumnIndex(OBSERVATION_COLUMN_TITLE)));
                    observation.setAdditionalcomment(cursor.getString(cursor.getColumnIndex(OBSERVATION_COLUMN_ADDITIONAL_COMMENT)));
                    observation.setDatetime(cursor.getString(cursor.getColumnIndex(OBSERVATION_COLUMN_DATE_TIME)));
                    observation.setType(cursor.getString(cursor.getColumnIndex(OBSERVATION_COLUMN_TYPE)));
                    observations.add(observation);
                } while (cursor.moveToNext());
            }
        db.close();
        return observations;
    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE from " + HIKE_TABLE_NAME + " ; ");
        db.execSQL("DELETE from " + OBSERVATION_TABLE_NAME + " ; ");
        db.close();
    }
}
