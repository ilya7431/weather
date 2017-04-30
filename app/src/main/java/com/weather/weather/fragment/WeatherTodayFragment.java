package com.weather.weather.fragment;

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
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.weather.weather.R;
import com.weather.weather.data.model.Forecast;
import com.weather.weather.data.model.Weather;
import com.weather.weather.data.model.today.WeatherToday;
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
    private WeatherToday weather;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_weather_today, null);
        graphTemp = (GraphTemp) view.findViewById(R.id.graph);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        desc = (TextView) view.findViewById(R.id.tx_desc);
        press = (TextView) view.findViewById(R.id.tx_pressure_day);
        wint = (TextView) view.findViewById(R.id.tx_wind);
        humidity = (TextView) view.findViewById(R.id.tx_humidity);
        image = (ImageView) view.findViewById(R.id.im_weather_day);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.layout_info_day);

        mContext = getActivity();

        if (savedInstanceState != null) {
            weather = savedInstanceState.getParcelable(getString(R.string.bundle_weather_key));
        } else {
            weather = getArguments().getParcelable(getString(R.string.bundle_weather_key));
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);

        loadWeather();
       // loadForecast();
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

    private void loadWeather(){
        if (weather != null){
            Date date = new Date(weather.getDt() * 1000);
            DateFormat formatOut = new SimpleDateFormat(getString(R.string.simple_dae_format), Locale.getDefault());
            actionBar.setTitle(formatOut.format(date));

            String pressure = String.valueOf(weather.getMain().getPressure()).split("\\.")[0] + " " + mContext.getString(R.string.Press);
            String speed = String.valueOf(weather.getWind().getSpeed()) + " " + mContext.getString(R.string.Speed);
            String humidityString = String.valueOf(weather.getMain().getHumidity()) + "%";
            String minTemp = String.valueOf(weather.getMain().getTempMin()).split("\\.")[0] + "°";
            String middleTemp = String.valueOf(weather.getMain().getTemp()).split("\\.")[0] + "°";
            String maxTemp = String.valueOf(weather.getMain().getTempMax()).split("\\.")[0] + "°";

            graphTemp.setMinTemp(minTemp);
            graphTemp.setMiddleTemp(middleTemp);
            graphTemp.setMaxTemp(maxTemp);
            press.setText(pressure);
            wint.setText(speed);
            humidity.setText(humidityString);
            desc.setText(weather.getWeather().get(0).getDescription());

            GetColors getColor = new GetColors();

            long[] colors = getColor.GetImage(TypeWeather.getValue(weather.getWeather().get(0).getMain()));
            image.setImageDrawable(ImageWeather.getImageWeather(getResources(),weather.getWeather().get(0).getIcon()));
            toolbar.setBackgroundResource((int) colors[1]);
            relativeLayout.setBackgroundResource((int) colors[2]);

            showAnimationImage();
        }
    }

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
            toolbar.setBackgroundColor((int) colors[1]);
            relativeLayout.setBackgroundColor((int) colors[2]);

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
