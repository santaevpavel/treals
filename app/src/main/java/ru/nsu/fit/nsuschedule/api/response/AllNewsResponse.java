package ru.nsu.fit.nsuschedule.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import ru.nsu.fit.nsuschedule.model.News;

/**
 * Created by Pavel on 05.10.2016.
 */
public class AllNewsResponse extends BaseResponse{

    //{temp: $temp, phrase: $phrase, high: :$high, low: $low, uvindex: $uvindex}
    // => {temp:  "-3°", phrase: "ясно", high: :"12°", low:  "-5°", uvindex: "3"}
    @SerializedName(value = "news", alternate = {"academ"})
    public List<News> news;

    public AllNewsResponse() {
    }

    public AllNewsResponse(ArrayList<News> news) {
        this.news = news;
    }
}
