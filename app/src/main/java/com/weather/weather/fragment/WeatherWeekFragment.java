package com.weather.weather.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.weather.weather.R;
import com.weather.weather.adapters.ListAdapter;
import com.weather.weather.data.realmModel.ForecastRealm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


public class WeatherWeekFragment extends Fragment  {


    private Context mContext;
    private ListView listWeather;
    private ListAdapter listAdapter;
    private RealmResults<ForecastRealm> forecastRealms;
    private RealmChangeListener realmChangeListener;
    private  Realm realm;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.content_weather_week, null);
    }


    @Override
    public void onStart() {
        super.onStart();
        mContext = getActivity();
        realm = Realm.getDefaultInstance();
        realmChangeListener = new RealmChangeListener() {
            @Override
            public void onChange(Object element) {

                loadDATA();
            }
        };
        realm.addChangeListener(realmChangeListener);
        loadDATA();

    }

    @Override
    public void onDestroy() {
        realm.removeChangeListener(realmChangeListener);
        realm.close();
        super.onDestroy();
    }


    private void loadDATA(){

        forecastRealms = realm.where(ForecastRealm.class).findAll();
        if (forecastRealms.size() > 0) {
            ForecastRealm forecastRealm = new ForecastRealm();
            for (ForecastRealm f : forecastRealms) {
                forecastRealm = f;
            }
            listWeather = (ListView) getActivity().findViewById(R.id.lv_weathers);

            String[][] arrayInfo = new String[forecastRealm.getList().size()][6];
            for (int i = 0; i < forecastRealm.getList().size(); i++) {

                arrayInfo[i][1] = forecastRealm.getList().get(i).getTempRealm().getDay().toString().split("\\.")[0] + "°";

                arrayInfo[i][2] = forecastRealm.getList().get(i).getPressure().toString().split("\\.")[0] + mContext.getString(R.string.Press);
                arrayInfo[i][3] = forecastRealm.getList().get(i).getWeatherRealm().get(0).getMain();
                arrayInfo[i][4] = forecastRealm.getList().get(i).getSpeed().toString() + mContext.getString(R.string.Speed);
                arrayInfo[i][5] = forecastRealm.getList().get(i).getHumidity().toString() + "%";
                Date date = new Date(forecastRealm.getList().get(i).getDt() * 1000);
                DateFormat formatOut = new SimpleDateFormat("EEEE, dd MMMM");
                String dateString = formatOut.format(date);
                arrayInfo[i][0] = dateString;


            }

            listAdapter = new ListAdapter(arrayInfo);
            listWeather.setAdapter(listAdapter);
        }
    }


}
