package com.lxs.oa.tour.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.lxs.core.action.BaseAction;
import com.lxs.core.common.SystemConstant;
import com.lxs.core.common.page.PageResult;
import com.lxs.oa.tour.common.StatusEnum;
import com.lxs.oa.tour.common.StatisticTypeEnum;
import com.lxs.oa.tour.domain.TourCommon;
import com.lxs.oa.tour.domain.TourDetail;
import com.lxs.oa.tour.service.ITourService;
import com.lxs.security.domain.Dept;
import com.lxs.security.domain.Job;
import com.lxs.security.domain.User;
import com.opensymphony.xwork2.ActionContext;

@Controller
@Scope("prototype")
@Namespace("/tour")
@Actions({
		@Action(value = "noReported", className = "tourAction", results = {
				@Result(name = "add", location = "/WEB-INF/jsp/tour/factory/add.jsp"),
				@Result(name = "update", location = "/WEB-INF/jsp/tour/factory/update.jsp"),
				@Result(name = "list", location = "/WEB-INF/jsp/tour/factory/noReportedList.jsp"),
				@Result(name = "listAction", type = "redirect", location = "/tour/noReported!findPage.action?statisticType=${@com.lxs.oa.tour.common.StatisticTypeEnum@factory.value}&status=${@com.lxs.oa.tour.common.ReportStatusEnum@notReport.value}") }),
		@Action(value = "districtList", className = "tourAction", results = { @Result(name = "list", location = "/WEB-INF/jsp/tour/district/list.jsp") }),
		@Action(value = "districtStatistic", className = "tourAction", results = { @Result(name = "list", location = "/WEB-INF/jsp/tour/district/statisticList.jsp") }),
		@Action(value = "reported", className = "tourAction", results = {
				@Result(name = "list", location = "/WEB-INF/jsp/tour/factory/reportedList.jsp"),
				@Result(name = "toDetail", location = "/WEB-INF/jsp/tour/factory/detail.jsp") }),
		@Action(value = "townList", className = "tourAction", results = { @Result(name = "list", location = "/WEB-INF/jsp/tour/town/list.jsp") }),
		@Action(value = "townStatistic", className = "tourAction", results = { @Result(name = "list", location = "/WEB-INF/jsp/tour/town/statisticList.jsp") }) })
public class TourAction extends BaseAction<TourCommon> {
	@Resource
	private ITourService tourService;

	private String[] labelTexts;
	private Long[] inputMoneys;
	private String reprotYearAndMonth;
	private Long detailId;
	private Long money;
	private String startDate;
	private String endDate;
	private Long factoryId;

	public Long getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(Long factoryId) {
		this.factoryId = factoryId;
	}

	@Override
	public void beforFind(DetachedCriteria criteria) {
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		if (model.getStatisticType().equals( // 企业
				StatisticTypeEnum.factory.getValue())) {
			criteria.createAlias("user", "u");
			criteria.add(Restrictions.eq("u.id", u.getId()));
			criteria.add(Restrictions.eq("status", model.getStatus()));

		} else if (model.getStatisticType().equals(
				StatisticTypeEnum.town.getValue())) { // 镇政府
			if (model.getStatus().equals(StatusEnum.townList.getValue())) { // 列表
				List<Long> userIds = new ArrayList<Long>();
				if (null != factoryId) {// 通过页面选择企业查询
					Dept d = baseService.get(Dept.class, factoryId);
					for (User tempU : d.getUsers()) {// 企业中所有用户
						userIds.add(tempU.getId());
					}
				} else {// 没有选择特定企业是查询本镇下所有的企业下的所有用户
					u = baseService.get(User.class, u.getId());
					List<Dept> list = u.getDept().getChildren();// 获得政府下的所有企业
					for (Dept dept : list) {
						for (User tempU : dept.getUsers()) {// 企业中所有用户
							userIds.add(tempU.getId());
						}
					}
				}
				if (userIds.size() != 0) {
					criteria.createAlias("user", "u");
					criteria.add(Restrictions.in("u.id", userIds));
				} else {
					criteria.add(Restrictions.isNull("user"));
				}
			} else if (model.getStatus().equals(
					StatusEnum.townStatistic.getValue())) { // 统计列表

			}

			criteria.add(Restrictions.eq("status",
					StatusEnum.reported.getValue()));
		} else if (model.getStatisticType().equals(
				StatusEnum.districtList.getValue())) {
			List<Long> userIds = new ArrayList<Long>();
			u = baseService.get(User.class, u.getId());
			List<Dept> list = u.getDept().getChildren();// 获得区下面的所有镇
			for (Dept dept : list) {
				List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
				for (Dept factory : factyList) {
					for (User tempU : factory.getUsers()) {// 企业中所有用户
						userIds.add(tempU.getId());
					}
				}

			}
			if (userIds.size() != 0) {
				criteria.createAlias("user", "u");
				criteria.add(Restrictions.in("u.id", userIds));
			} else {
				criteria.add(Restrictions.isNull("user"));
			}

			criteria.add(Restrictions.eq("status",
					StatusEnum.reported.getValue()));
		}
		if (null != model.getJob() && null != model.getJob().getId()) {
			criteria.createAlias("job", "j");
			criteria.add(Restrictions.eq("j.id", model.getJob().getId()));
		}
		addDataCondition(criteria);
	}

