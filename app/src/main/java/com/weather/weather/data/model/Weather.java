package com.weather.weather.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Weather implements Parcelable {

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("main")
    @Expose
    private String main;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("icon")
    @Expose
    private String icon;

    public final static Parcelable.Creator<Weather> CREATOR = new Creator<Weather>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Weather createFromParcel(Parcel in) {
            Weather instance = new Weather();
            instance.id = ((long) in.readValue((long.class.getClassLoader())));
            instance.main = ((String) in.readValue((String.class.getClassLoader())));
            instance.description = ((String) in.readValue((String.class.getClassLoader())));
            instance.icon = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Weather[] newArray(int size) {
            return (new Weather[size]);
        }

    }
            ;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(main);
        dest.writeValue(description);
        dest.writeValue(icon);
    }

    public int describeContents() {
        return 0;
    }

}