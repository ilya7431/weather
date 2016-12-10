package com.weather.weather.helper;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.IntRange;

import com.weather.weather.R;
import com.weather.weather.data.model.City;

public final class PrefUtils {

    private static SharedPreferences sPreferences;


    public static City getPrefLocation(Context context) {
        final SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.SP_City), context.MODE_PRIVATE);
        City city = new City();
        city.id = pref.getInt(context.getString(R.string.pref_location_id_key),Integer.valueOf(context.getString(R.string.pref_location_id_default)));
        city.name = pref.getString(context.getString(R.string.pref_location_name_key),context.getString(R.string.pref_location_name_default));
        return city;
    }

    /**
     *
     * @param context
     * @param location id
     * @param name City
     */
    public static void setPrefLocation(Context context, int location,String name) {

        final SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.SP_City), context.MODE_PRIVATE);
        pref.edit().putInt(context.getString(R.string.pref_location_id_key), location).apply();
        pref.edit().putString(context.getString(R.string.pref_location_name_key), name).apply();
    }

    public static boolean getPrefUpdateWIFI(Context context) {
        final SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.SP_Settings), context.MODE_PRIVATE);
        boolean checkUpdateWIFI = pref.getBoolean(
                context.getString(R.string.pref_updatewifi_key), false);
        return checkUpdateWIFI;
    }

    public static void setPrefUpdateWIFI(Context context, boolean updateWIFI) {

        final SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.SP_Settings), context.MODE_PRIVATE);
        pref.edit().putBoolean(context.getString(R.string.pref_updatewifi_key), updateWIFI).apply();
    }

    public static void  setPrefSwitchAutoSearch(Context context,boolean autoSearch){
        final SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.SP_Settings), context.MODE_PRIVATE);
        pref.edit().putBoolean(context.getString(R.string.Sw_Enable_Enter_City), autoSearch).apply();
    }
    public static boolean  getPrefSwitchAutoSearch(Context context){
        final SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.SP_Settings), context.MODE_PRIVATE);
        boolean autoSearch = pref.getBoolean(
                context.getString(R.string.Sw_Enable_Enter_City), false);
        return autoSearch;
    }
    public static void  setPrefSwitchTimeUpdate(Context context,boolean switchUpdateTime){
        final SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.SP_Settings), context.MODE_PRIVATE);
        pref.edit().putBoolean(context.getString(R.string.pref_switch_update_time_key), switchUpdateTime).apply();
    }
    public static boolean  getPrefSwitchTimeUpdate(Context context){
        final SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.SP_Settings), context.MODE_PRIVATE);
        boolean autoSearch = pref.getBoolean(
                context.getString((R.string.pref_switch_update_time_key)), false);
        return autoSearch;
    }

    public static void  setPrefTimeUpdate(Context context,@IntRange(from=1,to=12)int timeUpdate){
        final SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.SP_Settings), context.MODE_PRIVATE);
        pref.edit().putInt(context.getString((R.string.pref_update_time_key)), timeUpdate).apply();
    }
    @IntRange(from=0,to=12)
    public static int  getPrefTimeUpdate(Context context){
        final SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.SP_Settings), context.MODE_PRIVATE);
        int time = pref.getInt(
                context.getString((R.string.pref_update_time_key)), 0);
        return time;
    }

}