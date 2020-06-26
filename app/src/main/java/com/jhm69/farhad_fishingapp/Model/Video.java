package com.jhm69.farhad_fishingapp.Model;

import java.io.Serializable;

public class Video implements Serializable {

    private String id;
    private String title;
    private String date;
    private String text;
    private String image;
    private String link;
    private String category;

    public Video(String id, String title, String date, String text, String image, String link, String category) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.text = text;
        this.image = image;
        this.link = link;
        this.category = category;
    }

    public Video() {
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
