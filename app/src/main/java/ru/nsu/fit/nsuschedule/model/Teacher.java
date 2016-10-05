package ru.nsu.fit.nsuschedule.model;

/**
 * Created by Pavel on 13.09.2016.
 */
public class Teacher {

    private long id;
    private String name;

    public Teacher(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
