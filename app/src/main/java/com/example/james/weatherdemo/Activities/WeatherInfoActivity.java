package com.example.james.weatherdemo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.james.weatherdemo.Model.FavoriteDto;
import com.example.james.weatherdemo.R;
import com.example.james.weatherdemo.SQLiteDB.DatabaseHelper;
import com.example.james.weatherdemo.Utilities.AppContext;
import com.example.james.weatherdemo.Utilities.FiveDayView;
import com.example.james.weatherdemo.Utilities.Requests;
import com.example.james.weatherdemo.Utilities.TodayView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WeatherInfoActivity extends AppCompatActivity {

    JSONObject jsonObject, todayJson, firstJson, secondJson, thirdJson, fourthJson, fifthJson = null;
    JSONArray jsonList = null;
    ListView myFavorite_listView;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_info);
        final String cityName = getIntent().getStringExtra("myCityName");
        db = new DatabaseHelper(AppContext.getContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        diplayInfos(cityName);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        myFavorite_listView = (ListView) findViewById(R.id.myFavorites);
        myFavorite_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String selection = (String) parent.getItemAtPosition(position);
                Requests requests = new Requests();
                requests.getWeatherInfo(selection, new Requests.VolleyCallbackWeather() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Intent intent = new Intent(WeatherInfoActivity.this, WeatherInfoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("myCity", jsonObject.toString());
                        bundle.putString("myCityName", selection);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        });
        List<String> allFavorites = db.getAllFavorites();
        if (allFavorites != null && allFavorites.size() > 0)
            addItems(db.getAllFavorites());

        final ImageView fab = (ImageView) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> allFavorites = db.getAllFavorites();
                if (!(allFavorites.contains(cityName))) {
                    FavoriteDto favoriteDto = new FavoriteDto();
                    favoriteDto.setFAVORITE_CITY_NAME(cityName);
                    db.insertFavoriteCity(favoriteDto);
                    addItems(db.getAllFavorites());
                    playAnimation(fab);
                }
            }
        });
    }


    private void diplayInfos(String cityName) {

        try {
            jsonObject = new JSONObject(getIntent().getStringExtra("myCity"));
            jsonList = jsonObject.getJSONArray("list");
            todayJson = jsonList.getJSONObject(0);
            firstJson = jsonList.getJSONObject(1);
            secondJson = jsonList.getJSONObject(2);
            thirdJson = jsonList.getJSONObject(3);
            fourthJson = jsonList.getJSONObject(4);
            fifthJson = jsonList.getJSONObject(5);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        View today = findViewById(R.id.todayView);
        View firstDay = findViewById(R.id.firstView);
        View secondDay = findViewById(R.id.secondView);
        View thirdDay = findViewById(R.id.thirdView);
        View fourthDay = findViewById(R.id.fourthView);
        View fifthDay = findViewById(R.id.fifthView);

        TodayView todayView = new TodayView(today, todayJson, cityName);
        FiveDayView oneDay = new FiveDayView(firstDay, firstJson, 1);
        FiveDayView twoDay = new FiveDayView(secondDay, secondJson, 2);
        FiveDayView threeDay = new FiveDayView(thirdDay, thirdJson, 3);
        FiveDayView fourDay = new FiveDayView(fourthDay, fourthJson, 4);
        FiveDayView fiveDay = new FiveDayView(fifthDay, fifthJson, 5);

    }

    private void playAnimation(ImageView myImage) {
        RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        myImage.startAnimation(rotateAnimation);
    }

    private void addItems(List<String> allFavorites) {
        if ((allFavorites.size() % 2) == 0)
            myFavorite_listView.setAdapter(new ArrayAdapter<>(AppContext.getContext(), R.layout.item_favorite_odd, R.id.favorite_city, allFavorites));
        else
            myFavorite_listView.setAdapter(new ArrayAdapter<>(AppContext.getContext(), R.layout.item_favorite_even, R.id.favorite_city, allFavorites));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(WeatherInfoActivity.this, SearchActivity.class);
            startActivity(intent);
        }
    }

}