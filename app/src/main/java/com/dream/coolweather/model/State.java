package com.dream.coolweather.model;

/**
 * Created by SuSong on 2015/3/4 0004.
 */
public class State {

    private int id;
    private String stateName;
    private String stateCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }
}
