package com.simple.webdemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.simple.webdemo.sys.entity.User;
import com.simple.webdemo.utils.JwtUtil;

@SpringBootTest
public class jwtTest {
    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void jwtTest(){
        User user = new User();
        user.setUsername("张三");
        user.setPhone("1829663466");
        String createToken = jwtUtil.createToken(user);
        System.out.println(createToken);
    }
}
