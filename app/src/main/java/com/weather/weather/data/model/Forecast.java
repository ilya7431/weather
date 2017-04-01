package com.weather.weather.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Forecast implements Parcelable
{

    @SerializedName("city")
    @Expose
    private City city;
    @SerializedName("cod")
    @Expose
    private String cod;
    @SerializedName("message")
    @Expose
    private double message;
    @SerializedName("cnt")
    @Expose
    private long cnt;
    @SerializedName("list")
    @Expose
    private java.util.List<ForecastItem> forecastItem = null;
    public final static Parcelable.Creator<Forecast> CREATOR = new Creator<Forecast>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Forecast createFromParcel(Parcel in) {
            Forecast instance = new Forecast();
            instance.city = ((City) in.readValue((City.class.getClassLoader())));
            instance.cod = ((String) in.readValue((String.class.getClassLoader())));
            instance.message = ((double) in.readValue((double.class.getClassLoader())));
            instance.cnt = ((long) in.readValue((long.class.getClassLoader())));
            in.readList(instance.forecastItem, (ForecastItem.class.getClassLoader()));
            return instance;
        }

        public Forecast[] newArray(int size) {
            return (new Forecast[size]);
        }

    }
            ;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public double getMessage() {
        return message;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public long getCnt() {
        return cnt;
    }

    public void setCnt(long cnt) {
        this.cnt = cnt;
    }

    public java.util.List<ForecastItem> getForecastItem() {
        return forecastItem;
    }

    public void setForecastItem(java.util.List<ForecastItem> forecastItem) {
        this.forecastItem = forecastItem;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(city);
        dest.writeValue(cod);
        dest.writeValue(message);
        dest.writeValue(cnt);
        dest.writeList(forecastItem);
    }

    public int describeContents() {
        return 0;
    }

}