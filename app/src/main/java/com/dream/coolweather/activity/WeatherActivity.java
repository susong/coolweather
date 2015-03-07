package com.dream.coolweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dream.coolweather.R;
import com.dream.coolweather.service.AutoUpdateService;
import com.dream.coolweather.util.HttpCallbackListener;
import com.dream.coolweather.util.HttpUtil;
import com.dream.coolweather.util.Utility;

/**
 * Created by SuSong on 2015/3/5 0005.
 */
public class WeatherActivity extends Activity implements View.OnClickListener {

    private Button switch_city;
    private TextView city_name;
    private Button refresh_weather;
    private TextView last_update;
    private LinearLayout weather_info_layout;
    private TextView current_date;
    private TextView weather_text;
    private TextView temperature;
    private TextView feels_like;
    private TextView wind_direction;
    private TextView wind_speed;
    private TextView wind_scale;
    private TextView humidity;
    private TextView visibility;
    private TextView pressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);
        switch_city = (Button) findViewById(R.id.switch_city);
        city_name = (TextView) findViewById(R.id.city_name);
        refresh_weather = (Button) findViewById(R.id.refresh_weather);
        last_update = (TextView) findViewById(R.id.last_update);
        weather_info_layout = (LinearLayout) findViewById(R.id.weather_info_layout);
        current_date = (TextView) findViewById(R.id.current_date);
        weather_text = (TextView) findViewById(R.id.weather_text);
        temperature = (TextView) findViewById(R.id.temperature);
        feels_like = (TextView) findViewById(R.id.feels_like);
        wind_direction = (TextView) findViewById(R.id.wind_direction);
        wind_speed = (TextView) findViewById(R.id.wind_speed);
        wind_scale = (TextView) findViewById(R.id.wind_scale);
        humidity = (TextView) findViewById(R.id.humidity);
        visibility = (TextView) findViewById(R.id.visibility);
        pressure = (TextView) findViewById(R.id.pressure);

        switch_city.setOnClickListener(this);
        refresh_weather.setOnClickListener(this);

        String countyCode = getIntent().getStringExtra("county_code");
        if (!TextUtils.isEmpty(countyCode)) {
            last_update.setText("同步中...");
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            editor.putString("county_code", countyCode);
            editor.apply();
            weather_info_layout.setVisibility(View.INVISIBLE);
            city_name.setVisibility(View.INVISIBLE);
            queryWeatherInfo(countyCode);
        } else {
            showWeather();
        }
    }

    private void queryWeatherInfo(String countyCode) {
        String address = "https://api.thinkpage.cn/v2/weather/now.json?city=" + countyCode + "&language=zh-chs&unit=c&key=UXF435TVI0";
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if (Utility.handleWeatherResponse(WeatherActivity.this, response)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                            last_update.setText(preferences.getString("status", ""));
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        last_update.setText("同步失败");
                    }
                });
            }
        });
    }


    private void showWeather() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        city_name.setText(preferences.getString("city_name", ""));
        last_update.setText(preferences.getString("last_update", ""));
        current_date.setText(preferences.getString("current_date", ""));
        weather_text.setText(preferences.getString("text", ""));
        temperature.setText(preferences.getString("temperature", "") + "℃");
        feels_like.setText(preferences.getString("feels_like", ""));
        wind_direction.setText(preferences.getString("wind_direction", ""));
        wind_speed.setText(preferences.getString("wind_speed", ""));
        wind_scale.setText(preferences.getString("wind_scale", ""));
        humidity.setText(preferences.getString("humidity", ""));
        visibility.setText(preferences.getString("visibility", ""));
        pressure.setText(preferences.getString("pressure", ""));
        weather_info_layout.setVisibility(View.VISIBLE);
        city_name.setVisibility(View.VISIBLE);

        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_city:
                Intent intent = new Intent(this, ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                startActivity(intent);
                finish();
                break;
            case R.id.refresh_weather:
                last_update.setText("同步中...");
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                String weatherCode = preferences.getString("county_code", "");
                if (!TextUtils.isEmpty(weatherCode)) {
                    queryWeatherInfo(weatherCode);
                }
                break;
        }
    }
}
