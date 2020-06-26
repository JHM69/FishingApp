package com.jhm69.farhad_fishingapp.Model;

public class Collection {
    private String model;
    private String subtitle;
    private String type;

    public Collection(String model, String subtitle, String type) {
        this.model = model;
        this.subtitle = subtitle;
        this.type = type;
    }

    public Collection() {
    }

    public String getModel() {
        return model;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getType() {
        return type;
    }
}
