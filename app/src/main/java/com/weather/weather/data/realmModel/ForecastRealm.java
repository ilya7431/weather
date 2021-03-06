package com.weather.weather.data.realmModel;

import com.weather.weather.data.model.City;
import com.weather.weather.data.model.ForecastItem;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class ForecastRealm extends RealmObject {
    private CityRealm cityRealm;
    private RealmList<ForecastItemRealm> list = new RealmList<>();

    @PrimaryKey
    private long _id;

    public RealmList<ForecastItemRealm> getList() {
        return list;
    }

    public void setList(RealmList<ForecastItemRealm> list) {
        this.list = list;
    }

    public CityRealm getCityRealm() {
        return cityRealm;
    }

    public void setCityRealm(CityRealm cityRealm) {
        this.cityRealm = cityRealm;
    }


}
