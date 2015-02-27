package com.dream.coolweather.util;

/**
 * Created by asus on 2015/2/27.
 */
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
