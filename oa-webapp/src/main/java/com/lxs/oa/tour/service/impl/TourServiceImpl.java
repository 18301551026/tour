package com.lxs.oa.tour.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Service;

import com.lxs.core.common.page.PageResult;
import com.lxs.oa.tour.dao.ITourDao;
import com.lxs.oa.tour.pageModel.SameCompareChartModel;
import com.lxs.oa.tour.pageModel.SameCompareModel;
import com.lxs.oa.tour.pageModel.StatisticReportModel;
import com.lxs.oa.tour.service.ITourService;
import com.lxs.security.domain.Dept;

@Service
public class TourServiceImpl implements ITourService {
	@Resource
	private ITourDao tourDao;

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
	public List<SameCompareChartModel> getCharts(DetachedCriteria nowCriteria,DetachedCriteria lastCriteria,String startDate,String endDate,Integer currentMonth,Integer pageMonthNum){
		return tourDao.getCharts(nowCriteria, lastCriteria, startDate, endDate, currentMonth, pageMonthNum);
	}

	@Override
	public List<SameCompareModel> getQuarterSameCompareModels(
			List<Long> userIds, int startDate, int endDate,
			List<Integer> quarters) {
		return tourDao.getQuarterSameCompareModels(userIds, startDate, endDate, quarters);
	}
	public List<SameCompareChartModel> getQuarterCharts(List<Long> userIds,int startDate,int endDate,List<Integer> quarters){
		return tourDao.getQuarterCharts(userIds, startDate, endDate, quarters);
	}
}
