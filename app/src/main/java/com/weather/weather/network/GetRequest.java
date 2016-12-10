package com.weather.weather.network;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.weather.weather.R;
import com.weather.weather.data.model.City;
import com.weather.weather.helper.PrefUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;


public class GetRequest extends AsyncTaskLoader<String> {


    private String returnValue;
    private Context mContext;
    private boolean checkSwitchWifi;
    private URL url;
    private String[] data;

    public GetRequest(Context context,Bundle data) {
        super(context);
       mContext = context;


    }

    @Override
    public String loadInBackground() {
        try {




            ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();


            if (PrefUtils.getPrefUpdateWIFI(mContext)){

                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                    checkSwitchWifi = true;

                }else checkSwitchWifi = false;

            }else  checkSwitchWifi = true;


            if (checkSwitchWifi){
                //mContext.getString(R.string.UrlTest)
                City city = PrefUtils.getPrefLocation(mContext);
                 url = new URL(OpenWeatherContract.ROOT_URL + OpenWeatherContract.METHOD_GET_DAILY_FORECAST
                         +  ("&" +OpenWeatherContract.PARAM_ID)
                         + "=" + city.id
                         + ("&" + OpenWeatherContract.PARAM_DAYS + "=7"));

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            connection.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null)
                        returnValue = inputLine;
                    in.close();


                } else {
                    returnValue = mContext.getString(R.string.ExceptionHTTPLocal);
                }
            }else returnValue = (mContext.getString(R.string.DisableWifi));

        }catch (UnknownHostException e){
            returnValue = mContext.getString(R.string.ExceptionHost);

        }catch (ConnectException e){
            returnValue = mContext.getString(R.string.ExceptionConnect);

        }catch (Exception e) {
            returnValue = mContext.getString(R.string.ExceptionAPPLocal);
        }
        return returnValue;
    }

}






