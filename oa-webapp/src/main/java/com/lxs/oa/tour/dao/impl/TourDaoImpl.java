package com.lxs.oa.tour.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Repository;

import com.lxs.core.common.page.PageResult;
import com.lxs.oa.tour.dao.ITourDao;
import com.lxs.oa.tour.domain.TourCommon;
import com.lxs.oa.tour.domain.TourDetail;
import com.lxs.oa.tour.pageModel.TownStatistic;
import com.lxs.security.domain.Job;

@Repository
public class TourDaoImpl implements ITourDao {
	@Resource
	private SessionFactory sessionFactory;

	@Override
	public PageResult findStatistic(DetachedCriteria criteria, int start,
			int pageSize) {
		PageResult page = new PageResult();
		Criteria c = criteria.getExecutableCriteria(sessionFactory
				.getCurrentSession());

		// 总行数
		long rowCount = (Long) c.setProjection(Projections.rowCount())
				.uniqueResult();
		page.setRowCount(rowCount);

		// 一页数据
		c.setProjection(null);
		c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		List<TourCommon> list = c.setFirstResult(start).setMaxResults(pageSize)
				.list();
		List<TownStatistic> tempList = new ArrayList<TownStatistic>();

		List<Job> jobs = sessionFactory.getCurrentSession()
				.createQuery("from Job").list();
		for (Job job : jobs) {
			TownStatistic ts = new TownStatistic();
			ts.setFactoryType(job.getJobName());
			Long tempTotalFactoryCount = 0l;
			Long tempTotalPersonCount = 0l;
			Long tempTotalIncome = 0l;
			for (TourCommon tourCommon : list) {
				if (tourCommon.getJob().getId().equals(job.getId())) {
					List<TourDetail> details = tourCommon.getDetails();
					for (TourDetail tourDetail : details) {
						tempTotalIncome += tourDetail.getMoney();
					}
					tempTotalFactoryCount += 1;
					tempTotalPersonCount += tourCommon.getTotalPersonNum();
				}
			}
			ts.setTotalFactoryCount(tempTotalFactoryCount);
			ts.setTotalPersonCount(tempTotalPersonCount);
			ts.setTotalIncome(tempTotalIncome);

			tempList.add(ts);
		}

		page.setResult(tempList);

		return page;
	}
	
}
