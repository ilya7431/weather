package com.weather.weather.data.realmModel;

import com.weather.weather.data.model.Temp;
import com.weather.weather.data.model.Weather;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;


public class ForecastItemRealm extends RealmObject {

    private Long dt;
    private TempRealm tempRealm;
    private Float pressure;
    private Integer humidity;
    private RealmList<WeatherRealm> weatherRealm = new RealmList<>();
    private Float speed;
    private Integer deg;
    private Integer clouds;
    private Float rain;


    public RealmList<WeatherRealm> getWeatherRealm() {
        return weatherRealm;
    }

    public void setWeatherRealm(RealmList<WeatherRealm> weatherRealm) {
        this.weatherRealm = weatherRealm;
    }

    public TempRealm getTempRealm() {
        return tempRealm;
    }

    public void setTempRealm(TempRealm tempRealm) {
        this.tempRealm = tempRealm;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Float getRain() {
        return rain;
    }

    public void setRain(Float rain) {
        this.rain = rain;
    }

    public Float getPressure() {
        return pressure;
    }

    public void setPressure(Float pressure) {
        this.pressure = pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Long getDt() {
        return dt;
    }

    public void setDt(Long dt) {
        this.dt = dt;
    }

    public Integer getDeg() {
        return deg;
    }

    public void setDeg(Integer deg) {
        this.deg = deg;
    }

    public Integer getClouds() {
        return clouds;
    }

    public void setClouds(Integer clouds) {
        this.clouds = clouds;
    }


}
