package com.lxs.oa.tour.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.stereotype.Repository;

import com.lxs.core.common.TimeUtil;
import com.lxs.core.common.page.PageResult;
import com.lxs.oa.tour.common.StatusEnum;
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
		Criteria factoryTypeCriteria = sessionFactory.getCurrentSession()
				.createCriteria(FactoryType.class);
		List<FactoryType> typeList = factoryTypeCriteria.list();

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

		for (FactoryType factoryType : typeList) {
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
			BigDecimal big=new BigDecimal(tempTotalIncome);
			town.setTotalIncome(big.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
			town.setTotalPersonCount(tempTotalPersonNum);
			town.setTotalFactoryCount(tempNum);
			tempList.add(town);
		}

		page.setResult(tempList);

		return page;
	}

	public List<SameCompareModel> findSameCompare(List<Long> userIds, String startDate,
			String endDate) {
		List<SameCompareModel> list=new ArrayList<SameCompareModel>();
		int endYear=0;
		int endMonth;
		int startYear=0;
		int startMonth=0;
		if (null!=endDate&&endDate.trim().length()!=0) {
			endYear=Integer.parseInt(endDate.substring(0,4));
			endMonth=Integer.parseInt(endDate.substring(5,7));
		} else{
			Calendar calendar=Calendar.getInstance();
			endYear=calendar.get(calendar.YEAR);
			endMonth=calendar.get(calendar.MONTH);//上个月
		}
		if (null!=startDate&&startDate.trim().length()!=0) {
			startYear=Integer.parseInt(startDate.substring(0,4));
			startMonth=Integer.parseInt(startDate.substring(5,7));
		}else{
				String sql = "select min(report_year_) from tour_common_";
				SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(
						sql);
				List objList = query.list();
				if (null!=objList&&objList.size()!=0) {
					startYear=Integer.parseInt(objList.get(0).toString());
				}
				sql="select min(report_month_) from tour_common_ as com where report_year_="+startYear;
				query=sessionFactory.getCurrentSession().createSQLQuery(sql);
				objList=query.list();
				if (null!=objList&&objList.size()!=0) {
					startMonth=Integer.parseInt(objList.get(0).toString());
				}
				
		}
		for(int year=endYear;year>=startYear;year--){//年
			for(int mon=12;mon>=1;mon--){
				if (year==endYear) {
					if (endMonth<mon) {//由结束年月开始
						continue;
					}
				}
				if (year==startYear) {
					if (mon<startMonth) {
						break;//小于开始年月时结束
					}
				}
				
				
				DetachedCriteria nowCriteria = DetachedCriteria
						.forClass(TourCommon.class);
				nowCriteria.add(Restrictions.eq("status",
						StatusEnum.reported.getValue()));
				if (userIds.size() != 0) {
					nowCriteria.createAlias("user", "u");
					nowCriteria.add(Restrictions.in("u.id", userIds));
				} else {
					nowCriteria.add(Restrictions.isNull("user"));
				}
				Criteria criteria1 = nowCriteria
						.getExecutableCriteria(sessionFactory
								.getCurrentSession());
				criteria1.add(Restrictions.eq("reportMonth", mon));
				criteria1.add(Restrictions.eq("reportYear", year));

				List<TourCommon> nowList = criteria1.list();

				
				
				DetachedCriteria lastCriteria = DetachedCriteria
						.forClass(TourCommon.class);
				lastCriteria.add(Restrictions.eq("status",
						StatusEnum.reported.getValue()));
				if (userIds.size() != 0) {
					lastCriteria.createAlias("user", "u");
					lastCriteria.add(Restrictions.in("u.id", userIds));
				} else {
					lastCriteria.add(Restrictions.isNull("user"));
				}
				Criteria criteria2 = lastCriteria
						.getExecutableCriteria(sessionFactory
								.getCurrentSession());
				criteria2.add(Restrictions.eq("reportYear",year-1));
				criteria2.add(Restrictions.eq("reportMonth",mon));

				List<TourCommon> lastList = criteria2.list();

				list.addAll(getSameCompareModelList(year, mon, nowList, lastList));
			}
		}
		
		return list;
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
		sql.append(" c.long_time_ >= "
				+ TimeUtil.getTimeInMillis(startYear + "年" + startMonth + "月"));
		sql.append(" and ");
		sql.append(" c.long_time_<= "
				+ TimeUtil.getTimeInMillis(endYear + "年" + endMonth + "月"));
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
			m.setUnit_("(个)");
			list.add(m);
		}
		StringBuffer incomeSql = new StringBuffer(
				"SELECT type_,SUM(total_income_), SUM(total_person_num_) FROM tour_common_ as c ");
		incomeSql.append(" where c.user_id_ in (  "
				+ userIds.substring(0, userIds.length() - 1));
		incomeSql.append(" ) and  ");
		incomeSql.append(" c.long_time_ >= "
				+ TimeUtil.getTimeInMillis(startYear + "年" + startMonth + "月"));
		incomeSql.append(" and ");
		incomeSql.append(" c.long_time_<= "
				+ TimeUtil.getTimeInMillis(endYear + "年" + endMonth + "月"));
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
		Criteria factoryTypeCriteria = sessionFactory.getCurrentSession()
				.createCriteria(FactoryType.class);
		List<FactoryType> typeList = factoryTypeCriteria.list();
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
			for (FactoryType factoryType : typeList) {
				SameCompareChartModel nowModel = new SameCompareChartModel();
				SameCompareChartModel lastModel = new SameCompareChartModel();
				nowModel.setYearType("今年");
				lastModel.setYearType("去年");
				nowModel.setType(factoryType.getName());
				lastModel.setType(factoryType.getName());
				// nowModel.setYearAndMonth(year + "年" + month + "月");
				// lastModel.setYearAndMonth(year + "年" + month + "月");

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
				
				BigDecimal nowBig=new BigDecimal(nowMoneyValues);
				nowModel.setMoneyValues(nowBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
				nowModel.setPersonNumValues(nowPersonNumValues);
				BigDecimal lastBig=new BigDecimal(lastMoneyValues);
				lastModel.setMoneyValues(lastBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
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
				for (FactoryType factoryType : typeList) {

					SameCompareChartModel nowModel = new SameCompareChartModel();
					SameCompareChartModel lastModel = new SameCompareChartModel();
					nowModel.setYearType("今年");
					lastModel.setYearType("去年");
					nowModel.setType(factoryType.getName());
					lastModel.setType(factoryType.getName());
					// nowModel.setYearAndMonth(tempYear + "年" + tempMonth +
					// "月");
					// lastModel.setYearAndMonth(tempYear + "年" + tempMonth +
					// "月");

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

					BigDecimal nowBig=new BigDecimal(nowMoneyValues);
					nowModel.setMoneyValues(nowBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
					nowModel.setPersonNumValues(nowPersonNumValues);
					BigDecimal lastBig=new BigDecimal(lastMoneyValues);
					lastModel.setMoneyValues(lastBig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
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
	public List<SameCompareModel> getQuarterSameCompareModels(
			List<Long> userIds, int startDate, int endDate,
			List<Integer> quarters) {
		List<SameCompareModel> list = new ArrayList<SameCompareModel>();
		int nowYear = 0;
		int nowMonth = 0;
		Calendar calendar = Calendar.getInstance();
		if (endDate == 0) {
			endDate = calendar.get(calendar.YEAR);
		}
		String sql = "select min(report_year_) from tour_common_";
		if (startDate == 0) {
			SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(
					sql);
			List objList = query.list();
			if (null != objList && objList.size() != 0) {
				startDate = Integer.parseInt(objList.get(0).toString());
			}
		}
		nowYear = calendar.get(calendar.YEAR);
		nowMonth = calendar.get(calendar.MONTH) + 1;
		for (int i = endDate; i >= startDate; i--) {// 年
			for (int j = 4; j >= 1; j--) { // 季度
				if (quarters.contains(j)) {// 如果是要查询的季度
					String quarter = "";
					if (j == 1) {
						quarter = "一季度";
						if (nowYear == i) {
							if (nowMonth < 2) {
								continue;
							}
						}
					} else if (j == 2) {
						quarter = "二季度";
						if (nowYear == i) {
							if (nowMonth < 5) {
								continue;
							}
						}
					} else if (j == 3) {
						quarter = "三季度";
						if (nowYear == i) {
							if (nowMonth < 8) {
								continue;
							}
						}
					} else if (j == 4) {
						quarter = "四季度";
						if (nowYear == i) {
							if (nowMonth < 11) {
								continue;
							}
						}
					}
					
					DetachedCriteria nowCriteria = DetachedCriteria
							.forClass(TourCommon.class);
					nowCriteria.add(Restrictions.eq("status",
							StatusEnum.reported.getValue()));
					if (userIds.size() != 0) {
						nowCriteria.createAlias("user", "u");
						nowCriteria.add(Restrictions.in("u.id", userIds));
					} else {
						nowCriteria.add(Restrictions.isNull("user"));
					}
					Criteria criteria1 = nowCriteria
							.getExecutableCriteria(sessionFactory
									.getCurrentSession());
					criteria1.add(Restrictions.eq("quarter", j));
					criteria1.add(Restrictions.eq("reportYear", i));

					List<TourCommon> nowList = criteria1.list();

					
					
					DetachedCriteria lastCriteria = DetachedCriteria
							.forClass(TourCommon.class);
					lastCriteria.add(Restrictions.eq("status",
							StatusEnum.reported.getValue()));
					if (userIds.size() != 0) {
						lastCriteria.createAlias("user", "u");
						lastCriteria.add(Restrictions.in("u.id", userIds));
					} else {
						lastCriteria.add(Restrictions.isNull("user"));
					}
					Criteria criteria2 = lastCriteria
							.getExecutableCriteria(sessionFactory
									.getCurrentSession());
					criteria2.add(Restrictions.eq("reportYear", i-1));
					criteria2.add(Restrictions.eq("quarter", j));

					List<TourCommon> lastList = criteria2.list();

					list.addAll(getSameCompareModel(i, quarter, nowList,
							lastList));
				}
			}
		}
		return list;
	}
	public List<SameCompareModel> getSameCompareModelList(Integer year,
			int mon, List<TourCommon> nowList, List<TourCommon> lastList) {
		List<SameCompareModel> list = new ArrayList<SameCompareModel>();
		Criteria factoryTypeCriteria = sessionFactory.getCurrentSession()
				.createCriteria(FactoryType.class);
		List<FactoryType> typeList = factoryTypeCriteria.list();
		for (FactoryType factoryType : typeList) {
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
			model.setYear(year);
			model.setMonth(mon);
			
			BigDecimal b=new BigDecimal(nowTotalIncome);
			nowTotalIncome=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
			
			BigDecimal b1=new BigDecimal(lastTotalIncome);
			lastTotalIncome=b1.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
			
			model.setNowTotalIncome(nowTotalIncome);
			model.setLastTotalIncome(lastTotalIncome);
			model.setNowTotalPersonNum(nowTotalPersonNum);
			model.setLastTotalPersonNum(lastTotalPersonNum);
			Double d = 0d;
			d = getPercent(nowTotalIncome, lastTotalIncome);
			model.setIncomePercent(d);
			Double d1 = 0d;
			d1 = getPercent(Double.parseDouble(nowTotalPersonNum + ""),
					Double.parseDouble(lastTotalPersonNum + ""));
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

	public List<SameCompareModel> getSameCompareModel(Integer startYear,
			String quarter, List<TourCommon> nowList, List<TourCommon> lastList) {
		List<SameCompareModel> list = new ArrayList<SameCompareModel>();
		Criteria factoryTypeCriteria = sessionFactory.getCurrentSession()
				.createCriteria(FactoryType.class);
		List<FactoryType> typeList = factoryTypeCriteria.list();
		for (FactoryType factoryType : typeList) {
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
			
			BigDecimal b=new BigDecimal(nowTotalIncome);
			nowTotalIncome=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
			
			BigDecimal b1=new BigDecimal(lastTotalIncome);
			lastTotalIncome=b1.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
			
			model.setNowTotalIncome(nowTotalIncome);
			model.setLastTotalIncome(lastTotalIncome);
			model.setNowTotalPersonNum(nowTotalPersonNum);
			model.setLastTotalPersonNum(lastTotalPersonNum);
			Double d = 0d;
			d = getPercent(nowTotalIncome, lastTotalIncome);
			model.setIncomePercent(d);
			Double d1 = 0d;
			d1 = getPercent(Double.parseDouble(nowTotalPersonNum + ""),
					Double.parseDouble(lastTotalPersonNum + ""));
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

	public List<SameCompareChartModel> getQuarterCharts(List<Long> userIds,
			int startDate, int endDate, List<Integer> quarters) {
		Criteria factoryTypeCriteria = sessionFactory.getCurrentSession()
				.createCriteria(FactoryType.class);
		List<FactoryType> typeList = factoryTypeCriteria.list();
		
		List<SameCompareChartModel> list = new ArrayList<SameCompareChartModel>();

		DetachedCriteria nowCriteria = DetachedCriteria
				.forClass(TourCommon.class);
		nowCriteria.add(Restrictions.eq("status",
				StatusEnum.reported.getValue()));
		if (userIds.size() != 0) {
			nowCriteria.createAlias("user", "u");
			nowCriteria.add(Restrictions.in("u.id", userIds));
		} else {
			nowCriteria.add(Restrictions.isNull("user"));
		}
		Criteria criteria1 = nowCriteria.getExecutableCriteria(sessionFactory
				.getCurrentSession());
		criteria1.add(Restrictions.eq("quarter",quarters.get(0)));//季度
		criteria1.add(Restrictions.eq("reportYear", startDate));	//年

		List<TourCommon> nowList = criteria1.list();

		DetachedCriteria lastCriteria = DetachedCriteria
				.forClass(TourCommon.class);
		lastCriteria.add(Restrictions.eq("status",
				StatusEnum.reported.getValue()));
		if (userIds.size() != 0) {
			lastCriteria.createAlias("user", "u");
			lastCriteria.add(Restrictions.in("u.id", userIds));
		} else {
			lastCriteria.add(Restrictions.isNull("user"));
		}
		Criteria criteria2 = lastCriteria.getExecutableCriteria(sessionFactory
				.getCurrentSession());
		criteria2.add(Restrictions.eq("quarter",quarters.get(0)));	//季度
		criteria2.add(Restrictions.eq("reportYear", startDate-1));	//相对去年

		List<TourCommon> lastList = criteria2.list();
		
		for (FactoryType factoryType : typeList) {
			SameCompareChartModel nowModel = new SameCompareChartModel();
			SameCompareChartModel lastModel = new SameCompareChartModel();
			nowModel.setYearType("今年");
			lastModel.setYearType("去年");
			nowModel.setType(factoryType.getName());
			lastModel.setType(factoryType.getName());

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
			BigDecimal nowb=new BigDecimal(nowMoneyValues);
			nowMoneyValues=nowb.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
			
			BigDecimal lastb=new BigDecimal(lastMoneyValues);
			lastMoneyValues=lastb.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
			nowModel.setMoneyValues(nowMoneyValues);
			nowModel.setPersonNumValues(nowPersonNumValues);
			lastModel.setMoneyValues(lastMoneyValues);
			lastModel.setPersonNumValues(lastPersonNumValues);

			list.add(nowModel);
			list.add(lastModel);

		}
		
		
		return list;
	}
	public static void main(String[] args) {
		Double d=0.0;
		Double d1=35.0;
		d+=d1;
		d+=24.63;
		BigDecimal b=new BigDecimal(d);
		d=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		System.out.println(d);
	}
}
