package ru.nsu.fit.nsuschedule.api;

import android.app.IntentService;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import nl.qbusict.cupboard.CupboardFactory;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.nsu.fit.nsuschedule.NsuScheduleApplication;
import ru.nsu.fit.nsuschedule.api.request.GetGroupsRequest;
import ru.nsu.fit.nsuschedule.api.request.GetLessonsRequest;
import ru.nsu.fit.nsuschedule.api.response.AllNewsResponse;
import ru.nsu.fit.nsuschedule.api.response.AllPlacesResponse;
import ru.nsu.fit.nsuschedule.api.response.BaseResponse;
import ru.nsu.fit.nsuschedule.api.response.DepartmentListResponse;
import ru.nsu.fit.nsuschedule.api.response.GroupListResponse;
import ru.nsu.fit.nsuschedule.api.response.LessonsResponse;
import ru.nsu.fit.nsuschedule.api.response.NewsResponse;
import ru.nsu.fit.nsuschedule.api.response.WeatherResponse;
import ru.nsu.fit.nsuschedule.db.CupboardSQLiteOpenHelper;
import ru.nsu.fit.nsuschedule.model.Lesson;
import ru.nsu.fit.nsuschedule.model.News;
import ru.nsu.fit.nsuschedule.util.Helper;

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
    public static final int CODE_GET_NSU_WEATHER = 4;
    public static final int CODE_GET_ALL_NEWS = 5;
    public static final int CODE_GET_NEWS = 6;
    public static final int CODE_GET_ALL_ACADEM_NEWS = 7;
    public static final int CODE_GET_ALL_ACADEM_PLACES = 8;


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

    private static <T extends BaseResponse> Bundle buildError(T t, String error) {
        Bundle bundle = new Bundle();
        t.setErrorMsg(error);
        bundle.putSerializable(KEY_RESPONSE, t);
        return bundle;
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
            case CODE_GET_NSU_WEATHER:
                response = getWeather();
                break;
            case CODE_GET_ALL_NEWS:
                response = getAllNews();
                break;
            case CODE_GET_NEWS:
                response = getNews(intent.getStringExtra(KEY_OBJECT));
                break;
            case CODE_GET_ALL_ACADEM_NEWS:
                response = getAllAcademNews();
                break;
            case CODE_GET_ALL_ACADEM_PLACES:
                response = getAllPlaces();
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
        } catch (UnknownHostException e){
            e.printStackTrace();
            return buildError(new LessonsResponse(), "Отсуствует интернет соединение");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e2){
            e2.printStackTrace();
        }
        Bundle bundle = new Bundle();
        if (null != response && response.isSuccessful()) {
            bundle.putSerializable(KEY_RESPONSE, response.body());
            saveLessons(response.body().lessons);
        } else {
            return buildError(new LessonsResponse(), "Ошибка при загрузке расписания");
        }
        return bundle;
    }

    private void saveLessons(List<Lesson> lessons) {
        if (lessons == null) {
            return;
        }
        SQLiteDatabase db = null;
        try {
            db = CupboardSQLiteOpenHelper.getDbHelper(NsuScheduleApplication.getAppContext()).getWritableDatabase();
            db.delete("Lesson", "", new String[0]);
            CupboardFactory.cupboard().withDatabase(db).put(lessons);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

    }

    private Bundle getClasses(){
        Call<GroupListResponse> call = api.getGroups();
        Response<GroupListResponse> response = null;
        Bundle bundle = new Bundle();
        try {
            response = call.execute();
        } catch (UnknownHostException e){
            e.printStackTrace();
            return buildError(new GroupListResponse(), "Отсуствует интернет соединение");
        } catch (IOException e2) {
            e2.printStackTrace();
            return buildError(new GroupListResponse(), "Ошибка при загрузке списка групп");
        } catch (Exception e3){
            e3.printStackTrace();
            return buildError(new GroupListResponse(), "Внутренняя ошибка приложения");
        }
        if (null != response && response.isSuccessful()) {
            bundle.putSerializable(KEY_RESPONSE, response.body());
        } else {
            return buildError(new GroupListResponse(), "Ошибка при загрузке списка групп");
        }
        return bundle;
    }

    private Bundle getWeather(){
        Call<WeatherResponse> call = api.getWeather();
        Response<WeatherResponse> response;
        Bundle bundle = new Bundle();
        try {
            response = call.execute();
        } catch (UnknownHostException e){
            e.printStackTrace();
            return buildError(new WeatherResponse(), "Отсуствует интернет соединение");
        } catch (IOException e2) {
            e2.printStackTrace();
            return buildError(new WeatherResponse(), "Ошибка при загрузке погоды");
        } catch (Exception e3){
            e3.printStackTrace();
            return buildError(new WeatherResponse(), "Внутренняя ошибка приложения");
        }
        if (null != response && response.isSuccessful()) {
            bundle.putSerializable(KEY_RESPONSE, response.body());
        } else {
            return buildError(new WeatherResponse(), "Ошибка при загрузке погоды");
        }
        return bundle;
    }

    private Bundle getNews(String url){
        Call<ResponseBody> call = api.getNews(url);
        Response<ResponseBody> response;
        Bundle bundle = new Bundle();
        String responseString;
        try {
            response = call.execute();
            responseString = response.body().string();
        } catch (UnknownHostException e){
            e.printStackTrace();
            return buildError(new NewsResponse(), "Отсуствует интернет соединение");
        } catch (IOException e) {
            e.printStackTrace();
            return buildError(new NewsResponse(), "Ошибка при загрузке новости");
        }
        if (null != response && response.isSuccessful()) {
            bundle.putSerializable(KEY_RESPONSE, responseString);
        } else {
            return buildError(new NewsResponse(), "Ошибка при загрузке новостей");
        }
        return bundle;
    }

    private Bundle getAllNews(){
        Call<AllNewsResponse> call = api.getAllNews();
        Response<AllNewsResponse> response;
        Bundle bundle = new Bundle();
        try {
            response = call.execute();
        } catch (UnknownHostException e){
            e.printStackTrace();
            return buildError(new AllNewsResponse(), "Отсуствует интернет соединение");
        } catch (IOException e2) {
            e2.printStackTrace();
            return buildError(new AllNewsResponse(), "Ошибка при загрузке новостей");
        } catch (Exception e3){
            e3.printStackTrace();
            return buildError(new AllNewsResponse(), "Внутренняя ошибка приложения");
        }
        if (null != response && response.isSuccessful()) {
            AllNewsResponse body = response.body();
            for (News news : body.news){
                news.setTitle(Helper.removeQuotes(news.getTitle()));
                news.setDescription(Helper.removeQuotes(news.getDescription()));
            }
            bundle.putSerializable(KEY_RESPONSE, body);
        } else {
            return buildError(new AllNewsResponse(), "Ошибка при загрузке новостей");
        }
        return bundle;
    }

    private Bundle getAllAcademNews() {
        Call<AllNewsResponse> call = api.getAllAcademNews();
        Response<AllNewsResponse> response;
        Bundle bundle = new Bundle();
        try {
            response = call.execute();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return buildError(new AllNewsResponse(), "Отсуствует интернет соединение");
        } catch (IOException e2) {
            e2.printStackTrace();
            return buildError(new AllNewsResponse(), "Ошибка при загрузке новостей");
        } catch (Exception e3) {
            e3.printStackTrace();
            return buildError(new AllNewsResponse(), "Внутренняя ошибка приложения");
        }
        if (null != response && response.isSuccessful()) {
            AllNewsResponse body = response.body();
            for (News news : body.news) {
                news.setTitle(Helper.removeQuotes(news.getTitle()));
                news.setDescription(Helper.removeQuotes(news.getDescription()));
            }
            bundle.putSerializable(KEY_RESPONSE, body);
        } else {
            return buildError(new AllNewsResponse(), "Ошибка при загрузке новостей");
        }
        return bundle;
    }

    private Bundle getAllPlaces() {
        Call<AllPlacesResponse> call = api.getAllPlacesNews();
        Response<AllPlacesResponse> response;
        Bundle bundle = new Bundle();
        try {
            response = call.execute();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return buildError(new AllPlacesResponse(), "Отсуствует интернет соединение");
        } catch (IOException e2) {
            e2.printStackTrace();
            return buildError(new AllPlacesResponse(), "Ошибка при загрузке новостей");
        } catch (Exception e3) {
            e3.printStackTrace();
            return buildError(new AllPlacesResponse(), "Внутренняя ошибка приложения");
        }
        if (null != response && response.isSuccessful()) {
            AllPlacesResponse body = response.body();
            bundle.putSerializable(KEY_RESPONSE, body);
        } else {
            return buildError(new AllPlacesResponse(), "Ошибка при загрузке новостей");
        }
        return bundle;
    }
}


