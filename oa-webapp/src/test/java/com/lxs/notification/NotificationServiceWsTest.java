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

	public void testSendBroadcast() {
//		notificationService.sendBroadcast("aa", "bb", "http://www.163.com");
	}
	

	@Test
	public void testSendBroadcast2User() {
		notificationService.sendNotifcationToUsers("0365fb08c5ba45499e50750d9c8a0a95, ", "aa", "bb", "http://www.163.com");
	}	

}
