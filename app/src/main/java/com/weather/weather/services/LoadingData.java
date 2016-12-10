package com.weather.weather.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.weather.weather.MainActivity;
import com.weather.weather.R;
import com.weather.weather.data.model.City;
import com.weather.weather.helper.ParserJson;
import com.weather.weather.helper.PrefUtils;
import com.weather.weather.network.OpenWeatherContract;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import io.realm.Realm;


public class LoadingData extends GcmTaskService {
    private String returnValue;
    private Context mContext;
    private boolean checkSwitchWifi;
    private URL url;
    private Realm realm;
    private NotificationManager notificationManager;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onRunTask(TaskParams taskParams) {
        connect();
        return 0;
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        return START_NOT_STICKY;
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;



    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //    realm.close();
    }


    private void connect() {
        new Thread() {
            @Override
            public void run() {
                try {


                    ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();


                    if (PrefUtils.getPrefUpdateWIFI(mContext)) {

                        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            checkSwitchWifi = true;

                        } else checkSwitchWifi = false;

                    } else checkSwitchWifi = true;


                    if (checkSwitchWifi) {

                        City city = PrefUtils.getPrefLocation(mContext);
                        url = new URL(OpenWeatherContract.ROOT_URL + OpenWeatherContract.METHOD_GET_DAILY_FORECAST
                                + ("&" + OpenWeatherContract.PARAM_ID)
                                + "=" + city.id
                                + ("&" + OpenWeatherContract.PARAM_DAYS + "=7"));

                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                            BufferedReader in = new BufferedReader(new InputStreamReader(
                                    connection.getInputStream()));
                            String inputLine;
                            while ((inputLine = in.readLine()) != null)

                                new ParserJson().loadingDataInDB(inputLine);
                            returnValue = "Ok";
                            in.close();


                        } else {
                            returnValue = mContext.getString(R.string.ExceptionHTTPLocal);
                        }
                    } else returnValue = (mContext.getString(R.string.DisableWifi));

                } catch (UnknownHostException e) {
                    returnValue = mContext.getString(R.string.ExceptionHost);

                } catch (ConnectException e) {
                    returnValue = mContext.getString(R.string.ExceptionConnect);

                } catch (Exception e) {
                    returnValue = mContext.getString(R.string.ExceptionAPPLocal);
                }
                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(mContext)
                                .setSmallIcon(R.drawable.ic_cloud)
                                .setLargeIcon(largeIcon);
                if(returnValue.equals(mContext.getString(R.string.ExceptionHTTPLocal))
                        | returnValue.equals(mContext.getString(R.string.ExceptionHost))
                        | returnValue.equals(mContext.getString(R.string.ExceptionConnect))
                        | returnValue.equals(mContext.getString(R.string.ExceptionAPPLocal))){

                    mBuilder.setContentTitle(mContext.getString(R.string.app_name))
                            .setContentText(mContext.getString(R.string.CheckConnection));

                }else if (returnValue.equals(mContext.getString(R.string.DisableWifi))){
                    mBuilder.setContentTitle(mContext.getString(R.string.app_name))
                            .setContentText(mContext.getString(R.string.DisableWifi));
                }else {
                    mBuilder.setContentTitle(mContext.getString(R.string.app_name))
                            .setContentText(mContext.getString(R.string.UpdateData));
                }


                Intent resultIntent = new Intent(mContext, MainActivity.class);


                TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                int mId =10;

                Notification notification = mBuilder.build();
                notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
                mNotificationManager.notify(mId, notification);
                stopSelf();
            }
        }.start();


    }


}
