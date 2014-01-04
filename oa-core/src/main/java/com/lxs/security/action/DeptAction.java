package com.lxs.security.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.lxs.core.action.BaseAction;
import com.lxs.core.common.BeanUtil;
import com.lxs.security.domain.Dept;
import com.lxs.security.domain.Menu;
import com.lxs.security.service.IDeptService;
import com.lxs.tour.domain.FactoryType;
import com.opensymphony.xwork2.ActionContext;


@Controller
@Scope("prototype")
@Namespace("/security")
@Action(value = "dept", className = "deptAction")
@Results({
		@Result(name = "add", location = "/WEB-INF/jsp/security/dept/add.jsp"),
		@Result(name = "update", location = "/WEB-INF/jsp/security/dept/update.jsp"),
		@Result(name = "list", location = "/WEB-INF/jsp/security/dept/list.jsp"),
		@Result(name = "listAction", type = "redirect", location = "/security/dept!findPage.action")

})
public class DeptAction extends BaseAction<Dept> {
	@Resource
	private IDeptService deptService;
	public List<FactoryType> getAllFactoryType(){
		List<FactoryType> list=new ArrayList<FactoryType>();
		DetachedCriteria criteria=DetachedCriteria.forClass(FactoryType.class);
		list=baseService.find(criteria);
		return list;
		
	}
	@Override
	public void beforeSave(Dept model) {
		if (null!=model.getFactoryType()&&null!=model.getFactoryType().getId()) {
			FactoryType t=baseService.get(FactoryType.class, model.getFactoryType().getId());
			model.setDeptType(t.getName());
		}
	}
	public void getAllDept() {
		List<Dept> list = deptService.findAllDept();
		for (Dept dept : list) {
			if (null != dept.getChildren() && dept.getChildren().size() != 0) {
				dept.setState("closed");
			} else {
				dept.setState("open");
			}
		}
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(Dept.class);
		filter.getExcludes().add("parent");
		String json = JSON.toJSONString(list, filter);
		System.out.println(json);
		getOut().print(json);
	}
	
	public void deleteDept() {
		baseService.delete(baseService.get(modelClass, model.getId()));
		getOut().print("成功");
	}

	public void updateDept() {
		Dept d = baseService.get(Dept.class, model.getId());
		BeanUtil.copy(model, d);
		baseService.save(d);
		if (null!=d.getFactoryType()&&null!=d.getFactoryType().getId()) {
			FactoryType t=baseService.get(FactoryType.class, d.getFactoryType().getId());
			d.setDeptType(t.getName());
		}
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(Menu.class);
		filter.getExcludes().add("children");
		getOut().print(JSON.toJSONString(d, filter));
	}

	@Override
	public void beforToUpdate() {
		ActionContext.getContext().getValueStack()
				.push(baseService.get(Dept.class, model.getId()));
	}

	public void addDept() {
		if (null != model.getPid()) {
			model.setParent(baseService.get(Dept.class, model.getPid()));
		}
		baseService.save(model);
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(Menu.class);
		filter.getExcludes().add("children");
		getOut().print(JSON.toJSONString(model, filter));

	}

}
