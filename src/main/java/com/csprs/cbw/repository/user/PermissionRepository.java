package com.csprs.cbw.repository.user;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.csprs.cbw.bean.user.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
	
	List<Permission> findByName(String name);
	
}
