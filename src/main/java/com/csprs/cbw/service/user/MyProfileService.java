package com.csprs.cbw.service.user;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.csprs.cbw.bean.user.MyProfile;
import com.csprs.cbw.bean.user.MyRole;
import com.csprs.cbw.dao.user.MyProfileDaoImpl;
import com.csprs.cbw.util.Constant;
import com.csprs.cbw.util.Utils;

@Service
public class MyProfileService {
	
	private final Logger logger = LoggerFactory.getLogger("CustomOperateLog");
	
	@Autowired
	private MyProfileDaoImpl myProfileDaoImpl;
	@Autowired
	private MyRoleService myRoleService;
	
	// 後端傳回的字串進行編輯或新增帳號
	@Transactional
	public String SaveAccountByAddUpdateString(String addupdate) {
		// 1 獲取帳號名稱以及是否為ADMIN帳號
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String account = authentication.getName();
		boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> Constant.ROLE_ADMIN.equals(a.getAuthority()));

		String result_status = Constant.BLANK;
		// 2 !!! 字串是以分號代替的，要小心如果資料裡面有;的話就完蛋了
		String[] addupdate_split = addupdate.split(Constant.SEMICOLON);
		// 3 獲取當天日期時間
		Date current = new Date();
		try {
			// 3.1 切分字串
			String id = addupdate_split[0];
			String name = addupdate_split[1];
			String password = addupdate_split[2];
			String description = addupdate_split[3];
			String roles = addupdate_split[4];
			String mail = addupdate_split[5];
			// 3.2 根據roles字串(EX:101)轉變為LIST型態
			List<MyRole> roleListBean = myRoleService.getRoleListBean(roles);
			// 3.3 宣告profile並輸入
			MyProfile profile = new MyProfile();
			profile.setName(name);
			profile.setPassword(password);
			profile.setDescription(description);
			profile.setRoles(roleListBean);
			profile.setMail(mail);
			profile.setLastModifiedTime(current);
			profile.setPwdHistory(password);
			// 3.4 利用ID是否為空判斷是否屬於創建(還要判斷ADMIN權限才能使用)
			if(Constant.BLANK.equals(id) && isAdmin) {
				profile.setCreatedTime(current);
				// 3.4.1 創建前必須要先查看 1.帳號是否存在
				int checkAccountExist = myProfileDaoImpl.checkAccountExist(name);
				if(checkAccountExist > 0) {
					result_status += Constant.ACCOUNT_EXISTED;
				}else {
					// 3.4.2 存取帳號前要先判斷是否符合密碼資安
					String checkPwdAllow = checkUserCanCreate(profile);
					if(Constant.IS_OK.equals(checkPwdAllow)) {
						myProfileDaoImpl.saveProfile(profile);
						logger.info("創建帳號: " + profile.getName());
					}else {
						return checkPwdAllow;
					}
				}
			// 3.5 更新(ADMIN才能更新所有人，其他的必須看帳號是否屬於自己)
			}else {
				// 3.6 如果這人既不是ADMIN帳號，且該帳號也不是他的，就報錯
				if(!isAdmin && !account.equals(name)) {
					return Constant.ACCOUNT_NOAUTH_CRUD;
				}
				// 3.7 更新profile，ID設定進給新的profile數據
				profile.setId(Long.parseLong(id));
				// 3.8 獲取以前的profile
				MyProfile oldProfile = myProfileDaoImpl.findById(Long.parseLong(id));
				// 3.9 更新前必須要先查看 1.密碼資安 2.前三密碼對照
				String checkResult = checkUserCanUpdate(profile, oldProfile);
				if(Constant.IS_OK.equals(checkResult)) {
					myProfileDaoImpl.updateProfile(profile, oldProfile, isAdmin);
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
	private String checkUserCanUpdate(MyProfile profile, MyProfile oldProfile) {
		String result = Constant.IS_OK;
		if(oldProfile != null) {
			// 1.判斷密碼資安
			if(!"".equals(isPasswordPass(profile.getPassword()))) {
				result = Constant.PWD_CRUD_NOTALLOW_MSG;
			}
			// 2.判斷前三密碼
			if(!Utils.isNullEmptyString(oldProfile.getPwdHistory())) {
				String[] histories = oldProfile.getPwdHistory().split(Constant.COMMA);
				if (Arrays.stream(histories).anyMatch(p -> p.equalsIgnoreCase(profile.getPassword()))) {
					result = Constant.PWD_CRUD_DUPLICATE_MSG;
				}
			}
		}
		return result;
	}
	
	// 判斷是否可以更新 1.密碼資安 2.前三密碼對照
	private String checkUserCanCreate(MyProfile profile) {
		String result = Constant.IS_OK;
		if (!"".equals(isPasswordPass(profile.getPassword()))) {
			result = Constant.PWD_CRUD_NOTALLOW_MSG;
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
