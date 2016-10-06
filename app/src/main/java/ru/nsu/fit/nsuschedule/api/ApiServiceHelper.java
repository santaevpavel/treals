package ru.nsu.fit.nsuschedule.api;

import android.content.Context;
import android.content.Intent;
import android.support.v4.os.ResultReceiver;

import java.io.Serializable;

import ru.nsu.fit.nsuschedule.NsuScheduleApplication;
import ru.nsu.fit.nsuschedule.api.request.GetGroupsRequest;
import ru.nsu.fit.nsuschedule.api.request.GetLessonsRequest;

/**
 * Created by Pavel on 16.09.2016.
 */
public class ApiServiceHelper {

    public static void getDepartments(Context context, ResultReceiver resultReceiver){
        startService(null, ApiService.CODE_GET_DEPARTMENTS, resultReceiver);
    }

    public static void getGroups(Context context, ResultReceiver resultReceiver, GetGroupsRequest request){
        startService(request, ApiService.CODE_GET_GROUPS, resultReceiver);
    }

    public static void getLessons(Context context, ResultReceiver resultReceiver,
                                  GetLessonsRequest request){
        startService(request, ApiService.CODE_GET_LESSONS, resultReceiver);
    }

    public static void getGroups(Context context, ResultReceiver resultReceiver){
        startService(null, ApiService.CODE_GET_ALL_GROUPS, resultReceiver);
    }

    public static void getWeather(Context context, ResultReceiver resultReceiver){
        startService(null, ApiService.CODE_GET_NSU_WEATHER, resultReceiver);
    }

    private static void startService(Serializable data, int action, ResultReceiver onServiceResult) {
        Intent intent = getIntent(action, onServiceResult);
        intent.putExtra(ApiService.KEY_CALLBACK, onServiceResult);
        if(data != null){
            intent.putExtra(ApiService.KEY_OBJECT, data);
        }
        NsuScheduleApplication.getAppContext().startService(intent);
    }

    private static Intent getIntent(int action, ResultReceiver onServiceResult) {
        final Intent i = new Intent(NsuScheduleApplication.getAppContext(), ApiService.class);
        i.putExtra(ApiService.KEY_CODE, action);
        i.putExtra(ApiService.KEY_OBJECT, onServiceResult);
        return i;
    }
}
