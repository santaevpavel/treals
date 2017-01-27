package ru.nsu.fit.nsuschedule.api;

import com.squareup.okhttp.ResponseBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.nsu.fit.nsuschedule.api.response.AllNewsResponse;
import ru.nsu.fit.nsuschedule.api.response.AllPlacesResponse;
import ru.nsu.fit.nsuschedule.api.response.DepartmentListResponse;
import ru.nsu.fit.nsuschedule.api.response.GroupListResponse;
import ru.nsu.fit.nsuschedule.api.response.LessonsResponse;
import ru.nsu.fit.nsuschedule.api.response.WeatherResponse;

/**
 * Created by Pavel on 15.09.2016.
 */
public interface IApiService {

    public static final String URL = "http://token-shop.ru/";

    @GET("/api/DEPARTMENTS")
    Call<DepartmentListResponse> getLessons();

    @GET("/api/DEPARTMENTS/{departmentId}/CLASSES")
    Call<GroupListResponse> getGroups(@Path("departmentId") String departmentId);

    @GET("/api/CLASSES/{groupId}/LESSONS")
    Call<LessonsResponse> getLessons(@Path("groupId") String groupId);

    @GET("/api/CLASSES")
    Call<GroupListResponse> getGroups();

    @GET("/api/WEATHER")
    Call<WeatherResponse> getWeather();

    @GET("/api/NEWS/{url}")
    Call<ResponseBody> getNews(@Path("url") String url);

    @GET("/api/NEWS")
    Call<AllNewsResponse> getAllNews();

    @GET("/api/ACADEM")
    Call<AllNewsResponse> getAllAcademNews();

    @GET("/api/ACADEM/PLACES")
    Call<AllPlacesResponse> getAllPlacesNews();
}
