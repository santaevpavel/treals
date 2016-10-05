package ru.nsu.fit.nsuschedule.api.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import ru.nsu.fit.nsuschedule.model.Department;

/**
 * Created by Pavel on 15.09.2016.
 */
public class DepartmentListResponse implements Serializable{

    @SerializedName("departments")
    public List<Department> departments;

    public DepartmentListResponse() {
    }
}
