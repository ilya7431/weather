package com.weather.weather.data.model;

import java.util.ArrayList;
import java.util.List;



public class ForecastItem {
    public Long dt;
    public Temp temp;
    public Float pressure;
    public Integer humidity;
    public List<Weather> weather = new ArrayList<>();
    public Float speed;
    public Integer deg;
    public Integer clouds;
    public Float rain;
}
