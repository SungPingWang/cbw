package com.csprs.cbw.bean.cbw;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@SuppressWarnings("all")
@Entity
@Table(name = "cbw_station")
@Data
public class cbwstation implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "station")
	private String station;

	@Column(name = "city")
	private String city;
	
	@Column(name = "coord_X")
	private String coord_X;
	
	@Column(name = "coord_Y")
	private String coord_Y;
	
}
