package com.lxs.oa.tour.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.lxs.core.common.TimeUtil;
import com.lxs.core.common.page.PageResult;
import com.lxs.oa.tour.dao.ITourDao;
import com.lxs.oa.tour.domain.TourCommon;
import com.lxs.oa.tour.pageModel.SameCompareChartModel;
import com.lxs.oa.tour.pageModel.SameCompareModel;
import com.lxs.oa.tour.pageModel.StatisticModel;
import com.lxs.oa.tour.pageModel.StatisticReportModel;
import com.lxs.security.domain.Dept;
import com.lxs.security.domain.User;
import com.lxs.tour.domain.FactoryType;


@Repository
public class TourDaoImpl implements ITourDao {
	@Resource
	private SessionFactory sessionFactory;

	@Override
	public PageResult findStatistic(DetachedCriteria criteria, Long userId,
			List<Dept> deptList) {
		Criteria factoryTypeCriteria=sessionFactory.getCurrentSession().createCriteria(FactoryType.class);
		List<FactoryType> typeList=factoryTypeCriteria.list();
		
		PageResult page = new PageResult();
		Criteria c = criteria.getExecutableCriteria(sessionFactory
				.getCurrentSession());

		// 总行数
		long rowCount = (Long) c.setProjection(Projections.rowCount())
				.uniqueResult();
		page.setRowCount(rowCount);

		c.setProjection(null);
		c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		List<TourCommon> list = c.list();// 所有数据
		List<StatisticModel> tempList = new ArrayList<StatisticModel>();

		for (FactoryType factoryType:typeList) {
			String str = factoryType.getName();
			Double tempTotalIncome = 0d;// 总收入
			Long tempTotalPersonNum = 0l;// 总接待人次
			Long tempNum = 0l; // 工厂总数
			String tourIds = ""; // ids
			for (TourCommon common : list) {
				if (str.equals(common.getType())) {
					tempTotalIncome += common.getTotalIncome();
					tempTotalPersonNum += common.getTotalPersonNum();
					tourIds += common.getId() + ",";
				}
			}
			for (Dept d : deptList) {
				if (d.getDeptType().equals(str)) {
					tempNum += 1;
				}
			}
			StatisticModel town = new StatisticModel();
			town.setFactoryType(str);
			if (tourIds.trim().length() != 0) {
				town.setTourIds(tourIds.substring(0, tourIds.length() - 1));
			}
			town.setTotalPersonCount(tempTotalPersonNum);
			town.setTotalIncome(tempTotalIncome);
			town.setTotalFactoryCount(tempNum);
			tempList.add(town);
		}

		page.setResult(tempList);

		return page;
	}

