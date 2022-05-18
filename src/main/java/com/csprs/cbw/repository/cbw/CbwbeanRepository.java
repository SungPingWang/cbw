package com.csprs.cbw.repository.cbw;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csprs.cbw.bean.cbw.cbwbean;


public interface CbwbeanRepository extends JpaRepository<cbwbean, Long> {
	
	/*獲取所有測站最新資料*/
	@Query(value = "SELECT *\r\n"
			+ "FROM (\r\n"
			+ "      SELECT A.city, A.station, MAX(A.curr_time) AS MAX_REG_DATE\r\n"
			+ "      FROM public.cbw_data as A\r\n"
			+ "      GROUP BY A.city, A.station\r\n"
			+ ") r\r\n"
			+ "INNER JOIN public.cbw_data t\r\n"
			+ "ON t.city = r.city AND t.station = r.station AND t.curr_time = r.MAX_REG_DATE", nativeQuery = true)
	List<cbwbean> findLatestCbwbeans();
	
	/*獲取特定縣市最新資料*/
	@Query(value = "SELECT *\r\n"
			+ "FROM (\r\n"
			+ "      SELECT A.city, A.station, MAX(A.curr_time) AS MAX_REG_DATE\r\n"
			+ "      FROM public.cbw_data as A\r\n"
			+ "      GROUP BY A.city, A.station\r\n"
			+ ") r\r\n"
			+ "INNER JOIN public.cbw_data t\r\n"
			+ "ON t.city = r.city AND t.station = r.station AND t.curr_time = r.MAX_REG_DATE\r\n"
			+ "WHERE t.city = ?;", nativeQuery = true)
	List<cbwbean> findUniqueCityLatestCbwbeans(String city);
	
	/*獲取特定測站當天資料*/
	@Query(value = "SELECT * from public.cbw_data WHERE station = ? AND curr_time >= ? ORDER BY curr_time DESC;", nativeQuery = true)
	List<cbwbean> findUniqueStationLatestCbwbeans(String station, Date DateGreaterThanString);
	
	/*獲取特定測站最新的一筆資料*/
	@Query(value = "SELECT *\r\n"
			+ "FROM (\r\n"
			+ "      SELECT A.city, A.station, MAX(A.curr_time) AS MAX_REG_DATE\r\n"
			+ "      FROM public.cbw_data as A\r\n"
			+ "      GROUP BY A.city, A.station\r\n"
			+ ") r\r\n"
			+ "INNER JOIN public.cbw_data t\r\n"
			+ "ON t.city = r.city AND t.station = r.station AND t.curr_time = r.MAX_REG_DATE\r\n"
			+ "WHERE t.station IN ('基隆', '臺北', '板橋', '新屋', '新竹', '臺中', '田中', '日月潭', '嘉義', '臺南', '高雄', '臺東', '花蓮', '宜蘭', '澎湖', '金門', '馬祖', '恆春', '東沙島', '南沙島');", nativeQuery = true)
	List<cbwbean> find20StationsLatestCbwbeans();
}
