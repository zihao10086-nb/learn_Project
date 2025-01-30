package com.qiaochu.usercenter.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = -743305834028200215L;
    private String userAccount;
    private String userPassword;

}
