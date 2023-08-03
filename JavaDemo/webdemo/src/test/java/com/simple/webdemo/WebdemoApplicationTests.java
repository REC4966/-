package com.simple.webdemo;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.simple.webdemo.sys.mapper.UserMapper;

import java.util.List;

@SpringBootTest
class WebdemoApplicationTests {
	@Resource
	private UserMapper userMapper;

	@Test
	void Test() {
		 List<String> stringList = userMapper.selectRole(1);
		 stringList.forEach(System.out::println);

	}

}
