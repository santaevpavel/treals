package ru.nsu.fit.nsuschedule.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Pavel on 16.09.2016.
 */
public class Group implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    public Group() {};

    public Group(String id, String name) {
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
