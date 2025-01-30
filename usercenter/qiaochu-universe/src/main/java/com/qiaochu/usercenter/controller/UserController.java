package com.qiaochu.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.qiaochu.usercenter.common.BaseResponse;
import com.qiaochu.usercenter.common.ErrorCode;
import com.qiaochu.usercenter.common.ResultUtils;
import com.qiaochu.usercenter.exception.BusinessException;
import com.qiaochu.usercenter.model.domain.User;
import com.qiaochu.usercenter.model.domain.UserLoginRequest;
import com.qiaochu.usercenter.model.domain.UserRegisterRequest;
import com.qiaochu.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.qiaochu.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.qiaochu.usercenter.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
           throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String password = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, password, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(userAccount, password, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<User> doLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /**
     * 查询用户
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        //鉴权
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            wrapper.like("username", username);
        }
        List<User> users = userService.list(wrapper);
        List<User> list = users.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    /**
     * 删除用户
     */
    @PostMapping("delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        //鉴权
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 判断是否管理员
     */
    public boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getRole() == ADMIN_ROLE;
    }

    /**
     * 获取当前用户登录态
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;

        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        long userId = currentUser.getId();
        //todo 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    /**
     * 用户注销
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> logout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int i = userService.userLogout(request);
        return ResultUtils.success(i);
    }

}
