package com.w6n.usercenter.service;

import com.w6n.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author 往年
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-08-04 02:34:37
*/
public interface UserService extends IService<User> {

    Integer userRegister(String userAccount,String password,String checkPassword);

    User userLogin(String userAccount, String password, HttpServletRequest request);

    /**
     * 用户注销
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);
}
