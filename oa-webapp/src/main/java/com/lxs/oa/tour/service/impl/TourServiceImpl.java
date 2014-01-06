package com.lxs.oa.tour.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.lxs.core.common.page.PageResult;
import com.lxs.core.dao.IBaseDao;
import com.lxs.oa.tour.common.StatusEnum;
import com.lxs.oa.tour.dao.ITourDao;
import com.lxs.oa.tour.domain.TourCommon;
import com.lxs.oa.tour.domain.TourDetail;
import com.lxs.oa.tour.pageModel.SameCompareChartModel;
import com.lxs.oa.tour.pageModel.SameCompareModel;
import com.lxs.oa.tour.pageModel.StatisticReportModel;
import com.lxs.oa.tour.service.ITourService;
import com.lxs.oa.tour.service.ITourServiceWs;
import com.lxs.security.domain.Dept;
import com.lxs.tour.domain.FactoryOption;

@Service
public class TourServiceImpl implements ITourService, ITourServiceWs {
	@Resource
	private ITourDao tourDao;
	
	@Resource
	private IBaseDao baseDao;

	@Override
	public PageResult findStatistic(DetachedCriteria criteria, Long userId,
			List<Dept> deptList) {
		return tourDao.findStatistic(criteria, userId, deptList);
	}

	public PageResult findSameCompare(List<Long> userIds, String startDate, String endDate,
			Integer currentMonth, Integer pageMonthNum) {
		return tourDao.findSameCompare(userIds, startDate,
				endDate, currentMonth, pageMonthNum);
	}

	public List<StatisticReportModel> getReportList(String startDate,
			String endDate, List<Dept> deptList) {
		return tourDao.getReportList(startDate, endDate, deptList);
	}

	public List<SameCompareChartModel> getCharts(DetachedCriteria nowCriteria,
			DetachedCriteria lastCriteria, String startDate, String endDate,
			Integer currentMonth, Integer pageMonthNum) {
		return tourDao.getCharts(nowCriteria, lastCriteria, startDate, endDate,
				currentMonth, pageMonthNum);
	}

	public String findWs(String userId, String status, String start,
			String pageSize) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TourCommon.class);
		if (null != userId && !"".equals(userId.trim())) {
			criteria.createAlias("user", "u");
			criteria.add(Restrictions.eq("u.id", Long.parseLong(userId)));
		}
		if (null != status && !"".equals(start.trim())) {
			criteria.add(Restrictions.eq("status", Integer.parseInt(status)));
		}
		PageResult page = baseDao.find(criteria, Integer.parseInt(start), Integer.parseInt(pageSize));
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(TourCommon.class, "id", "totalPersonNum", "totalIncome", "reportMonth", "reportYear");
		return JSON.toJSONString(page, filter);
	}
	
	public List<SameCompareModel> getQuarterSameCompareModels(
			List<Long> userIds, int startDate, int endDate,
			List<Integer> quarters) {
		return tourDao.getQuarterSameCompareModels(userIds, startDate, endDate, quarters);
	}
	public List<SameCompareChartModel> getQuarterCharts(List<Long> userIds,int startDate,int endDate,List<Integer> quarters){
		return tourDao.getQuarterCharts(userIds, startDate, endDate, quarters);
	}

	@Override
	public String findOptionWs(String deptId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FactoryOption.class);
		criteria.createAlias("type", "t");
		criteria.createAlias("t.depts", "d");
		criteria.add(Restrictions.eq("d.id", Long.parseLong(deptId)));
		List<Dept> list = baseDao.find(criteria);
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter("name");
		return JSON.toJSONString(list, filter);
	}

	@Override
	public String deleteTourWs(String id) {
		TourCommon tourCommon = baseDao.get(TourCommon.class, Long.parseLong(id));
		baseDao.delete(tourCommon);
		return "true";
	}

	@Override
	public String doConfirmTourWs(String id) {
		TourCommon tourCommon = baseDao.get(TourCommon.class, Long.parseLong(id));
		tourCommon.setStatus(StatusEnum.reported.getValue());
		baseDao.update(tourCommon);
		return "true";
	}

	@Override
	public String addTourWs(String json) {
		TourCommon c = JSON.parseObject(json, TourCommon.class);
		c.setStatus(StatusEnum.notReport.getValue());
		Double totalMoney = 0d;
		for (TourDetail d : c.getDetails()) {
			totalMoney = totalMoney + d.getMoney();
		}		
		c.setTotalIncome(totalMoney);
		//FIXME 添加之前的数据处理
		baseDao.add(c);
		
		for (TourDetail d : c.getDetails()) {
			d.setCommon(c);
			baseDao.add(d);
		}
		
		return "true";
	}

	@Override
	public String getTourWs(String id) {
		TourCommon c = baseDao.get(TourCommon.class, Long.parseLong(id));
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(
				"id", "totalPersonNum", "totalIncome", "reportMonth", 
				"reportYear", "details", "name", "money");
		return JSON.toJSONString(c, filter);		
	}
}
