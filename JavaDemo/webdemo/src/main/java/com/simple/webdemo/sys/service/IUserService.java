package com.simple.webdemo.sys.service;

import com.simple.webdemo.sys.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author laocai
 * @since 2023-07-12
 */
public interface IUserService extends IService<User> {

    Map<String, Object> login(User user);

    Map<String, Object> getUserInfo(String token);

    public void  logout(String token);

    void addUser(User user);

    User getUserById(Integer id);

    void updateUser(User user);

    void DeleteUserById(Integer id);

}
