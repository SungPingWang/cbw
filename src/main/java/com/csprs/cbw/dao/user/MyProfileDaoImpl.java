package com.csprs.cbw.dao.user;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csprs.cbw.bean.user.MyProfile;
import com.csprs.cbw.repository.user.MyProfileRepository;
import com.csprs.cbw.util.Utils;


@Service
public class MyProfileDaoImpl {
	
	private final Logger logger = LoggerFactory.getLogger("CustomOperateLog");
	
	@Autowired
	public MyProfileRepository myProfileRepository;
	
	// get all users
	@Transactional
	public List<MyProfile> getAllProfiles(){
		List<MyProfile> profiles = new ArrayList<>();
		try {
			profiles = myProfileRepository.findAll();
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return profiles;
	}
	
	// 儲存profile
	@Transactional
	public void saveProfile(MyProfile myProfile) {
		try {
			myProfileRepository.save(myProfile);
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}
	
	// 更新profile
	@Transactional
	public void updateProfile(MyProfile profile, MyProfile oldProfile, Boolean isAdmin) {
		
		try {
			String pwd3History = "";
			if(!Utils.isNullEmptyString(oldProfile.getPwdHistory())) {
				String[] histories = oldProfile.getPwdHistory().split(",");
				if(histories.length != 3) {
					pwd3History = String.join(",", histories) +","+ profile.getPwdHistory();
				}else {
					pwd3History = histories[1] +","+ histories[2] + "," + profile.getPwdHistory();
				}
			}else {
				pwd3History = profile.getPwdHistory();
			}
			
			profile.setPwdHistory(pwd3History);
			profile.setCreatedTime(oldProfile.getCreatedTime());
			// isAdmin --> 是否為ADMIN更新任何人資料，如果不是就代表是USER個人在編輯自己的資料
			if(!isAdmin) {
				profile.setRoles(oldProfile.getRolesList());
			}
			myProfileRepository.save(profile);
		} catch (NumberFormatException e) {
			logger.error(e.toString());
		}
	}
	
	// 刪除profile
	@Transactional
	public void deleteProfileById(String id) {
		long id_long = (long) Long.parseLong(id);
		myProfileRepository.deleteById(id_long);
	}
	
	// 查看profile是否存在
	@Transactional
	public int checkAccountExist(String name) {
		List<MyProfile> profiles = myProfileRepository.findByName(name);
		return profiles.size();
	}
	
	public MyProfile findByName(String name) {
		List<MyProfile> profiles = myProfileRepository.findByName(name);
		if(profiles.size() > 0) {
			return profiles.get(0);
		}
		return null;
	}
	
	public MyProfile findById(Long id) {
		MyProfile profile = myProfileRepository.findById(id).get();
		return profile;
	}
}
