package com.lxs.oa.tour.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.lxs.core.action.BaseAction;
import com.lxs.oa.tour.domain.TourCommon;

@Controller
@Scope("prototype")
@Namespace("/tour")
@Actions({
		@Action(value = "factory", className = "tourAction", results = {
				@Result(name = "add", location = "/WEB-INF/jsp/tour/factory/add.jsp"),
				@Result(name = "update", location = "/WEB-INF/jsp/work/leave/update.jsp"),
				@Result(name = "list", location = "/WEB-INF/jsp/tour/factory/list.jsp"),
				@Result(name = "listAction", type = "redirect", location = "/work/leave!findPage.action?status=${@com.lxs.process.common.StatusEnum@unstart.value}"),
				@Result(name = "listTask", type = "redirect", location = "/process/task!findPage.action") }),
		@Action(value = "district", className = "tourAction", results = { @Result(name = "list", location = "/WEB-INF/jsp/work/leave/listStarted.jsp") }),
		@Action(value = "town", className = "tourAction", results = {
				@Result(name = "list", location = "/WEB-INF/jsp/work/leave/listFinished.jsp"),
				@Result(name = "watch", location = "/WEB-INF/jsp/work/leave/watch.jsp") }) })
public class TourAction extends BaseAction<TourCommon> {
	private String[] labelTexts;
	private Long[] inputMoneys;

	@Override
	public void beforeSave(TourCommon model) {
		// TODO Auto-generated method stub
		super.beforeSave(model);
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
}
