package com.czdxwx.comptition.utils;

import static java.lang.Math.*;

public class CoordinateTransformUtil {

    private static final double PI = Math.PI;
    private static final double A = 6378245.0;  // 长半轴
    private static final double ES = 0.00669342162296594323;  // 偏心率平方

    private static double transformLat(double lng, double lat) {
        double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * sqrt(abs(lng));
        ret = getRet(lng, lat, ret);
        ret += (160.0 * sin(lat / 12.0 * PI) + 320 * sin(lat * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double getRet(double lng, double lat, double ret) {
        ret += (20.0 * sin(6.0 * lng * PI) + 20.0 * sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * sin(lat * PI) + 40.0 * sin(lat / 3.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLng(double lng, double lat) {
        double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * sqrt(abs(lng));
        ret = getRet(lng, lng, ret);
        ret += (150.0 * sin(lng / 12.0 * PI) + 300.0 * sin(lng / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

    public static String gcj02ToWgs84(String str) {
        String [] arr = str.split(",");
        double gcjLng=Double.parseDouble(arr[0]);
        double gcjLat=Double.parseDouble(arr[1]);

        double dLat = transformLat(gcjLng - 105.0, gcjLat - 35.0);
        double dLng = transformLng(gcjLng - 105.0, gcjLat - 35.0);
        double radLat = gcjLat / 180.0 * PI;
        double magic = sin(radLat);
        magic = 1 - ES * magic * magic;
        double sqrtMagic = sqrt(magic);
        dLat = (dLat * 180.0) / ((A * (1 - ES)) / (magic * sqrtMagic) * PI);
        dLng = (dLng * 180.0) / (A / sqrtMagic * cos(radLat) * PI);
        double mgLat = gcjLat + dLat;
        double mgLng = gcjLng + dLng;
        double lng = gcjLng * 2 - mgLng;
        double lat = gcjLat * 2 - mgLat;
        return lng+","+lat;
    }

}
