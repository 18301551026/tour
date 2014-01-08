package com.lxs.oa.tour.action;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.lxs.core.action.BaseAction;
import com.lxs.notification.service.INotificationServiceWs;
import com.lxs.oa.tour.domain.PhoneNotify;
import com.lxs.security.domain.Dept;
import com.lxs.security.domain.User;
import com.lxs.tour.domain.FactoryType;
import com.opensymphony.xwork2.ActionContext;
@Controller
@Scope("prototype")
@Namespace("/tour")
@Actions({ @Action(value = "notify", className = "phoneNotifyAction", results = {
		@Result(name = "add", location = "/WEB-INF/jsp/tour/notify/add.jsp"),
		@Result(name = "update", location = "/WEB-INF/jsp/tour/factoryType/update.jsp"),
		@Result(name = "list", location = "/WEB-INF/jsp/tour/notify/list.jsp"),
		@Result(name="toSelectReceiveUsers",location="/WEB-INF/jsp/tour/notify/selectReceiveUsers.jsp"),
		@Result(name = "listAction", type = "redirect", location = "/tour/notify!findPage.action")
	})
})
public class PhoneNotifyAction extends BaseAction<PhoneNotify> {
	@Resource
	private INotificationServiceWs notificationService;
	
	private String factoryName;
	private Long factoryTypeId;
	private String receiveIds;
	public String getFactoryName() {
		return factoryName;
	}
	
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	
	public String toSelectReceiveUsers(){
		DetachedCriteria detachedCriteria=DetachedCriteria.forClass(Dept.class);
		detachedCriteria.add(Restrictions.eq("deptLevel", "镇级"));
		List<Dept> townList=baseService.find(detachedCriteria);
		ActionContext.getContext().put("townList", townList);
		
		List<FactoryType> typeList=baseService.find(DetachedCriteria.forClass(FactoryType.class));
		ActionContext.getContext().put("typeList", typeList);
		return "toSelectReceiveUsers";
	}
	
	public void findFactoryByFactoryName(){
		DetachedCriteria detachedCriteria=DetachedCriteria.forClass(Dept.class);
		detachedCriteria.add(Restrictions.eq("deptLevel", "企业"));
		detachedCriteria.add(Restrictions.like("text", factoryName,MatchMode.ANYWHERE));
		List<Dept> list=baseService.find(detachedCriteria);
		StringBuffer script = new StringBuffer();
		for (Dept dept : list) {
			script.append("<option value=" + dept.getId() + ">"
					+ dept.getText() + "</option>");
		}
		getOut().print(script);
	}
	
	public void findFactoryByFactoryType(){
		DetachedCriteria detachedCriteria=DetachedCriteria.forClass(Dept.class);
		detachedCriteria.add(Restrictions.eq("deptLevel", "企业"));
		detachedCriteria.createAlias("factoryType", "t");
		detachedCriteria.add(Restrictions.eq("t.id", factoryTypeId));
		List<Dept> list=baseService.find(detachedCriteria);
		StringBuffer script = new StringBuffer();
		for (Dept dept : list) {
			script.append("<option value=" + dept.getId() + ">"
					+ dept.getText() + "</option>");
		}
		getOut().print(script);
	}
	@Override
	public void beforeSave(PhoneNotify model) {
		Set<User> list=new HashSet<User>();
		
		if (receiveIds.equals("0")) {
			DetachedCriteria detachedCriteria=DetachedCriteria.forClass(Dept.class);
			detachedCriteria.add(Restrictions.eq("deptLevel", "企业"));
			List<Dept> deptList=baseService.find(detachedCriteria);
			for (Dept dept : deptList) {
				list.addAll(dept.getUsers());
			}
		}else{
			String tempIds[]=receiveIds.split(",");
			for (String s: tempIds) {
				if (null!=s&&s.indexOf("t")!=-1) {//镇
					Dept d=baseService.get(Dept.class,Long.parseLong( s.substring(1,s.length())));
					for(Dept dept:d.getChildren()){
						list.addAll(dept.getUsers());
					}
				}else if(null!=s){
					list.addAll(baseService.get(Dept.class, Long.parseLong(s.trim())).getUsers());
				}
			}
		}
		
		String usersName="";
		for (User user : list) {
			if (null!=user) {
				usersName+=user.getUserName()+",";
			}
		}
		model.setCreateDate(new Date());
		usersName=usersName.substring(0,usersName.length()-1);
		
		model.setUserNames(usersName);
		
		notificationService.sendNotifcationToUsers(usersName, model.getTitle(), model.getContent(), "");
	}
	public Long getFactoryTypeId() {
		return factoryTypeId;
	}

	public void setFactoryTypeId(Long factoryTypeId) {
		this.factoryTypeId = factoryTypeId;
	}

	public String getReceiveIds() {
		return receiveIds;
	}

	public void setReceiveIds(String receiveIds) {
		this.receiveIds = receiveIds;
	}
	public static void main(String[] args) {
		String s="dd,";
		System.out.println(s.indexOf("j"));
		System.out.println(s.subSequence(0, s.length()));
	}
}
