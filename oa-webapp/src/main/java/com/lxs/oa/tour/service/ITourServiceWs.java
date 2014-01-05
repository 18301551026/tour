package com.lxs.oa.tour.service;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://www.lxs.com")
public interface ITourServiceWs {
	
	/**
	 * 分页查询指定状态的用户申报
	 * @param userId
	 * @param status
	 * @param start
	 * @param pageSize
	 * @return
	 */
	public String findWs(@WebParam(name = "userId") String userId
			, @WebParam(name = "status") String status
			, @WebParam(name = "start") String start
			, @WebParam(name = "pageSize") String pageSize);

}
