package com.example.james.weatherdemo.Utilities;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.james.weatherdemo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class FiveDayView {

    private View view;
    private ImageView weather_icon;
    private JSONObject jsonObject;
    private int i;


    public FiveDayView(View view, JSONObject jsonObject, int i) {
        this.view = view;
        this.jsonObject = jsonObject;
        this.i = i;
        setUpView();
    }

    private void setUpView() {
        TextView myDay = (TextView) view.findViewById(R.id.myDay_textView);
        weather_icon = (ImageView) view.findViewById(R.id.weather_icon);
        TextView maxTemp = (TextView) view.findViewById(R.id.maxTemp);
        TextView minTemp = (TextView) view.findViewById(R.id.minTemp);

        try {

            myDay.setText(getDay(i));

            JSONObject main = jsonObject.getJSONObject("main");
            maxTemp.setText(String.format("%.1f", main.getDouble("temp_max")) + " ℃");
            minTemp.setText(String.format("%.1f", main.getDouble("temp_min")) + " ℃");

            JSONObject details = jsonObject.getJSONArray("weather").getJSONObject(0);
            setWeatherIcon(details.getInt("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setWeatherIcon(int mIcon) {
        int id = mIcon / 100;
        if (mIcon == 800) {
            weather_icon.setImageResource(R.drawable.clear_day);
        } else {
            switch (id) {
                case 2:
                    weather_icon.setImageResource(R.drawable.wind);
                    break;
                case 3:
                    weather_icon.setImageResource(R.drawable.sleet);
                    break;
                case 7:
                    weather_icon.setImageResource(R.drawable.fog);
                    break;
                case 8:
                    weather_icon.setImageResource(R.drawable.cloudy);
                    break;
                case 6:
                    weather_icon.setImageResource(R.drawable.snow);
                    break;
                case 5:
                    weather_icon.setImageResource(R.drawable.rain);
                    break;
            }
        }
        weather_icon.setVisibility(View.VISIBLE);
    }

    private String getDay(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK, i);
        Date date = calendar.getTime();
        return new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime());
    }

}
