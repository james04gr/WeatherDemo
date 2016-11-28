package com.example.james.weatherdemo.SQLiteDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.james.weatherdemo.Model.CityDto;
import com.example.james.weatherdemo.Model.FavoriteDto;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CITY_DB";

    private static final String TABLE_CITY = "CityDto";
    private static final String TABLE_FAVORITES = "FavoriteDto";

    private final static String ALL_ID = "ALL_ID";
    private final static String ALL_CITY_NAME = "ALL_CITY_NAME";
    private final static String ALL_LAT = "ALL_LAT";
    private final static String ALL_LONG = "ALL_LONG";
    private final static String ALL_CITY_CODE = "ALL_CITY_CODE";

    private final static String FAVORITE_CITY_NAME = "FAVORITE_CITY_NAME";

    private static final String CREATE_CITY_TABLE = "create table if not exists "
            + TABLE_CITY
            + " ( _id integer primary key autoincrement,"
            + ALL_ID + " INTEGER,"
            + ALL_CITY_NAME + " TEXT NOT NULL,"
            + ALL_LAT + " TEXT NOT NULL,"
            + ALL_LONG + " TEXT NOT NULL,"
            + ALL_CITY_CODE + " TEXT NOT NULL);";

    private static final String CREATE_FAVORITE_CITIES_TABLE = "create table if not exists "
            + TABLE_FAVORITES
            + " ( _id integer primary key autoincrement,"
            + FAVORITE_CITY_NAME + " TEXT NOT NULL);";

    private SQLiteDatabase db;
    private Cursor cursor;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CITY_TABLE);
        db.execSQL(CREATE_FAVORITE_CITIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    boolean insertAllCities(List<CityDto> insertlist) {

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        long startTime = System.currentTimeMillis();
        try {
            ContentValues values = new ContentValues();
            for (CityDto city : insertlist) {
                values.put(ALL_ID, city.getALL_ID());
                values.put(ALL_CITY_NAME, city.getALL_CITY_NAME());
                values.put(ALL_LAT, city.getALL_LAT());
                values.put(ALL_LONG, city.getALL_LONG());
                values.put(ALL_CITY_CODE, city.getALL_CITY_CODE());
                db.insert(TABLE_CITY, null, values);
            }
            db.setTransactionSuccessful();
            long diff = System.currentTimeMillis() - startTime;
            System.out.println("Exec Time:" + Long.toString(diff) + "ms");

        } finally {
            db.endTransaction();
            return true;
        }
    }

    public boolean insertFavoriteCity(FavoriteDto favorite) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(FAVORITE_CITY_NAME, favorite.getFAVORITE_CITY_NAME());
            db.insert(TABLE_FAVORITES, null, contentValues);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            return true;
        }
    }

    public List<String> getAllFavorites() {
        String table = TABLE_FAVORITES;
        String[] columns = {FAVORITE_CITY_NAME};
        String orderBy = FAVORITE_CITY_NAME;

        db = getReadableDatabase();
        cursor = this.db.query(table, columns, null, null, null, null, orderBy);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            List<String> result = new ArrayList<>();
            do {
                result.add(cursor.getString(cursor.getColumnIndex(FAVORITE_CITY_NAME)));
            } while (cursor.moveToNext());
            cursor.close();
            return result;
        } else {
            cursor.close();
            return new ArrayList<>();
        }
    }

    public List<String> getAllCityNames() {

        String table = TABLE_CITY;
        String[] columns = {ALL_CITY_NAME};
        String orderBy = ALL_CITY_NAME;

        db = getReadableDatabase();
        cursor = this.db.query(table, columns, null, null, null, null, orderBy);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            List<String> result = new ArrayList<>();
            do {
                result.add(cursor.getString(cursor.getColumnIndex(ALL_CITY_NAME)));
            } while (cursor.moveToNext());
            cursor.close();
            return result;
        } else {
            cursor.close();
            return null;
        }
    }

    public String getId(String cityName) {

        String table = TABLE_CITY;
        String[] columns = {ALL_ID};
        String selection = ALL_CITY_NAME + "=?";
        String[] selectionArgs = {cityName};

        db = getReadableDatabase();
        cursor = this.db.query(table, columns, selection, selectionArgs, null, null, null, null);
        cursor.moveToFirst();


        String myID = cursor.getString(cursor.getColumnIndex(ALL_ID));
        cursor.close();
        return myID;
    }
}
