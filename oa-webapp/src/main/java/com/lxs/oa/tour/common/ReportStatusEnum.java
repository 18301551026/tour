package com.lxs.oa.tour.common;

public enum ReportStatusEnum {
	reported(1), // 已申报
	notReport(2);// 未申报
	private Integer value;

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	private ReportStatusEnum(Integer value) {
		this.value = value;
	}

}
