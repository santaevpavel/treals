package ru.nsu.fit.nsuschedule.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Pavel on 19.10.2016.
 */
public class Place implements Serializable {
    /*
    'id' =>	'1',
    'title' =>	'Янцзы',
    'type' =>	'Кафе',
    'descr' => 'Китайская кухня',
    'time' =>	'вс-чт 11.00–23.00, пт-сб 11.00–1.00, бизнес ланч пн-пт 11.00–16.00',
    'place' =>	'Морской проспект, 54а',
    'phone' =>	'+7 (383) 330 42 33',
    'price' =>	'до 700',
    'lat' =>	'54.835203487658816',
    'long' =>	'83.09678792953491',
     */

    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("type")
    private String type;
    @SerializedName("descr")
    private String description;
    @SerializedName("time")
    private String time;
    @SerializedName("place")
    private String place;
    @SerializedName("phone")
    private String phone;
    @SerializedName("long")
    private double lng;
    @SerializedName("lat")
    private double lat;
    @SerializedName("img")
    private String img;
    @SerializedName("price")
    private String price;

    public Place() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
