package com.w6n.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 5708466358452656756L;
    private String userAccount;
    private String password;
    private String checkPassword;
}
