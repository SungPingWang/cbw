package com.csprs.cbw.bean.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("all")
@Entity
@Table(name = "account_permission")
public class Permission {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "group")
	public String group;
	
	@Column(name = "name")
	public String name;
	
	@Column(name = "permission_code")
	public String permissionCode;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPermissionCode() {
		return permissionCode;
	}

	public void setPermissionCode(String permissionCode) {
		this.permissionCode = permissionCode;
	}

	@Override
	public String toString() {
		return "Permission [id=" + id + ", group=" + group + ", name=" + name + ", permissionCode=" + permissionCode
				+ "]";
	}

	public Permission(long id, String group, String name, String permissionCode) {
		super();
		this.id = id;
		this.group = group;
		this.name = name;
		this.permissionCode = permissionCode;
	}

	public Permission() {
		super();
	}	
	
	
}
