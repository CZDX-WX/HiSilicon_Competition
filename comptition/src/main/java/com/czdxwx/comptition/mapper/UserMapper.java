package com.czdxwx.comptition.mapper;

import com.czdxwx.comptition.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 12265
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-06-12 01:40:35
* @Entity com.czdxwx.comptition.model.User
*/

@Mapper
public interface UserMapper extends BaseMapper<User> {

}




