package com.csprs.cbw.service.user;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csprs.cbw.bean.user.MyRole;
import com.csprs.cbw.dao.user.MyRoleDaoImpl;


@Service
public class MyRoleService {
	@Autowired
	private MyRoleDaoImpl myRoleDaoImpl;
	
	/** 輸入權限代表的代號
	 * 依照代號來解析所屬的權限並且返回List<MyRole>陣列
	 * */
	public List<MyRole> getRoleListBean(String roleCode) {
		String formatRoleCode = String.format("%03d",Integer.valueOf(roleCode));
		String ADMIN_Role = formatRoleCode.substring(0,1);
		String MANAGER_ROLE = formatRoleCode.substring(1,2);
		String USER_ROLE = formatRoleCode.substring(2,3);
		List<MyRole> roles = new ArrayList<>();
		
		if("1".equals(ADMIN_Role)) {
			MyRole role = new MyRole();
			role.setName("ADMIN");
			role.setDescription("superuser");
			if(myRoleDaoImpl.getRoleByName("ADMIN").getName() == null) {
				myRoleDaoImpl.saveRole(role);
			}
			MyRole myRole = myRoleDaoImpl.getRoleByName("ADMIN");
			roles.add(myRole);
		}
		if("1".equals(MANAGER_ROLE)) {
			MyRole role = new MyRole();
			role.setName("MANAGER");
			role.setDescription("manager");
			if(myRoleDaoImpl.getRoleByName("MANAGER").getName() == null) {
				myRoleDaoImpl.saveRole(role);
			}
			MyRole myRole = myRoleDaoImpl.getRoleByName("MANAGER");
			roles.add(myRole);
		}
		if("1".equals(USER_ROLE)) {
			MyRole role = new MyRole();
			role.setName("USER");
			role.setDescription("user");
			if(myRoleDaoImpl.getRoleByName("USER").getName() == null) {
				myRoleDaoImpl.saveRole(role);
			}
			MyRole myRole = myRoleDaoImpl.getRoleByName("USER");
			roles.add(myRole);
		}
		
		return roles;
	}
}
