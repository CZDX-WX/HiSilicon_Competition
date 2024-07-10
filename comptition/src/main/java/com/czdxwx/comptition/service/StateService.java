package com.czdxwx.comptition.service;

import com.czdxwx.comptition.model.State;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 12265
* @description 针对表【state】的数据库操作Service
* @createDate 2024-07-07 14:13:41
*/

public interface StateService extends IService<State> {
    List<State> getStatesWithinHours(int hours);
}
