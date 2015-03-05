package com.dream.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dream.coolweather.model.City;
import com.dream.coolweather.model.County;
import com.dream.coolweather.model.Province;
import com.dream.coolweather.model.State;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2015/2/27.
 */
public class CoolWeatherDB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "cool_weather";

    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static CoolWeatherDB coolWeatherDB;

    private SQLiteDatabase db;

    /**
     * 将构造方法私有化
     */
    private CoolWeatherDB(Context context) {
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取CoolWeatherDB的实例
     */
    public synchronized static CoolWeatherDB getInstance(Context context) {
        if (coolWeatherDB == null) {
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    /**
     * 将State实例存储到数据库。
     */
    public void saveState(State state) {
        if (state != null) {
            ContentValues values = new ContentValues();
            values.put("state_name", state.getStateName());
            values.put("state_code", state.getStateCode());
            db.insert("State", null, values);
        }
    }

    /**
     * 从数据库读取国家信息。
     */
    public List<State> loadStates() {
        List<State> list = new ArrayList<>();
        Cursor cursor = db.query("State", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                State state = new State();
                state.setId(cursor.getInt(cursor.getColumnIndex("id")));
                state.setStateName(cursor.getString(cursor.getColumnIndex("state_name")));
                state.setStateCode(cursor.getString(cursor.getColumnIndex("state_code")));
                list.add(state);
            } while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * 将Province实例存储到数据库。
     */
    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            values.put("province_code", province.getProvinceCode());
            values.put("state_code", province.getStateId());
            db.insert("Province", null, values);
        }
    }

    /**
     * 从数据库读取全国所有的省份信息。
     */
    public List<Province> loadProvinces(String state_code) {
        List<Province> list = new ArrayList<>();
        Cursor cursor = db.query("Province", null, "state_code = ?", new String[]{state_code}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                province.setStateId(state_code);
                list.add(province);
            } while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * 将City实例存储到数据库。
     */
    public void saveCity(City city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("city_name", city.getCityName());
            values.put("city_code", city.getCityCode());
            values.put("province_code", city.getProvinceId());
            db.insert("City", null, values);
        }
    }

    /**
     * 从数据库读取某省下所有的城市信息。
     */
    public List<City> loadCity(String province_code) {
        List<City> list = new ArrayList<>();
        Cursor cursor = db.query("City", null, "province_code = ?", new String[]{province_code}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(province_code);
                list.add(city);
            } while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * 将County实例存储到数据库。
     */
    public void saveCounty(County county) {
        if (county != null) {
            ContentValues values = new ContentValues();
            values.put("county_name", county.getCountyName());
            values.put("county_code", county.getCountyCode());
            values.put("city_code", county.getCityId());
            db.insert("County", null, values);
        }
    }

    /**
     * 从数据库读取某城市下所有的县信息。
     */
    public List<County> loadCounties(String city_code) {
        List<County> list = new ArrayList<>();
        Cursor cursor = db.query("County", null, "city_code = ?", new String[]{city_code}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(city_code);
                list.add(county);
            } while (cursor.moveToNext());
        }
        return list;
    }
}
