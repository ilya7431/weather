package com.weather.weather.helper;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.weather.weather.R;

/**
 * Created by ilyas on 02.04.2017.
 */

public class ImageWeather {

    public static Drawable getImageWeather(Resources resources, String iconID) {
        if (iconID != null && !iconID.equals("") & iconID.length() >= 3) {
            switch (iconID) {
                case "01d": {
                    return resources.getDrawable(R.drawable.ic_01d);
                }
                case "01n": {
                    return resources.getDrawable(R.drawable.ic_01n);
                }
                case "02d": {
                    return resources.getDrawable(R.drawable.ic_02d);
                }
                case "02n": {
                    return resources.getDrawable(R.drawable.ic_02n);
                }
                case "03d": {
                    return resources.getDrawable(R.drawable.ic_04d);
                }
                case "03n": {
                    return resources.getDrawable(R.drawable.ic_04d);
                }
                case "04d": {
                    return resources.getDrawable(R.drawable.ic_04d);
                }
                case "04n": {
                    return resources.getDrawable(R.drawable.ic_04d);
                }
                case "09d": {
                    return resources.getDrawable(R.drawable.ic_10d);
                }
                case "09n": {
                    return resources.getDrawable(R.drawable.ic_10d);
                }
                case "10d": {
                    return resources.getDrawable(R.drawable.ic_10d);
                }
                case "10n": {
                    return resources.getDrawable(R.drawable.ic_10d);
                }
                case "11d": {
                    return resources.getDrawable(R.drawable.ic_11d);
                }
                case "11n": {
                    return resources.getDrawable(R.drawable.ic_11d);
                }
                case "13d": {
                    return resources.getDrawable(R.drawable.ic_13d);
                }
                case "13n": {
                    return resources.getDrawable(R.drawable.ic_13d);
                }
                case "50d": {
                    return resources.getDrawable(R.drawable.ic_50d);
                }
                case "50n": {
                    return resources.getDrawable(R.drawable.ic_50d);
                }
                default: {
                    return resources.getDrawable(R.drawable.ic_01d);
                }
            }
        } else {
            return resources.getDrawable(R.drawable.ic_01d);
        }
    }
}
