package com.example.mhike.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hike {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("id")
    @Expose
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("image_id")
    @Expose
    private Integer image_id;
    @SerializedName("parking_available")
    @Expose
    private String parking_available;

    @SerializedName("length_of_hike")
    @Expose
    private Integer length_of_hike;

    @SerializedName("level_of_difficulty")
    @Expose
    private Integer level_of_difficulty;


    @SerializedName("image_link")
    @Expose
    private String image_link;

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("rating")
    @Expose
    private String rating;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("tags")
    @Expose
    private String tags;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getImage_id() {
        return image_id;
    }

    public void setImage_id(Integer image_id) {
        this.image_id = image_id;
    }

    public String getParking_available() {
        return parking_available;
    }

    public void setParking_available(String parking_available) {
        this.parking_available = parking_available;
    }

    public Integer getLength_of_hike() {
        return length_of_hike;
    }

    public void setLength_of_hike(Integer length_of_hike) {
        this.length_of_hike = length_of_hike;
    }

    public Integer getLevel_of_difficulty() {
        return level_of_difficulty;
    }

    public void setLevel_of_difficulty(Integer level_of_difficulty) {
        this.level_of_difficulty = level_of_difficulty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Hike(String name, String date, String location, Integer image_id, String parking_available, Integer length_of_hike, Integer level_of_difficulty, String image_link, String description, String rating, String type, String tags) {
        this.name = name;
        this.date = date;
        this.location = location;
        this.image_id = image_id;
        this.parking_available = parking_available;
        this.length_of_hike = length_of_hike;
        this.level_of_difficulty = level_of_difficulty;
        this.image_link = image_link;
        this.description = description;
        this.rating = rating;
        this.type = type;
        this.tags = tags;
    }

    public Hike() {
    }
}
