package com.lxs.oa.tour.action;

import java.io.UnsupportedEncodingException;
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
import com.lxs.oa.tour.common.StatisticTypeEnum;
import com.lxs.oa.tour.common.StatusEnum;
import com.lxs.oa.tour.domain.TourCommon;
import com.lxs.oa.tour.domain.TourDetail;
import com.lxs.oa.tour.pageModel.SameCompareChartModel;
import com.lxs.oa.tour.pageModel.SameCompareDetailModel;
import com.lxs.oa.tour.pageModel.SameCompareModel;
import com.lxs.oa.tour.pageModel.StatisticReportModel;
import com.lxs.oa.tour.service.ITourService;
import com.lxs.security.domain.Dept;
import com.lxs.security.domain.User;
import com.lxs.tour.domain.FactoryType;
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
		@Action(value = "districtChart", className = "tourAction", results = { @Result(name = "list", location = "/WEB-INF/jsp/tour/town/list.jsp") }),
		@Action(value = "townQuarterSameCompare", className = "tourAction", results = {
				@Result(name = "list", location = "/WEB-INF/jsp/tour/town/quarterSameCompareList.jsp"),
				@Result(name = "toSelectChart", location = "/WEB-INF/jsp/tour/town/quarterSelectChart.jsp") }),
		@Action(value = "districtQuarterSameCompare", className = "tourAction", results = {
				@Result(name = "list", location = "/WEB-INF/jsp/tour/district/quarterSameCompareList.jsp"),
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

	@Resource
	private ITourService tourService;
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
	private Integer startYear = 0;
	private Integer endYear = 0;

	/**
	 * 图表
	 */

	public String toSelectChart() {
		startYear=Calendar.getInstance().get(Calendar.YEAR);
		return "toSelectChart";
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
		for (Dept dept : list) {
			List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
			for (Dept factory : factyList) {
				for (User tempU : factory.getUsers()) {// 企业中所有用户
					userIds.add(tempU.getId());
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
		parameters.put("deptName", u.getDept().getText());
		parameters.put("reportDate", reportDate);
		List<Integer> qu = new ArrayList<Integer>();
		if (null != quarters && quarters.length != 0) {
			for (int i : quarters) {
				qu.add(i);
			}
		}
		List<SameCompareChartModel> modelList = tourService.getQuarterCharts(
				userIds, startYear, endYear, qu);

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

	/**
	 * 区季度同比html图表
	 */
	public String districtQuarterHtmlChart() {
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
		List<SameCompareChartModel> modelList = tourService.getQuarterCharts(
				userIds, startYear, endYear, qu);

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
		List<SameCompareChartModel> modelList = tourService.getQuarterCharts(
				userIds, startYear, endYear, qu);

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
		List<SameCompareChartModel> modelList = tourService.getQuarterCharts(
				userIds, startYear, endYear, qu);
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

		parameters.put("reportDate", reportDate);
		parameters.put("deptName", u.getDept().getText());
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
		for (Dept tempDept : u.getDept().getChildren()) {
			deptList.addAll(tempDept.getChildren());
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
		for (Dept tempDept : u.getDept().getChildren()) {
			deptList.addAll(tempDept.getChildren());
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
			criteria.add(Restrictions.eq("status",
					StatusEnum.reported.getValue()));
		} else if (model.getStatisticType().equals(
				StatisticTypeEnum.district.getValue())) {
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
		criteria.addOrder(Order.desc("time"));
		addDataCondition(criteria);
		Calendar calendar = Calendar.getInstance();
		// startDate = calendar.get(calendar.YEAR) + "年"
		// + (calendar.get(calendar.MONTH)) + "月";
		// endDate = calendar.get(calendar.YEAR) + "年"
		// + (calendar.get(calendar.MONTH)) + "月";
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
		criteria.add(Restrictions.eq("time",
				TimeUtil.getTimeInMillis(reprotYearAndMonth)));
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
		if ((null == startDate || startDate.trim().length() == 0)
				&& null == endDate || endDate.trim().length() == 0) {
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
		if ((null == startDate || startDate.trim().length() == 0)
				&& null == endDate || endDate.trim().length() == 0) {
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
		PageResult page = tourService.findSameCompare(userIds, startDate,
				endDate, currentMonth, pageMonthNum);
		ActionContext.getContext().put(PAGE, page);
		if ((null == startDate || startDate.trim().length() == 0)
				&& null == endDate || endDate.trim().length() == 0) {
			String strDate = new SimpleDateFormat("yyyy年MM月").format(TimeUtil
					.getTimeInMillis("默认是上月"));
			startDate = strDate;
			endDate = strDate;

		}
		return LIST;
	}

	/**
	 * 区同比
	 * 
	 * @return
	 */
	public String districtSameCompare() {

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
		PageResult page = tourService.findSameCompare(userIds, startDate,
				endDate, currentMonth, pageMonthNum);
		ActionContext.getContext().put(PAGE, page);
		if ((null == startDate || startDate.trim().length() == 0)
				&& null == endDate || endDate.trim().length() == 0) {
			String strDate = new SimpleDateFormat("yyyy年MM月").format(TimeUtil
					.getTimeInMillis("默认是上月"));
			startDate = strDate;
			endDate = strDate;

		}
		return LIST;
	}

	public static Double getPercent(Double now, Double last) {
		Double percent = 0d;
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
			Double nowMoney = 0d;
			Double lastMoney = 0d;
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
		model.setType(u.getDept().getFactoryType().getName());
		model.setReportDate(new Date());
		model.setReportYear(Integer.parseInt(reprotYearAndMonth.substring(0, 4)
				.trim()));
		model.setReportMonth(Integer.parseInt(reprotYearAndMonth
				.substring(5, 7).trim()));
		model.setTime(TimeUtil.getTimeInMillis(reprotYearAndMonth));//
		Double totalIncome = 0d;
		for (Double d : inputMoneys) {
			totalIncome += d;
		}
		model.setTotalIncome(totalIncome);
		if (null == beans || beans.size() == 0) {
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
		List<SameCompareModel> modelList = tourService
				.getQuarterSameCompareModels(userIds, startYear, endYear, qu);// 全部数据
		List<SameCompareModel> resultList = new ArrayList<SameCompareModel>(); // 一页数据

		for (int i = (start * types.size()); i < (start * types.size() + (pageSize * types
				.size())); i++) {
			resultList.add(modelList.get(i));
		}
		PageResult page = new PageResult();
		page.setRowCount(modelList.size() / types.size());
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
		for (Dept dept : list) {
			List<Dept> factyList = dept.getChildren();// 获得镇下面的所有公司
			for (Dept factory : factyList) {
				for (User tempU : factory.getUsers()) {// 企业中所有用户
					userIds.add(tempU.getId());
				}
			}

		}
		List<Integer> qu = new ArrayList<Integer>();
		if (null != quarters && quarters.length != 0) {
			for (int i : quarters) {
				qu.add(i);
			}
		}
		List<SameCompareModel> modelList = tourService
				.getQuarterSameCompareModels(userIds, startYear, endYear, qu);// 全部数据
		List<SameCompareModel> resultList = new ArrayList<SameCompareModel>(); // 一页数据

		for (int i = (start * types.size()); i < (start * types.size() + (pageSize * types
				.size())); i++) {
			resultList.add(modelList.get(i));
		}
		PageResult page = new PageResult();
		page.setRowCount(modelList.size() / types.size());
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
		Calendar calendar = Calendar.getInstance();
		int date = Integer.parseInt("-" + calendar.get(calendar.DATE));
		int hour = Integer.parseInt("-" + calendar.get(calendar.HOUR));
		int minute = Integer.parseInt("-" + calendar.get(calendar.MINUTE));
		int second = Integer.parseInt("-" + calendar.get(calendar.SECOND));
		int mi = Integer.parseInt("-" + calendar.get(calendar.MILLISECOND));

		calendar.add(calendar.MILLISECOND, mi);
		calendar.add(calendar.SECOND, second);
		calendar.add(calendar.MINUTE, minute);
		calendar.add(calendar.HOUR, hour);
		calendar.add(calendar.DATE, date);

		System.out.println(calendar.get(calendar.DATE));
		String strDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(calendar.getTime());
		System.out.println(strDate);
		System.out.println("---------------------");

		Long l1 = TimeUtil.getTimeInMillis("2013年11月");
		String strDate1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(l1);
		System.out.println(1l);
		System.out.println(strDate1);

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

}
