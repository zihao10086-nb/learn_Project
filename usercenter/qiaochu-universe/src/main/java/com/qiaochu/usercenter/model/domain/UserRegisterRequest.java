package com.qiaochu.usercenter.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 9006163622805634114L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;

}
