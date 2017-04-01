package com.weather.weather.fragment;

import android.animation.ObjectAnimator;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.weather.weather.R;
import com.weather.weather.data.model.Forecast;
import com.weather.weather.data.realmModel.ForecastRealm;
import com.weather.weather.helper.GetColors;
import com.weather.weather.helper.GraphTemp;
import com.weather.weather.helper.ImageWeather;
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
    private TextView press;
    private TextView wint;
    private TextView desc;
    private TextView humidity;
    private ImageView image;
    private RelativeLayout relativeLayout;
    private Toolbar toolbar;
    private ActionBar actionBar;
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
        desc = (TextView) getActivity().findViewById(R.id.tx_desc);
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
        super.onDestroy();
        if (toolbar != null) toolbar.setBackgroundResource(R.color.colorDrawer);
    }

//    private void loadDATA() {
//
//        forecastRealms = realm.where(ForecastRealm.class).findAll();
//        if (forecastRealms.size() > 0) {
//
//
//            ForecastRealm forecastRealm = new ForecastRealm();
//            for (ForecastRealm f : forecastRealms) {
//                forecastRealm = f;
//            }
//            temp.setText(forecastRealm.getForecastItem().get(0).getTempRealm().getDay().toString().split("\\.")[0] + "°");
//            press.setText(forecastRealm.getForecastItem().get(0).getPressure().toString().split("\\.")[0] + mContext.getString(R.string.Press));
//            wint.setText(forecastRealm.getForecastItem().get(0).getSpeed().toString() + mContext.getString(R.string.Speed));
//            humidity.setText(forecastRealm.getForecastItem().get(0).getHumidity().toString() + "%");
//            GetColors getColor = new GetColors();
//
//            long[] colors = getColor.GetImage(TypeWeather.getValue(forecastRealm.getForecastItem().get(0).getWeatherRealm().get(0).getMain()));
//            Date date = new Date(forecastRealm.getForecastItem().get(0).getDt() * 1000);
//            DateFormat formatOut = new SimpleDateFormat(getString(R.string.simple_dae_format));
//            String dateString = formatOut.format(date);
//            actionBar.setTitle(dateString);
//            image.setImageResource((int) colors[0]);
//            toolbar.setBackgroundResource((int) colors[1]);
//            relativeLayout.setBackgroundResource((int) colors[2]);
//            progressDialog.cancel();
//        }
//    }

    private void loadForecast() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }

        mSubscription = RestAPI.getApi()
                .getForecast(PrefUtils.getCityName(getActivity().getApplicationContext()),
                        getString(R.string.lang_app),
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

                            Toast.makeText(getActivity().getApplicationContext(),getString(R.string.CheckConnection),Toast.LENGTH_SHORT).show();

                            //TODO check data in database and output data
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
            Date date = new Date(mForecast.getForecastItem().get(0).getDt() * 1000);
            DateFormat formatOut = new SimpleDateFormat(getString(R.string.simple_dae_format), Locale.getDefault());
            actionBar.setTitle(formatOut.format(date));

            String pressure = String.valueOf(mForecast.getForecastItem().get(0).getPressure()).split("\\.")[0] + " " + mContext.getString(R.string.Press);
            String speed = String.valueOf(mForecast.getForecastItem().get(0).getSpeed()) + " " + mContext.getString(R.string.Speed);
            String humidityString = String.valueOf(mForecast.getForecastItem().get(0).getHumidity()) + "%";
            String minTemp = String.valueOf(mForecast.getForecastItem().get(0).getTemp().getMin()).split("\\.")[0] + "°";
            String middleTemp = String.valueOf(mForecast.getForecastItem().get(0).getTemp().getDay()).split("\\.")[0] + "°";
            String maxTemp = String.valueOf(mForecast.getForecastItem().get(0).getTemp().getMax()).split("\\.")[0] + "°";

            graphTemp.setMinTemp(minTemp);
            graphTemp.setMiddleTemp(middleTemp);
            graphTemp.setMaxTemp(maxTemp);
            press.setText(pressure);
            wint.setText(speed);
            humidity.setText(humidityString);
            desc.setText(mForecast.getForecastItem().get(0).getWeather().get(0).getDescription());

            GetColors getColor = new GetColors();

            long[] colors = getColor.GetImage(TypeWeather.getValue(mForecast.getForecastItem().get(0).getWeather().get(0).getMain()));
            image.setImageDrawable(ImageWeather.getImageWeather(getResources(),mForecast.getForecastItem().get(0).getWeather().get(0).getIcon()));
            toolbar.setBackgroundResource((int) colors[1]);
            relativeLayout.setBackgroundResource((int) colors[2]);

            showAnimationImage();
        }
    }

    private void showAnimationImage(){
        final TranslateAnimation translateAnimationDown = new TranslateAnimation(0,0,-20,20);
        final TranslateAnimation translateAnimationUP = new TranslateAnimation(0,0,20,-20);
        translateAnimationDown.setDuration(2000);
        translateAnimationUP.setDuration(2000);

        image.startAnimation(translateAnimationDown);

        translateAnimationDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                image.startAnimation(translateAnimationUP);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        translateAnimationUP.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                image.startAnimation(translateAnimationDown);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
