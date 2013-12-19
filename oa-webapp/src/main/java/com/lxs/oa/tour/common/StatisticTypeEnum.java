package com.lxs.oa.tour.common;

public enum StatisticTypeEnum {
	factory(1), // 企业
	town(2), // 镇
	district(3);// 区
	private Integer value;

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	private StatisticTypeEnum(Integer value) {
		this.value = value;
	}

}
