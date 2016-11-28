package com.example.james.weatherdemo.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.james.weatherdemo.R;
import com.example.james.weatherdemo.Utilities.AppContext;
import com.example.james.weatherdemo.Utilities.CityAutoCompleteAdapter;
import com.example.james.weatherdemo.Utilities.Requests;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.location.LocationServices.API;
import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class SearchActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    final static int THERSHOLD = 3;
    GoogleApiClient mGoogleApiClient;
    ImageView clean;
    private AutoCompleteTextView mySearchView;
    private Requests requests = new Requests();
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(AppContext.getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(API)
                    .build();
        }


        TextView textView1 = (TextView) findViewById(R.id.textView1);
        TextView textView2 = (TextView) findViewById(R.id.textView2);

        mySearchView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        mySearchView.setThreshold(THERSHOLD);
        CityAutoCompleteAdapter myAdapter = new CityAutoCompleteAdapter(AppContext.getContext(), R.layout.item_search, AppContext.getAllCities());
        myAdapter.setNotifyOnChange(true);
        mySearchView.setAdapter(myAdapter);

        clean = (ImageView) findViewById(R.id.clean_button);
        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySearchView.setText("");
            }
        });

        mySearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                makeRequest(selection);
            }
        });


        Button location = (Button) findViewById(R.id.myLocation);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Geocoder mGeocoder = new Geocoder(SearchActivity.this, Locale.ENGLISH);
                try {
                    List<Address> address = mGeocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
                    String myCity = "";
                    if (address.get(0).getAdminArea() == null) {
                        myCity = address.get(0).getCountryName();
                    } else {
                        myCity = address.get(0).getAdminArea();
                    }
                    makeRequest(myCity);
                } catch (IOException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void makeRequest(final String cityName) {
        requests.getWeatherInfo(cityName, new Requests.VolleyCallbackWeather() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                Intent intent = new Intent(SearchActivity.this, WeatherInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("myCity", jsonObject.toString());
                bundle.putString("myCityName", cityName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

}
