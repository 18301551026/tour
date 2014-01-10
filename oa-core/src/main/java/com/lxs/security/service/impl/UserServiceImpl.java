package com.lxs.security.service.impl;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.lxs.core.cache.ClearCache;
import com.lxs.core.dao.IBaseDao;
import com.lxs.notification.domain.ApnUser;
import com.lxs.security.dao.IUserDao;
import com.lxs.security.domain.Job;
import com.lxs.security.domain.Role;
import com.lxs.security.domain.User;
import com.lxs.security.service.IUserService;
import com.lxs.security.service.IUserServiceWs;

@Service
public class UserServiceImpl implements IUserService, IUserServiceWs {
	
	@Resource
	private IUserDao userDao;
	@Resource
	private IBaseDao baseDao;
	
	@Override
	public User login(String userName, String password) {
		return userDao.login(userName, password);
	}
	
	@Override
	public boolean validateAuth(Long userId, String path) {
		return userDao.validateAuth(userId, path);
	}

	@Override
	@ClearCache
	public void addRole(Long roleId, Long userId) {
		User user = baseDao.get(User.class, userId);
		Role role = baseDao.get(Role.class, roleId);
		
		role.getUsers().add(user);
		baseDao.save(role);		
	}

	@Override
	@ClearCache
	public void deleteRole(Long roleId, Long userId) {
		User user = baseDao.get(User.class, userId);
		Role role = baseDao.get(Role.class, roleId);
		
		role.getUsers().remove(user);
		baseDao.save(role);		
	}

	@Override
	public void addJob(Long jobId, Long userId) {
		User user = baseDao.get(User.class, userId);
		Job job = baseDao.get(Job.class, jobId);
		user.getJobs().add(job);
		baseDao.save(user);		
	}

	@Override
	public void deleteJob(Long jobId, Long userId) {
		User user = baseDao.get(User.class, userId);
		Job job = baseDao.get(Job.class, jobId);
		user.getJobs().remove(job);
		baseDao.save(user);		
	}

	@Override
	public String doLoginWs(String xmppUserName, String userName, String password) {
		updateXmppUser(xmppUserName, userName);
		
		User user = userDao.login(userName, password);
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter("id", "dept", "deptType", "deptLevel", "realName");
		return JSON.toJSONString(user, filter);
	}
	
	/**
	 * 修改app_user 表，关联xmpp_user和系统user
	 * @param xmppUserName
	 * @param userName
	 */
	private void updateXmppUser(String xmppUserName, String userName) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ApnUser.class);
		criteria.add(Restrictions.eq("username", xmppUserName));
		ApnUser apnUser = baseDao.uniqueResult(criteria);
		
		if (apnUser != null) {
			criteria = DetachedCriteria.forClass(User.class);
			criteria.add(Restrictions.eq("userName", userName));
			User user = baseDao.uniqueResult(criteria);
			
			apnUser.setUser(user);
			baseDao.save(apnUser);
		}
	}


}
