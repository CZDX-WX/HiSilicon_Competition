package com.czdxwx.comptition.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.czdxwx.comptition.entity.ComFile;
import com.czdxwx.comptition.entity.PolyLineAndPath;
import com.czdxwx.comptition.entity.Result;
import com.czdxwx.comptition.model.Polyline;
import com.czdxwx.comptition.service.OSSService;
import com.czdxwx.comptition.service.PolylineService;
import com.czdxwx.comptition.service.RestTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/route")
public class RouteController {

    @Autowired
    private RestTemplateService restTemplateService;

    @Autowired
    private OSSService ossService;


    @Autowired
    private PolylineService polylineService;

    @PostMapping("/getPolyLineAndPath")
    public Result getPolyLine(@RequestBody ComFile OSS_PATH) {
        QueryWrapper<Polyline> queryWrapper = new QueryWrapper<>();
        log.info("**********************************这是getPolyLine的日志*************************");
        log.info("查询语句：{}",OSS_PATH.getFileName());
        queryWrapper.eq("name", OSS_PATH.getFileName());
        log.info("查询wrapper：{}",queryWrapper.toString());
        Polyline polyline = polylineService.getOne(queryWrapper);
        log.info("查询结果：{}",polyline==null?"null":polyline.toString());
        String s=ossService.exportFileAsBase64(OSS_PATH.getFileName());
        log.info("oss取到的数据长度：{}",s.length());
        log.info("*******************************************结束*********************************");
        if(polyline==null){
            return Result.error("NULL");
        }else{
            return Result.success(new PolyLineAndPath(polyline.getPolylines(),s));
        }
    }


}
