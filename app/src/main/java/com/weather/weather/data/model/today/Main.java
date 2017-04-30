package com.weather.weather.data.model.today;

/**
 * Created by ilyas on 01.05.2017.
 */

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Main implements Parcelable
{

    @SerializedName("temp")
    @Expose
    private double temp;
    @SerializedName("pressure")
    @Expose
    private long pressure;
    @SerializedName("humidity")
    @Expose
    private long humidity;
    @SerializedName("temp_min")
    @Expose
    private long tempMin;
    @SerializedName("temp_max")
    @Expose
    private long tempMax;
    public final static Parcelable.Creator<Main> CREATOR = new Creator<Main>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Main createFromParcel(Parcel in) {
            Main instance = new Main();
            instance.temp = ((double) in.readValue((double.class.getClassLoader())));
            instance.pressure = ((long) in.readValue((long.class.getClassLoader())));
            instance.humidity = ((long) in.readValue((long.class.getClassLoader())));
            instance.tempMin = ((long) in.readValue((long.class.getClassLoader())));
            instance.tempMax = ((long) in.readValue((long.class.getClassLoader())));
            return instance;
        }

        public Main[] newArray(int size) {
            return (new Main[size]);
        }

    }
            ;

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public long getPressure() {
        return pressure;
    }

    public void setPressure(long pressure) {
        this.pressure = pressure;
    }

    public long getHumidity() {
        return humidity;
    }

    public void setHumidity(long humidity) {
        this.humidity = humidity;
    }

    public long getTempMin() {
        return tempMin;
    }

    public void setTempMin(long tempMin) {
        this.tempMin = tempMin;
    }

    public long getTempMax() {
        return tempMax;
    }

    public void setTempMax(long tempMax) {
        this.tempMax = tempMax;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(temp);
        dest.writeValue(pressure);
        dest.writeValue(humidity);
        dest.writeValue(tempMin);
        dest.writeValue(tempMax);
    }

    public int describeContents() {
        return 0;
    }

}