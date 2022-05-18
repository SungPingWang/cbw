package com.csprs.cbw.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csprs.cbw.bean.user.MyProfile;


public interface MyProfileRepository extends JpaRepository<MyProfile, Long> {
	
	List<MyProfile> findByName(String name);
	List<MyProfile> findById(long id);
}
