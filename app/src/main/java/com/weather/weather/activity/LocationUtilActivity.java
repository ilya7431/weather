package com.weather.weather.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.weather.weather.BuildConfig;
import com.weather.weather.R;
import com.weather.weather.helper.PrefUtils;
import com.weather.weather.network.OpenWeatherContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class LocationUtilActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private int PERMISSION_REQUEST_CODE = 1;
    private Context mContext;
    private LocationManager lm;
    private URL url;
    private String nameCity;
    private float lat;
    private float lon;
    private Intent intent;
    private GoogleApiClient googleApiClient;
    private final int ENABLE_GPS = 1000;
    private ProgressDialog progressDialog;
    private LocationRequest mLocationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        intent = getIntent();

        checkPermission();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //new LocationPoint(mContext,true);
                    checkPermission();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        try {
            if (location != null) {

                lat = (float) location.getLatitude();
                lon = (float) location.getLongitude();
                Bundle bundle = new Bundle();
                bundle.putFloat(mContext.getString(R.string.Location_lat), (float) location.getLatitude());
                bundle.putFloat(mContext.getString(R.string.Location_lon), (float) location.getLongitude());
                if (lat != 0.0) {
                    RequestLocation requestLocation = new RequestLocation();
                    requestLocation.execute(bundle);
                    setResult(RESULT_OK);
                    finish();
                } else {
                    checkPermission();

                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void enableGPS() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(LocationUtilActivity.this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);

            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can
                            // initialize location
                            // requests here.
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(LocationUtilActivity.this, ENABLE_GPS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ENABLE_GPS: {
                if (resultCode == RESULT_OK) {
                    checkPermission();
                } else if (resultCode == RESULT_CANCELED) {
                    setResult(RESULT_CANCELED);
                    finish();
                }
                break;
            }
        }

    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setInterval(1000); // Update location every second

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(getClass().getName(), "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(getClass().getName(), "GoogleApiClient connection has failed");
    }



    private class RequestLocation extends AsyncTask<Bundle,Void,Void> {

        @Override
        protected Void doInBackground(Bundle... bundles) {
            try {

                if (bundles[0].get(mContext.getString(R.string.Location_lat)) != null){
                    lat = bundles[0].getFloat(mContext.getString(R.string.Location_lat));
                    lon = bundles[0].getFloat(mContext.getString(R.string.Location_lon));
                }else if (bundles[0].get(mContext.getString(R.string.Location_name_city)) != null){
                    nameCity = bundles[0].getString(mContext.getString(R.string.Location_name_city));
                }

                String stringUrl = (OpenWeatherContract.ROOT_URL
                        + "weather?");

                if (nameCity != null && !nameCity.isEmpty()){
                    stringUrl += OpenWeatherContract.PARAM_QUERY + "=" + nameCity;
                }else stringUrl += "lat=" + lat + "&lon=" + lon ;


                url = new URL(stringUrl + "&" +OpenWeatherContract.PARAM_APPID + "=" + BuildConfig.OPEN_WEATHER_MAP_API_KEY);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            connection.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null){
                        JSONObject jsonObject = new JSONObject(inputLine);
                        PrefUtils.setPrefLocation(mContext,jsonObject.getInt("id"),jsonObject.getString("name"));
                    }

                    in.close();


                } else {
                    //TODO
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    private void checkPermission(){
        if (intent.getBooleanExtra(getString(R.string.Intent_searchGPS),false)) {
            lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        },
                        PERMISSION_REQUEST_CODE);


            }else {
                if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER) | lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
                    googleApiClient = new GoogleApiClient.Builder(this)
                            .addApi(LocationServices.API)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                            .build();
                    googleApiClient.connect();

//                    Criteria crit = new Criteria();
//                    crit.setAccuracy(Criteria.ACCURACY_COARSE);
//                    String provider = lm.getBestProvider(crit, true);
//                    lm.getLastKnownLocation(provider);
//                    Location loc = lm.getLastKnownLocation(provider);
//                    lm.requestSingleUpdate(crit, this, null);

                    progressDialog = new ProgressDialog(mContext);
                    progressDialog.setMessage("search locations");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                } else {
                    enableGPS();
                }
            }
        }else {
            RequestLocation requestLocation = new RequestLocation();
            Bundle bundle = new Bundle();
            bundle.putString(mContext.getString(R.string.Location_name_city), intent.getStringExtra(mContext.getString(R.string.Location_name_city)));
            requestLocation.execute(bundle);
            finish();
        }
    }
}

