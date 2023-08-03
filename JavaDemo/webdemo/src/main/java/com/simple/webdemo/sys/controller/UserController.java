package com.simple.webdemo.sys.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.simple.webdemo.sys.service.IUserService;

import freemarker.template.utility.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.util.StringUtils;
import com.simple.webdemo.common.Result;
import com.simple.webdemo.sys.entity.User;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author laocai
 * @since 2023-07-12
 */
@Api(tags = {"用户接口列表"})
@RestController
@RequestMapping("/user")
// @CrossOrigin
public class UserController {
    @Autowired
    private  IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
   

    @GetMapping("/all")
    public Result<List<User>> UserAll() {
        return Result.success(userService.list());
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<Map<String,Object>> login(@RequestBody User user) {
       Map<String,Object> data = userService.login(user);
       if(data != null)
       {
              return Result.success(data);
       }
        return Result.fail(20002,"用户名或密码错误");
    }

    @GetMapping("/info")
    public Result<?> info(@RequestParam("token") String token) {
       Map<String,Object> data = userService.getUserInfo(token);
        if(data != null)
        {
            return Result.success(data);
        }
        return Result.fail(20003,"用户登录信息失效从新登录");
    }

    @PostMapping("/logout")
    public Result<?> logout(@RequestHeader("X-Token") String token) {
        userService.logout(token);
        return Result.success();
    }
    @GetMapping("/list")
    public Result<?> getUserList(@RequestParam(value = "username",required  = false)String username,
                                @RequestParam(value = "phone",required=false)String phone,
                                @RequestParam(value = "pageNo")Long pageNo,
                                @RequestParam(value = "pageSize")Long pageSize){
    LambdaQueryWrapper<User> wrapper =new LambdaQueryWrapper<>();
    wrapper.eq(StringUtils.hasLength(username),User::getUsername,username);
    wrapper.eq(StringUtils.hasLength(phone),User::getPhone,phone);
    wrapper.orderByDesc(User::getId);
    Page<User>page  = new Page<>(pageNo,pageSize);
    userService.page(page, wrapper);
    Map<String,Object> data =new HashMap<>();
    data.put("total", page.getTotal());
    data.put("rows", page.getRecords());
    return Result.success(data);
    }

    @PostMapping
    public Result<?> addUser(@RequestBody User user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.addUser(user);
        return Result.success("增加用户成功");
    }
    @PutMapping
    public Result<?> updateUser(@RequestBody User user)
    {
        user.setPassword(null);
        userService.updateUser(user);
        return Result.success("修改用户成功");
    }
    @GetMapping("/{id}")
    public Result<?> getUserById(@PathVariable("id") Integer id){
        User user = userService.getUserById(id);
        return Result.success(user);
    }

    @DeleteMapping("/{id}")
     public Result<?> DeleteUserById(@PathVariable("id") Integer id){
     userService.DeleteUserById(id);
     return Result.success("删除用户成功");
    }
}
