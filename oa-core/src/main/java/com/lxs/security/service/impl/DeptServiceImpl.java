package com.lxs.security.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lxs.security.dao.IDeptDao;
import com.lxs.security.domain.Dept;
import com.lxs.security.service.IDeptService;

@Service
public class DeptServiceImpl implements IDeptService {
	@Resource
	private IDeptDao deptDao;

	public List<Dept> findAllDept(Long pid) {
		return deptDao.findAllDept(pid);
	}
}
