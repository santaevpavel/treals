package ru.nsu.fit.nsuschedule.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ru.nsu.fit.nsuschedule.model.Place;

/**
 * Created by Pavel on 05.10.2016.
 */
public class AllPlacesResponse extends BaseResponse {

    @SerializedName("academ_places")
    public List<Place> places;

    public AllPlacesResponse() {
    }
}
