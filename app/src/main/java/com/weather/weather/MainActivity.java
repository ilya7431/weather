package com.weather.weather;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weather.weather.data.model.City;
import com.weather.weather.helper.ClearCache;
import com.weather.weather.helper.PrefUtils;
import com.weather.weather.services.LoadingData;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context mContext;
    private TextView cityName;
    private LinearLayout linearLayoutDrawerExit;
    private WeatherTodayFragment weatherTodayFragment;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private boolean isTablet;
    private final int START_SETTING = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            linearLayoutDrawerExit = (LinearLayout) findViewById(R.id.li_drawer_exit);

            mContext = this;
            weatherTodayFragment = new WeatherTodayFragment();
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
            cityName = (TextView) findViewById(R.id.tx_city_name);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadingData();

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_weather_today: {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.set_fragment, weatherTodayFragment);
                fragmentTransaction.commit();
                break;
            }

            case R.id.nav_weather_week: {
                fragmentManager = getFragmentManager();
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
                if (resultCode == RESULT_OK) {
                    City city = PrefUtils.getPrefLocation(mContext);
                    cityName.setText(city.name);
                    fragmentManager.beginTransaction().replace(R.id.set_fragment, weatherTodayFragment).commit();

                }
                break;
            }
        }


    }

    void loadingData() {
        try {
            fragmentManager.beginTransaction()
                    .replace(R.id.set_fragment, weatherTodayFragment)
                    .commit();

            cityName.setText(PrefUtils.getPrefLocation(mContext).name);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ClearCache.deleteCache(this);

    }


}