	/**
	 * 给criteria添加时间条件
	 * 
	 * @param criteria
	 */
	public void addDataCondition(DetachedCriteria criteria) {
		if (null != startDate && startDate.length() != 0 && endDate != null
				&& endDate.length() != 0) {// 有查询开始时间和结束时间
			Criterion c1 = Restrictions.ge("reportYear",
					Integer.parseInt(startDate.substring(0, 4).trim()));
			Criterion c2 = Restrictions.ge("reportMonth",
					Integer.parseInt(startDate.substring(5, 7).trim()));
			Criterion c3 = Restrictions.and(c1, c2);

			Criterion c5 = Restrictions.le("reportYear",
					Integer.parseInt(endDate.substring(0, 4).trim()));
			Criterion c6 = Restrictions.le("reportMonth",
					Integer.parseInt(endDate.substring(5, 7).trim()));
			Criterion c4 = Restrictions.and(c5, c6);

			criteria.add(Restrictions.and(c3, c4));
		} else if (null != startDate && startDate.length() != 0
				&& endDate == null) {// 有开始时间没结束时间
			Criterion c1 = Restrictions.ge("reportYear",
					Integer.parseInt(startDate.substring(0, 4).trim()));
			Criterion c2 = Restrictions.ge("reportMonth",
					Integer.parseInt(startDate.substring(5, 7).trim()));
			Criterion c3 = Restrictions.and(c1, c2);

			Calendar c = Calendar.getInstance();
			Criterion c5 = Restrictions.le("reportYear", c.get(c.YEAR));
			Criterion c6 = Restrictions.le("reportMonth", c.get(c.MONTH) + 1);
			Criterion c4 = Restrictions.and(c5, c6);

			criteria.add(Restrictions.and(c3, c4));

		} else if (null == startDate && endDate != null
				&& endDate.length() != 0) {// 有开始时间没结束时间
			Criterion c5 = Restrictions.le("reportYear",
					Integer.parseInt(endDate.substring(0, 4).trim()));
			Criterion c6 = Restrictions.le("reportMonth",
					Integer.parseInt(endDate.substring(5, 7).trim()));
			criteria.add(Restrictions.and(c5, c6));
		}
	}

	/**
	 * 镇府查询时添加条件
	 * 
	 * @param criteria
	 */
	public void townAddCondition(DetachedCriteria criteria) {
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		List<Long> userIds = new ArrayList<Long>();
		u = baseService.get(User.class, u.getId());
		List<Dept> list = u.getDept().getChildren();// 获得政府下的所有企业
		for (Dept dept : list) {
			for (User tempU : dept.getUsers()) {// 企业中所有用户
				userIds.add(tempU.getId());
			}
		}
		if (userIds.size() != 0) {
			criteria.createAlias("user", "u");
			criteria.add(Restrictions.in("u.id", userIds));
		} else {
			criteria.add(Restrictions.isNull("user"));
		}
		addDataCondition(criteria);
	}

