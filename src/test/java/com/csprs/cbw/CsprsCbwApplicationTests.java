package com.csprs.cbw;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.csprs.cbw.bean.cbw.cbwbean;
import com.csprs.cbw.bean.cbw.cbwstation;
import com.csprs.cbw.bean.superset.SupersetResponse;
import com.csprs.cbw.bean.user.LoginInfo;
import com.csprs.cbw.bean.user.Permission;
import com.csprs.cbw.dao.user.PermissionDaoImpl;
import com.csprs.cbw.repository.user.LoginInfoRepository;
import com.csprs.cbw.repository.user.MyProfileRepository;
import com.csprs.cbw.service.cbw.CbwService;
import com.csprs.cbw.service.user.LoginInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class CsprsCbwApplicationTests {
	@Autowired CbwService cbwService;

	private final Logger logger = LoggerFactory.getLogger("CustomOperateLog");
	
	@Autowired
	private MyProfileRepository myProfileRepository;
	@Autowired
	private PermissionDaoImpl permissionDaoImpl;
	@Autowired
	private LoginInfoService loginInfoService;
	@Autowired
	private LoginInfoRepository loginInfoRepository;
	@Autowired
	private RestTemplate restTemplate;
	
	@Test
	void TestRestTemplate() {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode node = objectMapper.createObjectNode();
		node.put("username", "admin");
		node.put("password", "admin");
		node.put("provider", "db");
		node.put("refresh", true);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<String> request = new HttpEntity<String>(node.toString(), headers);
		
		
		String url = "http://172.22.110.27:8088/api/v1/security/login";
		ResponseEntity<SupersetResponse> postForEntity = restTemplate.postForEntity(url, request, SupersetResponse.class);
		System.out.println(postForEntity.getBody().getAccess_token());
	}
	
	@Test
	void testLoginInfoService() {
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setAccount("Lui");
		loginInfo.setLoginTime(new Date());
		loginInfoRepository.save(loginInfo);
		System.out.println("ok");
	}
	
	@Test
	void testPermission() {
		
		List<String> adminList = new ArrayList<String>();
		List<String> managerList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		List<Permission> permissions = permissionDaoImpl.getAllPermission();
		StringBuffer buf = new StringBuffer();
		// 獲取不重複的name名字
		Set<String> set = new HashSet<>();
		for(int i=0; i<permissions.size(); i++) {
			set.add(permissions.get(i).getName());
		}
		Object[] array = set.toArray();
		
		for(int i=0; i<array.length; i++) {
			String object = (String)array[i];
			List<Permission> collects = permissions.stream()
					.filter(p -> p.getName().equals(object))
					.map(p -> new Permission(p.getId(), p.getGroup(), p.getName(), "\""+p.getPermissionCode()+"\""))
					.collect(Collectors.toList());
			String join = "";
			Set<String> set2 = new HashSet<>();
			collects.forEach(p -> set2.add(p.getPermissionCode()));
			System.out.println(set2.size());
			collects.forEach(p -> System.out.println(p.getPermissionCode()));
			for(int j=0; j<collects.size(); j++) {
				set2.add(collects.get(j).getPermissionCode());
			}
			join = String.join(",", set2.toArray(new String[set2.size()]));
			String str = "{\""+collects.get(0).getName()+"\":[\""+collects.get(0).getName()+"\","+join+"]}\r\n";

			if("admin".equals(collects.get(0).getGroup())) {
				adminList.add(str);
			}else if("manager".equals(collects.get(0).getGroup())) {
				managerList.add(str);
			}else {
				userList.add(str);
			}
		}
		buf.append("{\r\n");
		buf.append("\"admin\": [\r\n").append(String.join(",", adminList.toArray(new String[adminList.size()]))).append("],\r\n");
		buf.append("\"manager\": [\r\n").append(String.join(",", managerList.toArray(new String[managerList.size()]))).append("],\r\n");
		buf.append("\"user\": [\r\n").append(String.join(",", userList.toArray(new String[userList.size()]))).append("]\r\n");
		buf.append("}");
		System.out.println(buf.toString());
	}
	
	@Test
	void testSplit() {
		String newHistory = "";
		String newstr = "he";
		String test1 = "1,2,3";
		String test2 = "1,2";
		String test3 = "1";
		
		String[] containers = test2.split(",");
		if(containers.length != 3) {
			newHistory = String.join(",", containers) +","+ newstr;
		}else {
			newHistory = containers[0] +","+ containers[1] + "," + newstr;
		}
		System.out.println(newHistory);
		
	}
	
	@Test
	void contextLoads() {
		List<cbwstation> cbwstationsView = cbwService.getShowCbwstations();
		System.out.println(cbwstationsView);
		
		// 獲取展示20測站的最新資料
		List<cbwbean> Station20Cbwbeans = cbwService.get20StationLatestCbwbeans();
		String cbwbeanListToJson = cbwService.cbwbeanListToJson(Station20Cbwbeans);
		System.out.println("Station20Cbwbeans: " + Station20Cbwbeans);
	}
	
	@Test
	void PostSuperset() throws Exception { 
		/*DefaultHttpClient httpClient = new DefaultHttpClient();
		
		HttpPost httpPost = new HttpPost("http://172.22.110.17/login/?next=http%3A%2F%2F172.22.110.17%2Fsuperset%2Fdashboard%2Fdisaster%2F%3Fpreselect_filters%3D%257B%257D");
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("username", "admin"));
		parameters.add(new BasicNameValuePair("password", "admin"));
		
		HttpEntity entity = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
		httpPost.setEntity(entity);
		httpPost.setHeader("Cookie", "session=.eJwlzjsKwzAMANC7eM5gS_JHuUzQz7QQWkjaqfTuDXR82_ukbR5x3tL6Ot6xpO3uaU1Axh36UGwWCkUhWpMyciuMjjIHKQ4MyNbU1aEVtWw8suZQFQdyd50k0ohKZi1KHtUmdHbGbBWoiTPbCJ-BUyohl16FKk5LS9qfJntcl3hcep9x_GslfX-B-DQV.YKIu6A.D0A0V-a_Pm937UnWUss8_c1-dug");
		
		HttpResponse response = httpClient.execute(httpPost);
		System.out.println(response.getStatusLine().getStatusCode());
		httpPost.abort();*/
		
		
		//String redirectURL = response.getFirstHeader("Cookie").getValue();
		//HttpGet httpGet = new HttpGet(redirectURL);
		//HttpResponse response2 = httpClient.execute(httpGet);
		
		
		//System.out.println(response2.toString());
		//httpGet.abort();
		
		//System.out.println(redirectURL);
		
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
                GetMethod getMethod =  new  GetMethod("http://172.22.110.17/superset/dashboard/disaster/");
                // 每次访问需授权的网址时需带上前面的 cookie 作为通行证
                getMethod.setRequestHeader( "cookie" , tmpcookies.toString());
                postMethod.setRequestHeader( "Connection", "keep-alive" );
                postMethod.setRequestHeader( "Host", "172.22.110.17" );
                postMethod.setRequestHeader( "Connection", "keep-alive" );
                postMethod.setRequestHeader( "Upgrade-Insecure-Requests", "1" );
                postMethod.setRequestHeader( "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36" );
                httpClient.executeMethod(getMethod);
                
                //httpPost.setHeader("Cookie", "session=.eJwlzjsKwzAMANC7eM5gS_JHuUzQz7QQWkjaqfTuDXR82_ukbR5x3tL6Ot6xpO3uaU1Axh36UGwWCkUhWpMyciuMjjIHKQ4MyNbU1aEVtWw8suZQFQdyd50k0ohKZi1KHtUmdHbGbBWoiTPbCJ-BUyohl16FKk5LS9qfJntcl3hcep9x_GslfX-B-DQV.YKIu6A.D0A0V-a_Pm937UnWUss8_c1-dug");
    			
                // 打印出返回数据，检验一下是否成功
                String text = getMethod.getResponseBodyAsString();
                System.out.println(text);
            }
            else  {
                System.out.println( "登录失败" );
            }
        }
        catch  (Exception e) {
        	logger.error(e.toString());
        }
		
		
	}

}
