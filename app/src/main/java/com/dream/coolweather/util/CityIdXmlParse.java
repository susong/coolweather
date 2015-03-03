package com.dream.coolweather.util;

import android.content.Context;
import android.util.Log;

import com.dream.coolweather.R;
import com.dream.coolweather.model.Area;
import com.dream.coolweather.model.City;
import com.dream.coolweather.model.Root;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by SuSong on 2015/3/2 0002.
 */
public class CityIdXmlParse {

    public static void parseCityId(Context context) {
        Root root = new Root();
        int depth = 0;
        int depth0count = 0;
        int depth1count = 0;
        int depth2count = 0;
        try {
            XmlPullParser parser = context.getResources().getXml(R.xml.city_id);
//            XmlPullParser parser = Resources.getSystem().getXml(R.xml.city_id);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("area".equals(nodeName)) {
                            String id = parser.getAttributeValue(0);
                            String name = parser.getAttributeValue(1);
                            String en = parser.getAttributeValue(2);
                            Area area = new Area(id, name, en);
                            Log.d("CoolWeather", "area id = " + id + " , name = " + name + " , depth = " + depth + " , depth0count = " + depth0count + " , depth1count = " + depth1count + " , depth2count = " + depth2count);
                            if (depth == 0) {
                                root.getAreas().add(area);
                            } else if (depth == 1) {
                                root.getAreas().get(depth0count).getAreas().add(area);
                            } else if (depth == 2) {
                                root.getAreas().get(depth0count).getAreas().get(depth1count).getAreas().add(area);
                            }
                            depth++;
                        } else if ("city".equals(nodeName)) {
                            String id = parser.getAttributeValue(0);
                            String name = parser.getAttributeValue(1);
                            String en = parser.getAttributeValue(2);
                            City city = new City(id, name, en);
                            Log.d("CoolWeather", "city id = " + id + " , name = " + name + " , depth = " + depth + " , depth0count = " + depth0count + " , depth1count = " + depth1count + " , depth2count = " + depth2count);
                            root.getAreas().get(depth0count).getAreas().get(depth1count).getAreas().get(depth2count).getCities().add(city);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("area".equals(nodeName)) {
                            if (depth == 3) {
                                depth2count++;
                            } else if (depth == 2) {
                                depth1count++;
                                depth2count = 0;
                            } else if (depth == 1) {
                                depth0count++;
                                depth1count = 0;
                            }
                            depth--;
                        }
                        break;
                }
                eventType = parser.next();
            }

            System.out.println("");
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

    }
}
