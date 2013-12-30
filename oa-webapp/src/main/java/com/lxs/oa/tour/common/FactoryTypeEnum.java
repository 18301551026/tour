package com.lxs.oa.tour.common;

public enum FactoryTypeEnum {
	visit("观光园"), // 观光园
	custom("民俗旅游"),// 民俗
	jingqu("旅游风景"), // 景区
	live("旅游住宿"), // 住宿
	factory("工业旅游"); // 工业
	
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private FactoryTypeEnum(String value) {
		this.value = value;
	}

}
