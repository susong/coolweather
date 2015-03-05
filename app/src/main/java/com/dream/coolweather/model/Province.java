package com.dream.coolweather.model;

/**
 * Created by asus on 2015/2/27.
 */
public class Province {
    private int id;
    private String provinceName;
    private String provinceCode;
    private String stateId;

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

}
