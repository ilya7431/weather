package com.weather.weather.helper;


public  enum TypeWeather {
    Clear_Sky("clear sky"),
    Few_Clouds("few clouds"),
    Scattered_Clouds("scattered clouds"),
    BrokenClouds("broken clouds"),
    Shower_Rain("shower rain"),
    Rain("rain"),
    Thunderstorm("thunderstorm")
    ,Snow("snow"),
    Mist("mist");

    private final String description;

    private TypeWeather(String description)
    {
        this.description = description;
    }

    public  final String getDescription()
    {
        return description;
    }

    public static  TypeWeather getValue(String value){

        for (TypeWeather t :TypeWeather.values()){
            if (t.description.equalsIgnoreCase(value)) {
                return t;
            }
        }
        return TypeWeather.Clear_Sky;
    }
}
