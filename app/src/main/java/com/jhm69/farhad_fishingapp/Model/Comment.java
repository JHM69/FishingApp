package com.jhm69.farhad_fishingapp.Model;

public class Comment {
    private String userId;
    private String name;
    private String img;
    private String date;
    private String comment;

    public Comment(String userId,String name, String img, String date, String comment) {
        this.userId = userId;
        this.name = name;
        this.img = img;
        this.date = date;
        this.comment = comment;
    }

    public Comment() {
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getImg() {
        return img;
    }

    public String getDate() {
        return date;
    }

    public String getComment() {
        return comment;
    }
}
