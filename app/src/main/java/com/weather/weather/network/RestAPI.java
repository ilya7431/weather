package com.weather.weather.network;

import com.google.gson.Gson;
import com.weather.weather.BuildConfig;
import com.weather.weather.data.model.Forecast;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

import static com.weather.weather.network.OpenWeatherContract.METHOD_GET_DAILY_FORECAST;
import static com.weather.weather.network.OpenWeatherContract.PARAM_DAYS;
import static com.weather.weather.network.OpenWeatherContract.PARAM_ID;
import static com.weather.weather.network.OpenWeatherContract.PARAM_QUERY;
import static com.weather.weather.network.OpenWeatherContract.ROOT_URL;

public class RestAPI {
    private static Api sApi;
    private static Gson sGson;

    public static Api getApi() {
        if (sApi == null) {
            sApi = createApi();
        }
        return sApi;
    }

    public static Gson getGson() {
        if (sGson == null) {
            sGson = new Gson();
        }
        return sGson;
    }

    private static Api createApi() {
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okBuilder.addInterceptor(loggingInterceptor);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okBuilder.build())
                .build();
        return retrofit.create(Api.class);
    }

    public interface Api {
        @GET(METHOD_GET_DAILY_FORECAST)
        Observable<Forecast> getForecast(@Query(PARAM_QUERY) final String _cityID,
                                         @Query(PARAM_DAYS) final int _day);
    }
}
