package com.czdxwx.comptition.controller;

import com.czdxwx.comptition.entity.Result;
import com.czdxwx.comptition.model.Device;
import com.czdxwx.comptition.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @PostMapping("/getDevices")
    public Result<List<Device>> getDevices() {
        List<Device> devices = deviceService.list();
        return Result.success(devices);
    }
}