	public String townStatisticList() {
		DetachedCriteria criteria = DetachedCriteria.forClass(TourCommon.class);
		criteria.add(Restrictions.eq("status", StatusEnum.reported.getValue()));
		townAddCondition(criteria);
		PageResult page = tourService.findStatistic(criteria, start, pageSize);
		ActionContext.getContext().put(PAGE, page);
		return LIST;
	}

	/**
	 * 区查询时添加条件
	 * 
	 * @param criteria
	 */
	public void districtAddCondition(DetachedCriteria criteria) {
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		List<Long> userIds = new ArrayList<Long>();
		u = baseService.get(User.class, u.getId());
		List<Dept> list = u.getDept().getChildren();// 获得区下面的所有镇
		for (Dept dept : list) {
			List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
			for (Dept factory : factyList) {
				for (User tempU : factory.getUsers()) {// 企业中所有用户
					userIds.add(tempU.getId());
				}
			}

		}
		if (userIds.size() != 0) {
			criteria.createAlias("user", "u");
			criteria.add(Restrictions.in("u.id", userIds));
		} else {
			criteria.add(Restrictions.isNull("user"));
		}
		addDataCondition(criteria);
	}

	public String districtStatisticList() {
		DetachedCriteria criteria = DetachedCriteria.forClass(TourCommon.class);
		criteria.add(Restrictions.eq("status", StatusEnum.reported.getValue()));
		districtAddCondition(criteria);
		PageResult page = tourService.findStatistic(criteria, start, pageSize);
		ActionContext.getContext().put(PAGE, page);
		return LIST;
	}

	public List<Job> getAllJobs() {
		return baseService.find(DetachedCriteria.forClass(Job.class));
	}

	public String toReport() {
		TourCommon t = baseService.get(TourCommon.class, model.getId());
		t.setStatus(StatusEnum.reported.getValue());
		baseService.save(t);
		return LIST_ACTION;
	}

	public List<Dept> getTownFactorys() {
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		u = baseService.get(User.class, u.getId());
		return u.getDept().getChildren();
	}

	public void updateDetail() {
		TourDetail d = baseService.get(TourDetail.class, detailId);
		d.setMoney(money);
		baseService.save(d);

	}

	@Override
	public void beforeSave(TourCommon model) {
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		model.setReportDate(new Date());
		model.setReportYear(Integer.parseInt(reprotYearAndMonth.substring(0, 4)
				.trim()));
		model.setReportMonth(Integer.parseInt(reprotYearAndMonth
				.substring(5, 7).trim()));
		model.setUser(u);
		model.setStatus(StatusEnum.notReport.getValue());
	}

	public String toDetail() {
		ActionContext.getContext().getValueStack()
				.push(baseService.get(TourCommon.class, model.getId()));
		return "toDetail";
	}

	public List<Job> getUserJobs() {
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		u = baseService.get(User.class, u.getId());
		List<Job> jobs = new ArrayList<Job>();
		for (Job j : u.getJobs()) {
			jobs.add(j);
		}
		return jobs;
	}

	@Override
	public void afterSave(TourCommon m) {
		for (int i = 0; i < inputMoneys.length; i++) {
			if (null != inputMoneys[i]) {
				Long temp = inputMoneys[i];
				String lbl = labelTexts[i];
				TourDetail detail = new TourDetail();
				detail.setCommon(m);
				detail.setMoney(temp);
				detail.setName(lbl);
				baseService.add(detail);
			}
		}

	}

	public String[] getLabelTexts() {
		return labelTexts;
	}

	public void setLabelTexts(String[] labelTexts) {
		this.labelTexts = labelTexts;
	}

	public Long[] getInputMoneys() {
		return inputMoneys;
	}

	public void setInputMoneys(Long[] inputMoneys) {
		this.inputMoneys = inputMoneys;
	}

	public String getReprotYearAndMonth() {
		return reprotYearAndMonth;
	}

	public void setReprotYearAndMonth(String reprotYearAndMonth) {
		this.reprotYearAndMonth = reprotYearAndMonth;
	}

	public static void main(String[] args) {
		// String s = "2012年01";
		// System.err.println(s.substring(5, 7));
		Calendar c = Calendar.getInstance();
		System.out.println(c.get(c.MONDAY));
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public Long getMoney() {
		return money;
	}

	public void setMoney(Long money) {
		this.money = money;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
