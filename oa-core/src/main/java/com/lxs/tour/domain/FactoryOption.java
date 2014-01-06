package com.lxs.tour.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;

@Entity
@Table(name="factory_option_")
@JSONType(ignores = "hibernateLazyInitializer")
public class FactoryOption implements Serializable {
	private Long id;
	private String name;
	private FactoryType type;
	@Id
	@GeneratedValue
	@Column(name = "id_")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="name_")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="type_id_")
	@JSONField(serialize = false)
	public FactoryType getType() {
		return type;
	}
	
	@JSONField(deserialize = false)
	public void setType(FactoryType type) {
		this.type = type;
	}
}
