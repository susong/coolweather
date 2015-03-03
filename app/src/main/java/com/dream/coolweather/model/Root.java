package com.dream.coolweather.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SuSong on 2015/3/2 0002.
 */
public class Root {
    private List<Area> areas;

    public Root() {
        areas = new ArrayList<>();
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }
}
