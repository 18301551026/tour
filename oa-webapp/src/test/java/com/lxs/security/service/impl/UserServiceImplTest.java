package com.lxs.security.service.impl;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.lxs.security.service.IUserServiceWs;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/applicationContext*.xml")
@Transactional
public class UserServiceImplTest {
	
	@Resource
	private IUserServiceWs userServiceWs; 
	

	public void testLoginWs() {
		String json = userServiceWs.loginWs("lxs", "1");
		System.out.println(json);
	}

}
