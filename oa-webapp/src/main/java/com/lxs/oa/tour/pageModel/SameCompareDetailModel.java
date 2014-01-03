package com.lxs.oa.tour.pageModel;

/**查看同比详情时页面的model
 * @author liuman
 *
 */
public class SameCompareDetailModel {
	private String name;
	private Double nowMoney;
	private Double lastMoney;
	private String time;
	private Double percent;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getNowMoney() {
		return nowMoney;
	}
	public void setNowMoney(Double nowMoney) {
		this.nowMoney = nowMoney;
	}
	public Double getLastMoney() {
		return lastMoney;
	}
	public void setLastMoney(Double lastMoney) {
		this.lastMoney = lastMoney;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Double getPercent() {
		return percent;
	}
	public void setPercent(Double percent) {
		this.percent = percent;
	}
}
