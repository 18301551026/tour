package com.lxs.oa.tour.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.lxs.security.domain.Job;
import com.lxs.security.domain.User;

@Entity
@Table(name = "tour_common_")
@JSONType(ignores = "hibernateLazyInitializer")
public class TourCommon implements Serializable {
	private Long id;
	private Integer totalPersonNum;// 接待人次
	private Double totalIncome; // 总收入
	private Date reportDate;// 申报时间
	private Integer status;// 申报状态
	private Integer reportMonth;// 申报月份
	private Integer reportYear; // 申报年份
	private Long time;
	private String desc; // 描述
	private User user;
	private String type;

	@Column(name = "type_")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private List<TourDetail> details = new ArrayList<TourDetail>();// 详情

	@Id
	@GeneratedValue
	@Column(name = "id_")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "total_person_num_")
	public Integer getTotalPersonNum() {
		return totalPersonNum;
	}

	public void setTotalPersonNum(Integer totalPersonNum) {
		this.totalPersonNum = totalPersonNum;
	}

	@Column(name = "total_income_")
	public Double getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(Double totalIncome) {
		this.totalIncome = totalIncome;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "report_date_")
	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	@Column(name = "status_")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "report_month_")
	public Integer getReportMonth() {
		return reportMonth;
	}

	public void setReportMonth(Integer reportMonth) {
		this.reportMonth = reportMonth;
	}

	@Column(name = "report_year_")
	public Integer getReportYear() {
		return reportYear;
	}

	public void setReportYear(Integer reportYear) {
		this.reportYear = reportYear;
	}

	@Lob
	@Column(name = "desc_")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id_")
	@JSONField(serialize = false)
	public User getUser() {
		return user;
	}

	@JSONField(deserialize = false)
	public void setUser(User user) {
		this.user = user;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "common")
	public List<TourDetail> getDetails() {
		return details;
	}

	public void setDetails(List<TourDetail> details) {
		this.details = details;
	}

	private Integer statisticType;

	@Transient
	public Integer getStatisticType() {
		return statisticType;
	}

	public void setStatisticType(Integer statisticType) {
		this.statisticType = statisticType;
	}

	@Column(name = "long_time_")
	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

}
