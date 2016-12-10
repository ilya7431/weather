package com.weather.weather.data.realmModel;


import io.realm.RealmObject;

public class CoordRealm extends RealmObject {

    private Double lon;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    private Double lat;
}

