package com.dream.coolweather.util;

import android.content.Context;

import com.dream.coolweather.R;
import com.dream.coolweather.model.Area;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SuSong on 2015/3/2 0002.
 */
public class AreaXmlParse {

    public static List<Area> parseCityId(Context context) {
        List<Area> areas = new ArrayList<Area>();
        int depth = 0;
        int depth0count = 0;
        int depth1count = 0;
        int depth2count = 0;
        try {
            XmlPullParser parser = context.getResources().getXml(R.xml.area);
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
                            if (depth == 0) {
                                areas.add(area);
                            } else if (depth == 1) {
                                areas.get(depth0count).getAreas().add(area);
                            } else if (depth == 2) {
                                areas.get(depth0count).getAreas().get(depth1count).getAreas().add(area);
                            }
                            depth++;
                        } else if ("city".equals(nodeName)) {
                            String id = parser.getAttributeValue(0);
                            String name = parser.getAttributeValue(1);
                            String en = parser.getAttributeValue(2);
                            Area area = new Area(id, name, en);
                            areas.get(depth0count).getAreas().get(depth1count).getAreas().get(depth2count).getAreas().add(area);
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
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return areas;
    }
}
