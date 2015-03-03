package com.dream.coolweather.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SuSong on 2015/3/2 0002.
 */
public class Area {
    private String id;
    private String name;
    private String en;

    private List<Area> areas;

    private List<City> cities;

    public Area(String id, String name, String en) {
        this.id = id;
        this.name = name;
        this.en = en;
        areas = new ArrayList<>();
        cities = new ArrayList<>();
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

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }
}
