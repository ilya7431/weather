package com.weather.weather.network;


import com.weather.weather.BuildConfig;

public final class OpenWeatherContract {

    public static final String ROOT_URL = "http://api.openweathermap.org/data/2.5/";

    public static final String PARAM_QUERY = "q";
    public static final String PARAM_ID = "id";
    public static final String PARAM_FORMAT = "mode";
    public static final String PARAM_UNITS = "units";
    public static final String PARAM_DAYS = "cnt";
    public static final String PARAM_APPID = "APPID";
    public static final String PARAM_FORECAST = "forecast";
    public static final String PARAM_WEATHER = "weather";
    public static final String PARAM_DAILY = "daily";
    public static final String PARAM_LANG = "lang";

    public static final String BASE_PARAM = "?" + PARAM_FORMAT + "=json&"
            + PARAM_UNITS + "=metric&"
            + PARAM_APPID + "=" + BuildConfig.OPEN_WEATHER_MAP_API_KEY;

    public static final String METHOD_GET_DAILY_FORECAST = PARAM_FORECAST + "/" + PARAM_DAILY  + BASE_PARAM;

    public static final String METHOD_GET_DAILY_WEATHER = PARAM_WEATHER + "?" + BASE_PARAM;


}
