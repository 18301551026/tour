package com.lxs.oa.tour.pageModel;

/**查看同比详情时页面的model
 * @author liuman
 *
 */
public class SameCompareDetailModel {
	private String name;
	private Long nowMoney;
	private Long lastMoney;
	private String time;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getNowMoney() {
		return nowMoney;
	}
	public void setNowMoney(Long nowMoney) {
		this.nowMoney = nowMoney;
	}
	public Long getLastMoney() {
		return lastMoney;
	}
	public void setLastMoney(Long lastMoney) {
		this.lastMoney = lastMoney;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
