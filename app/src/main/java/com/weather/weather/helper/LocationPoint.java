package com.weather.weather.helper;


import android.Manifest;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.weather.weather.BuildConfig;
import com.weather.weather.LocationUtilActivity;
import com.weather.weather.R;
import com.weather.weather.network.OpenWeatherContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LocationPoint extends AsyncTask<Bundle,Void,Void> implements LocationListener {

    private Context mContext;
    private LocationManager lm;
    private Bundle bundle;
    private URL url;
    private String nameCity;
    private float lat;
    private float lon;
    private int PERMISSION_REQUEST_CODE;


    /**
     *
     * @param context
     * @param searchGPS Search via GPS
     */
    public LocationPoint(Context context,boolean searchGPS) {

        mContext = context;

        if (searchGPS) {
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(context,LocationUtilActivity.class);
                context.startActivity(intent);



            }else {
                if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    lm.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);

                } else {
                    lm.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
                }
            }
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
                if (lat != 0.0){
                    this.execute(bundle);
                }else {
                    new LocationPoint(mContext,true);

                }



            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(final String provider) {

        Message msg = handler.obtainMessage();
        msg.arg1 = 1;
        handler.sendMessage(msg);


    }





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

            if (nameCity != null && nameCity.isEmpty()){
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
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.arg1 == 1){

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage("Your GPS is disabled! Would you like to enable it?").setCancelable(false).setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            mContext.startActivity(gpsOptionsIntent);
                        }
                    });
                    builder.setNegativeButton("Do nothing", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();

            }

        }
    };
}