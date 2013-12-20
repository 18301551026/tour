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
	public PageResult findStatistic(DetachedCriteria criteria, int start,
			int pageSize) {
		return tourDao.findStatistic(criteria, start, pageSize);
	}

}
