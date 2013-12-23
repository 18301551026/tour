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
import com.lxs.oa.tour.common.FactoryTypeEnum;
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
	public PageResult findStatistic(DetachedCriteria criteria, Long userId) {
		PageResult page = new PageResult();
		Criteria c = criteria.getExecutableCriteria(sessionFactory
				.getCurrentSession());

		// 总行数
		long rowCount = (Long) c.setProjection(Projections.rowCount())
				.uniqueResult();
		page.setRowCount(rowCount);

		c.setProjection(null);
		c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		List<TourCommon> list = c.list();// 所有数据
		List<TownStatistic> tempList = new ArrayList<TownStatistic>();
		for (FactoryTypeEnum typeEnum : FactoryTypeEnum.values()) {
			String str = typeEnum.getValue();
			Long tempTotalIncome = 0l;
			Long tempTotalPersonNum = 0l;
			Long tempNum = 0l;
			String tourIds = "";
			for (TourCommon common : list) {
				if (str.equals(common.getType())) {
					tempTotalIncome += common.getTotalIncome();
					tempTotalPersonNum += common.getTotalPersonNum();
					tempNum += 1;
					tourIds += common.getId() + ",";
				}
			}
			TownStatistic town = new TownStatistic();
			town.setFactoryType(str);
			if (tourIds.trim().length() != 0) {
				town.setTourIds(tourIds.substring(0, tourIds.length() - 1));
			}
			town.setTotalPersonCount(tempTotalPersonNum);
			town.setTotalIncome(tempTotalIncome);
			town.setTotalFactoryCount(tempNum);
			tempList.add(town);
		}

		page.setResult(tempList);

		return page;
	}

	public static void main(String[] args) {
		// for (FactoryTypeEnum tempType : FactoryTypeEnum.values()) {
		// tempType.getValue();
		// }
		String ids = "2,3,";
		System.out.println(ids.substring(0, ids.length() - 1));
	}

}
