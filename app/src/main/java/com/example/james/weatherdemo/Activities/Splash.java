package com.example.james.weatherdemo.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.james.weatherdemo.R;
import com.example.james.weatherdemo.SQLiteDB.CSVReader;
import com.example.james.weatherdemo.SQLiteDB.DatabaseHelper;
import com.example.james.weatherdemo.Utilities.AppContext;

public class Splash extends AppCompatActivity {

    String url = "http://openweathermap.org/help/city_list.txt";
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prefs = getSharedPreferences("com.example.james.weatherdemo", MODE_PRIVATE);

        TextView textView = (TextView) findViewById(R.id.textView);
        final ImageView imageView = (ImageView) findViewById(R.id.weather_icon);
        final Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anim_splash);
        imageView.startAnimation(animation);

        class SetUpData extends AsyncTask<Void, Void, Boolean> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                if (prefs.getBoolean("firstRun", true)) {
                    CSVReader csvReader = new CSVReader();
                    if (csvReader.downFile(url)) {
                        prefs.edit().putBoolean("firstRun", false).apply();
                        AppContext.listSetUp(new DatabaseHelper(AppContext.getContext()));
                        return true;
                    }
                } else {
                    AppContext.listSetUp(new DatabaseHelper(AppContext.getContext()));
                    return true;
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean bool) {
                super.onPostExecute(bool);

                imageView.setVisibility(View.GONE);
                animation.cancel();
                Intent intent = new Intent(Splash.this, SearchActivity.class);
                startActivity(intent);
            }
        }
        new SetUpData().execute();
    }
}

