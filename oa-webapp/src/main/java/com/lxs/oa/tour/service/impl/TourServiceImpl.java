package com.lxs.oa.tour.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.lxs.core.common.TimeUtil;
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

	public List<SameCompareModel> findSameCompare(List<Long> userIds, String startDate, String endDate) {
		return tourDao.findSameCompare(userIds, startDate,
				endDate);
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
		
		if (c.getReportMonth() >=1 && c.getReportMonth() <=3) {
			c.setQuarter(1);
		} 
		if (c.getReportMonth() >=4 && c.getReportMonth() <=6) {
			c.setQuarter(2);
		} 
		if (c.getReportMonth() >=7 && c.getReportMonth() <=9) {
			c.setQuarter(3);
		}
		if (c.getReportMonth() >=10 && c.getReportMonth() <=12) {
			c.setQuarter(4);
		}
		
		String yearMonthDate = c.getReportYear() + "年" + c.getReportMonth() + "月";
		c.setTime(TimeUtil.getTimeInMillis(yearMonthDate));
		
		if (existSameTimeTour(c.getReportYear(), c.getReportMonth(), c.getUser().getId())) {
			return "false";
		}
		
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

	private boolean existSameTimeTour(int year, int month, Long userId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TourCommon.class);
		criteria.createAlias("user", "u");
		criteria.add(Restrictions.eq("u.id", userId));
		criteria.add(Restrictions.eq("reportMonth", month));
		criteria.add(Restrictions.eq("reportYear", year));
		List<TourCommon> list = baseDao.find(criteria);
		if (null != list && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
}
