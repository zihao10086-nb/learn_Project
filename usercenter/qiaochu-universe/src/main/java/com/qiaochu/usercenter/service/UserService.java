package com.qiaochu.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qiaochu.usercenter.model.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 35341
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2025-01-28 14:38:38
 */
public interface UserService extends IService<User> {
    /**
     * 登录态键
     */
    String USER_LOGIN_STATE = "userLoginState";

    /**
     * 用户注册
     *
     * @param userAccount   账号
     * @param password      密码
     * @param checkPassword 确认密码
     * @return
     */
    long userRegister(String userAccount, String password, String checkPassword);

    /**
     * 用户注册
     * @param userAccount
     * @param password
     * @param request
     * @return
     */
    User userLogin(String userAccount, String password, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */

    User getSafetyUser(User originUser);
    /**
     * 用户注销
     *
     * @return
     */
    int userLogout(HttpServletRequest request);
}
