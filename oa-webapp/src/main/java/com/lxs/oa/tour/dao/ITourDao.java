package com.lxs.oa.tour.dao;

import org.hibernate.criterion.DetachedCriteria;

import com.lxs.core.common.page.PageResult;

public interface ITourDao  {
	public PageResult findStatistic(DetachedCriteria criteria, int start, int pageSize);
	

}
