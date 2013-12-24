package com.lxs.oa.tour.pageModel;

/**
 * 同比的时候页面用到的model
 * 
 * @author liuman
 * 
 */
public class SameCompareModel {
	private Integer year;
	private Integer month;
	private Long nowTotalCount;// 本年相应月份总个数
	private Long lastTotalCount;// 去年相应月份总个数
	private Long nowTotalPersonNum;// 本年相应月份接待总人数
	private Long lastTotalPersonNum;// 去年相应月份接待总人数
	private Long nowTotalIncome;// 本年相应月份总收入
	private Long lastTotalIncome;// 去年相应月份总收入
	private String type;// 类型
	
	private String nowIds;
	private String lastIds;

	public Long getNowTotalCount() {
		return nowTotalCount;
	}

	public void setNowTotalCount(Long nowTotalCount) {
		this.nowTotalCount = nowTotalCount;
	}

	public Long getLastTotalCount() {
		return lastTotalCount;
	}

	public void setLastTotalCount(Long lastTotalCount) {
		this.lastTotalCount = lastTotalCount;
	}

	public Long getNowTotalPersonNum() {
		return nowTotalPersonNum;
	}

	public void setNowTotalPersonNum(Long nowTotalPersonNum) {
		this.nowTotalPersonNum = nowTotalPersonNum;
	}

	public Long getLastTotalPersonNum() {
		return lastTotalPersonNum;
	}

	public void setLastTotalPersonNum(Long lastTotalPersonNum) {
		this.lastTotalPersonNum = lastTotalPersonNum;
	}

	public Long getNowTotalIncome() {
		return nowTotalIncome;
	}

	public void setNowTotalIncome(Long nowTotalIncome) {
		this.nowTotalIncome = nowTotalIncome;
	}

	public Long getLastTotalIncome() {
		return lastTotalIncome;
	}

	public void setLastTotalIncome(Long lastTotalIncome) {
		this.lastTotalIncome = lastTotalIncome;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public String getNowIds() {
		return nowIds;
	}

	public void setNowIds(String nowIds) {
		this.nowIds = nowIds;
	}

	public String getLastIds() {
		return lastIds;
	}

	public void setLastIds(String lastIds) {
		this.lastIds = lastIds;
	}

}
