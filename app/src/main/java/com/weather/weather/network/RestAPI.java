package com.weather.weather.network;

import com.google.gson.Gson;
import com.weather.weather.BuildConfig;
import com.weather.weather.WeatherApplication;
import com.weather.weather.data.model.Forecast;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

import static com.weather.weather.network.OpenWeatherContract.METHOD_GET_DAILY_FORECAST;
import static com.weather.weather.network.OpenWeatherContract.PARAM_DAYS;
import static com.weather.weather.network.OpenWeatherContract.PARAM_LANG;
import static com.weather.weather.network.OpenWeatherContract.PARAM_QUERY;
import static com.weather.weather.network.OpenWeatherContract.ROOT_URL;
import static okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS;
import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;

public class RestAPI {
    private static Api sApi;
    private static Gson sGson;
    private static final String CACHE_CONTROL = "Cache-Control";

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

    private static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {

                    }
                });
        httpLoggingInterceptor.setLevel(BuildConfig.DEBUG ? HEADERS : NONE);
        return httpLoggingInterceptor;
    }

    public static Interceptor provideCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                // re-write response header to force use of cache
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(2, TimeUnit.MINUTES)
                        .build();

                return response.newBuilder()
                        .header(CACHE_CONTROL, cacheControl.toString())
                        .build();
            }
        };
    }

    public static Interceptor provideOfflineCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                if (!WeatherApplication.hasNetwork()) {
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(7, TimeUnit.DAYS)
                            .build();

                    request = request.newBuilder()
                            .cacheControl(cacheControl)
                            .build();
                }

                return chain.proceed(request);
            }
        };
    }

    private static Cache provideCache() {
        Cache cache = null;
        try {
            cache = new Cache(new File(WeatherApplication.getInstance().getCacheDir(), "http-cache"),
                    10 * 1024 * 1024); // 10 MB
        } catch (Exception e) {
        }
        return cache;
    }

    private static Api createApi() {

        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        okBuilder.addInterceptor(provideHttpLoggingInterceptor())
                .addInterceptor(provideOfflineCacheInterceptor())
                .addNetworkInterceptor(provideCacheInterceptor())
                .cache(provideCache());


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
                                         @Query(PARAM_LANG) final String _lang,
                                         @Query(PARAM_DAYS) final int _day);
    }
}
