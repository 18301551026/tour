package com.lxs.oa.tour.action;

import java.util.Collection;

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
import com.lxs.tour.domain.FactoryOption;
import com.lxs.tour.domain.FactoryType;
import com.opensymphony.xwork2.ActionContext;

@Controller
@Scope("prototype")
@Namespace("/tour")
@Actions({ @Action(value = "factoryType", className = "factoryTypeAction", results = {
		@Result(name = "add", location = "/WEB-INF/jsp/tour/factoryType/add.jsp"),
		@Result(name = "update", location = "/WEB-INF/jsp/tour/factoryType/update.jsp"),
		@Result(name = "list", location = "/WEB-INF/jsp/tour/factoryType/list.jsp"),
		@Result(name = "listAction", type = "redirect", location = "/tour/factoryType!findPage.action")
	})
})
public class FactoryTypeAction extends BaseAction<FactoryType> {
	private String[] optionNames;
	private Collection<FactoryOption> beans;
	private Long optionId;
	@Override
	public void beforFind(DetachedCriteria criteria) {
		if (null!=model.getName()) {
			criteria.add(Restrictions.like("name", model.getName(),MatchMode.ANYWHERE));
		}
	}
	@Override
	public void afterSave(FactoryType m) {
		if (null!=optionNames&&optionNames.length!=0) {
			for(int i=0;i<optionNames.length;i++){
				FactoryOption o=new FactoryOption();
				o.setName(optionNames[i]);
				o.setType(m);
				baseService.add(o);
			}
		}
	}
	@Override
	public void beforeSave(FactoryType m) {
		if (null!=m&&null!=m.getId()) {//修改时修改选项
			if (null!=beans&&beans.size()!=0) {
				for(FactoryOption o:beans){
					FactoryOption tempO=baseService.get(FactoryOption.class, o.getId());
					tempO.setName(o.getName());
					baseService.update(tempO);
				}
			}
		}
	}
	@Override
	public void afterToUpdate(FactoryType t) {
		beans=t.getOptions();
		ActionContext.getContext().put("optionsNum", beans.size());
	}
	public void deleteOption(){
		baseService.delete(baseService.get(FactoryOption.class, optionId));
		getOut().print("成功");
	}
	public String[] getOptionNames() {
		return optionNames;
	}
	public void setOptionNames(String[] optionNames) {
		this.optionNames = optionNames;
	}
	
	public Collection<FactoryOption> getBeans() {
		return beans;
	}
	public void setBeans(Collection<FactoryOption> beans) {
		this.beans = beans;
	}
	public Long getOptionId() {
		return optionId;
	}
	public void setOptionId(Long optionId) {
		this.optionId = optionId;
	}
}
