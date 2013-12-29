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
	private Long nowTotalPersonNum;// 本年相应月份接待总人数
	private Long lastTotalPersonNum;// 去年相应月份接待总人数
	private Double nowTotalIncome;// 本年相应月份总收入
	private Double lastTotalIncome;// 去年相应月份总收入
	private String type;// 类型

	private String nowIds;
	private String lastIds;
	public SameCompareModel() {
	}
	
	public SameCompareModel(Long nowTotalPersonNum, Long lastTotalPersonNum,
			Double nowTotalIncome, Double lastTotalIncome, String type) {
		super();
		this.nowTotalPersonNum = nowTotalPersonNum;
		this.lastTotalPersonNum = lastTotalPersonNum;
		this.nowTotalIncome = nowTotalIncome;
		this.lastTotalIncome = lastTotalIncome;
		this.type = type;
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

	public Double getNowTotalIncome() {
		return nowTotalIncome;
	}

	public void setNowTotalIncome(Double nowTotalIncome) {
		this.nowTotalIncome = nowTotalIncome;
	}

	public Double getLastTotalIncome() {
		return lastTotalIncome;
	}

	public void setLastTotalIncome(Double lastTotalIncome) {
		this.lastTotalIncome = lastTotalIncome;
	}

	
}
