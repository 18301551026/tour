package com.lxs.oa.tour.service.impl;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.lxs.core.common.page.PageResult;
import com.lxs.oa.tour.service.ITourServiceWs;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/spring/applicationContext.xml", "/spring/applicationContext_activiti.xml"})
@Transactional
public class TourServiceImplTest {
	
	@Resource
	private ITourServiceWs tourServiceWs;

	@Test
	public void testFind() {
//		String json = tourServiceWs.findWs("2", "2", "0", "10");
//		System.out.println(json);
		String json = "{'result':[{'id':5,'reportMonth':11,'reportYear':2013,'totalIncome':90.5,'totalPersonNum':55},{'id':6,'reportMonth':11,'reportYear':2013,'totalIncome':268,'totalPersonNum':45},{'id':7,'reportMonth':11,'reportYear':2013,'totalIncome':135,'totalPersonNum':23},{'id':8,'reportMonth':10,'reportYear':2013,'totalIncome':73,'totalPersonNum':23},{'id':9,'reportMonth':12,'reportYear':2012,'totalIncome':135,'totalPersonNum':234},{'id':10,'reportMonth':11,'reportYear':2011,'totalIncome':1256.6,'totalPersonNum':34},{'id':11,'reportMonth':11,'reportYear':2012,'totalIncome':36.4,'totalPersonNum':234},{'id':12,'reportMonth':11,'reportYear':2012,'totalIncome':6.8,'totalPersonNum':234},{'id':13,'reportMonth':10,'reportYear':2013,'totalIncome':90,'totalPersonNum':23},{'id':14,'reportMonth':10,'reportYear':2013,'totalIncome':168,'totalPersonNum':34}],'rowCount':23}";
		PageResult page = JSON.parseObject(json, PageResult.class);
	}

}
