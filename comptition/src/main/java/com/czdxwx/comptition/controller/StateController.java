package com.czdxwx.comptition.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.czdxwx.comptition.entity.ComFile;
import com.czdxwx.comptition.entity.Result;
import com.czdxwx.comptition.entity.StateAndImage;
import com.czdxwx.comptition.model.State;
import com.czdxwx.comptition.service.OSSService;
import com.czdxwx.comptition.service.RestTemplateService;
import com.czdxwx.comptition.service.StateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/states")
public class StateController {

    //! http服务
    @Autowired
    private RestTemplateService restTemplateService;

    @Autowired
    private StateService stateService;
    @Autowired
    private OSSService ossService;

    private StringBuilder cachedStringBuilder = new StringBuilder(); // 使用StringBuilder存储累积的字符串
    private boolean isFirstPartReceived = false; // 标志第一部分是否已接收

    @PostMapping("/add")
    public Result add(@RequestBody StateAndImage stateAndImage) {
        log.info("stateAndImage: {}", stateAndImage.toString());

        if (isFirstPartReceived) {
            cachedStringBuilder.append(stateAndImage.getBase64File().getFile()); // 第二次接收到的部分追加到已有的字符串后面
            isFirstPartReceived = false; // 重置标志，等待下一次接收
            stateAndImage.getBase64File().setFile(cachedStringBuilder.toString());
            cachedStringBuilder.setLength(0); // 清空缓存
        }

        State state = stateAndImage.getState();
        String wgs84 = restTemplateService.getWgs84(state.getLongitude() + "," + state.getLatitude()).getLocations();
        String[] temp = wgs84.split(",");
        state.setLongitude(temp[0]);
        state.setLatitude(temp[1]);
        state.setDeviceName("步行辅助导航设备");
        state.setOwner("贾梓浚");
        //! 获取地理结构性位置
        JSONObject Geocode = restTemplateService.getGeocode(wgs84);
        JSONObject regeocode = Geocode.getJSONObject("regeocode");
        JSONObject addressComponent = regeocode.getJSONObject("addressComponent");
        String city = addressComponent.getString("city");
        String province = addressComponent.getString("province");
        String adcode = addressComponent.getString("adcode");
        String district = addressComponent.getString("district");

        //! 获取气温
        JSONObject result = restTemplateService.getTemperature(adcode);
        JSONArray lives = result.getJSONArray("lives");
        String temperature = lives.getJSONObject(0).getString("temperature");

        log.info("此状态获取的省份：{}", province);
        log.info("此状态获取的城市：{}", city);
        log.info("此状态获取的城市编码：{}", adcode);
        log.info("此状态获取的行政区：{}", district);
        log.info("此状态获取的气温：{}", temperature);

        state.setProvince(province);
        state.setCity(city);
        state.setDistrict(district);
        state.setTemperature(temperature);
        stateService.save(state);
        ossService.uploadBase64(stateAndImage.getBase64File());

        log.info("*********************************状态上传结束************************************");
        return Result.success();
    }

    @PostMapping(value = "/Half")
    public Result uploadFile(@RequestBody ComFile comFile) {
        cachedStringBuilder.setLength(0); // 清空缓存
        if (!isFirstPartReceived) {//第一次
            log.info("前半段接口读取到");
            cachedStringBuilder.append(comFile.getFileName()); // 第一次接收到的部分直接追加
            isFirstPartReceived = true;
            return Result.success();
        } else {
            return Result.error("error");
        }
    }


    @GetMapping("/getLists")
    public Result<List<State>> list(String time) {
        int hours = Integer.parseInt(time);
        return Result.success(stateService.getStatesWithinHours(hours));
    }


}
