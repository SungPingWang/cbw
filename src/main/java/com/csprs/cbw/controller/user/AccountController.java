package com.csprs.cbw.controller.user;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.csprs.cbw.bean.user.MyProfile;
import com.csprs.cbw.dao.user.MyProfileDaoImpl;
import com.csprs.cbw.service.page.PaginationInterface;
import com.csprs.cbw.service.page.PaginationService;
import com.csprs.cbw.service.user.MyProfileService;


@Secured("hasRole('ROLE_ADMIN')")
@Controller
@RequestMapping("/account")
public class AccountController {
	
	private final Logger logger = LoggerFactory.getLogger("CustomOperateLog");
	
	@Autowired
	private MyProfileDaoImpl myProfileDaoImpl;
	@Autowired
	private MyProfileService myProfileService;

	@GetMapping()
	public String index(Map<String, Object> map, 
			@RequestParam(value = "page", defaultValue = "1") Integer page) {
		if(page == 0) {
			map.put("warning", "'page' not found error !!!");
			page = 1;
		}
		try {
			List<MyProfile> pageBeans = myProfileDaoImpl.getAllProfiles();
			PaginationInterface pageRequest = new PaginationService(pageBeans, 5);
			map.put("listPage", pageRequest.getRightPage(page));
			map.put("maxPage", pageRequest.getMaxPage());
			map.put("pageRequest", pageRequest);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return "account/index.html";
	}
	
	@RequestMapping(value="/addUpdate", produces = "text/plain;charset=UTF-8")
 	@ResponseBody
 	public String addUpdateAcc(@RequestBody String addupdate){
 		String json = "";
 		try {
			String saveAccountStatus = myProfileService.SaveAccountByAddUpdateString(addupdate);
			if("".equals(saveAccountStatus)) {
				json = "success";
			}else {
				json += saveAccountStatus;
			}
		} catch (Exception e) {
			json = "error";
			logger.error(e.toString());
		}
 		return json;
 	}
	
	
	// 刪除
	@RequestMapping("/deleteAccount/{id}")
	public String DeleteMyBean(Map<String, Object> map, @PathVariable(value = "id") String id,
			@RequestParam(value = "page", defaultValue = "1") Integer page) {
		//System.out.println("page:" + page);
		//System.out.println("id:" + id);
		if (page == 0) {
			map.put("warning", "'page' not found error !!!");
			page = 1;
		}
		try {
			//System.out.println("delete id " + id);
			 myProfileDaoImpl.deleteProfileById(id);
			List<MyProfile> pageBeans = myProfileDaoImpl.getAllProfiles();
			//System.out.println(pageBeans);
			PaginationInterface pageRequest = new PaginationService(pageBeans, 5);
			map.put("listPage", pageRequest.getRightPage(page));
			map.put("maxPage", pageRequest.getMaxPage());
			map.put("pageRequest", pageRequest);
		} catch (Exception e) {
			logger.error(e.toString());
		}

		return "redirect:/account";
	}
	
}
