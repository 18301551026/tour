package com.lxs.security.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.lxs.security.dao.IDeptDao;
import com.lxs.security.domain.Dept;

@Repository
public class DeptDaoImpl implements IDeptDao {
	@Resource
	private SessionFactory sessionFactory;

	public List<Dept> findAllDept(Long pid) {
		List<Dept> list = null;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Dept.class);
		
		if (pid!=0l) {
//			criteria.createAlias("parent", "pd");
//			criteria.add(Restrictions.eq("pd.id",pid));
			criteria.add(Restrictions.eq("id",pid));
		}else{
			criteria.add(Restrictions.isNull("parent"));
		}
		criteria.setCacheable(true);
		list = criteria.list();
		return list;
	}
}
