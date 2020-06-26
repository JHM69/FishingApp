package com.jhm69.farhad_fishingapp.Model;

import android.text.format.DateFormat;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;

public class UserPost  implements Serializable {
    private String userId;
    private String userImg;
    private String name;
    private String date;
    private String text;
    private String image;
    private String location;
    private String id;

    public UserPost(String userId, String userImg, String name, String date, String text, String image, String location, String id) {
        this.userId = userId;
        this.userImg = userImg;
        this.name = name;
        this.date = date;
        this.text = text;
        this.image = image;
        this.location = location;
        this.id = id;
    }

    public UserPost() {
    }

    public String getLocation() {
        return location;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public String getImage() {
        return image;
    }


}
