package com.lxs.oa.tour.common;

public enum FactoryTypeEnum {
	visit(1),//观光园
	jingqu(2),//景区
	live(3),//住宿
	factory(4),//工业
	custom(5);//民俗
	private Integer value;
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	private FactoryTypeEnum(Integer value){
		this.value=value;
	}
	
}
