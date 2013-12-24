package com.lxs.oa.tour.service;

import org.hibernate.criterion.DetachedCriteria;

import com.lxs.core.common.page.PageResult;

public interface ITourService {
	public PageResult findStatistic(DetachedCriteria criteria, Long userId);

	public PageResult findSameCompare(DetachedCriteria nowCriteria,
			DetachedCriteria lastCriteria, String startDate, String endDate,
			Integer currentMonth, Integer pageMonthNum);
}
