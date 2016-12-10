package com.weather.weather.data.realmModel;


import com.weather.weather.data.model.Coord;

import io.realm.RealmObject;

public class CityRealm extends RealmObject {
    private Integer id;
    private String name;
    private CoordRealm coord;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public CoordRealm getCoord() {
        return coord;
    }

    public void setCoord(CoordRealm coord) {
        this.coord = coord;
    }

    private String country;
}
