package com.w6n.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -5753584211717562815L;
    private String userAccount;
    private String password;
}
