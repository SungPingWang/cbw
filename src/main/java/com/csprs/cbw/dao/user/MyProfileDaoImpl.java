package com.csprs.cbw.dao.user;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csprs.cbw.bean.user.MyProfile;
import com.csprs.cbw.bean.user.MyRole;
import com.csprs.cbw.repository.user.MyProfileRepository;


@Service
public class MyProfileDaoImpl {
	
	private final Logger logger = LoggerFactory.getLogger("CustomOperateLog");
	
	@Autowired
	public MyProfileRepository myProfileRepository;
	
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
	
	@Transactional
	public void saveProfile(MyProfile myProfile) {
		try {
			myProfileRepository.save(myProfile);
			System.out.println("Save the Profile in dao");
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}
	
	@Transactional
	public void updateProfile(String id, String name, String password, String description, List<MyRole> roles) {
		try {
			long id_long = Long.parseLong(id);
			MyProfile profile = myProfileRepository.findById(id_long).get(0);
			profile.setName(name);
			profile.setPassword(password);
			profile.setDescription(description);
			profile.setRoles(roles);
			// System.out.println(profile);
			myProfileRepository.save(profile);
			System.out.println("Update the Profile in dao");
		} catch (NumberFormatException e) {
			logger.error(e.toString());
		}
	}
	
	@Transactional
	public void deleteProfileById(String id) {
		long id_long = (long) Long.parseLong(id);
		myProfileRepository.deleteById(id_long);
	}
	
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
	
	
	
}
