package com.dream.coolweather;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.dream.coolweather.util.AreaXmlParse;
import com.dream.coolweather.util.HttpCallbackListener;
import com.dream.coolweather.util.HttpUtil;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testSendHttpRequest() {
        HttpUtil.sendHttpRequest("http://www.weather.com.cn/data/list3/city.xml", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("CoolWeather", "=======================" + response);
            }

            @Override
            public void onError(Exception e) {
                Log.d("CoolWeather", "=======================" + e.getMessage());
            }
        });
    }

    public void testParseCityId() {
        AreaXmlParse.parseCityId(getContext());
    }
}