package com.qiaochu.usercenter.service;


import com.qiaochu.usercenter.model.domain.User;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

import java.awt.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testDigest(){
        String s = DigestUtils.md5DigestAsHex(("qiaochu" + "123456").getBytes());
        System.out.println(s);
    }

    @Test
    public void testAddUser() {
        User user = new User();
        user.setUsername("qiaochu");
        user.setUserAccount("qiaochu123");
        user.setAvatarUrl("123");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("123");
        user.setEmail("123");
        boolean save = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(save);
    }

    @Test
    public void userRegister() {
        String userAccount="qiaochu";
        String password="";
        String checkPassword="123456";
        long result = userService.userRegister(userAccount, password, checkPassword);
        Assertions.assertEquals(-1,result);
        userAccount="chu";
        result = userService.userRegister(userAccount, password, checkPassword);
        Assertions.assertEquals(-1,result);
        userAccount="qiaochu";
        password="123456";
        result = userService.userRegister(userAccount, password, checkPassword);
        Assertions.assertEquals(-1,result);
        userAccount="qiao chu";
        password="12345678";
        checkPassword="12345678";
        result = userService.userRegister(userAccount, password, checkPassword);
        Assertions.assertEquals(-1,result);
        userAccount="qiaochu123";
        result = userService.userRegister(userAccount, password, checkPassword);
        Assertions.assertEquals(-1,result);
        userAccount="qiaochuadmin";
        result = userService.userRegister(userAccount, password, checkPassword);
        Assertions.assertTrue(result>0);



    }
}