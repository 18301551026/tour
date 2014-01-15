package com.lxs.security.dao;

import java.util.List;

import com.lxs.security.domain.Dept;

public interface IDeptDao {
	public List<Dept> findAllDept(Long pid);
}
