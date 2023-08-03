package com.simple.webdemo.sys.mapper;

import com.simple.webdemo.sys.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author laocai
 * @since 2023-07-12
 */
public interface UserMapper extends BaseMapper<User> {
   public List<String> selectRole(Integer id);

}
