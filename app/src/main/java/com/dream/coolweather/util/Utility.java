package com.dream.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dream.coolweather.db.CoolWeatherDB;
import com.dream.coolweather.model.Area;
import com.dream.coolweather.model.City;
import com.dream.coolweather.model.County;
import com.dream.coolweather.model.Province;
import com.dream.coolweather.model.State;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    /**
     * 解析服务器返回的JSON数据，并将解析出的数据存储到本地
     */
    public static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            JSONArray weathers = jsonObject.getJSONArray("weather");
            JSONObject weather = (JSONObject) weathers.get(0);
            String city_name = weather.getString("city_name");
            String last_update = weather.getString("last_update");
            JSONObject now = weather.getJSONObject("now");
            String text = now.getString("text");
            String temperature = now.getString("temperature");
            String feels_like = now.getString("feels_like");
            String wind_direction = now.getString("wind_direction");
            String wind_speed = now.getString("wind_speed");
            String wind_scale = now.getString("wind_scale");
            String humidity = now.getString("humidity");
            String visibility = now.getString("visibility");
            String pressure = now.getString("pressure");
            saveWeatherInfo(context, city_name, last_update, text, temperature, feels_like, wind_direction,
                    wind_speed, wind_scale, humidity, visibility, pressure);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param city_name      城市名
     * @param last_update    数据更新时间
     * @param text           天气情况
     * @param temperature    当前实时温度
     * @param feels_like     当前实时体感温度
     * @param wind_direction 风向
     * @param wind_speed     风速。单位：km/h
     * @param wind_scale     风力等级
     * @param humidity       湿度。单位：百分比%
     * @param visibility     能见度。单位：公里km
     * @param pressure       气压。单位：百帕hPa
     */
    public static void saveWeatherInfo(Context context, String city_name, String last_update,
                                       String text, String temperature, String feels_like,
                                       String wind_direction, String wind_speed, String wind_scale,
                                       String humidity, String visibility, String pressure) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy年M月d日 HH时mm分ss秒", Locale.CHINA);
        String last_update_time = "";
        try {
            Date date = simpleDateFormat.parse(last_update);
            last_update_time = simpleDate.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", city_name);
        editor.putString("last_update", last_update_time);
        editor.putString("current_date", format.format(new Date()));
        editor.putString("text", text);
        editor.putString("temperature", temperature);
        editor.putString("feels_like", feels_like);
        editor.putString("wind_direction", wind_direction);
        editor.putString("wind_speed", wind_speed);
        editor.putString("wind_scale", wind_scale);
        editor.putString("humidity", humidity);
        editor.putString("visibility", visibility);
        editor.putString("pressure", pressure);
        editor.apply();
    }
}
