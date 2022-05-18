package com.csprs.cbw.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/mapping")
public class DisplayController {
	
	@RequestMapping("/profile")
	public String profile() {
		
		return "profile.html";
	}
	
}
