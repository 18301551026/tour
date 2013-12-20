package com.lxs.oa.tour.pageModel;

public class TownStatistic {
	private String factoryType;// 企业类型
	private Long totalFactoryCount;// 个数
	private Long totalPersonCount; // 接待总人数
	private Long totalIncome; // 总收入

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

	public Long getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(Long totalIncome) {
		this.totalIncome = totalIncome;
	}

	public String getFactoryType() {
		return factoryType;
	}

	public void setFactoryType(String factoryType) {
		this.factoryType = factoryType;
	}

	
}
