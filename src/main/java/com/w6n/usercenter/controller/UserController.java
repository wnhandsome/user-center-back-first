package com.w6n.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.w6n.usercenter.common.BaseResponse;
import com.w6n.usercenter.common.ErrorCode;
import com.w6n.usercenter.common.ResultUtil;
import com.w6n.usercenter.exception.BusinessException;
import com.w6n.usercenter.model.domain.User;
import com.w6n.usercenter.model.request.UserLoginRequest;
import com.w6n.usercenter.model.request.UserRegisterRequest;
import com.w6n.usercenter.service.UserService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.w6n.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.w6n.usercenter.constant.UserConstant.USER_LOGIN_STATE;


@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Integer> register(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数不能为空");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (!StringUtils.hasText(userAccount) || !StringUtils.hasText(password) || !StringUtils.hasText(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数不能为空");
        }
        Integer res = userService.userRegister(userAccount, password, checkPassword);
        return ResultUtil.success(res);
    }

    @PostMapping("/login")
    public BaseResponse<User> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数不能为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String password = userLoginRequest.getPassword();
        if (!StringUtils.hasText(userAccount) || !StringUtils.hasText(password) ){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数不能为空");
        }
        User user = userService.userLogin(userAccount, password, request);
        return ResultUtil.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> logout(HttpServletRequest request){
        if (request == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int res = userService.userLogout(request);
        return ResultUtil.success(res,"退出成功");
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        //校验是否被禁用
        if (currentUser.getUserStatus() == 1){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        return ResultUtil.success(currentUser);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> getByUsername(String username,HttpServletRequest request){
        if (!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(username)){
            wrapper.like(User::getUsername,username);
        }
        List<User> userList = userService.list(wrapper);
        List<User> safetyUserList = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtil.success(safetyUserList);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteById(@RequestBody int id,HttpServletRequest request){
        if (!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(id);
        return ResultUtil.success(b);
    }

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getRole() == ADMIN_ROLE;
    }

}
