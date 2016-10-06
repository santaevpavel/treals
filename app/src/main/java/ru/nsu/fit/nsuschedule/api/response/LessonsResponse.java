package ru.nsu.fit.nsuschedule.api.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import ru.nsu.fit.nsuschedule.model.Group;
import ru.nsu.fit.nsuschedule.model.Lesson;

/**
 * Created by Pavel on 16.09.2016.
 */
public class LessonsResponse extends BaseResponse implements Serializable {

    @SerializedName("lessons")
    public List<Lesson> lessons;

    public LessonsResponse() {
    }
}
