package com.weather.weather.helper;


import com.weather.weather.data.ModelForecastAPI;
import com.weather.weather.data.model.City;
import com.weather.weather.data.model.Forecast;
import com.weather.weather.data.model.ForecastItem;
import com.weather.weather.data.model.Temp;
import com.weather.weather.data.model.Weather;
import com.weather.weather.data.realmModel.CityRealm;
import com.weather.weather.data.realmModel.ForecastItemRealm;
import com.weather.weather.data.realmModel.ForecastRealm;
import com.weather.weather.data.realmModel.TempRealm;
import com.weather.weather.data.realmModel.WeatherRealm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

public class ParserJson {

    List<ForecastItem> forecastItemList;
    List<Weather> weatherList;
    Forecast forecast= new Forecast();
    RealmList<ForecastItemRealm> forecastItemListRealm;
    RealmList<WeatherRealm> weatherListRealm;
    ForecastRealm forecastRealm= new ForecastRealm();

    public void loadingDataInDB(String value) {

        try {


            City city = new City();
            Weather weather = new Weather();

            CityRealm cityRealm = new CityRealm();
            WeatherRealm weatherRealm = new WeatherRealm();


            forecastItemList = new ArrayList<>();
            weatherList = new ArrayList<>();
            forecastItemListRealm = new RealmList<>();
            weatherListRealm = new RealmList<>();

            JSONObject json = new JSONObject(value);
            city.id = json.getJSONObject(ModelForecastAPI.CITY_ARRAY).getInt(ModelForecastAPI.CITY_ID);
            city.name = json.getJSONObject(ModelForecastAPI.CITY_ARRAY).getString(ModelForecastAPI.CITY_NAME);
            city.country = json.getJSONObject(ModelForecastAPI.CITY_ARRAY).getString(ModelForecastAPI.CITY_COUNTRY);

            cityRealm.setId(city.id);
            cityRealm.setName(city.name);
            cityRealm.setCountry(city.country);


            JSONArray jsonArray = json.getJSONArray(ModelForecastAPI.LIST_DAY);
            for (int i = 0; i < jsonArray.length(); i++) {
                ForecastItem forecastItem = new ForecastItem();
                Temp temp = new Temp();
                ForecastItemRealm forecastItemRealm = new ForecastItemRealm();
                TempRealm tempRealm = new TempRealm();

                forecastItem.dt = jsonArray.getJSONObject(i).getLong(ModelForecastAPI.DATA);
                forecastItem.humidity = jsonArray.getJSONObject(i).getInt(ModelForecastAPI.HUMIDITY);
                forecastItem.speed = (float) jsonArray.getJSONObject(i).getDouble(ModelForecastAPI.SPEED);
                forecastItem.pressure = (float) jsonArray.getJSONObject(i).getDouble(ModelForecastAPI.PRESSURE);

                forecastItemRealm.setDt(forecastItem.dt);
                forecastItemRealm.setHumidity(forecastItem.humidity);
                forecastItemRealm.setSpeed(forecastItem.speed);
                forecastItemRealm.setPressure(forecastItem.pressure);

                temp.day = (float) jsonArray.getJSONObject(i).getJSONObject(ModelForecastAPI.TEMPERATURE).getDouble(ModelForecastAPI.TEMPERATURE_DAY);
                temp.night = (float) jsonArray.getJSONObject(i).getJSONObject(ModelForecastAPI.TEMPERATURE).getDouble(ModelForecastAPI.TEMPERATURE_NIGHT);

                tempRealm.setDay(temp.day);
                tempRealm.setNight(temp.night);

                forecastItem.temp = temp;
                forecastItemRealm.setTempRealm(tempRealm);

                weather.description = jsonArray.getJSONObject(i).getJSONArray(ModelForecastAPI.ARRAY_WEATHER).getJSONObject(0).getString(ModelForecastAPI.WEATHER_DESCRIPTION);
                weather.main = jsonArray.getJSONObject(i).getJSONArray(ModelForecastAPI.ARRAY_WEATHER).getJSONObject(0).getString(ModelForecastAPI.WEATHER_MAIN);

                weatherRealm.setDescription(weather.description);
                weatherRealm.setMain(weather.main);

                weatherList.add(weather);
                weatherListRealm.add(weatherRealm);

                forecastItem.weather = weatherList;
                forecastItemRealm.setWeatherRealm(weatherListRealm);

                forecastItemList.add(forecastItem);
                forecastItemListRealm.add(forecastItemRealm);


            }

            forecast.city = city;
            forecast.list = forecastItemList;
            forecastRealm.setCityRealm(cityRealm);
            forecastRealm.setList(forecastItemListRealm);



        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }


        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(forecastRealm);

            }
        });

    }
}
