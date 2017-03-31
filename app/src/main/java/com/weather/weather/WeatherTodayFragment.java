package com.weather.weather;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weather.weather.data.model.Forecast;
import com.weather.weather.data.realmModel.ForecastRealm;
import com.weather.weather.helper.GetColors;
import com.weather.weather.helper.GraphTemp;
import com.weather.weather.helper.PrefUtils;
import com.weather.weather.helper.TypeWeather;
import com.weather.weather.network.RestAPI;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class WeatherTodayFragment extends Fragment {

    private Context mContext;
    private TextView temp;
    private TextView press;
    private TextView wint;
    private TextView humidity;
    private ImageView image;
    private RelativeLayout relativeLayout;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private Realm realm;
    private RealmChangeListener realmChangeListener;
    private RealmResults<ForecastRealm> forecastRealms;
    private ProgressDialog progressDialog;
    private GraphTemp graphTemp;
    private Subscription mSubscription;
    private Forecast mForecast;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_weather_today, null);
        graphTemp = (GraphTemp) view.findViewById(R.id.graph);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


        mContext = getActivity();
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        temp = (TextView) getActivity().findViewById(R.id.tx_temperature_day);
        press = (TextView) getActivity().findViewById(R.id.tx_pressure_day);
        wint = (TextView) getActivity().findViewById(R.id.tx_wind);
        humidity = (TextView) getActivity().findViewById(R.id.tx_humidity);
        image = (ImageView) getActivity().findViewById(R.id.im_weather_day);
        relativeLayout = (RelativeLayout) getActivity().findViewById(R.id.layout_info_day);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);

//        progressDialog = new ProgressDialog(mContext);
//        progressDialog.setMessage("Loading data");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//
//        realm = Realm.getDefaultInstance();
//        realmChangeListener = new RealmChangeListener() {
//            @Override
//            public void onChange(Object element) {
//
//                loadDATA();
//            }
//        };
//        realm.addChangeListener(realmChangeListener);
        loadForecast();
    }


//

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        if (toolbar != null) toolbar.setBackgroundResource(R.color.colorDrawer);
        realm.removeChangeListener(realmChangeListener);
        realm.close();
        super.onDestroy();
    }

    private void loadDATA() {

        forecastRealms = realm.where(ForecastRealm.class).findAll();
        if (forecastRealms.size() > 0) {


            ForecastRealm forecastRealm = new ForecastRealm();
            for (ForecastRealm f : forecastRealms) {
                forecastRealm = f;
            }
            temp.setText(forecastRealm.getList().get(0).getTempRealm().getDay().toString().split("\\.")[0] + "°");
            press.setText(forecastRealm.getList().get(0).getPressure().toString().split("\\.")[0] + mContext.getString(R.string.Press));
            wint.setText(forecastRealm.getList().get(0).getSpeed().toString() + mContext.getString(R.string.Speed));
            humidity.setText(forecastRealm.getList().get(0).getHumidity().toString() + "%");
            GetColors getColor = new GetColors();

            long[] colors = getColor.GetImage(TypeWeather.getValue(forecastRealm.getList().get(0).getWeatherRealm().get(0).getMain()));
            Date date = new Date(forecastRealm.getList().get(0).getDt() * 1000);
            DateFormat formatOut = new SimpleDateFormat(getString(R.string.simple_dae_format));
            String dateString = formatOut.format(date);
            actionBar.setTitle(dateString);
            image.setImageResource((int) colors[0]);
            toolbar.setBackgroundResource((int) colors[1]);
            relativeLayout.setBackgroundResource((int) colors[2]);
            progressDialog.cancel();
        }
    }

    private void loadForecast() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }

        mSubscription = RestAPI.getApi()
                .getForecast(PrefUtils.getCityName(getActivity().getApplicationContext()),
                        PrefUtils.getSelectDay(getActivity().getApplicationContext()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Forecast>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof UnknownHostException){
                            Log.d("loadForecastError", e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Forecast forecast) {
                        mForecast = forecast;
                        loadForecastOnDisplay();
                    }
                });
    }

    private void loadForecastOnDisplay(){
        if (mForecast != null){
            Date date = new Date(mForecast.list.get(0).dt * 1000);
            DateFormat formatOut = new SimpleDateFormat(getString(R.string.simple_dae_format), Locale.getDefault());
            actionBar.setTitle(formatOut.format(date));

            String pressure = mForecast.list.get(0).pressure.toString().split("\\.")[0] + mContext.getString(R.string.Press);
            String speed = mForecast.list.get(0).speed.toString() + mContext.getString(R.string.Speed);
            String humidityString = mForecast.list.get(0).humidity.toString() + "%";
            String minTemp = mForecast.list.get(0).temp.min.toString().split("\\.")[0] + "°";
            String middleTemp = mForecast.list.get(0).temp.day.toString().split("\\.")[0] + "°";
            String maxTemp = mForecast.list.get(0).temp.max.toString().split("\\.")[0] + "°";

            graphTemp.setMinTemp(minTemp);
            graphTemp.setMiddleTemp(middleTemp);
            graphTemp.setMaxTemp(maxTemp);
            press.setText(pressure);
            wint.setText(speed);
            humidity.setText(humidityString);

            GetColors getColor = new GetColors();

            long[] colors = getColor.GetImage(TypeWeather.getValue(mForecast.list.get(0).weather.get(0).main));
            image.setImageResource((int) colors[0]);
            toolbar.setBackgroundResource((int) colors[1]);
            relativeLayout.setBackgroundResource((int) colors[2]);
        }
    }
}
