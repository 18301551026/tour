package com.lxs.oa.tour.common;

public enum ReportStatus {
	reported(1), // 已申报
	notReport(2);// 未申报
	private Integer value;

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	private ReportStatus(Integer value) {
		this.value = value;
	}

}
