package com.csprs.cbw.dao.cbw;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csprs.cbw.bean.cbw.cbwstation;
import com.csprs.cbw.repository.cbw.CbwStationRepository;



@Service
public class CbwstationDaoImpl {
	
	@Autowired
	private CbwStationRepository cbwStationRepository;
	
	public List<cbwstation> getCbwstations() {
		List<cbwstation> findAll = cbwStationRepository.findAll();
		return findAll;
	}
	
	public List<cbwstation> getDirectCbwStations(){
		List<cbwstation> showingCbwStation = cbwStationRepository.getShowingCbwStation();
		return showingCbwStation;
	}
	
	
}
