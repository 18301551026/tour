package com.lxs.oa.tour.service.impl;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Service;

import com.lxs.core.common.page.PageResult;
import com.lxs.oa.tour.dao.ITourDao;
import com.lxs.oa.tour.service.ITourService;

@Service
public class TourServiceImpl implements ITourService {
	@Resource
	private ITourDao tourDao;

	@Override
	public PageResult findStatistic(DetachedCriteria criteria, Long userId) {
		return tourDao.findStatistic(criteria, userId);
	}

	public PageResult findSameCompare(DetachedCriteria nowCriteria,
			DetachedCriteria lastCriteria, String startDate, String endDate,
			Integer currentMonth, Integer pageMonthNum) {
		return tourDao.findSameCompare(nowCriteria, lastCriteria, startDate,
				endDate, currentMonth, pageMonthNum);
	}
}
