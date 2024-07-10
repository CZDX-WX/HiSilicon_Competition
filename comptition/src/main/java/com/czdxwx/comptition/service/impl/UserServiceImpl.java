package com.czdxwx.comptition.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdxwx.comptition.model.User;
import com.czdxwx.comptition.service.UserService;
import com.czdxwx.comptition.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 12265
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-06-12 01:40:35
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private UserMapper userMapper;
    @Override
    public boolean validateUser(User user) {
        // 创建查询条件
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("email", user.getEmail());

        // 查询用户列表
        List<User> userList = userMapper.selectByMap(queryMap);

        // 验证密码
        if (userList.size() == 1) {
            User dbUser = userList.get(0);
            return dbUser.getPassword().equals(user.getPassword());
        }

        return false;
    }
}




