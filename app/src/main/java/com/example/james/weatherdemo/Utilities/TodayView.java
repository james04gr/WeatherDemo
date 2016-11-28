package com.example.james.weatherdemo.Utilities;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.james.weatherdemo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


public class TodayView {

    private View view;
    private ImageView weather_icon;
    private JSONObject jsonObject;
    private String cityName;

    public TodayView(View view, JSONObject jsonObject, String cityName) {
        this.view = view;
        this.jsonObject = jsonObject;
        this.cityName = cityName;
        setUpView();
    }

    private void setUpView() {

        TextView myDay = (TextView) view.findViewById(R.id.myDay_textView);
        TextView myCity = (TextView) view.findViewById(R.id.myCity_textView);
        weather_icon = (ImageView) view.findViewById(R.id.weather_icon);
        TextView maxTemp = (TextView) view.findViewById(R.id.maxTemp);
        TextView minTemp = (TextView) view.findViewById(R.id.minTemp);

        try {
            myDay.setText(R.string.TODAY);
            myCity.setText(cityName);

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
            Date todayDate = new Date();
            if (todayDate.getHours() < 19 && todayDate.getHours() > 7) {
                weather_icon.setImageResource(R.drawable.clear_day);
            } else {
                weather_icon.setImageResource(R.drawable.clear_night);
            }
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

}
