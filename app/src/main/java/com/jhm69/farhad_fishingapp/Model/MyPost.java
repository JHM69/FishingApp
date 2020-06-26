package com.jhm69.farhad_fishingapp.Model;

import java.io.Serializable;

public class MyPost implements Serializable {

    private String id;
    private String title;
    private String date;
    private String text;
    private String image;
    private String category;

    public MyPost(String id, String title,String date, String text, String image, String category) {
        this.id = id;
        this.date = date;
        this.text = text;
        this.image = image;
        this.category = category;
        this.title = title;
    }
    public MyPost() {
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

}