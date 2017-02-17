package ru.nsu.fit.nsuschedule;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import ru.nsu.fit.nsuschedule.activity.CrashCatchActivity;
import ru.nsu.fit.nsuschedule.util.PreferenceHelper;

import static ru.nsu.fit.nsuschedule.activity.CrashCatchActivity.KEY_STACK_TRACE;

/**
 * Created by Pavel on 16.09.2016.
 */
public class NsuScheduleApplication extends Application {

    public static final String TAG = "NsuScheduleApplication";
    private static NsuScheduleApplication instance;

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        PreferenceHelper.setWeather(null);

        // Setup handler for uncaught exceptions.
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                handleUncaughtException(thread, e);
            }
        });
    }

    public void handleUncaughtException(Thread thread, Throwable e) {
        e.printStackTrace();
        final Intent intent = new Intent(getApplicationContext(), CrashCatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        StringBuilder stringBuilder = new StringBuilder();
        StackTraceElement[] stackTrace = e.getStackTrace();
        for (int i = 0; i < stackTrace.length; i++) {
            stringBuilder.append(stackTrace[i]);
        }
        intent.putExtra(KEY_STACK_TRACE, stringBuilder.toString());

        final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        try {
            pendingIntent.send(getApplicationContext(), 0, null);
        } catch (PendingIntent.CanceledException e1) {
            e1.printStackTrace();
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }
}
