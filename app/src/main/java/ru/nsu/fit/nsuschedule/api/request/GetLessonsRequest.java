package ru.nsu.fit.nsuschedule.api.request;

import java.io.Serializable;

/**
 * Created by Pavel on 16.09.2016.
 */
public class GetLessonsRequest implements Serializable{

    public String groupId;

    public GetLessonsRequest(String groupId) {
        this.groupId = groupId;
    }
}
