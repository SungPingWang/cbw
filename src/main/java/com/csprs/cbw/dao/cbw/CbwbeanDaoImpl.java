package com.csprs.cbw.dao.cbw;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csprs.cbw.bean.cbw.cbwbean;
import com.csprs.cbw.repository.cbw.CbwbeanRepository;



@Service
public class CbwbeanDaoImpl {
	
	@Autowired
	private CbwbeanRepository cbwbeanRepository;
	
	public List<cbwbean> getLatestByCityStation() {
		List<cbwbean> findLatestCbwbeans = cbwbeanRepository.findLatestCbwbeans();
		return findLatestCbwbeans;
	}
	
	public List<cbwbean> getCityLatestByCityStation(String city) {
		List<cbwbean> findLatestCbwbeans = cbwbeanRepository.findUniqueCityLatestCbwbeans(city);
		return findLatestCbwbeans;
	}
	
	public List<cbwbean> getStationLatestByCityStation(String station, Date DateGreaterThanString) {
		List<cbwbean> findLatestCbwbeans = cbwbeanRepository.findUniqueStationLatestCbwbeans(station, DateGreaterThanString);
		return findLatestCbwbeans;
	}
	
	public List<cbwbean> get20StationLatestByCityStation() {
		List<cbwbean> find20LatestCbwbeans = cbwbeanRepository.find20StationsLatestCbwbeans();
		return find20LatestCbwbeans;
	}
	
	
}
