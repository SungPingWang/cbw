package com.csprs.cbw.controller.cbw;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.csprs.cbw.bean.cbw.cbwbean;
import com.csprs.cbw.bean.cbw.cbwstation;
import com.csprs.cbw.service.cbw.CbwService;


@Controller
@RequestMapping("/cwb")
public class CbwController {
	
	private final Logger logger = LoggerFactory.getLogger("CustomOperateLog");
	
	@Autowired 
	private CbwService cbwService;
	
	// 防止有些人沒有打index
	@RequestMapping()
	public String index() {
		return "redirect:/cwb/index";
	}
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String IndexController(Map<String, Object> map, HttpSession session) {
		session.removeAttribute("CityLatestCBWBeans");
		List<cbwstation> cbwstationsView = cbwService.getShowCbwstations();
		map.put("showingStation", cbwstationsView);
		
		// 獲取展示20測站的最新資料
		List<cbwbean> Station20Cbwbeans = cbwService.get20StationLatestCbwbeans();
		String cbwbeanListToJson = cbwService.cbwbeanListToJson(Station20Cbwbeans);
		//System.out.println("Station20Cbwbeans: " + Station20Cbwbeans);
		map.put("station20Cbwbeans", cbwbeanListToJson);
		
		return "index.html";
	}
	
	/** @ControllerAction
	 * 下拉選單Ajax
	 * */
 	@RequestMapping(value="/cityGetData", produces = "text/plain;charset=UTF-8")
 	@ResponseBody
 	public String cityGetData(@RequestBody String code, HttpSession session){
 		String cbwbeansJson = "";
 		String city = cbwService.getCityByCode(code);
 		List<cbwbean> cityLatestCbwbeans = cbwService.getCityLatestCbwbeans(city);
 		session.setAttribute("CityLatestCBWBeans", cityLatestCbwbeans);
 		try {
			cbwbeansJson = cbwService.cbwbeanListToJson(cityLatestCbwbeans);
		} catch (Exception e) {
			logger.error(e.toString());
		}
 		
 		return cbwbeansJson;
 	}
	
 	@RequestMapping(value="/stationGetData", produces = "text/plain;charset=UTF-8")
 	@ResponseBody
 	public String stationGetData(@RequestBody String station){
 		String cbwbeansJson = "";
 		List<cbwbean> StationYesterdayTilNowCbwbeans = cbwService.getStationYesterdayTilNowCbwbeans(station);
 		
 		try {
 			cbwbeansJson = cbwService.cbwbeanListToJson(StationYesterdayTilNowCbwbeans);
		} catch (Exception e) {
			logger.error(e.toString());
		}
 		
 		return cbwbeansJson;
 	}
 	
 	/** @throws IOException 
 	 * @ControllerAction
	 * 下載 https://www.codejava.net/frameworks/spring-boot/csv-export-example
	 * */
 	@SuppressWarnings("all")
 	@RequestMapping(value="/download_Csv", produces = "text/plain;charset=UTF-8")
 	@ResponseBody
 	public void downloadCityCBWbeans(@RequestBody(required = false) String station, HttpSession session, HttpServletResponse response) throws IOException{
 		List<cbwbean> cbwbeans = (List<cbwbean>)session.getAttribute("CityLatestCBWBeans");
 		response.setContentType("text/csv;charset=UTF-8");
        String headerKey = "Content-Disposition";
        String fileName = java.net.URLEncoder.encode("縣市最新氣象資料", "UTF-8");
        String headerValue = "attachment; filename="+fileName+".csv";
        response.setHeader(headerKey, headerValue);
         
        // response.getWriter()
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"Id", "縣市", "測站名稱", "觀測時間", "溫度攝氏", "天氣", "風向", 
                "風力級", "陣風級", "能見度公里", "相對溼度百分比", "海平面氣壓百帕", 
                "當日累積雨量毫米", "日照時數"};
        String[] nameMapping = {"id", "city", "station", "curr_time", "temperature", "weather", "w_direction", "w_power", "w_level",
        		"visibility", "humidity", "sea_pressure", "rainfall_day", "sun_hour"};
         
        csvWriter.writeHeader(csvHeader);
        
        for (cbwbean bean : cbwbeans) {           
            csvWriter.write(bean, nameMapping);
        }
         
        csvWriter.close();
 	}
 	
 	@Deprecated
 	//@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String dashboardAction(RedirectAttributes redirectAttributes) {
 		
 		HttpClient httpClient =  new  HttpClient();
 		PostMethod postMethod =  new  PostMethod("http://172.22.110.17/login/");
 		NameValuePair[] data = {  new  NameValuePair( "username" ,  "admin" ),  new  NameValuePair( "password" ,  "admin" ) };
 		postMethod.setRequestBody(data);
 		
 		try  {
            // 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
            httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            int  statusCode=httpClient.executeMethod(postMethod);
                            
            // 获得登陆后的 Cookie
            Cookie[] cookies = httpClient.getState().getCookies();
            StringBuffer tmpcookies =  new  StringBuffer();
            for  (Cookie c : cookies) {
                tmpcookies.append(c.toString() +  ";" );
                System.out.println( "cookies = " +c.toString());
            }
            if (statusCode== 302 ){ //重定向到新的URL
                System.out.println( "模拟登录成功" );
                // 进行登陆后的操作
                //GetMethod getMethod =  new  GetMethod("http://172.22.110.17/superset/dashboard/disaster/");
                // 每次访问需授权的网址时需带上前面的 cookie 作为通行证
                //getMethod.setRequestHeader( "cookie" , tmpcookies.toString());
                //postMethod.setRequestHeader( "Connection", "keep-alive" );
                //postMethod.setRequestHeader( "Host", "172.22.110.17" );
                //postMethod.setRequestHeader( "Upgrade-Insecure-Requests", "1" );
                //postMethod.setRequestHeader( "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36" );
                //httpClient.executeMethod(getMethod);
                
                //httpPost.setHeader("Cookie", "session=.eJwlzjsKwzAMANC7eM5gS_JHuUzQz7QQWkjaqfTuDXR82_ukbR5x3tL6Ot6xpO3uaU1Axh36UGwWCkUhWpMyciuMjjIHKQ4MyNbU1aEVtWw8suZQFQdyd50k0ohKZi1KHtUmdHbGbBWoiTPbCJ-BUyohl16FKk5LS9qfJntcl3hcep9x_GslfX-B-DQV.YKIu6A.D0A0V-a_Pm937UnWUss8_c1-dug");
    			
                // 打印出返回数据，检验一下是否成功
                //String text = getMethod.getResponseBodyAsString();
                //System.out.println(text);
                
                // http://172.22.110.17/superset/dashboard/disaster/
                redirectAttributes.addAttribute("Cookie", tmpcookies.toString());
                //return "redirect:http://172.22.110.17/superset/dashboard/disaster/?preselect_filters=%7B%7D";
            }
            else  {
                logger.error("登入失敗");
            }
        }
        catch  (Exception e) {
            logger.error(e.toString());
        }
 		//return "redirect:https://google.com";
 		
 		return "superset.html";
	}
}
