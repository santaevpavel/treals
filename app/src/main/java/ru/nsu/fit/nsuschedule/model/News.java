package ru.nsu.fit.nsuschedule.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Pavel on 19.10.2016.
 */
public class News implements Serializable{
    /*
    $news[] = array(
'title' => $title,
'descr' => $descr,
'link' => $link,
'img' => $img, // может быть ''
'type' => $type, //'Событие', 'Новость', 'ФИТ', 'ФФ', 'ЭФ', ..
'date' => $date // может быть ''
     */

    @SerializedName("title")
    private String title;
    @SerializedName("descr")
    private String description;
    @SerializedName("link")
    private String link;
    @SerializedName("img")
    private String img;
    @SerializedName("type")
    private String type;
    @SerializedName("date")
    private String date;

    public News(String title, String description, String link, String img, String type, String date) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.img = img;
        this.type = type;
        this.date = date;
    }

    public News() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
