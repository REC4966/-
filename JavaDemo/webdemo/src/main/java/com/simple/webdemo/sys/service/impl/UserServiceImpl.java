package com.simple.webdemo.sys.service.impl;

import com.alibaba.fastjson2.JSON;
import com.simple.webdemo.sys.entity.Menu;
import com.simple.webdemo.sys.entity.User;
import com.simple.webdemo.sys.entity.UserRole;
import com.simple.webdemo.sys.mapper.UserMapper;
import com.simple.webdemo.sys.mapper.UserRoleMapper;
import com.simple.webdemo.sys.service.IMenuService;
import com.simple.webdemo.sys.service.IUserService;
import com.simple.webdemo.utils.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author laocai
 * @since 2023-07-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

     @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JwtUtil jwtUtils;
    @Resource
    private UserRoleMapper userRoleMapper;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public Map<String, Object> login(User user) {
        // 根据用户名密码查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,user.getUsername());
        User loginUser = this.baseMapper.selectOne(wrapper);;
        // 不为空，返回用户信息
        if(loginUser != null && passwordEncoder.matches(user.getPassword(), loginUser.getPassword())){
            // 生成token
           // String key = "user:" + UUID.randomUUID();
           String key = jwtUtils.createToken(loginUser);

            loginUser.setPassword(null);
            // 保存到redis
            // redisTemplate.opsForValue().set(key, loginUser,30, TimeUnit.MINUTES);

            // 保存用户信息
            // 返回
            Map<String,Object> data = new HashMap<>();
            data.put("token", key);
            return data;
        }
        return null;
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        // Object object = redisTemplate.opsForValue().get(token);
         User loginUser=null;
        try{
        loginUser = jwtUtils.parseToken(token, User.class);
        }catch(Exception e){
            e.printStackTrace();
        }
    
        if(loginUser != null){
            // User loginUser = JSON.parseObject(JSON.toJSONString(user), User.class);
            Map<String,Object> data = new HashMap<>();
            data.put("name",loginUser.getUsername());
            data.put("avatar",loginUser.getAvatar());
            //角色
            List<String> roleList  = this.baseMapper.selectRole(loginUser.getId());
            data.put("roles", roleList);
            // 权限列表
            List<Menu> menuListByUserId = menuService.getMenuListByUserId(loginUser.getId());;
            data.put("menuList", menuListByUserId);
            return data;
        }
        return null;
    }


    @Override
    public void logout(String token) {
        // redisTemplate.delete(token);
    }

    @Override
    @Transactional
    public void addUser(User user) {
        //写入用户表
        this.baseMapper.insert(user);
        //写入用户角色表
        List<Integer> roleIdList = user.getRoleIdList();
        if(roleIdList !=null){
            for (Integer roleId : roleIdList) {
             userRoleMapper.insert(new UserRole(null,user.getId(),roleId));   
            }
        }
    }

    @Override
    @Transactional
    public User getUserById(Integer id) {
        User user = this.baseMapper.selectById(id);

        LambdaQueryWrapper<UserRole> wrapper =new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,id);
        List<UserRole> userRoleList = userRoleMapper.selectList(wrapper);
        List<Integer> roleIdList =userRoleList.stream()
                                              .map(userRole -> {return userRole.getRoleId();})
                                              .collect(Collectors.toList());
        user.setRoleIdList(roleIdList);
        return user;
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        // 更新用户表
        this.baseMapper.updateById(user);
        //清除角色表
        LambdaQueryWrapper<UserRole> wrapper =new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,user.getId());
        userRoleMapper.delete(wrapper);
        //添加
        List<Integer> roleIdList = user.getRoleIdList();
        if(roleIdList !=null){
            for (Integer roleId : roleIdList) {
             userRoleMapper.insert(new UserRole(null,user.getId(),roleId));   
            }
        }
    }

    @Override
    @Transactional
    public void DeleteUserById(Integer id) {
        // 清除用户
        this.baseMapper.deleteById(id);
        LambdaQueryWrapper<UserRole> wrapper =new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId,id);
        userRoleMapper.delete(wrapper);
    }

}
