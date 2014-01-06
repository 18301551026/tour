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
	
	/**
	 * 查询部门的输入项
	 * @param deptId
	 * @return
	 */
	public String findOptionWs(@WebParam(name = "deptId") String deptId);
	
	/**
	 * 删除营业数据
	 * @param id
	 * @return
	 */
	public String deleteTourWs(@WebParam(name = "id") String id);
	
	/**
	 * 确认营业数据
	 * @param id
	 * @return
	 */
	public String doConfirmTourWs(@WebParam(name = "id") String id);
	
	/**
	 * 添加营业数据
	 * @param json
	 * @return
	 */
	public String addTourWs(@WebParam(name = "json") String json);
	
	/**
	 * 查看营业数据
	 * @param json
	 * @return
	 */
	public String getTourWs(@WebParam(name = "id") String id);
	
	
}
