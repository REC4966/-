package com.simple.webdemo.sys.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simple.webdemo.common.Result;
import com.simple.webdemo.sys.entity.Menu;
import com.simple.webdemo.sys.service.IMenuService;

import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author laocai
 * @since 2023-07-12
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private IMenuService menuService;
    @ApiOperation("查询所有")
    @GetMapping
    public Result<List<Menu>> getMenu(){
       List<Menu> menuList= menuService.getAllMenu();
        return Result.success(menuList);
    }
}
