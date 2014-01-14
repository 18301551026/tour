package com.lxs.oa.tour.action;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.util.FileBufferedOutputStream;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.lxs.core.action.BaseAction;
import com.lxs.core.common.SystemConstant;
import com.lxs.core.common.TimeUtil;
import com.lxs.core.common.page.PageResult;
import com.lxs.oa.tour.common.DeptLevelEnum;
import com.lxs.oa.tour.common.StatisticTypeEnum;
import com.lxs.oa.tour.common.StatusEnum;
import com.lxs.oa.tour.domain.TourCommon;
import com.lxs.oa.tour.domain.TourDetail;
import com.lxs.oa.tour.pageModel.SameCompareChartModel;
import com.lxs.oa.tour.pageModel.SameCompareDetailModel;
import com.lxs.oa.tour.pageModel.SameCompareModel;
import com.lxs.oa.tour.pageModel.StatisticReportModel;
import com.lxs.oa.tour.service.ITourService;
import com.lxs.security.common.SecurityHolder;
import com.lxs.security.domain.Dept;
import com.lxs.security.domain.User;
import com.lxs.tour.domain.FactoryType;
import com.opensymphony.xwork2.ActionContext;

@Controller
@Scope("prototype")
@Namespace("/tour")
@Actions({
		@Action(value = "noReported", className = "tourAction", results = {
				@Result(name="lastQuarterSameCompareList",location="/WEB-INF/jsp/protal/lastQuarterSameCompareList.jsp"),
				@Result(name="noData",location="/WEB-INF/jsp/protal/noData.jsp"),
				@Result(name="lastMonthStatisticList",location="/WEB-INF/jsp/protal/lastStatisticList.jsp"),
				@Result(name="last5ReportedList",location="/WEB-INF/jsp/protal/last5Reportedlist.jsp"),
				@Result(name="lastMonthSameCompareList",location="/WEB-INF/jsp/protal/lastMonthSameCompareList.jsp"),
				@Result(name = "add", location = "/WEB-INF/jsp/tour/factory/add.jsp"),
				@Result(name = "update", location = "/WEB-INF/jsp/tour/factory/update.jsp"),
				@Result(name = "list", location = "/WEB-INF/jsp/tour/factory/noReportedList.jsp"),
				@Result(name = "listAction", type = "redirect", location = "/tour/noReported!findPage.action?statisticType=${@com.lxs.oa.tour.common.StatisticTypeEnum@factory.value}&status=${@com.lxs.oa.tour.common.StatusEnum@notReport.value}") }),
		@Action(value = "districtList", className = "tourAction", results = { @Result(name = "list", location = "/WEB-INF/jsp/tour/district/list.jsp"),
				@Result(name = "toDetail", location = "/WEB-INF/jsp/tour/district/detail.jsp"),		
		}),
		@Action(value = "districtStatistic", className = "tourAction", results = { @Result(name = "list", location = "/WEB-INF/jsp/tour/district/statisticList.jsp"),
				@Result(name = "townStatisticListToDetail", location = "/WEB-INF/jsp/tour/district/statisticDetail.jsp")
		}),
		@Action(value = "reported", className = "tourAction", results = {
				@Result(name = "list", location = "/WEB-INF/jsp/tour/factory/reportedList.jsp"),
				@Result(name = "toDetail", location = "/WEB-INF/jsp/tour/factory/detail.jsp") }),
		@Action(value = "townList", className = "tourAction", results = { @Result(name = "list", location = "/WEB-INF/jsp/tour/town/list.jsp"),
				@Result(name = "update", location = "/WEB-INF/jsp/tour/town/update.jsp"),
				@Result(name = "toDetail", location = "/WEB-INF/jsp/tour/town/detail.jsp"),
				@Result(name = "listAction", type = "redirect", location = "/tour/townList!findPage.action?statisticType=2&status=3")
		}),
		@Action(value = "districtChart", className = "tourAction", results = { @Result(name = "list", location = "/WEB-INF/jsp/tour/town/list.jsp") }),
		@Action(value = "townQuarterSameCompare", className = "tourAction", results = {
				@Result(name = "list", location = "/WEB-INF/jsp/tour/town/quarterSameCompareList.jsp"),
				@Result(name = "toDetail", location = "/WEB-INF/jsp/tour/town/quarterSameCompareDetail.jsp"),
				@Result(name = "toSelectChart", location = "/WEB-INF/jsp/tour/town/quarterSelectChart.jsp") }),
		@Action(value = "districtQuarterSameCompare", className = "tourAction", results = {
				@Result(name = "list", location = "/WEB-INF/jsp/tour/district/quarterSameCompareList.jsp"),
				@Result(name = "toDetail", location = "/WEB-INF/jsp/tour/district/quarterSameCompareDetail.jsp"),
				@Result(name = "toSelectChart", location = "/WEB-INF/jsp/tour/district/quarterSelectChart.jsp") }),
		@Action(value = "townSameCompare", className = "tourAction", results = {
				@Result(name = "list", location = "/WEB-INF/jsp/tour/town/sameCompareList.jsp"),
				@Result(name = "toSelectChart", location = "/WEB-INF/jsp/tour/town/selectChart.jsp"),
				@Result(name = "toDetail", location = "/WEB-INF/jsp/tour/town/sameCompareDetail.jsp") }),
		@Action(value = "districtSameCompare", className = "tourAction", results = {
				@Result(name = "list", location = "/WEB-INF/jsp/tour/district/sameCompareList.jsp"),
				@Result(name = "toSelectChart", location = "/WEB-INF/jsp/tour/district/selectChart.jsp"),
				@Result(name = "toDetail", location = "/WEB-INF/jsp/tour/district/sameCompareDetail.jsp") }),
		@Action(value = "townStatistic", className = "tourAction", results = {
				@Result(name = "list", location = "/WEB-INF/jsp/tour/town/statisticList.jsp"),
				@Result(name = "townStatisticListToDetail", location = "/WEB-INF/jsp/tour/town/statisticDetail.jsp") }) })
public class TourAction extends BaseAction<TourCommon> {
	private String redirectAddress;
	@Resource
	private ITourService tourService;
	
	private Long companyId;
	private Long townId;
	private String deptType;
	private String[] labelTexts;
	private Double[] inputMoneys;
	private String reprotYearAndMonth;
	private Long detailId;
	private Long money;
	private String startDate;
	private String endDate;
	private String tourIds;
	private String tempTourIds[];
	private Date tempDate;

	// 同比查看详情是用
	private String nowIds;
	private String lastIds;

	// 分页使用
	private Integer currentMonth = 0;
	private Integer pageMonthNum = 1;

	// 修改的时候使用
	private Collection<TourDetail> beans;
	private Connection conn;
	private Map<String, Object> parameters = new HashMap<String, Object>();

	// 保存报表类型
	private String reprotType;

	// 保存季度(查询)
	private Integer quarters[] = { 1, 2, 3, 4 };
	private Integer startYear;
	private Integer endYear;
	
	private Integer firstStatus=0;//等于0的时候查询上个月的
	
	private Long factoryTypeId;
	
	private String tempReportDate;
	/**
	 * 图表
	 */

