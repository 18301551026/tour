package com.lxs.security.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;

@SuppressWarnings("serial")
@Entity
@Table(name = "dept_")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JSONType(ignores = "hibernateLazyInitializer")
public class Dept implements Serializable {
	private Long id;
	private String text;
	private String deptDesc;
	private String icon;
	private Set<User> users = new HashSet<User>();
	private Dept parent;
	private List<Dept> children = new ArrayList<Dept>();
	private String deptType;
	private String deptLevel;
	@Column(name="dept_type_")
	public String getDeptType() {
		return deptType;
	}

	public void setDeptType(String deptType) {
		this.deptType = deptType;
	}
	@Column(name="dept_level_")
	public String getDeptLevel() {
		return deptLevel;
	}

	public void setDeptLevel(String deptLevel) {
		this.deptLevel = deptLevel;
	}

	@Id
	@GeneratedValue
	@Column(name = "id_")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	@JSONField(serialize = false)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "dept", cascade = CascadeType.REMOVE)
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Lob
	@Column(name = "dept_desc_")
	public String getDeptDesc() {
		return deptDesc;
	}

	public void setDeptDesc(String deptDesc) {
		this.deptDesc = deptDesc;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pid_")
	public Dept getParent() {
		return parent;
	}

	public void setParent(Dept parent) {
		this.parent = parent;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.REMOVE)
	public List<Dept> getChildren() {
		return children;
	}

	public void setChildren(List<Dept> children) {
		this.children = children;
	}

	@Column(name = "icon_")
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	private Long pid;
	private String state;

	@Transient
	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	@Transient
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	@Column(name="text_")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	private Integer tempLevel;
	public Integer getTempLevel() {
		return tempLevel;
	}

	public void setTempLevel(Integer tempLevel) {
		this.tempLevel = tempLevel;
	}
}
