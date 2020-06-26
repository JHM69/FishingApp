package com.jhm69.farhad_fishingapp.Model;

public class Angler {
    private String blood;
    private String fb;
    private String home;
    private String img;
    private String name;
    private String pesha;
    private String number;
    private String uid;


    public Angler(String blood, String fb, String home, String img, String name, String number, String pesha, String uid) {
        this.blood = blood;
        this.fb = fb;
        this.home = home;
        this.img = img;
        this.name = name;
        this.pesha = pesha;
        this.number = number;
        this.uid = uid;
    }


    public Angler() {
    }

    public String getPesha() {
        return pesha;
    }
    public String getBlood() {
        return blood;
    }

    public String getFb() {
        return fb;
    }

    public String getHome() {
        return home;
    }

    public String getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getUid() {
        return uid;
    }
}
