package com.lxs.oa.tour.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.mapping.Array;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.lxs.core.action.BaseAction;
import com.lxs.core.common.BeanUtil;
import com.lxs.core.common.SystemConstant;
import com.lxs.core.common.page.PageResult;
import com.lxs.oa.tour.common.FactoryTypeEnum;
import com.lxs.oa.tour.common.StatusEnum;
import com.lxs.oa.tour.common.StatisticTypeEnum;
import com.lxs.oa.tour.domain.TourCommon;
import com.lxs.oa.tour.domain.TourDetail;
import com.lxs.oa.tour.pageModel.SameCompareDetailModel;
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
				@Result(name = "listAction", type = "redirect", location = "/tour/noReported!findPage.action?statisticType=${@com.lxs.oa.tour.common.StatisticTypeEnum@factory.value}&status=${@com.lxs.oa.tour.common.StatusEnum@notReport.value}") }),
		@Action(value = "districtList", className = "tourAction", results = { @Result(name = "list", location = "/WEB-INF/jsp/tour/district/list.jsp") }),
		@Action(value = "districtStatistic", className = "tourAction", results = { @Result(name = "list", location = "/WEB-INF/jsp/tour/district/statisticList.jsp") }),
		@Action(value = "reported", className = "tourAction", results = {
				@Result(name = "list", location = "/WEB-INF/jsp/tour/factory/reportedList.jsp"),
				@Result(name = "toDetail", location = "/WEB-INF/jsp/tour/factory/detail.jsp") }),
		@Action(value = "townList", className = "tourAction", results = { @Result(name = "list", location = "/WEB-INF/jsp/tour/town/list.jsp") }),
		@Action(value = "townSameCompare", className = "tourAction", results = {
				@Result(name = "list", location = "/WEB-INF/jsp/tour/town/sameCompareList.jsp"),
				@Result(name = "toDetail", location = "/WEB-INF/jsp/tour/town/sameCompareDetail.jsp") }),
		@Action(value = "districtSameCompare", className = "tourAction", results = {
				@Result(name = "list", location = "/WEB-INF/jsp/tour/district/sameCompareList.jsp"),
				@Result(name = "toDetail", location = "/WEB-INF/jsp/tour/district/sameCompareDetail.jsp") }),
		@Action(value = "townStatistic", className = "tourAction", results = {
				@Result(name = "list", location = "/WEB-INF/jsp/tour/town/statisticList.jsp"),
				@Result(name = "townStatisticListToDetail", location = "/WEB-INF/jsp/tour/town/statisticDetail.jsp") }) })
public class TourAction extends BaseAction<TourCommon> {
	@Resource
	private ITourService tourService;
	private String deptType;
	private String[] labelTexts;
	private Long[] inputMoneys;
	private String reprotYearAndMonth;
	private Long detailId;
	private Long money;
	private String startDate;
	private String endDate;
	private String tourIds;

	// 同比查看详情是用
	private String nowIds;
	private String lastIds;

	// 分页使用
	private Integer currentMonth = 0;
	private Integer pageMonthNum = 1;

	// 修改的时候使用
	private Collection<TourDetail> beans;

