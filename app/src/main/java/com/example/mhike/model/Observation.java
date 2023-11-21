package com.example.mhike.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Observation {

    @SerializedName("hike_id")
    @Expose
    private Integer hike_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("datetime")
    @Expose
    private String datetime;

    @SerializedName("additionalcomment")
    @Expose
    private String additionalcomment;

    @SerializedName("type")
    @Expose
    private String type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getAdditionalcomment() {
        return additionalcomment;
    }

    public void setAdditionalcomment(String additionalcomment) {
        this.additionalcomment = additionalcomment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getHike_id() {
        return hike_id;
    }

    public void setHike_id(Integer hike_id) {
        this.hike_id = hike_id;
    }
}
