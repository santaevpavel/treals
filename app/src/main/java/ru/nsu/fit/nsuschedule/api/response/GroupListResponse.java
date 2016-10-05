package ru.nsu.fit.nsuschedule.api.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import ru.nsu.fit.nsuschedule.model.Department;
import ru.nsu.fit.nsuschedule.model.Group;

/**
 * Created by Pavel on 16.09.2016.
 */
public class GroupListResponse  implements Serializable {

    @SerializedName("classes")
    public List<Group> groups;

    public GroupListResponse() {
    }

    public boolean hasError(){
        return null == groups || groups.size() == 0;
    }
}
