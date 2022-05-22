package com.csprs.cbw.service.user;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csprs.cbw.bean.user.MyRole;
import com.csprs.cbw.dao.user.MyRoleDaoImpl;
import com.csprs.cbw.util.Constant;


@Service
public class MyRoleService {
	@Autowired
	private MyRoleDaoImpl myRoleDaoImpl;
	
	/** 輸入權限代表的代號
	 * 依照代號來解析所屬的權限並且返回List<MyRole>陣列
	 * */
	public List<MyRole> getRoleListBean(String roleCode) {
		String formatRoleCode = String.format("%03d",Integer.valueOf(roleCode));
		// 傳進來的值如：(101) --> (ADMIN,MANAGER,USER)
		String ADMIN_Role = formatRoleCode.substring(0,1);
		String MANAGER_ROLE = formatRoleCode.substring(1,2);
		String USER_ROLE = formatRoleCode.substring(2,3);
		List<MyRole> roles = new ArrayList<>();
		
		if((Constant.ONE).equals(ADMIN_Role)) {
			MyRole role = new MyRole();
			role.setName(Constant.AUTH_ADMIN);
			role.setDescription(Constant.AUTH_ADMIN_DESCRIPTION);
			if(myRoleDaoImpl.getRoleByName(Constant.AUTH_ADMIN).getName() == null) {
				myRoleDaoImpl.saveRole(role);
			}
			MyRole myRole = myRoleDaoImpl.getRoleByName(Constant.AUTH_ADMIN);
			roles.add(myRole);
		}
		if((Constant.ONE).equals(MANAGER_ROLE)) {
			MyRole role = new MyRole();
			role.setName(Constant.AUTH_MANAGER);
			role.setDescription(Constant.AUTH_MANAGER_DESCRIPTION);
			if(myRoleDaoImpl.getRoleByName(Constant.AUTH_MANAGER).getName() == null) {
				myRoleDaoImpl.saveRole(role);
			}
			MyRole myRole = myRoleDaoImpl.getRoleByName(Constant.AUTH_MANAGER);
			roles.add(myRole);
		}
		if((Constant.ONE).equals(USER_ROLE)) {
			MyRole role = new MyRole();
			role.setName(Constant.AUTH_USER);
			role.setDescription(Constant.AUTH_USER_DESCRIPTION);
			if(myRoleDaoImpl.getRoleByName(Constant.AUTH_USER).getName() == null) {
				myRoleDaoImpl.saveRole(role);
			}
			MyRole myRole = myRoleDaoImpl.getRoleByName(Constant.AUTH_USER);
			roles.add(myRole);
		}
		
		return roles;
	}
}
