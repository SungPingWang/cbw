package com.csprs.cbw.controller.gis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gis")
public class GisController {
	
	private final Logger logger = LoggerFactory.getLogger("CustomOperateLog");
	
	@GetMapping("/platform")
	public String index() {
		
		return "gis/index.html";
	}
	
}
