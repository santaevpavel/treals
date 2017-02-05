package ru.nsu.fit.nsuschedule.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Pair;

import ru.nsu.fit.nsuschedule.NsuScheduleApplication;

/**
 * Created by Pavel on 18.09.2016.
 */
public class PreferenceHelper {

    private static final String PREFS = "PREFS";

    public static String getGroupName(){
        return getReferences().getString("groupName", null);
    }

    public static void setGroupName(String value) {
        setString("groupName", value);
    }

    public static String getWeather(){
        return getReferences().getString("weather", null);
    }

    public static void setWeather(String value){
        setString("weather", value);
    }

    public static String getGroup() {
        return getReferences().getString("group", null);
    }

    public static void setGroup(String value){
        setString("group", value);
    }

    public static void setLocation(double lat, double lng) {
        setDouble("locationLat", lat);
        setDouble("locationLng", lng);
    }

    public static String getDepartment(){
        return getReferences().getString("department", null);
    }

    public static void setDepartment(String value){
        setString("department", value);
    }

    public static Pair<Double, Double> getLocation() {
        double lat = getReferences().getFloat("locationLat", -1);
        double lng = getReferences().getFloat("locationLng", -1);
        if (lng == -1) {
            return null;
        } else {
            return new Pair<>(lat, lng);
        }
    }

    private static void setString(String key, String value){
        getReferences().edit().putString(key, value).apply();
    }

    private static void setDouble(String key, double value) {
        getReferences().edit().putFloat(key, (float) value).apply();
    }

    private static SharedPreferences getReferences(){
        return NsuScheduleApplication.getAppContext().getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
    }
}
