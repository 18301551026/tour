package com.lxs.oa.tour.pageModel;

/**
 * 统计是页面用到的model
 * 
 * @author liuman
 * 
 */
public class StatisticModel {
	private String factoryType;// 企业类型
	private Long totalFactoryCount;// 个数
	private Long totalPersonCount; // 接待总人数
	private Double totalIncome; // 总收入
	private String tourIds;

	public Long getTotalFactoryCount() {
		return totalFactoryCount;
	}

	public void setTotalFactoryCount(Long totalFactoryCount) {
		this.totalFactoryCount = totalFactoryCount;
	}

	public Long getTotalPersonCount() {
		return totalPersonCount;
	}

	public void setTotalPersonCount(Long totalPersonCount) {
		this.totalPersonCount = totalPersonCount;
	}

	public Double getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(Double totalIncome) {
		this.totalIncome = totalIncome;
	}

	public String getFactoryType() {
		return factoryType;
	}

	public void setFactoryType(String factoryType) {
		this.factoryType = factoryType;
	}

	public String getTourIds() {
		return tourIds;
	}

	public void setTourIds(String tourIds) {
		this.tourIds = tourIds;
	}

}
