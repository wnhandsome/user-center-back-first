package com.w6n.usercenter.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * id主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * （用户名）昵称
     */
    private String username;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String password;

    /**
     * 头像地址
     */
    private String avatarUrl;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户状态 (0-正常,1-禁用)
     */
    private Integer userStatus;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户角色（0-普通用户,1-管理员）
     */
    private Integer role;

    /**
     * 创建时间 (数据插入)
     */
    private Date createTime;

    /**
     * 更新时间 (数据更新)
     */
    private Date updateTime;

    /**
     * 是否删除 0 1 (逻辑删除)
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}