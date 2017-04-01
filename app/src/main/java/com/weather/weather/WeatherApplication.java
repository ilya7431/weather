package com.weather.weather;


import android.app.Application;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.PeriodicTask;
import com.weather.weather.helper.PrefUtils;
import com.weather.weather.services.LoadingData;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class WeatherApplication extends Application {

    private GcmNetworkManager mGcmNetworkManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        mGcmNetworkManager = GcmNetworkManager.getInstance(this);
        //startService(new Intent(this, LoadingData.class));
        OneoffTask taskOne = new OneoffTask.Builder()
                .setService(LoadingData.class)
                .setTag("Update data")
                .setExecutionWindow(0L, 3600L)
                .build();

        mGcmNetworkManager.schedule(taskOne);

        if (PrefUtils.getPrefSwitchTimeUpdate(this)){
            if (PrefUtils.getPrefTimeUpdate(this) != 0){

                PeriodicTask task = new PeriodicTask.Builder()
                        .setService(LoadingData.class)
                        .setTag("Update Time")
                        .setPeriod((PrefUtils.getPrefTimeUpdate(this)*60*60))
                        .build();

                mGcmNetworkManager.schedule(task);
            }
        }
    }


}
