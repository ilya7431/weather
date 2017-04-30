package com.weather.weather.data.model.today;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.weather.weather.data.model.Weather;

import java.util.List;

/**
 * Created by ilyas on 01.05.2017.
 */

public class WeatherToday implements Parcelable
{

    @SerializedName("weather")
    @Expose
    private List<Weather> weather = null;
    @SerializedName("main")
    @Expose
    private Main main;
    @SerializedName("wind")
    @Expose
    private Wind wind;
    @SerializedName("dt")
    @Expose
    private long dt;


    public final static Parcelable.Creator<WeatherToday> CREATOR = new Creator<WeatherToday>() {


        @SuppressWarnings({
                "unchecked"
        })
        public WeatherToday createFromParcel(Parcel in) {
            WeatherToday instance = new WeatherToday();
            in.readList(instance.weather, (Weather.class.getClassLoader()));
            instance.main = ((Main) in.readValue((Main.class.getClassLoader())));
            instance.wind = ((Wind) in.readValue((Wind.class.getClassLoader())));
            instance.dt = ((long) in.readValue((long.class.getClassLoader())));
            return instance;
        }

        public WeatherToday[] newArray(int size) {
            return (new WeatherToday[size]);
        }

    };

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }


    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }


    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }


    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(weather);
        dest.writeValue(main);
        dest.writeValue(wind);
        dest.writeValue(dt);

    }

    public int describeContents() {
        return 0;
    }

}
