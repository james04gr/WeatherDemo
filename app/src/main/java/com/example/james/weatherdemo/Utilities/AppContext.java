package com.example.james.weatherdemo.Utilities;

import android.app.Application;
import android.content.Context;

import com.example.james.weatherdemo.SQLiteDB.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;


public class AppContext extends Application {
    public static List<String> allCities;
    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static List<String> getAllCities() {
        return allCities;
    }

    public static void listSetUp(final DatabaseHelper db) {
        allCities = new ArrayList<>();
        allCities = db.getAllCityNames();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
