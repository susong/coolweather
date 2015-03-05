package com.dream.coolweather.util;

import com.dream.coolweather.db.CoolWeatherDB;
import com.dream.coolweather.model.Area;
import com.dream.coolweather.model.City;
import com.dream.coolweather.model.County;
import com.dream.coolweather.model.Province;
import com.dream.coolweather.model.State;

import java.util.List;

/**
 * Created by SuSong on 2015/3/4 0004.
 */
public class Utility {
    public synchronized static boolean handleResponce(CoolWeatherDB coolWeatherDB, List<Area> areas) {
        if (areas != null) {
            for (Area s : areas) {
                State state = new State();
                state.setStateCode(s.getId());
                state.setStateName(s.getName());
                coolWeatherDB.saveState(state);
                for (Area p : s.getAreas()) {
                    Province province = new Province();
                    province.setProvinceCode(p.getId());
                    province.setProvinceName(p.getName());
                    province.setStateId(s.getId());
                    coolWeatherDB.saveProvince(province);
                    for (Area c : p.getAreas()) {
                        City city = new City();
                        city.setCityCode(c.getId());
                        city.setCityName(c.getName());
                        city.setProvinceId(p.getId());
                        coolWeatherDB.saveCity(city);
                        for (Area co : c.getAreas()) {
                            County county = new County();
                            county.setCountyCode(co.getId());
                            county.setCountyName(co.getName());
                            county.setCityId(c.getId());
                            coolWeatherDB.saveCounty(county);
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
}