	public String toSelectChart() {
		startYear=Calendar.getInstance().get(Calendar.YEAR);
		return "toSelectChart";
	}
	public void sameCompareDetailToWord() throws Exception{
		parameters.put("reportDate", tempReportDate);
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
		List<SameCompareChartModel> list=new ArrayList<SameCompareChartModel>();
		for(String n:detailNames){
			SameCompareChartModel nowModel = new SameCompareChartModel();
			SameCompareChartModel lastModel = new SameCompareChartModel();
			nowModel.setYearType("今年");
			lastModel.setYearType("去年");
			nowModel.setType(n);
			lastModel.setType(n);
			Double nowMoneyValues = 0d;
			Double lastMoneyValues = 0d;

			// 本年的
			for (TourCommon tourCommon : nowList) {
				for(TourDetail d:tourCommon.getDetails()){
					if (d.getName().equals(n)) {
						nowMoneyValues+=d.getMoney();
					}
				}
			}
			// 去年
			for (TourCommon tourCommon : lastList) {
				for(TourDetail d:tourCommon.getDetails()){
					if (d.getName().equals(n)) {
						lastMoneyValues+=d.getMoney();
					}
				}
			}
			nowModel.setMoneyValues(nowMoneyValues);
			lastModel.setMoneyValues(lastMoneyValues);
			list.add(nowModel);
			list.add(lastModel);
		}
		
		
		String path = ServletActionContext.getServletContext().getRealPath("/")
				+ "reports/";
		FileBufferedOutputStream fbos = new FileBufferedOutputStream();
		JRBeanCollectionDataSource dataSource = null;
		dataSource = new JRBeanCollectionDataSource(list);
		JRDocxExporter exporter = new JRDocxExporter(
				DefaultJasperReportsContext.getInstance());
		JasperPrint jasperPrint = JasperFillManager.fillReport(path
				+ "sameCompareDetail_tour_report.jasper", parameters, dataSource);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);
		exporter.exportReport();
		fbos.close();
		String fileName = new String("明细同比展示.docx".getBytes("GBK"),
				"ISO8859_1");
		response.setCharacterEncoding("UTF-8");
		;
		response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ fileName);
		response.setContentLength(fbos.size());
		ServletOutputStream ouputStream = response.getOutputStream();

		fbos.writeData(ouputStream);
		fbos.dispose();
		ouputStream.flush();

		ouputStream.close();
		fbos.close();
		fbos.dispose();
	}
	public String sameCompareDetailToHtml(){
		parameters.put("reportDate", tempReportDate);
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
		List<SameCompareChartModel> list=new ArrayList<SameCompareChartModel>();
		for(String n:detailNames){
			SameCompareChartModel nowModel = new SameCompareChartModel();
			SameCompareChartModel lastModel = new SameCompareChartModel();
			nowModel.setYearType("今年");
			lastModel.setYearType("去年");
			nowModel.setType(n);
			lastModel.setType(n);
			Double nowMoneyValues = 0d;
			Double lastMoneyValues = 0d;

			// 本年的
			for (TourCommon tourCommon : nowList) {
				for(TourDetail d:tourCommon.getDetails()){
					if (d.getName().equals(n)) {
						nowMoneyValues+=d.getMoney();
					}
				}
			}
			// 去年
			for (TourCommon tourCommon : lastList) {
				for(TourDetail d:tourCommon.getDetails()){
					if (d.getName().equals(n)) {
						lastMoneyValues+=d.getMoney();
					}
				}
			}
			nowModel.setMoneyValues(nowMoneyValues);
			lastModel.setMoneyValues(lastMoneyValues);
			list.add(nowModel);
			list.add(lastModel);
		}
		ActionContext.getContext().put("myList", list);
		return "html";
	}
	public void townWordChart() throws Exception {
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
		addParameters();
		List<SameCompareChartModel> modelList = tourService.getCharts(
				nowCriteria, lastCriteria, startDate, null, currentMonth,
				pageMonthNum);
		String path = ServletActionContext.getServletContext().getRealPath("/")
				+ "reports/";
		FileBufferedOutputStream fbos = new FileBufferedOutputStream();
		JRBeanCollectionDataSource dataSource = null;
		dataSource = new JRBeanCollectionDataSource(modelList);
		JRDocxExporter exporter = new JRDocxExporter(
				DefaultJasperReportsContext.getInstance());
		JasperPrint jasperPrint = JasperFillManager.fillReport(path
				+ "tour_sameCompare_chart.jasper", parameters, dataSource);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);
		exporter.exportReport();
		fbos.close();
		String fileName = new String("镇域旅游发展同比.docx".getBytes("GBK"),
				"ISO8859_1");
		response.setCharacterEncoding("UTF-8");
		;
		response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ fileName);
		response.setContentLength(fbos.size());
		ServletOutputStream ouputStream = response.getOutputStream();

		fbos.writeData(ouputStream);
		fbos.dispose();
		ouputStream.flush();

		ouputStream.close();
		fbos.close();
		fbos.dispose();
	}
	public void townWordReport() throws Exception {
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		u=baseService.get(User.class, u.getId());
		parameters.put("deptName", u.getDept().getText());
		List<Long> userIds = new ArrayList<Long>();
		u = baseService.get(User.class, u.getId());
		List<Dept> list = u.getDept().getChildren();// 获得政府下的所有企业
		for (Dept dept : list) {
			for (User tempU : dept.getUsers()) {// 企业中所有用户
				userIds.add(tempU.getId());
			}
		}
		endDate=startDate;
		List<SameCompareModel> modelList=tourService.findSameCompare(userIds, startDate, endDate);
		String path = ServletActionContext.getServletContext().getRealPath("/")
				+ "reports/";
		FileBufferedOutputStream fbos = new FileBufferedOutputStream();
		JRBeanCollectionDataSource dataSource = null;
		dataSource = new JRBeanCollectionDataSource(modelList);
		JRDocxExporter exporter = new JRDocxExporter(
				DefaultJasperReportsContext.getInstance());
		JasperPrint jasperPrint = JasperFillManager.fillReport(path
				+ "sameCompareMonthReport.jasper", parameters, dataSource);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);
		exporter.exportReport();
		fbos.close();
		String fileName = new String("镇域旅游发展同比.docx".getBytes("GBK"),
				"ISO8859_1");
		response.setCharacterEncoding("UTF-8");
		;
		response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ fileName);
		response.setContentLength(fbos.size());
		ServletOutputStream ouputStream = response.getOutputStream();

		fbos.writeData(ouputStream);
		fbos.dispose();
		ouputStream.flush();

		ouputStream.close();
		fbos.close();
		fbos.dispose();
	}
	/**
	 * 镇同比html报表
	 */
	public String townHtmlReport() {
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		u=baseService.get(User.class, u.getId());
		parameters.put("deptName", u.getDept().getText());
		List<Long> userIds = new ArrayList<Long>();
		u = baseService.get(User.class, u.getId());
		List<Dept> list = u.getDept().getChildren();// 获得政府下的所有企业
		for (Dept dept : list) {
			for (User tempU : dept.getUsers()) {// 企业中所有用户
				userIds.add(tempU.getId());
			}
		}
		endDate=startDate;
		List<SameCompareModel> modelList=tourService.findSameCompare(userIds, startDate, endDate);
		ActionContext.getContext().put("myList", modelList);

		return "html";
	}
	/**
	 * 镇同比html图表
	 */
	public String townHtmlChart() {

		DetachedCriteria nowCriteria = DetachedCriteria
				.forClass(TourCommon.class);
		townAddCondition(nowCriteria);
		nowCriteria.add(Restrictions.eq("status",
				StatusEnum.reported.getValue()));
		addParameters();
		DetachedCriteria lastCriteria = DetachedCriteria
				.forClass(TourCommon.class);
		townAddCondition(lastCriteria);
		lastCriteria.add(Restrictions.eq("status",
				StatusEnum.reported.getValue()));

		List<SameCompareChartModel> modelList = tourService.getCharts(
				nowCriteria, lastCriteria, startDate, null, currentMonth,
				pageMonthNum);
		ActionContext.getContext().put("myList", modelList);

		return "html";
	}

	/**
	 * 区季度同比导出word
	 * 
	 * @throws Exception
	 */
	public void districtQuarterWordChart() throws Exception {
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		List<Long> userIds = new ArrayList<Long>();
		u = baseService.get(User.class, u.getId());
		List<Dept> list = u.getDept().getChildren();// 获得区下面的所有镇
		if (null!=townId) {
			for (Dept dept : list) {
				if (dept.getId().equals(townId)) {
					List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
					for (Dept factory : factyList) {
						for (User tempU : factory.getUsers()) {// 企业中所有用户
							userIds.add(tempU.getId());
						}
					}
					break;
				}

			}
		}else{
			for (Dept dept : list) {
				List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
				for (Dept factory : factyList) {
					for (User tempU : factory.getUsers()) {// 企业中所有用户
						userIds.add(tempU.getId());
					}
				}

			}
		}

		String reportDate = startYear + "";
		if (null != quarters && quarters.length != 0) {

			int qu = quarters[0];
			if (qu == 1) {
				reportDate += "一季度";
			} else if (qu == 2) {
				reportDate += "二季度";
			} else if (qu == 3) {
				reportDate += "三季度";
			} else if (qu == 4) {
				reportDate += "四季度";
			}
		}
		if (null!=townId) {
			parameters.put("deptName",baseService.get(Dept.class, townId).getText());
		}else{
			parameters.put("deptName", u.getDept().getText());
		}
		parameters.put("reportDate", reportDate);
		List<Integer> qu = new ArrayList<Integer>();
		if (null != quarters && quarters.length != 0) {
			for (int i : quarters) {
				qu.add(i);
			}
		}
		int tempStartYear=0;
		int tempEndYear=0;
		if (null!=startYear) {
			tempStartYear=startYear;
		}
		if (null!=endYear) {
			tempEndYear=endYear;
		}
		List<SameCompareChartModel> modelList=tourService.getQuarterCharts(userIds, tempStartYear, tempEndYear, qu);

		String path = ServletActionContext.getServletContext().getRealPath("/")
				+ "reports/";
		FileBufferedOutputStream fbos = new FileBufferedOutputStream();
		JRBeanCollectionDataSource dataSource = null;
		dataSource = new JRBeanCollectionDataSource(modelList);
		JRDocxExporter exporter = new JRDocxExporter(
				DefaultJasperReportsContext.getInstance());
		JasperPrint jasperPrint = JasperFillManager.fillReport(path
				+ "tour_sameCompare_chart.jasper", parameters, dataSource);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);
		exporter.exportReport();
		fbos.close();
		String fileName = new String("镇域旅游发展同比.docx".getBytes("GBK"),
				"ISO8859_1");
		response.setCharacterEncoding("UTF-8");
		;
		response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ fileName);
		response.setContentLength(fbos.size());
		ServletOutputStream ouputStream = response.getOutputStream();

		fbos.writeData(ouputStream);
		fbos.dispose();
		ouputStream.flush();

		ouputStream.close();
		fbos.close();
		fbos.dispose();
	}
	public String districtQuarterHtmlReport(){
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		u=baseService.get(User.class, u.getId());
		if (null!=townId) {
			parameters.put("deptName",baseService.get(Dept.class, townId).getText());
		}else{
			parameters.put("deptName", u.getDept().getText());
		}
		List<Long> userIds = new ArrayList<Long>();
		u = baseService.get(User.class, u.getId());
		List<Dept> list = u.getDept().getChildren();// 获得区下面的所有镇
		if (null!=townId) {
			for (Dept dept : list) {
				if (dept.getId().equals(townId)) {
					List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
					for (Dept factory : factyList) {
						for (User tempU : factory.getUsers()) {// 企业中所有用户
							userIds.add(tempU.getId());
						}
					}
					break;
				}

			}
		}else{
			for (Dept dept : list) {
				List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
				for (Dept factory : factyList) {
					for (User tempU : factory.getUsers()) {// 企业中所有用户
						userIds.add(tempU.getId());
					}
				}

			}
		}
		List<Integer> qu = new ArrayList<Integer>();
		if (null != quarters && quarters.length != 0) {
			for (int i : quarters) {
				qu.add(i);
			}
		}
		
		int tempStartYear=0;
		int tempEndYear=0;
		if (null!=startYear) {
			tempStartYear=startYear;
		}
		if (null!=endYear) {
			tempEndYear=endYear;
		}
		endYear=startYear;
		List<SameCompareModel> modelList = tourService
				.getQuarterSameCompareModels(userIds, tempStartYear, tempEndYear, qu);// 全部数据
		ActionContext.getContext().put("myList", modelList);
		return "html";
	}
	public void districtQuarterWordReport() throws Exception{
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		u=baseService.get(User.class, u.getId());
		if (null!=townId) {
			parameters.put("deptName",baseService.get(Dept.class, townId).getText());
		}else{
			parameters.put("deptName", u.getDept().getText());
		}
		List<Long> userIds = new ArrayList<Long>();
		u = baseService.get(User.class, u.getId());
		List<Dept> list = u.getDept().getChildren();// 获得区下面的所有镇
		if (null!=townId) {
			for (Dept dept : list) {
				if (dept.getId().equals(townId)) {
					List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
					for (Dept factory : factyList) {
						for (User tempU : factory.getUsers()) {// 企业中所有用户
							userIds.add(tempU.getId());
						}
					}
					break;
				}

			}
		}else{
			for (Dept dept : list) {
				List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
				for (Dept factory : factyList) {
					for (User tempU : factory.getUsers()) {// 企业中所有用户
						userIds.add(tempU.getId());
					}
				}

			}
		}
		List<Integer> qu = new ArrayList<Integer>();
		if (null != quarters && quarters.length != 0) {
			for (int i : quarters) {
				qu.add(i);
			}
		}
		
		int tempStartYear=0;
		int tempEndYear=0;
		if (null!=startYear) {
			tempStartYear=startYear;
		}
		if (null!=endYear) {
			tempEndYear=endYear;
		}
		endYear=startYear;
		List<SameCompareModel> modelList = tourService
				.getQuarterSameCompareModels(userIds, tempStartYear, tempEndYear, qu);// 全部数据
		
		
		ActionContext.getContext().put("myList", modelList);
		String path = ServletActionContext.getServletContext().getRealPath("/")
				+ "reports/";
		FileBufferedOutputStream fbos = new FileBufferedOutputStream();
		JRBeanCollectionDataSource dataSource = null;
		dataSource = new JRBeanCollectionDataSource(modelList);
		JRDocxExporter exporter = new JRDocxExporter(
				DefaultJasperReportsContext.getInstance());
		JasperPrint jasperPrint = JasperFillManager.fillReport(path
				+ "sameCompareQuarterReport.jasper", parameters, dataSource);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);
		exporter.exportReport();
		fbos.close();
		String fileName = new String("镇域旅游发展同比.docx".getBytes("GBK"),
				"ISO8859_1");
		response.setCharacterEncoding("UTF-8");
		;
		response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ fileName);
		response.setContentLength(fbos.size());
		ServletOutputStream ouputStream = response.getOutputStream();

		fbos.writeData(ouputStream);
		fbos.dispose();
		ouputStream.flush();

		ouputStream.close();
		fbos.close();
		fbos.dispose();
	}
	/**
	 * 区季度同比html图表
	 */
	public String districtQuarterHtmlChart() {
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		List<Long> userIds = new ArrayList<Long>();
		u = baseService.get(User.class, u.getId());
		List<Dept> list = u.getDept().getChildren();// 获得区下面的所有镇
		if (null!=townId) {
			for (Dept dept : list) {
				if (dept.getId().equals(townId)) {
					List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
					for (Dept factory : factyList) {
						for (User tempU : factory.getUsers()) {// 企业中所有用户
							userIds.add(tempU.getId());
						}
					}
					break;
				}

			}
		}else{
			for (Dept dept : list) {
				List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
				for (Dept factory : factyList) {
					for (User tempU : factory.getUsers()) {// 企业中所有用户
						userIds.add(tempU.getId());
					}
				}

			}
		}

		String reportDate = startYear + "";
		if (null != quarters && quarters.length != 0) {

			int qu = quarters[0];
			if (qu == 1) {
				reportDate += "一季度";
			} else if (qu == 2) {
				reportDate += "二季度";
			} else if (qu == 3) {
				reportDate += "三季度";
			} else if (qu == 4) {
				reportDate += "四季度";
			}
		}
		if (null!=townId) {
			parameters.put("deptName", baseService.get(Dept.class, townId).getText());
		}else{
			parameters.put("deptName", u.getDept().getText());
		}
		parameters.put("reportDate", reportDate);

		List<Integer> qu = new ArrayList<Integer>();
		if (null != quarters && quarters.length != 0) {
			for (int i : quarters) {
				qu.add(i);
			}
		}
		int tempStartYear=0;
		int tempEndYear=0;
		if (null!=startYear) {
			tempStartYear=startYear;
		}
		if (null!=endYear) {
			tempEndYear=endYear;
		}
		List<SameCompareChartModel> modelList=tourService.getQuarterCharts(userIds, tempStartYear, tempEndYear, qu);
		ActionContext.getContext().put("myList", modelList);

		return "html";
	}

	/**
	 * 镇季度同比导出word
	 * 
	 * @throws Exception
	 */
	public void townQuarterWordChart() throws Exception {
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

		String reportDate = startYear + "";
		if (null != quarters && quarters.length != 0) {

			int qu = quarters[0];
			if (qu == 1) {
				reportDate += "一季度";
			} else if (qu == 2) {
				reportDate += "二季度";
			} else if (qu == 3) {
				reportDate += "三季度";
			} else if (qu == 4) {
				reportDate += "四季度";
			}
		}
		parameters.put("deptName", u.getDept().getText());
		parameters.put("reportDate", reportDate);

		List<Integer> qu = new ArrayList<Integer>();
		if (null != quarters && quarters.length != 0) {
			for (int i : quarters) {
				qu.add(i);
			}
		}
		int tempStartYear=0;
		int tempEndYear=0;
		if (null!=startYear) {
			tempStartYear=startYear;
		}
		if (null!=endYear) {
			tempEndYear=endYear;
		}
		List<SameCompareChartModel> modelList=tourService.getQuarterCharts(userIds, tempStartYear, tempEndYear, qu);

		String path = ServletActionContext.getServletContext().getRealPath("/")
				+ "reports/";
		FileBufferedOutputStream fbos = new FileBufferedOutputStream();
		JRBeanCollectionDataSource dataSource = null;
		dataSource = new JRBeanCollectionDataSource(modelList);
		JRDocxExporter exporter = new JRDocxExporter(
				DefaultJasperReportsContext.getInstance());
		JasperPrint jasperPrint = JasperFillManager.fillReport(path
				+ "tour_sameCompare_chart.jasper", parameters, dataSource);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);
		exporter.exportReport();
		fbos.close();
		String fileName = new String("镇域旅游发展同比.docx".getBytes("GBK"),
				"ISO8859_1");
		response.setCharacterEncoding("UTF-8");
		;
		response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ fileName);
		response.setContentLength(fbos.size());
		ServletOutputStream ouputStream = response.getOutputStream();

		fbos.writeData(ouputStream);
		fbos.dispose();
		ouputStream.flush();

		ouputStream.close();
		fbos.close();
		fbos.dispose();
	}
	public void townQuarterWordReport() throws Exception{
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		u=baseService.get(User.class, u.getId());
		parameters.put("deptName", u.getDept().getText());
		List<Long> userIds = new ArrayList<Long>();
		u = baseService.get(User.class, u.getId());
		List<Dept> list = u.getDept().getChildren();// 获得区下面的所有镇
		for (Dept dept : list) {
			for (User tempU : dept.getUsers()) {// 企业中所有用户
				userIds.add(tempU.getId());
			}
		}
		List<Integer> qu = new ArrayList<Integer>();
		if (null != quarters && quarters.length != 0) {
			for (int i : quarters) {
				qu.add(i);
			}
		}
		
		int tempStartYear=0;
		int tempEndYear=0;
		if (null!=startYear) {
			tempStartYear=startYear;
		}
		if (null!=endYear) {
			tempEndYear=endYear;
		}
		endYear=startYear;
		List<SameCompareModel> modelList = tourService
				.getQuarterSameCompareModels(userIds, tempStartYear, tempEndYear, qu);// 全部数据
		
		
		ActionContext.getContext().put("myList", modelList);
		String path = ServletActionContext.getServletContext().getRealPath("/")
				+ "reports/";
		FileBufferedOutputStream fbos = new FileBufferedOutputStream();
		JRBeanCollectionDataSource dataSource = null;
		dataSource = new JRBeanCollectionDataSource(modelList);
		JRDocxExporter exporter = new JRDocxExporter(
				DefaultJasperReportsContext.getInstance());
		JasperPrint jasperPrint = JasperFillManager.fillReport(path
				+ "sameCompareQuarterReport.jasper", parameters, dataSource);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);
		exporter.exportReport();
		fbos.close();
		String fileName = new String("镇域旅游发展同比.docx".getBytes("GBK"),
				"ISO8859_1");
		response.setCharacterEncoding("UTF-8");
		;
		response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ fileName);
		response.setContentLength(fbos.size());
		ServletOutputStream ouputStream = response.getOutputStream();

		fbos.writeData(ouputStream);
		fbos.dispose();
		ouputStream.flush();

		ouputStream.close();
		fbos.close();
		fbos.dispose();
	}
	public String townQuarterHtmlReport(){
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		u=baseService.get(User.class, u.getId());
		parameters.put("deptName", u.getDept().getText());
		List<Long> userIds = new ArrayList<Long>();
		u = baseService.get(User.class, u.getId());
		List<Dept> list = u.getDept().getChildren();// 获得政府下的所有企业
		for (Dept dept : list) {
			for (User tempU : dept.getUsers()) {// 企业中所有用户
				userIds.add(tempU.getId());
			}
		}
		List<Integer> qu = new ArrayList<Integer>();
		if (null != quarters && quarters.length != 0) {
			for (int i : quarters) {
				qu.add(i);
			}
		}
		
		int tempStartYear=0;
		int tempEndYear=0;
		if (null!=startYear) {
			tempStartYear=startYear;
		}
		if (null!=endYear) {
			tempEndYear=endYear;
		}
		endYear=startYear;
		List<SameCompareModel> modelList = tourService
				.getQuarterSameCompareModels(userIds, tempStartYear, tempEndYear, qu);// 全部数据
		ActionContext.getContext().put("myList", modelList);
		return "html";
	}
	/**
	 * 镇季度同比html图表
	 */
	public String townQuarterHtmlChart() {
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

		String reportDate = startYear + "";
		if (null != quarters && quarters.length != 0) {

			int qu = quarters[0];
			if (qu == 1) {
				reportDate += "一季度";
			} else if (qu == 2) {
				reportDate += "二季度";
			} else if (qu == 3) {
				reportDate += "三季度";
			} else if (qu == 4) {
				reportDate += "四季度";
			}
		}
		parameters.put("deptName", u.getDept().getText());
		parameters.put("reportDate", reportDate);

		List<Integer> qu = new ArrayList<Integer>();
		if (null != quarters && quarters.length != 0) {
			for (int i : quarters) {
				qu.add(i);
			}
		}
		int tempStartYear=0;
		int tempEndYear=0;
		if (null!=startYear) {
			tempStartYear=startYear;
		}
		if (null!=endYear) {
			tempEndYear=endYear;
		}
		List<SameCompareChartModel> modelList=tourService.getQuarterCharts(userIds, tempStartYear, tempEndYear, qu);
		ActionContext.getContext().put("myList", modelList);

		return "html";
	}

	public void districtWordChart() throws Exception {
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
		addParameters();
		
		List<SameCompareChartModel> modelList = tourService.getCharts(
				nowCriteria, lastCriteria, startDate, null, currentMonth,
				pageMonthNum);
		String path = ServletActionContext.getServletContext().getRealPath("/")
				+ "reports/";
		FileBufferedOutputStream fbos = new FileBufferedOutputStream();
		JRBeanCollectionDataSource dataSource = null;
		dataSource = new JRBeanCollectionDataSource(modelList);
		JRDocxExporter exporter = new JRDocxExporter(
				DefaultJasperReportsContext.getInstance());
		JasperPrint jasperPrint = JasperFillManager.fillReport(path
				+ "tour_sameCompare_chart.jasper", parameters, dataSource);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);
		exporter.exportReport();
		fbos.close();
		String fileName = new String("镇域旅游发展同比.docx".getBytes("GBK"),
				"ISO8859_1");
		response.setCharacterEncoding("UTF-8");
		;
		response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ fileName);
		response.setContentLength(fbos.size());
		ServletOutputStream ouputStream = response.getOutputStream();

		fbos.writeData(ouputStream);
		fbos.dispose();
		ouputStream.flush();

		ouputStream.close();
		fbos.close();
		fbos.dispose();
	}
	public void districtWordReport() throws Exception {
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		u=baseService.get(User.class, u.getId());
		if (null!=townId) {
			parameters.put("deptName",baseService.get(Dept.class, townId).getText());
		}else{
			parameters.put("deptName", u.getDept().getText());
		}
		List<Long> userIds = new ArrayList<Long>();
		u = baseService.get(User.class, u.getId());
		List<Dept> list = u.getDept().getChildren();// 获得区下面的所有镇
		if (null!=townId) {
			for (Dept dept : list) {
				if (dept.getId().equals(townId)) {
					List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
					for (Dept factory : factyList) {
						for (User tempU : factory.getUsers()) {// 企业中所有用户
							userIds.add(tempU.getId());
						}
					}
				}

			}
		}else{
			for (Dept dept : list) {
				List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
				for (Dept factory : factyList) {
					for (User tempU : factory.getUsers()) {// 企业中所有用户
						userIds.add(tempU.getId());
					}
				}

			}
		}
		endDate=startDate;
		List<SameCompareModel> modelList=tourService.findSameCompare(userIds, startDate, endDate);
		String path = ServletActionContext.getServletContext().getRealPath("/")
				+ "reports/";
		FileBufferedOutputStream fbos = new FileBufferedOutputStream();
		JRBeanCollectionDataSource dataSource = null;
		dataSource = new JRBeanCollectionDataSource(modelList);
		JRDocxExporter exporter = new JRDocxExporter(
				DefaultJasperReportsContext.getInstance());
		JasperPrint jasperPrint = JasperFillManager.fillReport(path
				+ "sameCompareMonthReport.jasper", parameters, dataSource);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);
		exporter.exportReport();
		fbos.close();
		String fileName = new String("镇域旅游发展同比.docx".getBytes("GBK"),
				"ISO8859_1");
		response.setCharacterEncoding("UTF-8");
		;
		response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ fileName);
		response.setContentLength(fbos.size());
		ServletOutputStream ouputStream = response.getOutputStream();

		fbos.writeData(ouputStream);
		fbos.dispose();
		ouputStream.flush();

		ouputStream.close();
		fbos.close();
		fbos.dispose();
	}
	/**
	 * 区同比html报表
	 */
	public String districtHtmlReport() {
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		u=baseService.get(User.class, u.getId());
		if (null!=townId) {
			parameters.put("deptName",baseService.get(Dept.class, townId).getText());
		}else{
			parameters.put("deptName", u.getDept().getText());
		}
		List<Long> userIds = new ArrayList<Long>();
		u = baseService.get(User.class, u.getId());
		List<Dept> list = u.getDept().getChildren();// 获得区下面的所有镇
		if (null!=townId) {
			for (Dept dept : list) {
				if (dept.getId().equals(townId)) {
					List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
					for (Dept factory : factyList) {
						for (User tempU : factory.getUsers()) {// 企业中所有用户
							userIds.add(tempU.getId());
						}
					}
				}

			}
		}else{
			for (Dept dept : list) {
				List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
				for (Dept factory : factyList) {
					for (User tempU : factory.getUsers()) {// 企业中所有用户
						userIds.add(tempU.getId());
					}
				}

			}
		}
		endDate=startDate;
		List<SameCompareModel> modelList=tourService.findSameCompare(userIds, startDate, endDate);
		ActionContext.getContext().put("myList", modelList);

		return "html";
	}
	/**
	 * 区同比html图表
	 */
	public String districtHtmlChart() {

		DetachedCriteria nowCriteria = DetachedCriteria
				.forClass(TourCommon.class);
		districtAddCondition(nowCriteria);
		nowCriteria.add(Restrictions.eq("status",
				StatusEnum.reported.getValue()));
		addParameters();
		DetachedCriteria lastCriteria = DetachedCriteria
				.forClass(TourCommon.class);
		districtAddCondition(lastCriteria);
		lastCriteria.add(Restrictions.eq("status",
				StatusEnum.reported.getValue()));

		List<SameCompareChartModel> modelList = tourService.getCharts(
				nowCriteria, lastCriteria, startDate, null, currentMonth,
				pageMonthNum);
		ActionContext.getContext().put("myList", modelList);

		return "html";
	}

	/**
	 * 区同比图表
	 */
	public String districtChart() {
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

		List<SameCompareChartModel> modelList = tourService.getCharts(
				nowCriteria, lastCriteria, startDate, endDate, currentMonth,
				pageMonthNum);
		ActionContext.getContext().put("myList", modelList);

		return "html";
	}

	/**
	 * 导出数据
	 */

	/**
	 * 镇统计导出xls或html
	 * 
	 * @return
	 */
	public String townExportXlsOaHtml() {
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		u = baseService.get(User.class, u.getId());
		List<StatisticReportModel> modelList = tourService.getReportList(
				startDate, endDate, u.getDept().getChildren());
		ActionContext.getContext().put("myList", modelList);

		addParameters();

		return reprotType;
	}

	/**
	 * 导出时设定参数
	 */
	public void addParameters() {
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		u = baseService.get(User.class, u.getId());
		String reportDate = "";
		reportDate = new SimpleDateFormat("yyyy年MM月").format(TimeUtil
				.getTimeInMillis("上个月"));
		if (null != startDate && startDate.trim().length() != 0
				&& endDate != null && endDate.trim().length() != 0) {
			reportDate = startDate + "--" + endDate;
		} else if (((null != startDate && startDate.trim().length() != 0) && (null == endDate || endDate
				.trim().length() == 0))) {
			reportDate = startDate;
		} else if ((null == startDate || startDate.trim().length() == 0)
				&& (null != endDate && endDate.trim().length() != 0)) {
			reportDate = endDate;
		}
		if (null!=townId) {
			parameters.put("deptName",baseService.get(Dept.class, townId).getText());
		}else{
			parameters.put("deptName", u.getDept().getText());
		}
		parameters.put("reportDate", reportDate);
		
	}

	/**
	 * 镇导出word
	 * 
	 * @throws Exception
	 */
	public void townExportWord() throws Exception {
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		u = baseService.get(User.class, u.getId());
		List<StatisticReportModel> modelList = tourService.getReportList(
				startDate, endDate, u.getDept().getChildren());

		String path = ServletActionContext.getServletContext().getRealPath("/")
				+ "reports/";
		// 报表参数
		addParameters();

		FileBufferedOutputStream fbos = new FileBufferedOutputStream();
		JRBeanCollectionDataSource dataSource = null;
		dataSource = new JRBeanCollectionDataSource(modelList);
		JRDocxExporter exporter = new JRDocxExporter(
				DefaultJasperReportsContext.getInstance());
		JasperPrint jasperPrint = JasperFillManager.fillReport(path
				+ "tour_statistic_report.jasper", parameters, dataSource);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);
		exporter.exportReport();
		fbos.close();
		String fileName = new String("镇域旅游发展.docx".getBytes("GBK"), "ISO8859_1");
		response.setCharacterEncoding("UTF-8");
		;
		response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ fileName);
		response.setContentLength(fbos.size());
		ServletOutputStream ouputStream = response.getOutputStream();

		fbos.writeData(ouputStream);
		fbos.dispose();
		ouputStream.flush();

		ouputStream.close();
		fbos.close();
		fbos.dispose();

	}

	/**
	 * 区导出word
	 * 
	 * @throws Exception
	 */
	public void districtExportWord() throws Exception {
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		u = baseService.get(User.class, u.getId());
		List<Dept> deptList = new ArrayList<Dept>();
		if (null!=townId) {
			for (Dept tempDept : u.getDept().getChildren()) {
				deptList.addAll(tempDept.getChildren());
			}
		}else{
			for (Dept tempDept : u.getDept().getChildren()) {
				if (tempDept.getId().equals(townId)) {
					deptList.addAll(tempDept.getChildren());
					break;
				}
			}
		}
		
		List<StatisticReportModel> modelList = tourService.getReportList(
				startDate, endDate, deptList);
		// 报表参数
		String path = ServletActionContext.getServletContext().getRealPath("/")
				+ "reports/";

		addParameters();
		String fileName = new String("镇域旅游发展.docx".getBytes("GBK"), "ISO8859_1");
		FileBufferedOutputStream fbos = new FileBufferedOutputStream();
		JRBeanCollectionDataSource dataSource = null;
		dataSource = new JRBeanCollectionDataSource(modelList);
		JRDocxExporter exporter = new JRDocxExporter(
				DefaultJasperReportsContext.getInstance());
		JasperPrint jasperPrint = JasperFillManager.fillReport(path
				+ "tour_statistic_report.jasper", parameters, dataSource);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);
		exporter.exportReport();
		fbos.close();

		response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ fileName);
		response.setContentLength(fbos.size());
		ServletOutputStream ouputStream = response.getOutputStream();

		fbos.writeData(ouputStream);
		fbos.dispose();
		ouputStream.flush();

		ouputStream.close();
		fbos.close();
		fbos.dispose();

	}

	/**
	 * 区统计导出xls或html
	 * 
	 * @return
	 */
	public String districtExportXlsOaHtml() {
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		u = baseService.get(User.class, u.getId());
		List<Dept> deptList = new ArrayList<Dept>();
		if (null!=townId) {
			for (Dept tempDept : u.getDept().getChildren()) {
				if (tempDept.getId().equals(townId)) {
					deptList.addAll(tempDept.getChildren());
					break;
				}
			}
		}else{
			for (Dept tempDept : u.getDept().getChildren()) {
				deptList.addAll(tempDept.getChildren());
			}
		}
		
		List<StatisticReportModel> modelList = tourService.getReportList(
				startDate, endDate, deptList);
		ActionContext.getContext().put("myList", modelList);

		addParameters();

		return reprotType;
	}

	/**
	 * 获取下载文件显示名称（客户端名称）
	 * 
	 * @return 下载文件显示名称
	 * @throws Exception
	 */
	public String getExportFileName() throws Exception {
		String downFileName = "";
		if (reprotType.equals("xls")) {
			downFileName = "镇域旅游发展.xls";
		}
		try {
			downFileName = new String(downFileName.getBytes(), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {

		}

		return downFileName;
	}
	
	/**
	 * 查询指定企业条件
	 * @param criteria
	 */
	private void findByCompany(DetachedCriteria criteria, Long companyId) {
		criteria.createAlias("u.dept", "d");
		criteria.add(Restrictions.eq("d.id", companyId));				
	}

	@Override
	public void beforFind(DetachedCriteria criteria) {
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		u = baseService.get(User.class, u.getId());
		
		criteria.createAlias("user", "u");
		//镇查询企业
		if (null != companyId) {
			findByCompany(criteria, companyId);
		}
		if (model.getStatisticType().equals( // 企业
				StatisticTypeEnum.factory.getValue())) {
			findByCompany(criteria, u.getDept().getId());
			criteria.add(Restrictions.eq("status", model.getStatus()));

		} else if (model.getStatisticType().equals(
				StatisticTypeEnum.town.getValue())) { // 镇政府
			List<Dept> list = u.getDept().getChildren();// 获得政府下的所有企业
			findByTown(criteria, list);
			
			criteria.add(Restrictions.eq("status",
					StatusEnum.reported.getValue()));
		} else if (model.getStatisticType().equals(
				StatisticTypeEnum.district.getValue())) {
			//区查询镇
			if (null != townId) {
				Dept dept = baseService.get(Dept.class, townId);
				findByTown(criteria, dept.getChildren());
			} else {
				findByDistrict(criteria, u);
			}

			criteria.add(Restrictions.eq("status",
					StatusEnum.reported.getValue()));
		}
		if (null != deptType && deptType.trim().length() != 0) {
			criteria.add(Restrictions.eq("type", deptType));
		}
		criteria.addOrder(Order.desc("time"));
		addDataCondition(criteria);
	}
	
	/**
	 * 查询区里所有营业数据
	 * @param criteria
	 * @param u
	 */
	private void findByDistrict(DetachedCriteria criteria, User u) {
		List<Long> userIds = new ArrayList<Long>();
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
			criteria.add(Restrictions.in("u.id", userIds));
		}
	}
	
	/**
	 * 查询指定镇的数据
	 * @param criteria
	 * @param list：镇中所有企业
	 */
	private void findByTown(DetachedCriteria criteria, List<Dept> list) {
		List<Long> userIds = new ArrayList<Long>();
		for (Dept dept : list) {
			for (User tempU : dept.getUsers()) {// 企业中所有用户
				userIds.add(tempU.getId());
			}
		}
		if (userIds.size() != 0) {
			criteria.add(Restrictions.in("u.id", userIds));
		}
	}

	/**
	 * 检查用户这个月是否申报过
	 */
	public void checkUserIsReportThisMonth() {
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		DetachedCriteria criteria = DetachedCriteria.forClass(TourCommon.class);
		criteria.createAlias("user", "u");
		criteria.add(Restrictions.eq("u.id", u.getId()));
		 Long time=TimeUtil.getTimeInMillis(reprotYearAndMonth);
		criteria.add(Restrictions.eq("time",
				time));
		List<TourCommon> list = baseService.find(criteria);
		if (null != list && list.size() != 0) {
			getOut().print("已经申报");
		}

	}

	/**
	 * 给criteria添加时间条件
	 * 
	 * @param criteria
	 */
	public void addDataCondition(DetachedCriteria criteria) {
		if (null != startDate && startDate.trim().length() != 0) {
			criteria.add(Restrictions.ge("time",
					TimeUtil.getTimeInMillis(startDate)));
		}
		if (null != endDate && endDate.trim().length() != 0) {
			criteria.add(Restrictions.le("time",
					TimeUtil.getTimeInMillis(endDate)));
		}
	}

	@Override
	public void afterToUpdate(TourCommon e) {
		reprotYearAndMonth =new SimpleDateFormat("yyyy年MM月").format(e.getTime());
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

	/**
	 * 镇统计列表
	 * 
	 * @return
	 */
	public String townStatisticList() {
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		u = baseService.get(User.class, u.getId());
		DetachedCriteria criteria = DetachedCriteria.forClass(TourCommon.class);
		criteria.add(Restrictions.eq("status", StatusEnum.reported.getValue()));
		townAddCondition(criteria);
		addDataCondition(criteria);
		if (firstStatus==0) {
			firstStatus+=1;
			String strDate = new SimpleDateFormat("yyyy年MM月").format(TimeUtil
					.getTimeInMillis("默认是上月"));
			startDate = strDate;
			endDate = strDate;
			criteria.add(Restrictions.eq("time",
					TimeUtil.getTimeInMillis("默认是上月")));

		}
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
		if (null!=townId) {
			for (Dept dept : list) {
				if (dept.getId().equals(townId)) {
					List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
					for (Dept factory : factyList) {
						for (User tempU : factory.getUsers()) {// 企业中所有用户
							userIds.add(tempU.getId());
						}
					}
					break;
				}
	
			}
		}else{
			for (Dept dept : list) {
				List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
				for (Dept factory : factyList) {
					for (User tempU : factory.getUsers()) {// 企业中所有用户
						userIds.add(tempU.getId());
					}
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

	/**
	 * 区统计列表
	 * 
	 * @return
	 */
	public String districtStatisticList() {
		User user = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		user = baseService.get(User.class, user.getId());
		DetachedCriteria criteria = DetachedCriteria.forClass(TourCommon.class);
		criteria.add(Restrictions.eq("status", StatusEnum.reported.getValue()));
		districtAddCondition(criteria);
		addDataCondition(criteria);
		if (firstStatus==0) {
			firstStatus+=1;
			String strDate = new SimpleDateFormat("yyyy年MM月").format(TimeUtil
					.getTimeInMillis("默认是上月"));
			startDate = strDate;
			endDate = strDate;
			criteria.add(Restrictions.eq("time",
					TimeUtil.getTimeInMillis("默认是上月")));

		}
		List<Dept> deptList = new ArrayList<Dept>();
		for (Dept d1 : user.getDept().getChildren()) {
			deptList.addAll(d1.getChildren());
		}
		PageResult page = tourService.findStatistic(criteria, user.getId(),
				deptList);
		ActionContext.getContext().put(PAGE, page);

		return LIST;
	}

	/**
	 * 申报
	 * 
	 * @return
	 */
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

		if (null != u.getDept() && null != u.getDept().getFactoryType()
				&& null != u.getDept().getFactoryType().getOptions()) {
			ActionContext.getContext().put("factoryOptions",
					u.getDept().getFactoryType().getOptions());
		}
	}

	/**
	 * 镇同比
	 * 
	 * @return
	 */
	public String townSameCompare() {
		if (pageSize == 10) {
			pageSize = 1;
		}
		List<FactoryType> types = baseService.find(DetachedCriteria
				.forClass(FactoryType.class));
		ActionContext.getContext().put("typeNums", types.size());
		
		
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
		List<SameCompareModel> modelList=tourService.findSameCompare(userIds, startDate, endDate);
		List<SameCompareModel> resultList=new ArrayList<SameCompareModel>();
		
		if (null!=modelList&&modelList.size()!=0) {
			for (int i = (start * types.size()); i < (start * types.size() + (pageSize * types
					.size())); i++) {
				if (i>(modelList.size()-1)) {
					break;
				}
				resultList.add(modelList.get(i));
			}
		}
		PageResult page=new PageResult();
		if(null!=modelList&&modelList.size()!=0){
			page.setRowCount(modelList.size()/types.size());
		}
		page.setResult(resultList);
		ActionContext.getContext().put(PAGE, page);
		
		return LIST;
}

	/**
	 * 区同比
	 * 
	 * @return
	 */
	public String districtSameCompare() {

		if (pageSize == 10) {
			pageSize = 1;
		}
		List<FactoryType> types = baseService.find(DetachedCriteria
				.forClass(FactoryType.class));
		ActionContext.getContext().put("typeNums", types.size());
		
		
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		List<Long> userIds = new ArrayList<Long>();
		u = baseService.get(User.class, u.getId());
		List<Dept> list = u.getDept().getChildren();// 获得区下面的所有镇
		if (null!=townId) {
			for (Dept dept : list) {
				if (dept.getId().equals(townId)) {
					List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
					for (Dept factory : factyList) {
						for (User tempU : factory.getUsers()) {// 企业中所有用户
							userIds.add(tempU.getId());
						}
					}
					break;
				}
			}
		}else{
			for (Dept dept : list) {
				List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
				for (Dept factory : factyList) {
					for (User tempU : factory.getUsers()) {// 企业中所有用户
						userIds.add(tempU.getId());
					}
				}
	
			}
		}
		List<SameCompareModel> modelList=tourService.findSameCompare(userIds, startDate, endDate);
		List<SameCompareModel> resultList=new ArrayList<SameCompareModel>();
		
		if (null!=modelList&&modelList.size()!=0) {
			for (int i = (start * types.size()); i < (start * types.size() + (pageSize * types
					.size())); i++) {
				if (i>(modelList.size()-1)) {
					break;
				}
				resultList.add(modelList.get(i));
			}
		}
		PageResult page=new PageResult();
		if(null!=modelList&&modelList.size()!=0){
			page.setRowCount(modelList.size()/types.size());
		}
		page.setResult(resultList);
		ActionContext.getContext().put(PAGE, page);
		return LIST;
	}

	public static Double getPercent(Double now, Double last) {
		Double percent = 0d;
		if (last == 0d && now == 0d) {//两个都等于0
			return 0d;
		} else if (last == 0d && now != 0d) {//去年为0
			return 100d;
		}
		percent = (now - last) / last;
		if (percent.toString().equals("NaN")) {
			return 0d;
		}

		int i = 0;
		percent = percent * 100;
		i = percent.toString().indexOf(".");
		if (i != 0
				&& percent.toString()
						.substring(i + 1, percent.toString().length()).length() >= 3) {
			if (null != percent.toString().substring(i + 3, i + 4)
					&& Integer.parseInt(percent.toString().substring(i + 3,
							i + 4)) >= 5) {
				if (percent > 0) {
					percent += 0.01d;
				} else {
					percent -= 0.01d;
				}
			}
			percent = Double
					.parseDouble(percent.toString().substring(0, i + 3));
		}
		return percent;
	}
	/**
	 * 同比详情
	 * 
	 * @return
	 */
	public String sameCompareToDetail() throws Exception {
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
		tempReportDate=new String(tempReportDate.getBytes("ISO-8859-1"),"UTF-8");
		List<SameCompareDetailModel> modelDetails = new ArrayList<SameCompareDetailModel>();
		for (String str : detailNames) {
			SameCompareDetailModel tempDetail = new SameCompareDetailModel();
			Double nowMoney = 0d;
			Double lastMoney = 0d;
			String time = tempReportDate;
			for (TourCommon comm : nowList) {
				for (TourDetail d : comm.getDetails()) {
					if (str.equals(d.getName())) {
						nowMoney += d.getMoney();
					}
				}
			}
			for (TourCommon comm : lastList) {
				for (TourDetail d : comm.getDetails()) {
					if (str.equals(d.getName())) {
						lastMoney += d.getMoney();
					}
				}
			}
			Double p = 0d;
			p = getPercent(nowMoney, lastMoney);
			tempDetail.setPercent(p);
			tempDetail.setNowMoney(nowMoney);
			tempDetail.setLastMoney(lastMoney);
			tempDetail.setName(str);
			tempDetail.setTime(time);
			modelDetails.add(tempDetail);
		}

		ActionContext.getContext().put("sameCompareDetaiList", modelDetails);
		return "toDetail";
	}

	/**
	 * 镇统计详情
	 * 
	 * @return
	 */
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
					Double money = 0d;
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
		System.out.println("dddddddd");
		Long tempYear = Long.parseLong(reprotYearAndMonth.substring(0, 4)
				.trim());
		Integer tempMonth = Integer.parseInt(reprotYearAndMonth.substring(5, 7)
				.trim());
		if (tempMonth >= 2 && tempMonth <= 4) {
			model.setQuarter(1);
		}
		if (tempMonth >= 5 && tempMonth <= 7) {
			model.setQuarter(2);
		}
		if (tempMonth >= 8 && tempMonth <= 10) {
			model.setQuarter(3);
		}
		if (tempMonth >= 11 || tempMonth == 1) {
			model.setQuarter(4);
		}
		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		u = baseService.get(User.class, u.getId());
		if (null!=u.getDept()&&null!=u.getDept().getFactoryType()) {
			model.setType(u.getDept().getFactoryType().getName());
		}
		model.setReportDate(new Date());
		model.setReportYear(Integer.parseInt(reprotYearAndMonth.substring(0, 4)
				.trim()));
		model.setReportMonth(Integer.parseInt(reprotYearAndMonth
				.substring(5, 7).trim()));
		model.setTime(TimeUtil.getTimeInMillis(reprotYearAndMonth));//
		Double totalIncome = 0d;
		if (null==model.getId()) {
			for(Double d:inputMoneys){
				totalIncome+=d;
			}
		}
		if (null == beans || beans.size() == 0) {
			model.setUser(u);
			model.setStatus(StatusEnum.notReport.getValue());
		} else {
			for (TourDetail d : beans) {
				if (null != d && null != d.getId()) {
					totalIncome+=d.getMoney();
					TourDetail tempDetail = baseService.get(TourDetail.class,
							d.getId());
					tempDetail.setMoney(d.getMoney());
					baseService.update(tempDetail);
				}
			}
		}
		BigDecimal big=new BigDecimal(totalIncome);
		model.setTotalIncome(big.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
	}

	/**
	 * 列表详情
	 * 
	 * @return
	 */
	public String toDetail() {
		ActionContext.getContext().getValueStack()
				.push(baseService.get(TourCommon.class, model.getId()));
		return "toDetail";
	}

	@Override
	public void afterSave(TourCommon m) {
		if (null == beans || beans.size() == 0) {
			for (int i = 0; i < inputMoneys.length; i++) {
				Double temp = inputMoneys[i];
				String lbl = labelTexts[i];
				if (null == temp) {
					temp = 0d;
				}
				TourDetail detail = new TourDetail();
				detail.setCommon(m);
				detail.setMoney(temp);
				detail.setName(lbl);
				baseService.add(detail);
			}
		}

	}
	public String lastQuarterSameCompareList(){
		if (pageSize == 10) {
			pageSize = 1;
		}
		List<FactoryType> types = baseService.find(DetachedCriteria
				.forClass(FactoryType.class));
		User u=(User)ActionContext.getContext().getSession().get(SystemConstant.CURRENT_USER);
		u=baseService.get(User.class, u.getId());
		List<Long> userIds = new ArrayList<Long>();
		Dept d=u.getDept();
		if (d.getDeptLevel().equals("镇级")) {
			List<Dept> list = u.getDept().getChildren();// 获得政府下的所有企业
			for (Dept dept : list) {
				for (User tempU : dept.getUsers()) {// 企业中所有用户
					userIds.add(tempU.getId());
				}
			}
		}else if(d.getDeptLevel().equals("区级")){
			List<Dept> list = u.getDept().getChildren();// 获得区下面的所有镇
			for (Dept dept : list) {
				List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
				for (Dept factory : factyList) {
					for (User tempU : factory.getUsers()) {// 企业中所有用户
						userIds.add(tempU.getId());
					}
				}
			}
		}else{
			return "noData";
		}

		
		List<Integer> qu = new ArrayList<Integer>();
		if (null != quarters && quarters.length != 0) {
			for (int i : quarters) {
				qu.add(i);
			}
		}
		int tempStartYear=0;
		int tempEndYear=0;
		if (null!=startYear) {
			tempStartYear=startYear;
		}
		if (null!=endYear) {
			tempEndYear=endYear;
		}
		List<SameCompareModel> modelList = tourService
				.getQuarterSameCompareModels(userIds, tempStartYear, tempEndYear, qu);// 全部数据
		List<SameCompareModel> resultList = new ArrayList<SameCompareModel>(); // 一页数据

		if (null!=modelList&&modelList.size()!=0) {
			for (int i = (start * types.size()); i < (start * types.size() + (pageSize * types
					.size())); i++) {
				if (i>(modelList.size()-1)) {
					break;
				}
				resultList.add(modelList.get(i));
			}
		}
		PageResult page = new PageResult();
		if (null!=modelList&&modelList.size()!=0) {
			page.setRowCount(modelList.size() / types.size());
		}
		page.setResult(resultList);
		ActionContext.getContext().put(PAGE, page);
		return "lastQuarterSameCompareList";
	}
	public String lastMonthSameCompareList(){
		User u=(User)ActionContext.getContext().getSession().get(SystemConstant.CURRENT_USER);
		u=baseService.get(User.class, u.getId());
		List<Long> userIds = new ArrayList<Long>();
		Dept d=u.getDept();
		if (d.getDeptLevel().equals("镇级")) {
			List<Dept> list = u.getDept().getChildren();// 获得政府下的所有企业
			for (Dept dept : list) {
				for (User tempU : dept.getUsers()) {// 企业中所有用户
					userIds.add(tempU.getId());
				}
			}
		}else if(d.getDeptLevel().equals("区级")){
			List<Dept> list = u.getDept().getChildren();// 获得区下面的所有镇
			for (Dept dept : list) {
				List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
				for (Dept factory : factyList) {
					for (User tempU : factory.getUsers()) {// 企业中所有用户
						userIds.add(tempU.getId());
					}
				}
			}
		}else{
			return "noData";
		}

		Calendar calendar=Calendar.getInstance();
		calendar.add(calendar.MONTH, -1);
		startDate=calendar.get(calendar.YEAR)+"年"+(calendar.get(calendar.MONTH)+1)+"月";//上个月
		endDate=startDate;
		System.out.println(startDate);
		List<SameCompareModel> modelList=tourService.findSameCompare(userIds, startDate, endDate);
		PageResult page=new PageResult();
		page.setResult(modelList);
		ActionContext.getContext().put(PAGE, page);
		return "lastMonthSameCompareList";
	}
	public String last5ReportedList(){
		pageSize=5;
		User u=(User)ActionContext.getContext().getSession().get(SystemConstant.CURRENT_USER);
		u=baseService.get(User.class, u.getId());
		List<Long> userIds = new ArrayList<Long>();
		Dept d=u.getDept();
		if (d.getDeptLevel().equals("镇级")) {
			List<Dept> list = u.getDept().getChildren();// 获得政府下的所有企业
			for (Dept dept : list) {
				for (User tempU : dept.getUsers()) {// 企业中所有用户
					userIds.add(tempU.getId());
				}
			}
		}else if(d.getDeptLevel().equals("区级")){
			List<Dept> list = u.getDept().getChildren();// 获得区下面的所有镇
			for (Dept dept : list) {
				List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
				for (Dept factory : factyList) {
					for (User tempU : factory.getUsers()) {// 企业中所有用户
						userIds.add(tempU.getId());
					}
				}
			}
		}else{
			return "noData";
		}
		DetachedCriteria detachedCriteria=DetachedCriteria.forClass(TourCommon.class);
		detachedCriteria.createAlias("user", "u");
		detachedCriteria.add(Restrictions.in("u.id", userIds));
		detachedCriteria.add(Restrictions.eq("status", StatusEnum.reported.getValue()));
		detachedCriteria.addOrder(Order.desc("time"));
		List<TourCommon> list=baseService.find(detachedCriteria);
		PageResult page=new PageResult();
		if (null!=list&&list.size()>5) {
			List<TourCommon> tempList=new ArrayList<TourCommon>();
			for(int i=0;i<5;i++){
				tempList.add(list.get(i));
			}
			page.setResult(tempList);
		}else{
			page.setResult(list);
		}
		
		
		ActionContext.getContext().put(PAGE, page);
		return "last5ReportedList";
	}
	public String lastMonthStatisticList(){
		User u=(User)ActionContext.getContext().getSession().get(SystemConstant.CURRENT_USER);
		u=baseService.get(User.class, u.getId());
		List<Dept> factoryList=new ArrayList<Dept>();
		Dept d=u.getDept();
		if (d.getDeptLevel().equals("镇级")) {
			List<Dept> list = u.getDept().getChildren();// 获得政府下的所有企业
			factoryList=list;
		}else if(d.getDeptLevel().equals("区级")){
			List<Dept> list = u.getDept().getChildren();// 获得区下面的所有镇
			for (Dept dept : list) {
				List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
				factoryList.addAll(factyList);
			}
		}else{
			return "noData";
		}
		
		DetachedCriteria criteria = DetachedCriteria.forClass(TourCommon.class);
		criteria.add(Restrictions.eq("status", StatusEnum.reported.getValue()));
		criteria.add(Restrictions.eq("time", TimeUtil.getTimeInMillis("上个月")));
		Long l=TimeUtil.getTimeInMillis("上个月");
		PageResult page = tourService.findStatistic(criteria, u.getId(), factoryList);
		ActionContext.getContext().put(PAGE, page);
		return "lastMonthStatisticList";
	}
	public String quarterChart() {

		return reprotType;
	}

	public String townQuarterSameCompare() {
		if (pageSize == 10) {
			pageSize = 1;
		}
		List<FactoryType> types = baseService.find(DetachedCriteria
				.forClass(FactoryType.class));
		ActionContext.getContext().put("typeNums", types.size());

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
		List<Integer> qu = new ArrayList<Integer>();
		if (null != quarters && quarters.length != 0) {
			for (int i : quarters) {
				qu.add(i);
			}
		}
		int tempStartYear=0;
		int tempEndYear=0;
		if (null!=startYear) {
			tempStartYear=startYear;
		}
		if (null!=endYear) {
			tempEndYear=endYear;
		}
		List<SameCompareModel> modelList = tourService
				.getQuarterSameCompareModels(userIds, tempStartYear, tempEndYear, qu);// 全部数据
		List<SameCompareModel> resultList = new ArrayList<SameCompareModel>(); // 一页数据

		if (null!=modelList&&modelList.size()!=0) {
			for (int i = (start * types.size()); i < (start * types.size() + (pageSize * types
					.size())); i++) {
				if (i>(modelList.size()-1)) {
					break;
				}
				resultList.add(modelList.get(i));
			}
		}
		PageResult page = new PageResult();
		if(null!=modelList&&modelList.size()!=0){
			page.setRowCount(modelList.size()/types.size());
		}
		page.setResult(resultList);
		ActionContext.getContext().put(PAGE, page);
		return LIST;
	}

	public String districtQuarterSameCompare() {
		if (pageSize == 10) {
			pageSize = 1;
		}
		List<FactoryType> types = baseService.find(DetachedCriteria
				.forClass(FactoryType.class));
		ActionContext.getContext().put("typeNums", types.size());

		User u = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		List<Long> userIds = new ArrayList<Long>();
		u = baseService.get(User.class, u.getId());
		List<Dept> list = u.getDept().getChildren();// 获得区下面的所有镇
		if (null!=townId) {
			for (Dept dept : list) {
				if (dept.getId().equals(townId)) {
					List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
					for (Dept factory : factyList) {
						for (User tempU : factory.getUsers()) {// 企业中所有用户
							userIds.add(tempU.getId());
						}
					}
					break;
				}
			}
		}else{
			for (Dept dept : list) {
				List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
				for (Dept factory : factyList) {
					for (User tempU : factory.getUsers()) {// 企业中所有用户
						userIds.add(tempU.getId());
					}
				}

			}
		}
		
		List<Integer> qu = new ArrayList<Integer>();
		if (null != quarters && quarters.length != 0) {
			for (int i : quarters) {
				qu.add(i);
			}
		}
		int tempStartYear=0;
		int tempEndYear=0;
		if (null!=startYear) {
			tempStartYear=startYear;
		}
		if (null!=endYear) {
			tempEndYear=endYear;
		}
		List<SameCompareModel> modelList = tourService
				.getQuarterSameCompareModels(userIds, tempStartYear, tempEndYear, qu);// 全部数据
		List<SameCompareModel> resultList = new ArrayList<SameCompareModel>(); // 一页数据

		if (null!=modelList&&modelList.size()!=0) {
			for (int i = (start * types.size()); i < (start * types.size() + (pageSize * types
					.size())); i++) {
				if (i>(modelList.size()-1)) {
					break;
				}
				resultList.add(modelList.get(i));
			}
		}
		PageResult page = new PageResult();
		if(null!=modelList&&modelList.size()!=0){
			page.setRowCount(modelList.size()/types.size());
		}
		page.setResult(resultList);
		ActionContext.getContext().put(PAGE, page);
		return LIST;
	}

	public List<FactoryType> getAllFactoryType() {
		List<FactoryType> list = new ArrayList<FactoryType>();
		DetachedCriteria criteria = DetachedCriteria
				.forClass(FactoryType.class);
		list = baseService.find(criteria);
		return list;

	}

	public String[] getLabelTexts() {
		return labelTexts;
	}

	public void setLabelTexts(String[] labelTexts) {
		this.labelTexts = labelTexts;
	}

	public Double[] getInputMoneys() {
		return inputMoneys;
	}

	public void setInputMoneys(Double[] inputMoneys) {
		this.inputMoneys = inputMoneys;
	}

	public String getReprotYearAndMonth() {
		return reprotYearAndMonth;
	}

	public void setReprotYearAndMonth(String reprotYearAndMonth) {
		this.reprotYearAndMonth = reprotYearAndMonth;
	}

	public static void main(String[] args) {
//		System.out.println(TimeUtil.getTimeInMillis("2013年11月"));
//		System.out.println(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss:SS").format(new Long("1385870400000")));
//		System.out.println(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss:SS").format(new Long("1383235200000")));
//		System.out.println(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss:SS").format(new Long("1383278400000")));
//		
		Long longTime=TimeUtil.getTimeInMillis("asdfasdf");
		System.out.println(longTime);
		System.out.println(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss:SS").format(longTime));
		
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

	public String[] getTempTourIds() {
		return tempTourIds;
	}

	public void setTempTourIds(String[] tempTourIds) {
		this.tempTourIds = tempTourIds;
	}

	public String getReprotType() {
		return reprotType;
	}

	public void setReprotType(String reprotType) {
		this.reprotType = reprotType;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public Integer[] getQuarters() {
		return quarters;
	}

	public void setQuarters(Integer[] quarters) {
		this.quarters = quarters;
	}

	public Integer getStartYear() {
		return startYear;
	}

	public void setStartYear(Integer startYear) {
		this.startYear = startYear;
	}

	public Integer getEndYear() {
		return endYear;
	}

	public void setEndYear(Integer endYear) {
		this.endYear = endYear;
	}

	public Integer getFirstStatus() {
		return firstStatus;
	}

	public void setFirstStatus(Integer firstStatus) {
		this.firstStatus = firstStatus;
	}
	public Long getFactoryTypeId() {
		return factoryTypeId;
	}
	public void setFactoryTypeId(Long factoryTypeId) {
		this.factoryTypeId = factoryTypeId;
	}
	public String getTempReportDate() {
		return tempReportDate;
	}
	public void setTempReportDate(String tempReportDate) {
		this.tempReportDate = tempReportDate;
	}
	public String getRedirectAddress() {
		return redirectAddress;
	}
	public void setRedirectAddress(String redirectAddress) {
		this.redirectAddress = redirectAddress;
	}
	
	/**
	 * 查询镇的企业：用于下拉框
	 * @return
	 */
	public List<Dept> getTownCompany() {
		User currentUser = SecurityHolder.getCurrentUser();
		User u = baseService.get(User.class, currentUser.getId());
		return u.getDept().getChildren();
	}
	
	/**
	 * TODO 如果系统处理多个区，这里代码有错误
	 * 查询区得企业：用于下拉框
	 * @return
	 */
	public List<Dept> getDistrictCompany() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Dept.class);
		criteria.add(Restrictions.eq("deptLevel", DeptLevelEnum.company.getValue()));
		return baseService.find(criteria);
	}	
	
	/**
	 * TODO 如果系统处理多个区，这里代码有错误
	 * 查询区的镇
	 * @return
	 */
	public List<Dept> getDistrictTown() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Dept.class);
		criteria.add(Restrictions.eq("deptLevel", DeptLevelEnum.town.getValue()));
		return baseService.find(criteria);
	}		
	
	
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public Long getTownId() {
		return townId;
	}
	public void setTownId(Long townId) {
		this.townId = townId;
	}
	public Date getTempDate() {
		return tempDate;
	}
	public void setTempDate(Date tempDate) {
		this.tempDate = tempDate;
	}
	
}
