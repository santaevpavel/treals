package ru.nsu.fit.nsuschedule.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestRequest<T> {

    private static final String TAG = "RestClient";

    private Retrofit retrofit;

    public RestRequest(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public T createService(Class<T> tClass) {
        return retrofit.create(tClass);
    }

}