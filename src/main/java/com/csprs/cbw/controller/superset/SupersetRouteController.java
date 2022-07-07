package com.csprs.cbw.controller.superset;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.csprs.cbw.bean.superset.SupersetResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@CrossOrigin
@Controller
public class SupersetRouteController {
	
	@Value("${superset.url}")
	private String SUPERSET_URL;
	@Value("${superset.username}")
	private String SUPERSET_USERNAME;
	@Value("${superset.password}")
	private String SUPERSET_PASSWORD;
	@Value("${superset.provider}")
	private String SUPERSET_PROVIDER;
	
	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/redirect")
	public void redirect(HttpServletResponse response) {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode node = objectMapper.createObjectNode();
		node.put("username", SUPERSET_USERNAME);
		node.put("password", SUPERSET_PASSWORD);
		node.put("provider", SUPERSET_PROVIDER);
		node.put("refresh", true);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<String> request = new HttpEntity<String>(node.toString(), headers);
		String url = SUPERSET_URL + "/api/v1/security/login";
		ResponseEntity<SupersetResponse> postForEntity = restTemplate.postForEntity(url, request, SupersetResponse.class);
		String url2 = SUPERSET_URL +  "/superset/welcome/";
		//String url2 = SUPERSET_URL +  "/superset/explore/p/kvP5m1Zl92O/";
		response.setHeader("X-CSRFToken", postForEntity.getBody().getAccess_token());
		response.setHeader("Accept", "application/json");
		response.setHeader("Referer", url2);
		response.setHeader("Location", url2); // 重新導向的url
        response.setStatus(HttpServletResponse.SC_FOUND); // 302 Found
	}
	
	
	@GetMapping("/redirectIframe")
	public String redirectIframe(Map<String, Object> map) {
		map.put("chart1", "http://172.22.110.27:8787/superset/explore/p/jkx2A1nw6PW/?standalone=1&height=400");
		map.put("chart2", "http://172.22.110.27:8787/superset/explore/p/ogJDA6Y72nx/?standalone=1&height=400");
		map.put("chart3", "http://172.22.110.27:8787/superset/explore/p/W2lLwvNwomN/?standalone=1&height=400");
		map.put("chart4", "http://172.22.110.27:8787/superset/explore/p/PZ6zwp0w1k8/?standalone=1&height=400");
		
		return "superset/iframe.html";
	}
	

}
