package com.lxs.oa.tour.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Service;

import com.lxs.core.common.page.PageResult;
import com.lxs.oa.tour.dao.ITourDao;
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

	public PageResult findSameCompare(DetachedCriteria nowCriteria,
			DetachedCriteria lastCriteria, String startDate, String endDate,
			Integer currentMonth, Integer pageMonthNum) {
		return tourDao.findSameCompare(nowCriteria, lastCriteria, startDate,
				endDate, currentMonth, pageMonthNum);
	}
}
