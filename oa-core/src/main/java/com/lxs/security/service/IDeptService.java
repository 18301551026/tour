package com.lxs.security.service;

import java.util.List;

import com.lxs.security.domain.Dept;

public interface IDeptService {
	public List<Dept> findAllDept(Long pid);
}
