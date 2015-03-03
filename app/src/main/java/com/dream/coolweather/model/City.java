package com.dream.coolweather.model;

/**
 * Created by asus on 2015/2/27.
 */
public class City {
    private String id;
    private String name;
    private String en;

    public City(String id, String name, String en) {
        this.id = id;
        this.name = name;
        this.en = en;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }
}