	public PageResult findSameCompare(DetachedCriteria nowCriteria,
			DetachedCriteria lastCriteria, String startDate, String endDate,
			Integer currentMonth, Integer pageMonthNum) {
		Criteria factoryTypeCriteria=sessionFactory.getCurrentSession().createCriteria(FactoryType.class);
		List<FactoryType> typeList=factoryTypeCriteria.list();
		
		PageResult page = new PageResult();
		List<SameCompareModel> tempList = new ArrayList<SameCompareModel>();
		Integer year = 0;
		Integer month = 0;
		// 第一种情况默认查询上个月的
		if (((null == startDate || startDate.trim().length() == 0) && (endDate == null || endDate
				.trim().length() == 0))// 默认
				|| (null != startDate && startDate.trim().length() != 0 && (null == endDate || endDate
						.trim().length() == 0))// 有开始时间
				|| ((null == startDate || startDate.trim().length() == 0) && (endDate != null && endDate
						.trim().length() != 0))) {

			Calendar tempCalendar = Calendar.getInstance();
			year = tempCalendar.get(tempCalendar.YEAR);
			month = tempCalendar.get(tempCalendar.MONTH);
			
			
			// 有开始时间
			if (null != startDate && startDate.trim().length() != 0) {
				year = Integer.parseInt(startDate.trim().substring(0, 4));
				month = Integer.parseInt(startDate.trim().substring(5, 7));
			}
			// 有结束时间
			if (null != endDate && endDate.trim().length() != 0) {
				year = Integer.parseInt(endDate.trim().substring(0, 4));
				month = Integer.parseInt(endDate.trim().substring(5, 7));
			}

			Calendar calendar = Calendar.getInstance(); // 开始
			calendar.set(calendar.YEAR, year);
			calendar.set(calendar.MONTH, month);
			calendar.add(calendar.MONTH, currentMonth);// 加月差以实现分页效果

			Calendar calendar1 = Calendar.getInstance(); // 结束
			calendar1.set(calendar1.YEAR, calendar.get(calendar.YEAR));
			calendar1.set(calendar1.MONTH, calendar.get(calendar.MONTH));
			calendar1.add(calendar1.MONTH, pageMonthNum);

			calendar.add(calendar.MONTH, -1);// 将月数减1

			int tempYear = calendar.get(calendar.YEAR);
			int tempMonth = calendar.get(calendar.MONTH) + 1;
			Criteria criteria1 = nowCriteria
					.getExecutableCriteria(sessionFactory.getCurrentSession());

			Criteria criteria2 = lastCriteria
					.getExecutableCriteria(sessionFactory.getCurrentSession());

			criteria1.add(Restrictions.eq("reportYear", tempYear));
			criteria1.add(Restrictions.eq("reportMonth", tempMonth));

			List<TourCommon> nowList = criteria1.list();

			int lastYear = tempYear - 1;
			criteria2.add(Restrictions.eq("reportYear", lastYear));
			criteria2.add(Restrictions.eq("reportMonth", tempMonth));
			
			
			
			List<TourCommon> lastList = criteria2.list();
			for (FactoryType factoryType :typeList) {
				SameCompareModel model = new SameCompareModel();
				Long nowTotalCount = 0l;// 本年相应月份总个数
				Long lastTotalCount = 0l;// 去年相应月份总个数
				Long nowTotalPersonNum = 0l;// 本年相应月份接待总人数
				Long lastTotalPersonNum = 0l;// 去年相应月份接待总人数
				Double nowTotalIncome = 0d;// 本年相应月份总收入
				Double lastTotalIncome = 0d;// 去年相应月份总收入
				String nowIds = "";
				String lastIds = "";

				// 本年的
				for (TourCommon tourCommon : nowList) {
					if (tourCommon.getType().equals(factoryType.getName())) {
						nowTotalCount += 1;
						nowTotalIncome += tourCommon.getTotalIncome();
						nowTotalPersonNum += tourCommon.getTotalPersonNum();
						nowIds += tourCommon.getId() + ",";
					}
				}
				// 去年
				for (TourCommon tourCommon : lastList) {
					if (tourCommon.getType().equals(factoryType.getName())) {
						lastTotalCount += 1;
						lastTotalIncome += tourCommon.getTotalIncome();
						lastTotalPersonNum += tourCommon.getTotalPersonNum();
						lastIds += tourCommon.getId() + ",";
					}
				}
				model.setType(factoryType.getName());
				model.setYear(tempYear);
				model.setMonth(tempMonth);
				// model.setNowTotalCount(nowTotalCount);
				// model.setLastTotalCount(lastTotalCount);
				model.setNowTotalIncome(nowTotalIncome);
				model.setLastTotalIncome(lastTotalIncome);
				model.setNowTotalPersonNum(nowTotalPersonNum);
				model.setLastTotalPersonNum(lastTotalPersonNum);
				Double d=0d;
				d=getPercent(nowTotalIncome, lastTotalIncome);
				model.setIncomePercent(d);
				Double d1=0d;
				d1=getPercent(Double.parseDouble(nowTotalPersonNum+""),Double.parseDouble(lastTotalPersonNum+""));
				model.setPersonNumPercent(d1);
				if (nowIds.trim().length() != 0) {
					model.setNowIds(nowIds.substring(0, nowIds.length() - 1));
				}
				if (lastIds.trim().length() != 0) {
					model.setLastIds(lastIds.substring(0, lastIds.length() - 1));
				}

				tempList.add(model);
			}

			page.setRowCount(1);
		} else if (null != startDate && startDate.trim().length() != 0
				&& endDate != null && endDate.trim().length() != 0) {// 有开始时间和结束时间
			year = Integer.parseInt(startDate.trim().substring(0, 4));
			Integer endYear = Integer.parseInt(endDate.trim().substring(0, 4));
			month = Integer.parseInt(startDate.trim().substring(5, 7));
			Integer endMonth = Integer.parseInt(endDate.trim().substring(5, 7));

			Integer totalMonthCount = 0; // 总月数
			if (year != endYear) {
				totalMonthCount += 12 * (endYear - year);// 一年相差12个月
			}
			totalMonthCount += (endMonth - month + 1);

			page.setRowCount(Long.parseLong(totalMonthCount + ""));

			Calendar calendar = Calendar.getInstance(); // 开始
			calendar.set(calendar.YEAR, year);
			calendar.set(calendar.MONTH, month);
			calendar.add(calendar.MONTH, currentMonth);// 加月差以实现分页效果

			Calendar calendar1 = Calendar.getInstance(); // 结束
			calendar1.set(calendar1.YEAR, calendar.get(calendar.YEAR));
			calendar1.set(calendar1.MONTH, calendar.get(calendar.MONTH));
			calendar1.add(calendar1.MONTH, pageMonthNum);

			calendar.add(calendar.MONTH, -1);// 将月数减1
			for (int i = 0; i < pageMonthNum && i < totalMonthCount
					&& i < (totalMonthCount - currentMonth); i++) {

				int tempYear = calendar.get(calendar.YEAR);
				int tempMonth = calendar.get(calendar.MONTH) + 1;

				Criteria criteria1 = nowCriteria
						.getExecutableCriteria(sessionFactory
								.getCurrentSession());
				criteria1.add(Restrictions.eq("reportYear", tempYear));
				criteria1.add(Restrictions.eq("reportMonth", tempMonth));

				List<TourCommon> nowList = criteria1.list();

				int lastYear = tempYear - 1;
				Criteria criteria2 = lastCriteria
						.getExecutableCriteria(sessionFactory
								.getCurrentSession());
				criteria2.add(Restrictions.eq("reportYear", lastYear));
				criteria2.add(Restrictions.eq("reportMonth", tempMonth));
				List<TourCommon> lastList = criteria2.list();
				for (FactoryType factoryType: typeList) {
					SameCompareModel model = new SameCompareModel();
					Long nowTotalCount = 0l;// 本年相应月份总个数
					Long lastTotalCount = 0l;// 去年相应月份总个数
					Long nowTotalPersonNum = 0l;// 本年相应月份接待总人数
					Long lastTotalPersonNum = 0l;// 去年相应月份接待总人数
					Double nowTotalIncome = 0d;// 本年相应月份总收入
					Double lastTotalIncome = 0d;// 去年相应月份总收入
					String nowIds = "";
					String lastIds = "";

					// 本年的
					for (TourCommon tourCommon : nowList) {
						if (tourCommon.getType().equals(factoryType.getName())) {
							nowTotalCount += 1;
							nowTotalIncome += tourCommon.getTotalIncome();
							nowTotalPersonNum += tourCommon.getTotalPersonNum();
							nowIds += tourCommon.getId() + ",";
						}
					}
					// 去年
					for (TourCommon tourCommon : lastList) {
						if (tourCommon.getType().equals(factoryType.getName())) {
							lastTotalCount += 1;
							lastTotalIncome += tourCommon.getTotalIncome();
							lastTotalPersonNum += tourCommon
									.getTotalPersonNum();
							lastIds += tourCommon.getId() + ",";
						}
					}
					model.setType(factoryType.getName());
					model.setYear(tempYear);
					model.setMonth(tempMonth);
					// model.setNowTotalCount(nowTotalCount);
					// model.setLastTotalCount(lastTotalCount);
					model.setNowTotalIncome(nowTotalIncome);
					model.setLastTotalIncome(lastTotalIncome);
					model.setNowTotalPersonNum(nowTotalPersonNum);
					model.setLastTotalPersonNum(lastTotalPersonNum);
					Double d=0d;
					d=getPercent(nowTotalIncome, lastTotalIncome);
					model.setIncomePercent(d);
					Double d1=0d;
					d1=getPercent(Double.parseDouble(nowTotalPersonNum+""),Double.parseDouble(lastTotalPersonNum+""));
					model.setPersonNumPercent(d1);
					if (nowIds.trim().length() != 0) {
						model.setNowIds(nowIds.substring(0, nowIds.length() - 1));
					}
					if (lastIds.trim().length() != 0) {
						model.setLastIds(lastIds.substring(0,
								lastIds.length() - 1));
					}

					tempList.add(model);
				}
				Calendar tempCalendar = Calendar.getInstance();
				tempCalendar.set(tempCalendar.YEAR, endYear);
				tempCalendar.set(tempCalendar.MONTH, endMonth);
				calendar.add(calendar.MONTH, 1);

			}

		}

		page.setResult(tempList);
		return page;
	}

