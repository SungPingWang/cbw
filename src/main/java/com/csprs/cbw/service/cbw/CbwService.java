package com.csprs.cbw.service.cbw;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.csprs.cbw.bean.cbw.cbwbean;
import com.csprs.cbw.bean.cbw.cbwstation;
import com.csprs.cbw.dao.cbw.CbwbeanDaoImpl;
import com.csprs.cbw.dao.cbw.CbwstationDaoImpl;


/**所有CBW資料的service*/
@Service
public class CbwService {
	
	@Autowired
	private CbwbeanDaoImpl cbwbeanDaoImpl;
	@Autowired
	private CbwstationDaoImpl cbwstationDaoImpl;
	
	private Map<String, String> CityCodeMap = new HashMap<>();
	
	public CbwService() {
		CityCodeMap.put("10017", "基隆市");
		CityCodeMap.put("63", "臺北市");
		CityCodeMap.put("65", "新北市");
		CityCodeMap.put("68", "桃園市");
		CityCodeMap.put("10018", "新竹市");
		CityCodeMap.put("10004", "新竹縣");
		CityCodeMap.put("10005", "苗栗縣");
		CityCodeMap.put("66", "臺中市");
		CityCodeMap.put("10007", "彰化縣");
		CityCodeMap.put("10008", "南投縣");
		CityCodeMap.put("10009", "雲林縣");
		CityCodeMap.put("10020", "嘉義市"); 
		CityCodeMap.put("10010", "嘉義縣");
		CityCodeMap.put("67", "臺南市");
		CityCodeMap.put("64", "高雄市");
		CityCodeMap.put("10013", "屏東縣");
		CityCodeMap.put("10002", "宜蘭縣");
		CityCodeMap.put("10015", "花蓮縣");
		CityCodeMap.put("10014", "臺東縣");
		CityCodeMap.put("10016", "澎湖縣");
		CityCodeMap.put("09020", "金門縣");
		CityCodeMap.put("09007", "連江縣");
	}
	
	// 獲取最新Cbw資料，此外是獨立city, station
	/** @這部份二級快取沒有用的原因
	 * 因為會不斷的切換查詢，所以查詢的東西部一樣，就不會有快取
	 * */
	@Cacheable(cacheNames = "lui_cache")
	public List<cbwbean> getLatestCbwbeans(){
		List<cbwbean> cbwbeans = cbwbeanDaoImpl.getLatestByCityStation();
		return cbwbeans;
	}
	
	// 獲取指定20測站最新Cbw資料
	public List<cbwbean> get20StationLatestCbwbeans() {
		List<cbwbean> cbwbeans = cbwbeanDaoImpl.get20StationLatestByCityStation();
		return cbwbeans;
	}

	// 獲取指定縣市最新Cbw資料，此外是獨立city, station
	@Cacheable(cacheNames = "lui_cache")
	public List<cbwbean> getCityLatestCbwbeans(String city) {
		List<cbwbean> cbwbeans = cbwbeanDaoImpl.getCityLatestByCityStation(city);
		return cbwbeans;
	}

	// 獲取指定測站在固定天(預設是前天開始)的
	@Cacheable(cacheNames = "lui_cache")
	public List<cbwbean> getStationYesterdayTilNowCbwbeans(String station) {
        List<cbwbean> cbwbeans = cbwbeanDaoImpl.getStationLatestByCityStation(station, yesterday());
		
		return cbwbeans;
	}
	
	// 獲取所有的測站
	@Cacheable(cacheNames = "lui_cache")
	public List<cbwstation> getAllCbwstations(){
		List<cbwstation> cbwstations = cbwstationDaoImpl.getCbwstations();
		return cbwstations;
	}
	
	// 獲取指定顯示在前端的測站
	@Cacheable(cacheNames = "lui_cache")
	public List<cbwstation> getShowCbwstations(){
		List<cbwstation> cbwstations = cbwstationDaoImpl.getDirectCbwStations();
		return cbwstations;
	}
	
	// **********************************************************************************
	
	private Date yesterday() {
	    final Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    cal.add(Calendar.DATE, -2);
	    return cal.getTime();
	}
	
	public String getCityByCode(String code) {
		return CityCodeMap.get(code);
	}
	
	// cbwbean轉為Json格式
	public String cbwbeanListToJson(List<cbwbean> cbwbeans) {
		
		String json = "[";
		int cnt = 0;
		for(cbwbean beans : cbwbeans) {
			String city = beans.getCity();
			String station = beans.getStation();
			String curr_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(beans.getCurr_time());
			String time = curr_time.substring(0, curr_time.length()-3);
			String temperature = String.valueOf(beans.getTemperature());
			String weather = beans.getWeather();
			String w_direction = beans.getW_direction();
			String w_power = beans.getW_power();
			String w_level = beans.getW_level();
			String visibility = beans.getVisibility();
			String humidity = String.valueOf(beans.getHumidity());
			String sea_pressure = String.valueOf(beans.getSea_pressure());
			String rainfall_day = String.valueOf(beans.getRainfall_day());
			String sun_hour = String.valueOf(beans.getSun_hour());
			
			String instance_json = "{'city':'"+city+"','station':'"+station+"','curr_time':'"+time+"',"+
					"'temperature':'"+temperature+"','weather':'"+weather+"','w_direction':'"+w_direction+"','w_power':'"+w_power+"',"+
					"'w_level':'"+w_level+"','visibility':'"+visibility+"','humidity':'"+humidity+"','sea_pressure':'"+sea_pressure+"',"+
					"'rainfall_day':'"+rainfall_day+"','sun_hour':'"+sun_hour+"'}";
			
			if(cnt == 0) {
				json += instance_json;
			}else {
				json += "," + instance_json;
			}
			cnt ++;
		}
		json += "]";
		
		return json;
	}
	
	
}
