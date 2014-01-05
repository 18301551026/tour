package com.lxs.tour.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.lxs.security.domain.Dept;

@Entity
@Table(name="factory_type_")
public class FactoryType implements Serializable {
	private String name;
	private Long id;
	private String desc;
	private List<FactoryOption> options=new ArrayList<FactoryOption>();
	private List<Dept> depts=new ArrayList<Dept>();
	@Column(name="name_")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Id
	@GeneratedValue
	@Column(name="id_")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Lob
	@Column(name="desc_")
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@OneToMany(fetch=FetchType.LAZY,mappedBy="type",cascade=CascadeType.REMOVE)
	public List<FactoryOption> getOptions() {
		return options;
	}
	public void setOptions(List<FactoryOption> options) {
		this.options = options;
	}
	@OneToMany(fetch=FetchType.LAZY,mappedBy="factoryType")
	public List<Dept> getDepts() {
		return depts;
	}
	public void setDepts(List<Dept> depts) {
		this.depts = depts;
	}
}
