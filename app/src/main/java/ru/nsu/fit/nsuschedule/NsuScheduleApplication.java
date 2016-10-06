package ru.nsu.fit.nsuschedule;

import android.app.Application;
import android.content.Context;

import ru.nsu.fit.nsuschedule.util.PreferenceHelper;

/**
 * Created by Pavel on 16.09.2016.
 */
public class NsuScheduleApplication extends Application{

    private static NsuScheduleApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        PreferenceHelper.setWeather(null);
    }

    public static Context getAppContext(){
        return instance.getApplicationContext();
    }
}
