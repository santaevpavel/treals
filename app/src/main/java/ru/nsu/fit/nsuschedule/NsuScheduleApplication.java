package ru.nsu.fit.nsuschedule;

import android.app.Application;
import android.content.Context;

/**
 * Created by Pavel on 16.09.2016.
 */
public class NsuScheduleApplication extends Application{

    private static NsuScheduleApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getAppContext(){
        return instance.getApplicationContext();
    }
}