	@Override
	public void beforFind(DetachedCriteria criteria) {
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		if (model.getStatisticType().equals( // 企业
				StatisticTypeEnum.factory.getValue())) {
			criteria.createAlias("user", "u");
			criteria.add(Restrictions.eq("u.id", u.getId()));
			criteria.add(Restrictions.eq("status", model.getStatus()));
			if (null != deptType && deptType.trim().length() != 0) {
				criteria.add(Restrictions.eq("type", deptType));
			}

		} else if (model.getStatisticType().equals(
				StatisticTypeEnum.town.getValue())) { // 镇政府
			if (model.getStatus().equals(StatusEnum.townList.getValue())) { // 列表
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
		if (null != deptType && deptType.trim().length() != 0) {
			criteria.add(Restrictions.eq("type", deptType));
		}
		// if (null != deptType) {
		// criteria.createAlias("user", "u");
		// criteria.add(Restrictions.eq("u.dept.deptType", deptType));
		// }
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
				&& (endDate == null || endDate.trim().length() == 0)) {// 有开始时间没结束时间
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

		} else if ((null == startDate || startDate.trim().length() == 0)
				&& endDate != null && endDate.length() != 0) {// 有开始时间没结束时间
			Criterion c5 = Restrictions.le("reportYear",
					Integer.parseInt(endDate.substring(0, 4).trim()));
			Criterion c6 = Restrictions.le("reportMonth",
					Integer.parseInt(endDate.substring(5, 7).trim()));
			criteria.add(Restrictions.and(c5, c6));
		} else if ((null == startDate || startDate.trim().length() == 0)
				&& null == endDate || endDate.trim().length() == 0) {
			Calendar calendar = Calendar.getInstance();// 默认为上个月
			Criterion c1 = Restrictions.eq("reportYear",
					calendar.get(calendar.YEAR));
			Criterion c2 = Restrictions.eq("reportMonth",
					calendar.get(calendar.MONTH));
			criteria.add(Restrictions.and(c1, c2));
			startDate = calendar.get(calendar.YEAR) + "年"
					+ (calendar.get(calendar.MONTH)) + "月";
		}
	}

	@Override
	public void afterToUpdate(TourCommon e) {
		reprotYearAndMonth = e.getReportYear() + "年" + e.getReportMonth() + "月";
		beans = e.getDetails();
		ActionContext.getContext().put("detailNum", e.getDetails().size());
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

	}

	public String townStatisticList() {
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		u = baseService.get(User.class, u.getId());
		DetachedCriteria criteria = DetachedCriteria.forClass(TourCommon.class);
		criteria.add(Restrictions.eq("status", StatusEnum.reported.getValue()));
		townAddCondition(criteria);
		addDataCondition(criteria);
		PageResult page = tourService.findStatistic(criteria, u.getId(), u
				.getDept().getChildren());
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

	}

	public String districtStatisticList() {
		User user = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		user = baseService.get(User.class, user.getId());
		DetachedCriteria criteria = DetachedCriteria.forClass(TourCommon.class);
		criteria.add(Restrictions.eq("status", StatusEnum.reported.getValue()));
		districtAddCondition(criteria);
		addDataCondition(criteria);
		List<Dept> deptList = new ArrayList<Dept>();
		for (Dept d1 : user.getDept().getChildren()) {
			deptList.addAll(d1.getChildren());
		}
		PageResult page = tourService.findStatistic(criteria, user.getId(),
				deptList);
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

	@Override
	public void beforToAdd() {
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		u = baseService.get(User.class, u.getId());
		if (null != u.getDept() && null != u.getDept().getDeptType()) {
			ActionContext.getContext().put("deptType",
					u.getDept().getDeptType());
		}
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

	/**
	 * 镇同比
	 * 
	 * @return
	 */
	public String townSameCompare() {
		DetachedCriteria nowCriteria = DetachedCriteria
				.forClass(TourCommon.class);
		townAddCondition(nowCriteria);
		nowCriteria.add(Restrictions.eq("status",
				StatusEnum.reported.getValue()));

		DetachedCriteria lastCriteria = DetachedCriteria
				.forClass(TourCommon.class);
		townAddCondition(lastCriteria);
		lastCriteria.add(Restrictions.eq("status",
				StatusEnum.reported.getValue()));

		PageResult page = tourService.findSameCompare(nowCriteria,
				lastCriteria, startDate, endDate, currentMonth, pageMonthNum);
		ActionContext.getContext().put(PAGE, page);
		
		if ((null == startDate || startDate.trim().length() == 0)
				&& (null == endDate || endDate.trim().length() == 0)) {
			Calendar calendar = Calendar.getInstance();// 默认为上个月
			startDate = calendar.get(calendar.YEAR) + "年"
					+ (calendar.get(calendar.MONTH)) + "月";
		}
		return LIST;
	}

	/**
	 * 区同比
	 * 
	 * @return
	 */
	public String districtSameCompare() {
		DetachedCriteria nowCriteria = DetachedCriteria
				.forClass(TourCommon.class);
		districtAddCondition(nowCriteria);
		nowCriteria.add(Restrictions.eq("status",
				StatusEnum.reported.getValue()));

		DetachedCriteria lastCriteria = DetachedCriteria
				.forClass(TourCommon.class);
		districtAddCondition(lastCriteria);
		lastCriteria.add(Restrictions.eq("status",
				StatusEnum.reported.getValue()));

		PageResult page = tourService.findSameCompare(nowCriteria,
				lastCriteria, startDate, endDate, currentMonth, pageMonthNum);
		ActionContext.getContext().put(PAGE, page);
		if ((null == startDate || startDate.trim().length() == 0)
				&&( null == endDate || endDate.trim().length() == 0)) {
			Calendar calendar = Calendar.getInstance();// 默认为上个月
			startDate = calendar.get(calendar.YEAR) + "年"
					+ (calendar.get(calendar.MONTH)) + "月";
		}
		return LIST;
	}

	public String sameCompareToDetail() {
		List<TourCommon> nowList = new ArrayList<TourCommon>();
		List<TourCommon> lastList = new ArrayList<TourCommon>();
		if (null != nowIds && nowIds.trim().length() != 0) {
			String nowTempIds[] = nowIds.split(",");
			for (String str : nowTempIds) {
				if (null != str) {
					TourCommon common = baseService.get(TourCommon.class,
							Long.parseLong(str));
					nowList.add(common);
				}
			}
		}
		if (null != lastIds && lastIds.trim().length() != 0) {
			String lastTempIds[] = lastIds.split(",");
			for (String str : lastTempIds) {
				if (null != str) {
					TourCommon common = baseService.get(TourCommon.class,
							Long.parseLong(str));
					lastList.add(common);
				}
			}
		}
		List<String> detailNames = new ArrayList<String>();
		if (null != nowList && nowList.size() != 0) {
			for (TourDetail d : nowList.get(0).getDetails()) {
				detailNames.add(d.getName());
			}
		}
		if (detailNames.size() == 0 && null != lastList && lastList.size() != 0) {
			for (TourDetail d : lastList.get(0).getDetails()) {
				detailNames.add(d.getName());
			}
		}
		List<SameCompareDetailModel> modelDetails = new ArrayList<SameCompareDetailModel>();
		for (String str : detailNames) {
			SameCompareDetailModel tempDetail = new SameCompareDetailModel();
			Long nowMoney = 0l;
			Long lastMoney = 0l;
			String time = null;
			for (TourCommon comm : nowList) {
				for (TourDetail d : comm.getDetails()) {
					if (str.equals(d.getName())) {
						if (null == time) {
							time = comm.getReportYear() + "年"
									+ comm.getReportMonth() + "月";
						}
						nowMoney += d.getMoney();
					}
				}
			}
			for (TourCommon comm : lastList) {
				for (TourDetail d : comm.getDetails()) {
					if (str.equals(d.getName())) {
						if (null == time) {
							time = (comm.getReportYear() + 1) + "年"
									+ comm.getReportMonth() + "月";
						}
						lastMoney += d.getMoney();
					}
				}
			}
			tempDetail.setNowMoney(nowMoney);
			tempDetail.setLastMoney(lastMoney);
			tempDetail.setName(str);
			tempDetail.setTime(time);
			modelDetails.add(tempDetail);
		}

		ActionContext.getContext().put("sameCompareDetaiList", modelDetails);
		return "toDetail";
	}

	public String townStatisticListToDetail() {
		if (null != tourIds && tourIds.trim().length() != 0) {
			String[] strs = tourIds.split(",");
			List<TourCommon> list = new ArrayList<TourCommon>();
			for (String s : strs) {
				list.add(baseService.get(TourCommon.class,
						Long.parseLong(s.trim())));
			}
			List<TourDetail> details = new ArrayList<TourDetail>();
			List<String> detailNames = new ArrayList<String>();
			if (null != list && list.size() != 0) {
				for (TourDetail d : list.get(0).getDetails()) {
					detailNames.add(d.getName());
				}

				for (String name : detailNames) {
					TourDetail tempD = new TourDetail();
					Long money = 0l;
					for (TourCommon com : list) {
						for (TourDetail d : com.getDetails()) {
							if (name.equals(d.getName())) {
								money += d.getMoney();
								break;
							}
						}
					}
					tempD.setMoney(money);
					tempD.setName(name);
					details.add(tempD);
				}
			}
			ActionContext.getContext().put("statisticDetails", details);
		}
		return "townStatisticListToDetail";
	}

	@Override
	public void beforeSave(TourCommon model) {
		if (null == beans || beans.size() == 0) {
			User u = (User) ActionContext.getContext().getSession()
					.get(SystemConstant.CURRENT_USER);
			model.setReportDate(new Date());
			model.setReportYear(Integer.parseInt(reprotYearAndMonth.substring(
					0, 4).trim()));
			model.setReportMonth(Integer.parseInt(reprotYearAndMonth.substring(
					5, 7).trim()));
			model.setUser(u);
			model.setStatus(StatusEnum.notReport.getValue());
		} else {
			for (TourDetail d : beans) {
				if (null != d && null != d.getId()) {
					TourDetail tempDetail = baseService.get(TourDetail.class,
							d.getId());
					tempDetail.setMoney(d.getMoney());
					baseService.update(tempDetail);

				}
			}
		}
	}

	public String toDetail() {
		ActionContext.getContext().getValueStack()
				.push(baseService.get(TourCommon.class, model.getId()));
		return "toDetail";
	}

	@Override
	public void afterSave(TourCommon m) {
		if (null == beans || beans.size() == 0) {
			for (int i = 0; i < inputMoneys.length; i++) {
				Long temp = inputMoneys[i];
				String lbl = labelTexts[i];
				if (null == temp) {
					temp = 0l;
				}
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

	public String getDeptType() {
		return deptType;
	}

	public void setDeptType(String deptType) {
		this.deptType = deptType;
	}

	public String getTourIds() {
		return tourIds;
	}

	public void setTourIds(String tourIds) {
		this.tourIds = tourIds;
	}

	public Integer getCurrentMonth() {
		return currentMonth;
	}

	public void setCurrentMonth(Integer currentMonth) {
		this.currentMonth = currentMonth;
	}

	public Integer getPageMonthNum() {
		return pageMonthNum;
	}

	public void setPageMonthNum(Integer pageMonthNum) {
		this.pageMonthNum = pageMonthNum;
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

	public Collection<TourDetail> getBeans() {
		return beans;
	}

	public void setBeans(Collection<TourDetail> beans) {
		this.beans = beans;
	}

}
