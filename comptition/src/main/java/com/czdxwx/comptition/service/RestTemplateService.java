package com.czdxwx.comptition.service;

import com.alibaba.fastjson.JSONObject;
import com.czdxwx.comptition.Wgs84;
import com.czdxwx.comptition.entity.Destination;
import com.czdxwx.comptition.entity.Route;
import com.czdxwx.comptition.entity.TipInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateService {

    @Autowired
    private RestTemplate restTemplate;

    public Route getRouteJson(String origin, String destination) {
        String url = "https://restapi.amap.com/v3/direction/walking?origin=" + origin + "&destination=" + destination + "&key=d0f52bb91c6a794e2089d08bdf1bc41c";
        return restTemplate.getForObject(url, Route.class);
    }

    public TipInfo getTipInfoJson(String key, String location) {
        String url = "https://restapi.amap.com/v3/assistant/inputtips?&key=d0f52bb91c6a794e2089d08bdf1bc41c&location=" + location + "&keywords=" + key;
        return restTemplate.getForObject(url, TipInfo.class);
    }

    public Destination getDestinationJson(String origin, String keywords) {
        String url = "https://restapi.amap.com/v5/place/around?key=d0f52bb91c6a794e2089d08bdf1bc41c&radius=100000&location=" + origin + "&keywords=" + keywords;
        return restTemplate.getForObject(url, Destination.class);
    }


    public Wgs84 getWgs84(String str) {
        String url = "https://restapi.amap.com/v3/assistant/coordinate/convert?coordsys=gps&key=d0f52bb91c6a794e2089d08bdf1bc41c&locations=" + str;
        return restTemplate.getForObject(url, Wgs84.class);
    }


    public JSONObject getGeocode(String location) {
        String url="https://restapi.amap.com/v3/geocode/regeo?key=d0f52bb91c6a794e2089d08bdf1bc41c&extensions=base&radius=1000&location="+location;
        return restTemplate.getForObject(url, JSONObject.class);
    }

    public JSONObject getTemperature(String adcode){
        String url="https://restapi.amap.com/v3/weather/weatherInfo?key=d0f52bb91c6a794e2089d08bdf1bc41c&city="+adcode;
        return restTemplate.getForObject(url, JSONObject.class);
    }
}