	public static void main(String[] args) {
		// for (FactoryTypeEnum tempType : FactoryTypeEnum.values()) {
		// tempType.getValue();
		// }
		// String ids = "2,3,";
		// System.out.println(ids.substring(0, ids.length() - 1));
		// Calendar c = Calendar.getInstance();
		// c.set(c.YEAR, 2013);
		// c.set(c.MONTH, 12);
		// // System.out.println(c.get(c.MONTH) + "\t" + c.get(c.YEAR));
		// Calendar c1 = Calendar.getInstance();
		// c1.set(c1.YEAR, 2013);
		// c1.set(c1.MONTH, 11);
		// System.out.println(c.after(c1));
		// ;
		
		//System.out.println(getPercent(12.45d, 53d));;
		
		//System.out.println(new SimpleDateFormat("yyyy年MM月").format(TimeUtil.getTimeInMillis("asdfasdf")));
		Calendar calendar=Calendar.getInstance();
		int c=calendar.get(calendar.MONTH);
		System.out.println(c);
	}
	public static Double getPercent(Double now,Double last){
		Double percent=0d;
		percent=(now-last)/last;
		if (percent.toString().equals("NaN")) {
			return 0d;
		}
		
		int i=0;
		percent=percent*100;
		i=percent.toString().indexOf(".");
		if (i!=0&&percent.toString().substring(i+1, percent.toString().length()).length()>=3) {
			if (null!=percent.toString().substring(i+3, i+4)&&Integer.parseInt(percent.toString().substring(i+3, i+4))>=5) {
				if (percent>0) {
					percent+=0.01d;
				}else{
					percent-=0.01d;
				}
			}
			percent=Double.parseDouble(percent.toString().substring(0, i+3));
		}
		return percent;
	}
	public void insertData() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/oa", "root", "root");
		conn.setAutoCommit(false);
		Statement st = null;
		for (int i = 0; i < 100; i++) {
			StringBuffer sql = new StringBuffer(
					"insert into tour_common_ (id_,report_month_,report_year_,status_,total_income_,total_person_num_,type_,user_id_) ");
			sql.append(" values (" + (100000 + i) + "," + 11 + "," + (1990 + i)
					+ "," + 2 + "," + 100 + i + "," + 50 + i + ",'工业旅游',6)");
			st = conn.createStatement();
			st.execute(sql.toString());
		}

