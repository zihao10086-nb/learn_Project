package com.qiaochu.usercenter.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.qiaochu.usercenter.common.ErrorCode;
import com.qiaochu.usercenter.exception.BusinessException;
import com.qiaochu.usercenter.mapper.UserMapper;
import com.qiaochu.usercenter.model.domain.User;
import com.qiaochu.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 35341
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2025-01-28 14:38:38
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    /**
     * 盐值
     */
    private static final String SALT = "qiaochu";

    @Autowired
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String password, String checkPassword) {

        //1.校验
        if (StringUtils.isAnyBlank(userAccount, password, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账户过短");
        }
        if (password.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度过短");
        }
        //特殊字符
        String validateRegExp = "[`~!@#$%^&*()+=|{}':;', \\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validateRegExp).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }
        //两次是否相同
        if (!password.equals(checkPassword)) {
            return -1;
        }
        //账户不能重复
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userAccount", userAccount);
        Long count = userMapper.selectCount(wrapper);

        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号重复");
        }
        //2.加密

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());

        //3.插入数据

        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean save = this.save(user);
        if (!save) {
            return -1;
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String password, HttpServletRequest request) {

        //1.校验
        if (StringUtils.isAnyBlank(userAccount, password)) {
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (password.length() < 8) {
            return null;
        }

        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }
        //2.检验密码是否正确
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userAccount", userAccount);
        wrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            log.info("user login error,userAccount not match userPassword");
            return null;
        }
        //3.用户信息脱敏
        User safetyUser = getSafetyUser(user);
        //4.记录登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        //5.返回安全信息
        return safetyUser;

    }

    /**
     * 用户脱敏
     */

    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setRole(originUser.getRole());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

}




