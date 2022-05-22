package com.csprs.cbw.controller.cbw;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.csprs.cbw.bean.cbw.cbwstation;
import com.csprs.cbw.dao.cbw.CbwstationDaoImpl;
import com.csprs.cbw.service.page.PaginationInterface;
import com.csprs.cbw.service.page.PaginationService;
import com.csprs.cbw.util.Constant;


@Controller
@RequestMapping("/cbw_station")
public class CbwStationController {
	
	private final Logger logger = LoggerFactory.getLogger("CustomOperateLog");
	
	@Autowired
	private CbwstationDaoImpl cbwstationDao;

	@GetMapping()
	public String index(Map<String, Object> map, 
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "city", defaultValue = "") String city) {
		if(page == 0) {
			map.put("warning", Constant.CBW_STATION_NOT_FOUND);
			page = 1;
		}
		try {
			List<cbwstation> pageBeans = cbwstationDao.getCbwstations();
			if(!"".equals(city) && !"全縣市".equals(city)) {
				pageBeans = pageBeans.stream().filter(p -> city.equals(p.getCity())).collect(Collectors.toList());
				map.put("searchCity", city);
			}
			PaginationInterface pageRequest = new PaginationService(pageBeans, 10);
			map.put("listPage", pageRequest.getRightPage(page));
			map.put("maxPage", pageRequest.getMaxPage());
			map.put("pageRequest", pageRequest);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return "cbw_station/index.html";
	}
	
}