		conn.commit();
		st.close();
		conn.close();

	}

	/*
	 * 获得报表数据
	 * 
	 * @see com.lxs.oa.tour.dao.ITourDao#getReportList()
	 */
	public List<StatisticReportModel> getReportList(String startDate,
			String endDate, List<Dept> deptList) {
		List<StatisticReportModel> list = new ArrayList<StatisticReportModel>();
		String userIds = "";
		for (Dept tempD : deptList) {
			Set<User> users = tempD.getUsers();
			for (User u : users) {
				userIds += (u.getId() + ",");
			}
		}
		Calendar calendar = Calendar.getInstance();

		Integer startYear = calendar.get(calendar.YEAR);
		Integer startMonth = calendar.get(calendar.MONTH);
		Integer endYear = calendar.get(calendar.YEAR);
		Integer endMonth = calendar.get(calendar.MONTH);// 默认查询上个月的

		if (null != startDate && startDate.trim().length() != 0) {
			startYear = Integer.parseInt(startDate.substring(0, 4));
			startMonth = Integer.parseInt(startDate.substring(5, 7));
		}
		if (null != endDate && endDate.trim().length() != 0) {
			endYear = Integer.parseInt(endDate.substring(0, 4));
			endMonth = Integer.parseInt(endDate.substring(5, 7));
		}

		StringBuffer sql = new StringBuffer(
				"SELECT c.type_,d.name_,SUM(d.money_) FROM tour_detail_ d INNER JOIN tour_common_ c ON c.id_ = d.common_id_");
		sql.append(" where c.user_id_ in (  "
				+ userIds.substring(0, userIds.length() - 1));
		sql.append(" ) and  ");
		sql.append(" c.long_time_ >= " +TimeUtil.getTimeInMillis(startYear+"年"+startMonth+"月"));
		sql.append(" and ");
		sql.append(" c.long_time_<= " +TimeUtil.getTimeInMillis(endYear+"年"+endMonth+"月"));
		sql.append(" GROUP BY type_, name_  ");
		System.out.println(sql.toString());
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(
				sql.toString());
		List<Object[]> objList = query.list();
		for (Object[] object : objList) {
			StatisticReportModel m = new StatisticReportModel();
			m.setType_(object[0].toString());
			m.setName_(object[1].toString());
			m.setMoney_(Double.parseDouble(object[2].toString().trim()));
			if (m.getType_().equals("观光园") || m.getType_().equals("旅游风景")) {
				m.setUnit_("(个)");
			} else if (m.getType_().equals("民俗旅游")) {
				m.setUnit_("(户)");
			} else if (m.getType_().equals("旅游住宿")
					|| m.getType_().equals("工业旅游")) {
				m.setUnit_("(家)");
			}

			list.add(m);
		}
		StringBuffer incomeSql = new StringBuffer(
				"SELECT type_,SUM(total_income_), SUM(total_person_num_) FROM tour_common_ as c ");
		incomeSql.append(" where c.user_id_ in (  "
				+ userIds.substring(0, userIds.length() - 1));
		incomeSql.append(" ) and  ");
		incomeSql.append(" c.long_time_ >= " +TimeUtil.getTimeInMillis(startYear+"年"+startMonth+"月"));
		incomeSql.append(" and ");
		incomeSql.append(" c.long_time_<= " +TimeUtil.getTimeInMillis(endYear+"年"+endMonth+"月"));
		incomeSql.append("	GROUP BY type_ ");
		SQLQuery query1 = sessionFactory.getCurrentSession().createSQLQuery(
				incomeSql.toString());
		List<Object[]> totalList = query1.list();
		for (StatisticReportModel m : list) {// 添加单位个数
			Long factoryNum = 0l;
			for (Dept d : deptList) {
				if (m.getType_().equals(d.getDeptType())) {
					factoryNum += 1;
				}
			}
			m.setDept_num_(factoryNum);
			for (Object[] obj : totalList) {
				if (m.getType_().equals(obj[0].toString())) {
					m.setSum_money_(Double.parseDouble(obj[1].toString()));
					m.setPerson_num_(Long.parseLong(obj[2].toString()));
				}
			}
		}
		return list;
	}

	public List<SameCompareChartModel> getCharts(DetachedCriteria nowCriteria,
			DetachedCriteria lastCriteria, String startDate, String endDate,
			Integer currentMonth, Integer pageMonthNum) {
		List<SameCompareChartModel> list = new ArrayList<SameCompareChartModel>();
		Criteria factoryTypeCriteria=sessionFactory.getCurrentSession().createCriteria(FactoryType.class);
		List<FactoryType> typeList=factoryTypeCriteria.list();
		Integer year = 0;
		Integer month = 0;
		// 第一种情况默认查询上个月的
		if (((null == startDate || startDate.trim().length() == 0) && (endDate == null || endDate
				.trim().length() == 0))// 默认
				|| (null != startDate && startDate.trim().length() != 0 && (null == endDate || endDate
						.trim().length() == 0))// 有开始时间
				|| ((null == startDate || startDate.trim().length() == 0) && (endDate != null && endDate
						.trim().length() != 0))) {

			Calendar tempCalendar = Calendar.getInstance();
			year = tempCalendar.get(tempCalendar.YEAR);
			month = tempCalendar.get(tempCalendar.MONTH);

			// 有开始时间
			if (null != startDate && startDate.trim().length() != 0) {
				year = Integer.parseInt(startDate.trim().substring(0, 4));
				month = Integer.parseInt(startDate.trim().substring(5, 7));
			}
			// 有结束时间
			if (null != endDate && endDate.trim().length() != 0) {
				year = Integer.parseInt(endDate.trim().substring(0, 4));
				month = Integer.parseInt(endDate.trim().substring(5, 7));
			}

			Calendar calendar = Calendar.getInstance(); // 开始
			calendar.set(calendar.YEAR, year);
			calendar.set(calendar.MONTH, month);
			calendar.add(calendar.MONTH, currentMonth);// 加月差以实现分页效果

			Calendar calendar1 = Calendar.getInstance(); // 结束
			calendar1.set(calendar1.YEAR, calendar.get(calendar.YEAR));
			calendar1.set(calendar1.MONTH, calendar.get(calendar.MONTH));
			calendar1.add(calendar1.MONTH, pageMonthNum);

			calendar.add(calendar.MONTH, -1);// 将月数减1

			int tempYear = calendar.get(calendar.YEAR);
			int tempMonth = calendar.get(calendar.MONTH) + 1;
			Criteria criteria1 = nowCriteria
					.getExecutableCriteria(sessionFactory.getCurrentSession());

			Criteria criteria2 = lastCriteria
					.getExecutableCriteria(sessionFactory.getCurrentSession());

			criteria1.add(Restrictions.eq("reportYear", tempYear));
			criteria1.add(Restrictions.eq("reportMonth", tempMonth));

			List<TourCommon> nowList = criteria1.list();

			int lastYear = tempYear - 1;
			criteria2.add(Restrictions.eq("reportYear", lastYear));
			criteria2.add(Restrictions.eq("reportMonth", tempMonth));
			List<TourCommon> lastList = criteria2.list();
			for (FactoryType factoryType: typeList) {
				SameCompareChartModel nowModel = new SameCompareChartModel();
				SameCompareChartModel lastModel = new SameCompareChartModel();
				nowModel.setYearType("今年");
				lastModel.setYearType("去年");
				nowModel.setType(factoryType.getName());
				lastModel.setType(factoryType.getName());
//				nowModel.setYearAndMonth(year + "年" + month + "月");
//				lastModel.setYearAndMonth(year + "年" + month + "月");

				Long nowPersonNumValues = 0l;
				Double nowMoneyValues = 0d;
				Long lastPersonNumValues = 0l;
				Double lastMoneyValues = 0d;

				// 本年的
				for (TourCommon tourCommon : nowList) {
					if (tourCommon.getType().equals(factoryType.getName())) {
						nowPersonNumValues += tourCommon.getTotalPersonNum();
						nowMoneyValues += tourCommon.getTotalIncome();
					}
				}
				// 去年
				for (TourCommon tourCommon : lastList) {
					if (tourCommon.getType().equals(factoryType.getName())) {
						lastPersonNumValues += tourCommon.getTotalPersonNum();
						lastMoneyValues += tourCommon.getTotalIncome();
					}
				}

				nowModel.setMoneyValues(nowMoneyValues);
				nowModel.setPersonNumValues(nowPersonNumValues);
				lastModel.setMoneyValues(lastMoneyValues);
				lastModel.setPersonNumValues(lastPersonNumValues);

				list.add(nowModel);
				list.add(lastModel);

			}
		} else if (null != startDate && startDate.trim().length() != 0
				&& endDate != null && endDate.trim().length() != 0) {// 有开始时间和结束时间
			year = Integer.parseInt(startDate.trim().substring(0, 4));
			Integer endYear = Integer.parseInt(endDate.trim().substring(0, 4));
			month = Integer.parseInt(startDate.trim().substring(5, 7));
			Integer endMonth = Integer.parseInt(endDate.trim().substring(5, 7));

			Integer totalMonthCount = 0; // 总月数
			if (year != endYear) {
				totalMonthCount += 12 * (endYear - year);// 一年相差12个月
			}
			totalMonthCount += (endMonth - month + 1);

			Calendar calendar = Calendar.getInstance(); // 开始
			calendar.set(calendar.YEAR, year);
			calendar.set(calendar.MONTH, month);
			calendar.add(calendar.MONTH, currentMonth);// 加月差以实现分页效果

			Calendar calendar1 = Calendar.getInstance(); // 结束
			calendar1.set(calendar1.YEAR, calendar.get(calendar.YEAR));
			calendar1.set(calendar1.MONTH, calendar.get(calendar.MONTH));
			calendar1.add(calendar1.MONTH, pageMonthNum);

			calendar.add(calendar.MONTH, -1);// 将月数减1
			for (int i = 0; i < pageMonthNum && i < totalMonthCount
					&& i < (totalMonthCount - currentMonth); i++) {

				int tempYear = calendar.get(calendar.YEAR);
				int tempMonth = calendar.get(calendar.MONTH) + 1;

				Criteria criteria1 = nowCriteria
						.getExecutableCriteria(sessionFactory
								.getCurrentSession());
				criteria1.add(Restrictions.eq("reportYear", tempYear));
				criteria1.add(Restrictions.eq("reportMonth", tempMonth));

				List<TourCommon> nowList = criteria1.list();

				int lastYear = tempYear - 1;
				Criteria criteria2 = lastCriteria
						.getExecutableCriteria(sessionFactory
								.getCurrentSession());
				criteria2.add(Restrictions.eq("reportYear", lastYear));
				criteria2.add(Restrictions.eq("reportMonth", tempMonth));
				List<TourCommon> lastList = criteria2.list();
				for (FactoryType factoryType :typeList) {

					SameCompareChartModel nowModel = new SameCompareChartModel();
					SameCompareChartModel lastModel = new SameCompareChartModel();
					nowModel.setYearType("今年");
					lastModel.setYearType("去年");
					nowModel.setType(factoryType.getName());
					lastModel.setType(factoryType.getName());
//					nowModel.setYearAndMonth(tempYear + "年" + tempMonth + "月");
//					lastModel.setYearAndMonth(tempYear + "年" + tempMonth + "月");

					Long nowPersonNumValues = 0l;
					Double nowMoneyValues = 0d;
					Long lastPersonNumValues = 0l;
					Double lastMoneyValues = 0d;

					// 本年的
					for (TourCommon tourCommon : nowList) {
						if (tourCommon.getType().equals(factoryType.getName())) {
							nowPersonNumValues += tourCommon
									.getTotalPersonNum();
							nowMoneyValues += tourCommon.getTotalIncome();
						}
					}
					// 去年
					for (TourCommon tourCommon : lastList) {
						if (tourCommon.getType().equals(factoryType.getName())) {
							lastPersonNumValues += tourCommon
									.getTotalPersonNum();
							lastMoneyValues += tourCommon.getTotalIncome();
						}
					}

					nowModel.setMoneyValues(nowMoneyValues);
					nowModel.setPersonNumValues(nowPersonNumValues);
					lastModel.setMoneyValues(lastMoneyValues);
					lastModel.setPersonNumValues(lastPersonNumValues);

					list.add(nowModel);
					list.add(lastModel);

				}
				Calendar tempCalendar = Calendar.getInstance();
				tempCalendar.set(tempCalendar.YEAR, endYear);
				tempCalendar.set(tempCalendar.MONTH, endMonth);
				calendar.add(calendar.MONTH, 1);
			}
		}

		return list;
	}
	
	@Override
	public PageResult findQuarterSameCompare(DetachedCriteria nowCriteria,DetachedCriteria lastCriteria,String startDate,String endDate,Long currentQuarter,Long currentYear,Long quarterNum,Boolean orention,Integer[] quarters){
		List<SameCompareModel> list = new ArrayList<SameCompareModel>();
		PageResult page=new PageResult();
		if ((null==startDate||startDate.trim().length()==0)&&(null==startDate||startDate.trim().length()==0)) {
			String defaultDate=new SimpleDateFormat("yyyy年MM月").format(TimeUtil.getTimeInMillis("获得上个月"));
			Integer startYear=Integer.parseInt(defaultDate.substring(0, 4));
			Integer endYear=startYear;
			Integer startMonth=0;
			Integer endMonth=0;
			String quarter="";
			Long month=Long.parseLong(defaultDate.substring(5,7));
			//一季度
			if (month>=2&&month<=4) {
				startMonth=2;
				endMonth=4;
				quarter="一季度";
			}
			//二季度
			if (month>=5&&month<=7) {
				startMonth=5;
				endMonth=7;
				quarter="二季度";
			}
			//三季度
			if (month>=8&&month<=10) {
				startMonth=8;
				endMonth=10;
				quarter="三季度";
			}
			//四季度
			if (month>=11||month<=1) {
				startYear-=1;
				startMonth=11;
				endMonth=1;
				quarter="四季度";
				
			}
			Criteria c1=nowCriteria.getExecutableCriteria(sessionFactory.getCurrentSession());
			Criterion cr1=Restrictions.ge("time", TimeUtil.getTimeInMillis(startYear+"年"+startMonth+"月"));
			Criterion cr2=Restrictions.le("time", TimeUtil.getTimeInMillis(endYear+"年"+endMonth+"月"));
			c1.add(Restrictions.and(cr1,cr2));
			List<TourCommon> nowList=c1.list();
			
			
			Criteria c2=lastCriteria.getExecutableCriteria(sessionFactory.getCurrentSession());
			Criterion cr3=Restrictions.ge("time", TimeUtil.getTimeInMillis((startYear-1)+"年"+startMonth+"月"));
			Criterion cr4=Restrictions.le("time", TimeUtil.getTimeInMillis((endYear-1)+"年"+endMonth+"月"));
			c2.add(Restrictions.and(cr3,cr4));
			List<TourCommon> lastList=c2.list();
			
			list.addAll(getSameCompareModel(startYear, quarter, nowList, lastList));
				
		}else if(null!=startDate&&startDate.trim().length()!=0){
			
			List<Integer> quList=new ArrayList<Integer>();
			if (null!=quarters&&quarters.length!=0) {
				for(Integer str:quarters){
					quList.add(str);
				}
			}
			if (null==endDate||endDate.trim().length()==0) {
				Calendar calendar=Calendar.getInstance();
				endDate=calendar.get(calendar.YEAR)+"";
			}
			int loopNum=0;//循环次数（一页显示多少个季度）
			for(int i=Integer.parseInt(endDate);i>=Integer.parseInt(startDate);i--){//年份//倒序
				boolean flag=false;
				if (quList.size()!=0) {
					for(int j=4;j>=1;j--){//季度
						if (!quList.contains(j)) {//如果没有选中查询此季度就不查询
							continue;
						}
						Calendar calendar=Calendar.getInstance();
						int nowYear=calendar.get(calendar.YEAR);
						int nowMonth=calendar.get(calendar.MONTH)+1;
						
						String quarter="";
						if (j==1) {
							quarter="一季度";
							if (i==nowYear) {
								if (nowMonth>4) {
									break;
								}
							}
						}else if (j==2) {
							quarter="二季度";
							if (i==nowYear) {
								if (nowMonth>7) {
									break;
								}
							}
						}else if (j==3) {
							quarter="三季度";
							if (i==nowYear) {
								if (nowMonth>10) {
									break;
								}
							}
						}else if (j==4) {
							quarter="四季度";
						}
						loopNum+=1;
						if (loopNum<=quarterNum) {
							Criteria c1=nowCriteria.getExecutableCriteria(sessionFactory.getCurrentSession());
							c1.add(Restrictions.eq("reportYear", i));
							c1.add(Restrictions.eq("quarter", j));
							List<TourCommon> nowList=c1.list();
							
							
							Criteria c2=lastCriteria.getExecutableCriteria(sessionFactory.getCurrentSession());
							c2.add(Restrictions.eq("reportYear", i-1));
							c2.add(Restrictions.eq("quarter",j));
							List<TourCommon> lastList=c2.list();
							
							list.addAll(getSameCompareModel(i, quarter, nowList, lastList));
							
						}else{
							flag=true;
							break;
						}
					}
					if (flag) {
						break;
					}
				}
			}
		}
		
		page.setResult(list);
		return page;
	}
	
	public List<SameCompareModel> getSameCompareModel(Integer startYear,String quarter,List<TourCommon> nowList,List<TourCommon> lastList){
		List<SameCompareModel> list=new ArrayList<SameCompareModel>();
		Criteria factoryTypeCriteria=sessionFactory.getCurrentSession().createCriteria(FactoryType.class);
		List<FactoryType> typeList=factoryTypeCriteria.list();
		for (FactoryType factoryType: typeList) {
			SameCompareModel model = new SameCompareModel();
			Long nowTotalCount = 0l;// 本年相应月份总个数
			Long lastTotalCount = 0l;// 去年相应月份总个数
			Long nowTotalPersonNum = 0l;// 本年相应月份接待总人数
			Long lastTotalPersonNum = 0l;// 去年相应月份接待总人数
			Double nowTotalIncome = 0d;// 本年相应月份总收入
			Double lastTotalIncome = 0d;// 去年相应月份总收入
			String nowIds = "";
			String lastIds = "";

			// 本年的
			for (TourCommon tourCommon : nowList) {
				if (tourCommon.getType().equals(factoryType.getName())) {
					nowTotalCount += 1;
					nowTotalIncome += tourCommon.getTotalIncome();
					nowTotalPersonNum += tourCommon.getTotalPersonNum();
					nowIds += tourCommon.getId() + ",";
				}
			}
			// 去年
			for (TourCommon tourCommon : lastList) {
				if (tourCommon.getType().equals(factoryType.getName())) {
					lastTotalCount += 1;
					lastTotalIncome += tourCommon.getTotalIncome();
					lastTotalPersonNum += tourCommon.getTotalPersonNum();
					lastIds += tourCommon.getId() + ",";
				}
			}
			model.setType(factoryType.getName());
			model.setYear(startYear);
			model.setQuarter(quarter);
			model.setNowTotalIncome(nowTotalIncome);
			model.setLastTotalIncome(lastTotalIncome);
			model.setNowTotalPersonNum(nowTotalPersonNum);
			model.setLastTotalPersonNum(lastTotalPersonNum);
			Double d=0d;
			d=getPercent(nowTotalIncome, lastTotalIncome);
			model.setIncomePercent(d);
			Double d1=0d;
			d1=getPercent(Double.parseDouble(nowTotalPersonNum+""),Double.parseDouble(lastTotalPersonNum+""));
			model.setPersonNumPercent(d1);
			if (nowIds.trim().length() != 0) {
				model.setNowIds(nowIds.substring(0, nowIds.length() - 1));
			}
			if (lastIds.trim().length() != 0) {
				model.setLastIds(lastIds.substring(0, lastIds.length() - 1));
			}

			list.add(model);
		}
		return list;
	}
	
}
