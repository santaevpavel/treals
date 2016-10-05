package ru.nsu.fit.nsuschedule.api.request;

import java.io.Serializable;

/**
 * Created by Pavel on 16.09.2016.
 */
public class GetGroupsRequest implements Serializable{

    public String departmentId;

    public GetGroupsRequest(String departmentId) {
        this.departmentId = departmentId;
    }
}
