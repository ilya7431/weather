package com.weather.weather;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weather.weather.data.realmModel.ForecastRealm;
import com.weather.weather.helper.GetColors;
import com.weather.weather.helper.GraphTemp;
import com.weather.weather.helper.TypeWeather;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


public class WeatherTodayFragment extends Fragment  {

    private Context mContext;
    private TextView temp ;
    private TextView press;
    private TextView wint ;
    private TextView humidity ;
    private ImageView image ;
    private RelativeLayout relativeLayout;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private  Realm realm;
    private RealmChangeListener realmChangeListener;
    private RealmResults<ForecastRealm> forecastRealms;
    private ProgressDialog progressDialog;
    GraphTemp graphTemp;




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

        graphTemp.setMinTemp("-30");
        graphTemp.setMiddleTemp("30");
        graphTemp.setHighTemp("60");
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading data");
        progressDialog.setCancelable(false);
        progressDialog.show();

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




    private void loadDATA(){

        forecastRealms = realm.where(ForecastRealm.class).findAll();
        if (forecastRealms.size() > 0){


        ForecastRealm forecastRealm = new ForecastRealm();
        for(ForecastRealm f : forecastRealms){
            forecastRealm = f;
        }
        temp.setText(forecastRealm.getList().get(0).getTempRealm().getDay().toString().split("\\.")[0] + "°");
        press.setText(forecastRealm.getList().get(0).getPressure().toString().split("\\.")[0] + mContext.getString(R.string.Press));
        wint.setText(forecastRealm.getList().get(0).getSpeed().toString() + mContext.getString(R.string.Speed));
        humidity.setText(forecastRealm.getList().get(0).getHumidity().toString()+ "%");
        GetColors getColor = new GetColors();

        long[] colors = getColor.GetImage(TypeWeather.getValue(forecastRealm.getList().get(0).getWeatherRealm().get(0).getMain()));
        Date date = new Date(forecastRealm.getList().get(0).getDt() * 1000);
        DateFormat formatOut = new SimpleDateFormat("EEEE, dd MMMM");
        String dateString = formatOut.format(date);
        actionBar.setTitle(dateString);
        image.setImageResource((int)colors[0]);
        toolbar.setBackgroundResource((int)colors[1]);
        relativeLayout.setBackgroundResource((int)colors[2]);
        progressDialog.cancel();
        }
    }


}
