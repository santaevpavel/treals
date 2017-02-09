package ru.nsu.fit.nsuschedule.model;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Pavel on 13.09.2016.
 */
public class Lesson implements Serializable {

    @SerializedName("lesson_id")
    private String id;
    @SerializedName("group_id")
    private String groupId;
    @SerializedName("name")
    private String name;
    @SerializedName("time_start")
    private String startTime;
    @SerializedName("time_end")
    private String endTime;
    @SerializedName("place")
    private String room;
    @SerializedName("type")
    private Type type;
    @SerializedName("days")
    private List<Date> days;
    @SerializedName("teacher_id")
    private String teacherId;
    @SerializedName("teacher_name")
    private String teacherName;

    public Lesson(String id, String groupId, String name, String startTime,
                  String endTime, String room, Type type, List<Date> days, String teacherId) {
        this.id = id;
        this.groupId = groupId;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.type = type;
        this.days = days;
        this.teacherId = teacherId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Date> getDays() {
        return days;
    }

    public void setDays(List<Date> days) {
        this.days = days;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    @JsonAdapter(TypeJsonAdapter.class)
    public enum Type {
        UNKNOWN(""),
        LECTURE("Лекция"),
        PRACTICUM("Практикум"),
        SEMINAR("Семинар");

        public final String name;

        Type(String name) {
            this.name = name;
        }
    }

    public class TypeJsonAdapter extends TypeAdapter<Type> {

        @Override
        public void write(JsonWriter out, Type value) throws IOException {
        }

        @Override
        public Type read(JsonReader in) throws IOException {

            String name = in.nextString();
            switch (name) {
                case "lecture":
                    return Type.LECTURE;
                case "practicum":
                    return Type.PRACTICUM;
                case "seminar":
                    return Type.SEMINAR;
                default:
                    return Type.UNKNOWN;
            }
        }
    }
}
