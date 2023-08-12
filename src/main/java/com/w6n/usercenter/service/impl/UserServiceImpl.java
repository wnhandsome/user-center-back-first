package com.w6n.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.w6n.usercenter.common.ErrorCode;
import com.w6n.usercenter.exception.BusinessException;
import com.w6n.usercenter.model.domain.User;
import com.w6n.usercenter.service.UserService;
import com.w6n.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.regex.Pattern;

import static com.w6n.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author 往年
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2023-08-04 02:34:37
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    /**
     * 判断用户账号密码的 regex 长度在6到20位之间，只包含字母和数字 (^[a-zA-Z0-9]{6,20}$)
     */
    private final String validPattern = "^[a-zA-Z0-9]{4,20}$";

    /**
     * 盐值，混淆密码
     */
    private final String SALT = "w6n";

    @Override
    public Integer userRegister(String userAccount, String password, String checkPassword) {
        //判空
        if (!StringUtils.hasText(userAccount) || !StringUtils.hasText(password) || !StringUtils.hasText(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        //长度在4到20位之间，只包含字母和数字 (^[a-zA-Z0-9]{6,20}$)
        if (!Pattern.matches(validPattern,userAccount) || !Pattern.matches(validPattern,password)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号或密码应该为字母和数字，长度在4到20位之间");
        }
        //密码和确认密码相同
        if (!password.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码和确认密码不一致");
        }
        //账号不能重复
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserAccount,userAccount);
        Long count = userMapper.selectCount(wrapper);
        if (count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号重复错误");
        }
        //密码加密
        final String SALT = "w6n";
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        //向数据库插入用户数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setPassword(encryptPassword);
        this.save(user);
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String password,HttpServletRequest request) {
        //1.校验
        //判空
        if (!StringUtils.hasText(userAccount) || !StringUtils.hasText(password) ){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        //长度在4到20位之间，只包含字母和数字 (^[a-zA-Z0-9]{6,20}$)
        if (!Pattern.matches(validPattern,userAccount) || !Pattern.matches(validPattern,password)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号或密码应该为字母和数字，长度在4到20位之间");
        }
        //2.是否存在该用户
        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        //根据账号查找密码，检验密码是否正确
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserAccount,userAccount);
        wrapper.eq(User::getPassword,encryptPassword);
        User user = userMapper.selectOne(wrapper);
        if (user == null){  //用户不存在
            log.info("用户不存在，账号密码错误");
            throw new BusinessException(ErrorCode.NO_USER,"账号密码错误");
        }
        //3.用户脱敏
        User safetyUser = getSafetyUser(user);
        //4.记录用户的登录状态
        HttpSession session = request.getSession();
        session.setAttribute(USER_LOGIN_STATE,safetyUser);
        return safetyUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    //用户脱敏
    @Override
    public User getSafetyUser(User originUser){
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setRole(originUser.getRole());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUpdateTime(originUser.getUpdateTime());
        return safetyUser;
    }
}




