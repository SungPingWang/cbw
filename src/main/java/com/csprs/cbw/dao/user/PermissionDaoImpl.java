package com.csprs.cbw.dao.user;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.csprs.cbw.bean.user.Permission;
import com.csprs.cbw.repository.user.PermissionRepository;

@Component
public class PermissionDaoImpl {
	@Autowired
	PermissionRepository permissionRepository;
	
	public List<Permission> getAllPermission() {
		List<Permission> findAll = permissionRepository.findAll();
		return findAll;
	}
}
