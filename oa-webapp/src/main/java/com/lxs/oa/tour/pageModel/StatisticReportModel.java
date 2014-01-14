package com.lxs.oa.tour.pageModel;

/**报表时用到的model
 * @author liuman
 *
 */
public class StatisticReportModel {
	private String type_;
	private String name_;
	private Long dept_num_;
	private Long person_num_;
	private Double sum_money_;
	private Long operate_num;
	private Long dx_num_;
	private Double money_;
	private String unit_;
	public String getType_() {
		return type_;
	}
	public void setType_(String type_) {
		this.type_ = type_;
	}
	public String getName_() {
		return name_;
	}
	public void setName_(String name_) {
		this.name_ = name_;
	}
	public Long getDept_num_() {
		return dept_num_;
	}
	public void setDept_num_(Long dept_num_) {
		this.dept_num_ = dept_num_;
	}
	public Long getPerson_num_() {
		return person_num_;
	}
	public void setPerson_num_(Long person_num_) {
		this.person_num_ = person_num_;
	}
	public Double getSum_money_() {
		return sum_money_;
	}
	public void setSum_money_(Double sum_money_) {
		this.sum_money_ = sum_money_;
	}
	public Double getMoney_() {
		return money_;
	}
	public void setMoney_(Double money_) {
		this.money_ = money_;
	}
	public String getUnit_() {
		return unit_;
	}
	public void setUnit_(String unit_) {
		this.unit_ = unit_;
	}
	public Long getOperate_num() {
		return operate_num;
	}
	public void setOperate_num(Long operate_num) {
		this.operate_num = operate_num;
	}
	public Long getDx_num_() {
		return dx_num_;
	}
	public void setDx_num_(Long dx_num_) {
		this.dx_num_ = dx_num_;
	}
	
}
