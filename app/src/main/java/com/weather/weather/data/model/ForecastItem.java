package com.weather.weather.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForecastItem implements Parcelable {

    @SerializedName("dt")
    @Expose
    private long dt;
    @SerializedName("temp")
    @Expose
    private Temp temp;
    @SerializedName("pressure")
    @Expose
    private double pressure;
    @SerializedName("humidity")
    @Expose
    private long humidity;
    @SerializedName("weather")
    @Expose
    private java.util.List<Weather> weather = null;
    @SerializedName("speed")
    @Expose
    private double speed;
    @SerializedName("deg")
    @Expose
    private long deg;
    @SerializedName("clouds")
    @Expose
    private long clouds;
    @SerializedName("rain")
    @Expose
    private double rain;
    public final static Parcelable.Creator<ForecastItem> CREATOR = new Creator<ForecastItem>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ForecastItem createFromParcel(Parcel in) {
            ForecastItem instance = new ForecastItem();
            instance.dt = ((long) in.readValue((long.class.getClassLoader())));
            instance.temp = ((Temp) in.readValue((Temp.class.getClassLoader())));
            instance.pressure = ((double) in.readValue((double.class.getClassLoader())));
            instance.humidity = ((long) in.readValue((long.class.getClassLoader())));
            in.readList(instance.weather, (com.weather.weather.data.model.Weather.class.getClassLoader()));
            instance.speed = ((double) in.readValue((double.class.getClassLoader())));
            instance.deg = ((long) in.readValue((long.class.getClassLoader())));
            instance.clouds = ((long) in.readValue((long.class.getClassLoader())));
            instance.rain = ((double) in.readValue((double.class.getClassLoader())));
            return instance;
        }

        public ForecastItem[] newArray(int size) {
            return (new ForecastItem[size]);
        }

    }
            ;

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public Temp getTemp() {
        return temp;
    }

    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public long getHumidity() {
        return humidity;
    }

    public void setHumidity(long humidity) {
        this.humidity = humidity;
    }

    public java.util.List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(java.util.List<Weather> weather) {
        this.weather = weather;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public long getDeg() {
        return deg;
    }

    public void setDeg(long deg) {
        this.deg = deg;
    }

    public long getClouds() {
        return clouds;
    }

    public void setClouds(long clouds) {
        this.clouds = clouds;
    }

    public double getRain() {
        return rain;
    }

    public void setRain(double rain) {
        this.rain = rain;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(dt);
        dest.writeValue(temp);
        dest.writeValue(pressure);
        dest.writeValue(humidity);
        dest.writeList(weather);
        dest.writeValue(speed);
        dest.writeValue(deg);
        dest.writeValue(clouds);
        dest.writeValue(rain);
    }

    public int describeContents() {
        return 0;
    }

}