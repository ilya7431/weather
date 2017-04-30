package com.weather.weather.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weather.weather.R;
import com.weather.weather.data.model.Weather;
import com.weather.weather.data.model.today.WeatherToday;
import com.weather.weather.fragment.WeatherTodayFragment;
import com.weather.weather.fragment.WeatherWeekFragment;
import com.weather.weather.helper.ClearCache;
import com.weather.weather.helper.PrefUtils;
import com.weather.weather.network.RestAPI;

import java.net.UnknownHostException;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context mContext;
    private TextView cityName;
    private LinearLayout linearLayoutDrawerExit;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private boolean isTablet;
    private final int START_SETTING = 10;
    private Subscription mSubscription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            linearLayoutDrawerExit = (LinearLayout) findViewById(R.id.li_drawer_exit);
            cityName = (TextView) findViewById(R.id.tx_city_name);

            mContext = this;
            fragmentManager = getFragmentManager();

            if (this.findViewById(R.id.drawer_layout).getTag() == getString(R.string.Tag_Main_View)) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(this);

                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
                drawer.setScrimColor(Color.TRANSPARENT);
                isTablet = true;
            } else {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.setDrawerListener(toggle);
                toggle.syncState();
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(this);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadingWeather();

    }

    @Override
    public void onBackPressed() {
        if (!isTablet) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                ClearCache.deleteCache(this);
                super.onBackPressed();
            }
        } else {
            ClearCache.deleteCache(this);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_weather_today: {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.set_fragment, new WeatherTodayFragment());
                fragmentTransaction.commit();
                break;
            }

            case R.id.nav_weather_week: {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.set_fragment, new WeatherWeekFragment());
                fragmentTransaction.commit();
                break;
            }

            case R.id.nav_setting: {
                Intent intent = new Intent(mContext, SettingActivity.class);
                startActivityForResult(intent, START_SETTING);
                break;
            }
        }
        if (!isTablet) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIn) {
        super.onActivityResult(requestCode, resultCode, dataIn);
        switch (requestCode) {
            case START_SETTING: {
//                if (resultCode == RESULT_OK) {
//                    City city = PrefUtils.getPrefLocation(mContext);
//                    cityName.setText(city.getName());
//                    fragmentManager.beginTransaction().replace(R.id.set_fragment, weatherTodayFragment).commit();
//
//                }
                break;
            }
        }


    }

    void loadingWeather() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        //TODO show progress dialog

        mSubscription = RestAPI.getApi()
                .getWeather(PrefUtils.getCityName(mContext),"eng")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WeatherToday>() {

                    @Override
                    public void onCompleted() {
                        //TODO hide show progress dialog
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof UnknownHostException){
                            //TODO get data from DB
                        }else {
                            //TODO show message error
                        }
                    }

                    @Override
                    public void onNext(WeatherToday weather) {
                        if (weather != null){
                            showWeatherDay(weather);
                        }
                    }
                    });
    }

    private void showWeatherDay(WeatherToday weather){
        WeatherTodayFragment weatherToday = new WeatherTodayFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.bundle_weather_key),weather);

        weatherToday.setArguments(bundle);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.set_fragment, weatherToday);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }


}
