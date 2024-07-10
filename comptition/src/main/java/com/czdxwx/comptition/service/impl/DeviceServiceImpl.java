package com.czdxwx.comptition.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdxwx.comptition.model.Device;
import com.czdxwx.comptition.service.DeviceService;
import com.czdxwx.comptition.mapper.DeviceMapper;
import org.springframework.stereotype.Service;

/**
* @author 12265
* @description 针对表【device】的数据库操作Service实现
* @createDate 2024-06-11 18:15:37
*/
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device>
    implements DeviceService{
}




