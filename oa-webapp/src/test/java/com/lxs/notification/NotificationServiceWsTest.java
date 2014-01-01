package com.lxs.notification;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.lxs.notification.service.INotificationServiceWs;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/spring/applicationContext.xml", "/spring/applicationContext_activiti.xml", "/spring/applicationContext_cxf.xml"})
@Transactional
public class NotificationServiceWsTest {
	
	@Resource
	private INotificationServiceWs notificationService;

	@Test
	public void testSendBroadcast() {
//		notificationService.sendBroadcast("aa", "bb", "http://www.163.com");
	}
	
	
	public void testSendBroadcast2User() {
//		notificationService.sendNotifcationToUsers("6d39ff1d7d4a480db9d623e8394227b3, aaaa", "aa", "bb", "http://www.163.com");
	}	

}
