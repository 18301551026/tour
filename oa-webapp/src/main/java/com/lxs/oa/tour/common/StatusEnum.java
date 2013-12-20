package com.lxs.oa.tour.common;

public enum StatusEnum {
	reported(1), // 已申报
	notReport(2),// 未申报
	townList(3),	//镇列表
	townStatistic(4),//镇统计
	districtList(5),//区列表
	districtStatistic(6);//区统计
	private Integer value;

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	private StatusEnum(Integer value) {
		this.value = value;
	}

}
