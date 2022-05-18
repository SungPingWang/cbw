package com.csprs.cbw.bean.cbw;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@SuppressWarnings("all")
@Entity
@Table(name = "cbw_data")
@Data
public class cbwbean implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "station")
	private String station;

	@Column(name = "city")
	private String city;
	
	@Column(name = "curr_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date curr_time;
	
	@Column(name = "temperature")
	private Double temperature;
	
	@Column(name = "weather")
	private String weather;
	
	@Column(name = "w_direction")
	private String w_direction;
	
	@Column(name = "w_power")
	private String w_power;
	
	@Column(name = "w_level")
	private String w_level;
	
	@Column(name = "visibility")
	private String visibility;
	
	@Column(name = "humidity")
	private Double humidity;
	
	@Column(name = "sea_pressure")
	private Double sea_pressure;
	
	@Column(name = "rainfall_day")
	private Double rainfall_day;
	
	@Column(name = "sun_hour")
	private Double sun_hour;
	
}
