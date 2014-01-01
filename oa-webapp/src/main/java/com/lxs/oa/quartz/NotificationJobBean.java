package com.lxs.oa.quartz;

import java.util.Date;

import com.lxs.notification.service.INotificationServiceWs;

public class NotificationJobBean {
	
	private INotificationServiceWs notificationService;

	public void work() {
		System.out.println(new Date() + "\t发送通知");
		notificationService.sendBroadcast("大兴旅游委", "请及时申报，上个月的营业数据谢谢", "http://www.163.com");
	}

	public void setNotificationService(INotificationServiceWs notificationService) {
		this.notificationService = notificationService;
	}

}
