package com.lxs.oa.tour.service;

import org.hibernate.criterion.DetachedCriteria;

import com.lxs.core.common.page.PageResult;

public interface ITourService {
	public PageResult findStatistic(DetachedCriteria criteria,Long userId);
	

}
