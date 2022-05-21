package com.csprs.cbw.service.manage;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csprs.cbw.bean.user.MyProfile;
import com.csprs.cbw.dao.user.MyProfileDaoImpl;
import com.csprs.cbw.service.user.MyProfileService;
import com.csprs.cbw.util.Constant;

@Service
public class LatestNewService {
	
	@Autowired
	private MyProfileService profileService;
	@Autowired
	private MyProfileDaoImpl profileDaoImpl;

	// 獲取警告
	public List<String> checkWarningMessage(String account) {
		List<String> resultLst = new ArrayList<String>();
		
		MyProfile profile = profileDaoImpl.findByName(account);
		String passwordPass = profileService.isPasswordPass(profile.getPassword());
		if(!"".equals(passwordPass)) {
			resultLst.add(Constant.PWD_NOTALLOW_MSG);
		}
		// 假設我們沒有
		resultLst.add(Constant.MAIL_NOTEXIST_MSG);
		resultLst.add(Constant.PWD_EXPIRED_MSG);
		return resultLst;
	}
	
}
