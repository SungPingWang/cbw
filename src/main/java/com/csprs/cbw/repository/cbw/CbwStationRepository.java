package com.csprs.cbw.repository.cbw;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csprs.cbw.bean.cbw.cbwstation;


public interface CbwStationRepository extends JpaRepository<cbwstation, Long> {
	
	@Query(value = "SELECT * FROM public.cbw_station WHERE station in ('基隆', '臺北', '板橋', '新屋', '新竹', '臺中', '田中', '日月潭', '嘉義', '臺南', '高雄', \r\n"
			+ "	'臺東', '花蓮', '宜蘭', '澎湖', '金門', '馬祖', '恆春', '東沙島', '南沙島');", nativeQuery = true)
	List<cbwstation> getShowingCbwStation();
	
}
