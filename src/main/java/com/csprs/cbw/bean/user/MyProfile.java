package com.csprs.cbw.bean.user;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("all")
@Entity
@Table(name = "Account")
@AllArgsConstructor
@NoArgsConstructor
public class MyProfile implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "name", unique = true)
	private String name;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "mail")
	private String mail;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_modified_time")
	private Date lastModifiedTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_time")
	private Date createdTime;
	
	@Column(name = "pwd_history")
	private String pwdHistory;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "account_role", joinColumns = {@JoinColumn(name = "a_id")}, 
		inverseJoinColumns = {@JoinColumn(name = "r_id")})
	public List<MyRole> roles = new ArrayList<>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
	
	
	

	public String getRoles() {
		String finalString = "";
		for (int i = 0; i < roles.size(); i++) {
			if(i == 0) {
				finalString += roles.get(i).getName();
			}else {
				finalString += "," + roles.get(i).getName();
			}
		}
		return finalString;
	}
	
	public List<MyRole> getRolesList() {
		return roles;
	}

	public void setRoles(List<MyRole> roles) {
		this.roles = roles;
	}

	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getPwdHistory() {
		return pwdHistory;
	}

	public void setPwdHistory(String pwdHistory) {
		this.pwdHistory = pwdHistory;
	}
	

	@Override
	public String toString() {
		return "MyProfile [id=" + id + ", name=" + name + ", password=" + password + ", description=" + description
				+ ", mail=" + mail + "]";
	}
}
