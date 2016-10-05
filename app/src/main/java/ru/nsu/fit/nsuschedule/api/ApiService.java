package ru.nsu.fit.nsuschedule.api;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.nsu.fit.nsuschedule.api.request.GetGroupsRequest;
import ru.nsu.fit.nsuschedule.api.request.GetLessonsRequest;
import ru.nsu.fit.nsuschedule.api.response.DepartmentListResponse;
import ru.nsu.fit.nsuschedule.api.response.GroupListResponse;
import ru.nsu.fit.nsuschedule.api.response.LessonsResponse;

/**
 * Created by Pavel on 16.09.2016.
 */
public class ApiService extends IntentService{

    public static final String KEY_CODE = "KEY_CODE";
    public static final String KEY_CALLBACK = "KEY_CALLBACK";
    public static final String KEY_OBJECT = "KEY_OBJECT";
    public static final String KEY_RESPONSE = "KEY_RESPONSE";

    public static final int CODE_GET_DEPARTMENTS = 0;
    public static final int CODE_GET_GROUPS = 1;
    public static final int CODE_GET_LESSONS = 2;
    public static final int CODE_GET_ALL_GROUPS = 3;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * @param name Used to name the worker thread, important only for debugging.
     */

    private IApiService api;

    public ApiService() {
        super("ApiService");

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IApiService.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        api = retrofit.create(IApiService.class);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int code = intent.getIntExtra(KEY_CODE, -1);
        ResultReceiver resultReceiver = intent.getParcelableExtra(KEY_CALLBACK);
        Bundle response = null;
        switch (code){
            case CODE_GET_DEPARTMENTS:
                response = getDepartments();
                break;
            case CODE_GET_GROUPS:
                response = getGroups((GetGroupsRequest) intent.getSerializableExtra(KEY_OBJECT));
                break;
            case CODE_GET_LESSONS:
                response = getLessons((GetLessonsRequest) intent.getSerializableExtra(KEY_OBJECT));
                break;
            case CODE_GET_ALL_GROUPS:
                response = getClasses();
                break;
        }
        resultReceiver.send(0, response);
    }

    private Bundle getDepartments(){
        Call<DepartmentListResponse> call = api.getLessons();
        Response<DepartmentListResponse> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e2){
            e2.printStackTrace();
        }
        Bundle bundle = new Bundle();
        if (null != response && response.isSuccessful()) {
            bundle.putSerializable(KEY_RESPONSE, response.body());
        }
        return bundle;
    }

    private Bundle getGroups(GetGroupsRequest request){
        Call<GroupListResponse> call = api.getGroups(request.departmentId);
        Response<GroupListResponse> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e2){
            e2.printStackTrace();
        }
        Bundle bundle = new Bundle();
        if (null != response && response.isSuccessful()) {
            bundle.putSerializable(KEY_RESPONSE, response.body());
        }
        return bundle;
    }

    private Bundle getLessons(GetLessonsRequest request){
        Call<LessonsResponse> call = api.getLessons(request.groupId);
        Response<LessonsResponse> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e2){
            e2.printStackTrace();
        }
        Bundle bundle = new Bundle();
        if (null != response && response.isSuccessful()) {
            bundle.putSerializable(KEY_RESPONSE, response.body());
        }
        return bundle;
    }

    private Bundle getClasses(){
        Call<GroupListResponse> call = api.getGroups();
        Response<GroupListResponse> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e2){
            e2.printStackTrace();
        }
        Bundle bundle = new Bundle();
        if (null != response && response.isSuccessful()) {
            bundle.putSerializable(KEY_RESPONSE, response.body());
        }
        return bundle;
    }
}


