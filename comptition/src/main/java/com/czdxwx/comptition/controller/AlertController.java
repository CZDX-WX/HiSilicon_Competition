package com.czdxwx.comptition.controller;

import com.czdxwx.comptition.entity.Result;
import com.czdxwx.comptition.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/alert")
public class AlertController {

    @Autowired
    private AlertService alertService;

    @RequestMapping(value = "/pushMsg")
    public Result pushMsg() {
        alertService.pushAlert("危险");
        return Result.success();
    }
}
