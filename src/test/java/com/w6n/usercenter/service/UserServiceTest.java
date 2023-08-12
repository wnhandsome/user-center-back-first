package com.w6n.usercenter.service;
import java.util.Date;
import java.util.List;

import com.w6n.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void register(){
        String userAccount = "   ";
        String password = "123456";
        String checkPassword = "123456";
        int res = userService.userRegister(userAccount, password, checkPassword);
        Assertions.assertEquals(-1,res);       //非空测试

        userAccount = "123";
        res = userService.userRegister(userAccount, password, checkPassword);
        Assertions.assertEquals(-1,res);       //长度测试

        userAccount = "123456？";
        res = userService.userRegister(userAccount, password, checkPassword);
        Assertions.assertEquals(-1,res);       //特殊字符测试

        userAccount = "123456";
        password = "1234567";
        res = userService.userRegister(userAccount, password, checkPassword);
        Assertions.assertEquals(-1,res);       //确认密码不同测试

        userAccount = "admin";
        password = "123456";
        res = userService.userRegister(userAccount, password, checkPassword);
        Assertions.assertEquals(-1,res);       //账号不能重复测试

        userAccount = "123456";
        res = userService.userRegister(userAccount, password, checkPassword);
        Assertions.assertNotEquals(-1,res);       //成功测试

    }

    @Test
    public void login(){

    }
}