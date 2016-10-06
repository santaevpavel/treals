package ru.nsu.fit.nsuschedule.util;

import android.app.Activity;
import android.content.SharedPreferences;

import ru.nsu.fit.nsuschedule.NsuScheduleApplication;

/**
 * Created by Pavel on 18.09.2016.
 */
public class PreferenceHelper {

    private static final String PREFS = "PREFS";

    public static String getGroupName(){
        return getReferences().getString("groupName", null);
    }

    public static String getGroup(){
        return getReferences().getString("group", null);
    }

    public static void setGroup(String value){
        setString("group", value);
    }

    public static void setGroupName(String value){
        setString("groupName", value);
    }

    public static String getDepartment(){
        return getReferences().getString("department", null);
    }

    public static void setDepartment(String value){
        setString("department", value);
    }

    private static void setString(String key, String value){
        getReferences().edit().putString(key, value).commit();
    }

    private static SharedPreferences getReferences(){
        return NsuScheduleApplication.getAppContext().getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
    }
}
