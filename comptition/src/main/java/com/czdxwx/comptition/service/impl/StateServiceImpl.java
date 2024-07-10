package com.czdxwx.comptition.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdxwx.comptition.model.State;
import com.czdxwx.comptition.service.StateService;
import com.czdxwx.comptition.mapper.StateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
* @author 12265
* @description 针对表【state】的数据库操作Service实现
* @createDate 2024-07-07 14:13:41
*/
@Service
public class StateServiceImpl extends ServiceImpl<StateMapper, State>
    implements StateService{
    @Autowired
    private StateMapper stateMapper;

    public List<State> getStatesWithinHours(int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -hours);
        Date startTime = calendar.getTime();

        QueryWrapper<State> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("date_time", startTime)
                .orderByDesc("date_time");  // 按时间降序排列

        return stateMapper.selectList(queryWrapper);
    }
}




