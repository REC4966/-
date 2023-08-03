package com.simple.webdemo.sys.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simple.webdemo.common.Result;
import com.simple.webdemo.sys.entity.Role;
import com.simple.webdemo.sys.service.IRoleService;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author laocai
 * @since 2023-07-12
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;
    @GetMapping("/list")
    public Result<?> getRoleList(@RequestParam(value = "roleName",required  = false)String roleName,
                                @RequestParam(value = "pageNo")Long pageNo,
                                @RequestParam(value = "pageSize")Long pageSize){
    LambdaQueryWrapper<Role> wrapper =new LambdaQueryWrapper<>();
    wrapper.eq(StringUtils.hasLength(roleName),Role::getRoleName,roleName);
    wrapper.orderByDesc(Role::getRoleId);
    Page<Role>page  = new Page<>(pageNo,pageSize);
    roleService.page(page, wrapper);
    Map<String,Object> data =new HashMap<>();
    data.put("total", page.getTotal());
    data.put("rows", page.getRecords());
    return Result.success(data);
    }

    @PostMapping
    public Result<?> addRole(@RequestBody Role role)
    {
        roleService.addRole(role);
        return Result.success("增加角色成功");
    }
    @PutMapping
    public Result<?> updateRole(@RequestBody Role role)
    {
        roleService.updateRole(role);
        return Result.success("修改角色成功");
    }
    @GetMapping("/{id}")
    public Result<?> getRoleById(@PathVariable("id") Integer id){
        Role role = roleService.getRoleById(id);
        return Result.success(role);
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteRoleById(@PathVariable("id") Integer id){
         roleService.deleteRoleById(id);
        return Result.success("删除角色成功");
    }
    @GetMapping("/all")
    public Result<List<Role>> getAllRole(){
        List<Role> roleList = roleService.list();
        return Result.success(roleList);
 }
}