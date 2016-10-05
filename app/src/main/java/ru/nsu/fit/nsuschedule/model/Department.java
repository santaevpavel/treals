package ru.nsu.fit.nsuschedule.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Pavel on 15.09.2016.
 */
public class Department implements Serializable{

    @SerializedName("department_id")
    private String id;

    @SerializedName("name")
    private String name;

    public Department(){};

    public Department(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
