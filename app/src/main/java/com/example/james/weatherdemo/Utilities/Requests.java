package com.example.james.weatherdemo.Utilities;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.james.weatherdemo.SQLiteDB.DatabaseHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Requests {
    private String api_key = "790ea50be7096295732e70d03f02de05";


    public void getWeatherInfo(final String cityName, final VolleyCallbackWeather volleyCallbackWeather) {
        DatabaseHelper db = new DatabaseHelper(AppContext.getContext());
        final String cityID = db.getId(cityName);
        String myUrl = "http://api.openweathermap.org/data/2.5/forecast?";
        String url = myUrl + "id=" + cityID + "&units=metric" + "&APPID=" + api_key;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        volleyCallbackWeather.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ErrorToast errorToast = new ErrorToast();
                        String string = "Error Requesting WeatherInfo for this City";
                        errorToast.ShowToastMsg(string);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Log.v("checkLogin", "getParams");
                Map<String, String> params = new HashMap<>();

                params.put("id", cityID);
                params.put("APPID", api_key);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.v("checkLogin", "getHeaders");
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json;charset=utf-8");
                headers.put("Accept", "application/json;charset=utf-8");

                return headers;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                response.headers.put("Content-Type", "application/json;charset=utf-8");
                response.headers.put("Accept", "application/json;charset=utf-8");
                return super.parseNetworkResponse(response);
            }
        };
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        RequestQueue queue = Volley.newRequestQueue(AppContext.getContext());
        queue.add(jsonObjectRequest);

    }


    public interface VolleyCallbackWeather {
        void onSuccess(JSONObject jsonObject);
    }
}
