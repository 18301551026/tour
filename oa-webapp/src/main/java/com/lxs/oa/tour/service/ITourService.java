package com.lxs.oa.tour.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.lxs.core.common.page.PageResult;
import com.lxs.oa.tour.pageModel.SameCompareChartModel;
import com.lxs.oa.tour.pageModel.StatisticReportModel;
import com.lxs.security.domain.Dept;

public interface ITourService {
	public PageResult findStatistic(DetachedCriteria criteria, Long userId,List<Dept> deptLis);

	public PageResult findSameCompare(DetachedCriteria nowCriteria,
			DetachedCriteria lastCriteria, String startDate, String endDate,
			Integer currentMonth, Integer pageMonthNum);
	public List<StatisticReportModel> getReportList(String startDate,String endDate,List<Dept> deptList);
	
	public List<SameCompareChartModel> getCharts(DetachedCriteria nowCriteria,DetachedCriteria lastCriteria,String startDate,String endDate,Integer currentMonth,Integer pageMonthNum);
}
