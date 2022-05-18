package com.csprs.cbw.bean.user;

import java.io.Serializable;
import java.util.ArrayList;
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

@SuppressWarnings("all")
@Entity
@Table(name = "Roles")
public class MyRole implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "name")
	public String name;
	
	@Column(name = "description")
	public String description;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "account_role", joinColumns = {@JoinColumn(name = "r_id")}, 
		inverseJoinColumns = {@JoinColumn(name = "a_id")})
	private List<MyProfile> profiles = new ArrayList<>();

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<MyProfile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<MyProfile> profiles) {
		this.profiles = profiles;
	}

	public MyRole(String name, String description) {
		this.name = name;
		this.description = description;
	}
	public MyRole() {
	}
	
}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
