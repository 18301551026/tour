package com.lxs.security.service.impl;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.lxs.security.domain.User;
import com.lxs.security.service.IUserServiceWs;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/spring/applicationContext.xml", "/spring/applicationContext_activiti.xml"})
@Transactional
public class UserServiceImplTest {
	
	@Resource
	private IUserServiceWs userServiceWs; 
	
	@Test
	public void testLoginWs() {
		String json = userServiceWs.doLoginWs("6d39ff1d7d4a480db9d623e8394227b3", "lxs", "1");
		System.out.println(json);
		
//		String json = "{\"dept\":{\"deptType\":\"工业旅游\",\"id\":3},\"id\":2,\"realName\":\"刘雪松\"}";
//		User u = JSON.parseObject(json, User.class);
//		System.out.println(u.getRealName());
//		System.out.println(u.getDept().getDeptType());
	}
	

}
