package com.csprs.cbw.service.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csprs.cbw.bean.user.MyProfile;
import com.csprs.cbw.bean.user.MyRole;
import com.csprs.cbw.dao.user.MyProfileDaoImpl;

@Service
public class MyProfileService {
	
	private final Logger logger = LoggerFactory.getLogger("CustomOperateLog");
	
	@Autowired
	private MyProfileDaoImpl myProfileDaoImpl;
	@Autowired
	private MyRoleService myRoleService;
	
	public String SaveAccountByAddUpdateString(String addupdate) {
		String result_status = "";
		String[] addupdate_split = addupdate.split(";");
		try {
			String id = addupdate_split[0];
			String name = addupdate_split[1];
			String password = addupdate_split[2];
			String description = addupdate_split[3];
			String roles = addupdate_split[4];
			String mail = addupdate_split[5];
			List<MyRole> roleListBean = myRoleService.getRoleListBean(roles);
			
			if("".equals(id)) {
				MyProfile profile = new MyProfile();
				profile.setName(name);
				profile.setPassword(password);
				profile.setDescription(description);
				profile.setRoles(roleListBean);
				profile.setMail(mail);
				int checkAccountExist = myProfileDaoImpl.checkAccountExist(name);
				if(checkAccountExist > 0) {
					result_status += "exist account name !!!";
				}else {
					myProfileDaoImpl.saveProfile(profile);
				};
				
			}else {
				myProfileDaoImpl.updateProfile(id, name, password, description, roleListBean, mail);
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		
		return result_status;
	}
	
	
	
	
}
