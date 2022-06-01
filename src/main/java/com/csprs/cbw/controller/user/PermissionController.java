package com.csprs.cbw.controller.user;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.csprs.cbw.bean.user.Permission;
import com.csprs.cbw.service.permission.PermissionService;

@Controller
@RequestMapping("/permission")
public class PermissionController {
	
	@Autowired
	private PermissionService permissionService;
	
	@GetMapping()
	public String index(Map<String, Object> map) {
		List<Permission> permissions = permissionService.getAllPermission();
		String permissionJson = permissionService.getPermissionJson();
		String permissionJson2 = permissionService.getPermissionJson(permissions);
		map.put("permissionJson", permissionJson);
		map.put("permissionJson2", permissionJson2);
		return "permission/index.html";
	}
	
}
