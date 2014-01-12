package com.lxs.oa.tour.common;

public enum DeptLevelEnum {
	
	company("企业"), town("镇级"), district("区级");
	
	private String value;
	
	private DeptLevelEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
