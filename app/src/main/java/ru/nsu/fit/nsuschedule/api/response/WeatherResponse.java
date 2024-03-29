package ru.nsu.fit.nsuschedule.api.response;

import com.google.gson.annotations.SerializedName;

import ru.nsu.fit.nsuschedule.model.Weather;

/**
 * Created by Pavel on 05.10.2016.
 */
public class WeatherResponse extends BaseResponse{

    //{temp: $temp, phrase: $phrase, high: :$high, low: $low, uvindex: $uvindex}
    // => {temp:  "-3°", phrase: "ясно", high: :"12°", low:  "-5°", uvindex: "3"}
    @SerializedName("weather")
    public Weather weather;

    public WeatherResponse() {
    }
}
