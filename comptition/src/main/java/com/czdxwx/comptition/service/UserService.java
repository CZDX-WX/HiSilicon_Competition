package com.czdxwx.comptition.service;

import com.czdxwx.comptition.mapper.UserMapper;
import com.czdxwx.comptition.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 12265
* @description 针对表【user】的数据库操作Service
* @createDate 2024-06-12 01:40:35
*/


public interface UserService extends IService<User> {

    boolean validateUser(User user) ;
}
