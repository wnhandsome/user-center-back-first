package com.w6n.usercenter.mapper;

import com.w6n.usercenter.model.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 往年
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2023-08-04 02:34:37
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




