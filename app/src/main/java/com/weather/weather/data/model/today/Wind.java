package com.weather.weather.data.model.today;

/**
 * Created by ilyas on 01.05.2017.
 */

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind implements Parcelable
{

    @SerializedName("speed")
    @Expose
    private double speed;
    @SerializedName("deg")
    @Expose
    private long deg;
    public final static Parcelable.Creator<Wind> CREATOR = new Creator<Wind>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Wind createFromParcel(Parcel in) {
            Wind instance = new Wind();
            instance.speed = ((double) in.readValue((double.class.getClassLoader())));
            instance.deg = ((long) in.readValue((long.class.getClassLoader())));
            return instance;
        }

        public Wind[] newArray(int size) {
            return (new Wind[size]);
        }

    }
            ;

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public long getDeg() {
        return deg;
    }

    public void setDeg(long deg) {
        this.deg = deg;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(speed);
        dest.writeValue(deg);
    }

    public int describeContents() {
        return 0;
    }

}