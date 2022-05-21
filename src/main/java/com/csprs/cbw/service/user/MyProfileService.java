package com.csprs.cbw.service.user;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csprs.cbw.bean.user.MyProfile;
import com.csprs.cbw.bean.user.MyRole;
import com.csprs.cbw.dao.user.MyProfileDaoImpl;
import com.csprs.cbw.util.Constant;

@Service
public class MyProfileService {
	
	private final Logger logger = LoggerFactory.getLogger("CustomOperateLog");
	
	@Autowired
	private MyProfileDaoImpl myProfileDaoImpl;
	@Autowired
	private MyRoleService myRoleService;
	
	@Transactional
	public String SaveAccountByAddUpdateString(String addupdate) {
		String result_status = "";
		// 要小心如果資料裡面有;的話就完蛋了
		String[] addupdate_split = addupdate.split(";");
		Date current = new Date();
		try {
			String id = addupdate_split[0];
			String name = addupdate_split[1];
			String password = addupdate_split[2];
			String description = addupdate_split[3];
			String roles = addupdate_split[4];
			String mail = addupdate_split[5];
			List<MyRole> roleListBean = myRoleService.getRoleListBean(roles);
			// class 
			MyProfile profile = new MyProfile();
			profile.setName(name);
			profile.setPassword(password);
			profile.setDescription(description);
			profile.setRoles(roleListBean);
			profile.setMail(mail);
			profile.setLastModifiedTime(current);
			profile.setPwdHistory(password);
			// 創建
			if("".equals(id)) {
				profile.setCreatedTime(current);
				// 創建前必須要先查看 1.帳號是否存在
				int checkAccountExist = myProfileDaoImpl.checkAccountExist(name);
				if(checkAccountExist > 0) {
					result_status += "此帳號已存在 !!!";
				}else {
					myProfileDaoImpl.saveProfile(profile);
					logger.info("創建帳號: " + profile.getName());
				}
			// 更新
			}else {
				profile.setId(Long.parseLong(id));
				// 更新前必須要先查看 1.密碼資安 2.前三密碼對照
				String checkResult = checkUserCanUpdate(profile);
				if("ok".equals(checkResult)) {
					myProfileDaoImpl.updateProfile(profile);
					logger.info("更新帳號: " + profile.getName());
				}else {
					result_status = checkResult;
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		
		return result_status;
	}
	
	// 判斷是否可以更新 1.密碼資安 2.前三密碼對照
	private String checkUserCanUpdate(MyProfile profile) {
		String result = "ok";
		MyProfile user = myProfileDaoImpl.findById(profile.getId());
		if(user != null) {
			// 1.判斷密碼資安
			if(!"".equals(isPasswordPass(profile.getPassword()))) {
				result = "密碼不符合資安認證 !!!";
			}
			// 2.判斷前三密碼
			String[] histories = user.getPwdHistory().split(",");
			if (Arrays.stream(histories).anyMatch(p -> p.equalsIgnoreCase(profile.getPassword()))) {
				result = "與前三筆中的其中一筆密碼相同 !!!";
			}
		}
		return result;
	}
	
	public String isPasswordPass(String password) {
		String result = "";
		// length must longer than 8 char
		if(password.length() < 8) {
			result += "1";
		}
		// contains !@#$%&*
		boolean anyMatch = Arrays.stream(Constant.PWD_ALLOW_SYMBOL).anyMatch(p -> password.contains(p));
		if(!anyMatch) {
			result += "2";
		}
		// 必須要有大小寫
		boolean noUpper = Arrays.stream(password.split("")).noneMatch(p -> Character.isUpperCase(p.charAt(0)));
		boolean noLower = Arrays.stream(password.split("")).noneMatch(p -> Character.isLowerCase(p.charAt(0)));
		boolean noDigit = Arrays.stream(password.split("")).noneMatch(p -> Character.isDigit(p.charAt(0)));
		if(noUpper || noLower || noDigit) {
			result += "3";
		}
		// 不能有逗點
		if(Arrays.stream(Constant.PWD_NOTALLOW_SYMBOL).anyMatch(p -> password.contains(p))) {
			result += "4";
		}
		
		return result;
	}
	
	
	
}
