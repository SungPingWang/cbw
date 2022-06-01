package com.csprs.cbw.service.permission;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csprs.cbw.bean.user.Permission;
import com.csprs.cbw.dao.user.PermissionDaoImpl;

@Service
public class PermissionService {
	
	@Autowired
	private PermissionDaoImpl permissionDaoImpl;
	
	public List<Permission> getAllPermission() {
		List<Permission> result = permissionDaoImpl.getAllPermission();
		return result;
	}
	
	public String getPermissionJson(List<Permission> permissions) {
		List<String> adminList = new ArrayList<String>();
		List<String> managerList = new ArrayList<String>();
		List<String> userList = new ArrayList<String>();
		StringBuffer buf = new StringBuffer();
		// 1 獲取不重複的name名字，並轉為array型態
		Set<String> nameSet = new HashSet<>();
		for(int i=0; i<permissions.size(); i++) {
			nameSet.add(permissions.get(i).getName());
		}
		String[] nameArray = nameSet.toArray(new String[nameSet.size()]);
		// 2 遍歷array
		for(int i=0; i<nameArray.length; i++) {
			String name = nameArray[i];
			// 3 java stream過濾並獲取該name的所有數據，順便把permission code前後都加個雙引號，在後面比較好處理
			List<Permission> permissionByName = permissions.stream()
					.filter(p -> p.getName().equals(name))
					.map(p -> new Permission(p.getId(), p.getGroup(), p.getName(), "\""+p.getPermissionCode()+"\""))
					.collect(Collectors.toList());
			// 4 宣告一個不重複的HashSet，裡面裝的是該使用對應不同的權限
			Set<String> permissionCodeByNameSet = new HashSet<>();
			// 5 將步驟3獲取到的對應姓名的各個permission code加入到hashSet中
			permissionByName.forEach(p -> permissionCodeByNameSet.add(p.getPermissionCode()));
			// 6 將該姓名對應的permission code都加轉換為array並利用逗點join起來
			String joinPermissionCode = String.join(",", permissionCodeByNameSet.toArray(new String[permissionCodeByNameSet.size()]));
			// 7 合成變成該姓名的所有對應權限
			String usernameJson = "{\"" + name + "\":[\"" + name + "\"," + joinPermissionCode + "]}\r\n";
			// 8 判斷該姓名對應的組，加入倒對應的ArrayList中
			String group = permissionByName.get(0).getGroup();
			if("admin".equals(group)) {
				adminList.add(usernameJson);
			}else if("manager".equals(group)) {
				managerList.add(usernameJson);
			}else {
				userList.add(usernameJson);
			}
		}
		// 9 開始組合json字串
		buf.append("{\r\n");
		buf.append("\"admin\": [\r\n").append(String.join(",", adminList.toArray(new String[adminList.size()]))).append("],\r\n");
		buf.append("\"manager\": [\r\n").append(String.join(",", managerList.toArray(new String[managerList.size()]))).append("],\r\n");
		buf.append("\"user\": [\r\n").append(String.join(",", userList.toArray(new String[userList.size()]))).append("]\r\n");
		buf.append("}");		
		
		return buf.toString();
	}

	public String getPermissionJson() {
		String json = "{\r\n"
				+ "        \"superuser\": [\r\n"
				+ "            { \"id1-1\": [\"Java\", \"1-1\", \"1-2\", \"2-3\", \"3-1\", \"3-3\", \"4-4\"] },\r\n"
				+ "            { \"id1-2\": [\"C#\", \"1-2\", \"1-4\", \"2-3\", \"3-1\", \"3-3\", \"4-4\"] } \r\n"
				+ "        ],\r\n"
				+ "        \"manager\": [\r\n"
				+ "            { \"id2-1\": [\"Python\", \"1-1\", \"1-2\", \"2-2\", \"3-1\", \"3-2\", \"4-1\"] },\r\n"
				+ "            { \"id2-2\": [\"Ruby\", \"1-1\", \"1-3\", \"2-1\", \"3-1\", \"3-3\", \"4-4\"] },\r\n"
				+ "            { \"id2-3\": [\"GoLang\", \"1-2\", \"1-4\", \"2-1\", \"3-1\", \"3-3\", \"4-2\"] }\r\n"
				+ "        ],\r\n"
				+ "        \"cities\": [\r\n"
				+ "            { \"id3-1\": [\"Html\", \"1-2\", \"1-3\", \"2-3\", \"3-1\", \"3-2\", \"4-3\"] },\r\n"
				+ "            { \"id3-2\": [\"JavaScript\", \"1-3\", \"1-4\", \"2-1\", \"3-1\", \"3-2\", \"4-1\"] },\r\n"
				+ "            { \"id3-3\": [\"Css\", \"1-2\", \"1-4\", \"2-1\", \"3-2\", \"3-3\", \"4-2\"] }\r\n"
				+ "        ],\r\n"
				+ "        \"agents\": [\r\n"
				+ "            { \"id4-1\": [\"Hadoop\", \"1-1\", \"1-2\", \"2-2\", \"3-2\", \"3-3\", \"4-2\"] },\r\n"
				+ "            { \"id4-2\": [\"Spark\", \"1-3\", \"1-4\", \"2-3\", \"3-1\", \"3-4\", \"4-1\"] },\r\n"
				+ "            { \"id4-3\": [\"Flink\", \"1-2\", \"1-4\", \"2-1\", \"3-1\", \"3-3\", \"4-4\"] }\r\n"
				+ "        ],\r\n"
				+ "        \"normal\": [\r\n"
				+ "            { \"id5-1\": [\"Servlet\", \"1-1\", \"1-2\", \"2-1\", \"3-2\", \"3-3\", \"4-2\"] },\r\n"
				+ "            { \"id5-2\": [\"JSP\", \"1-2\", \"1-4\", \"2-2\", \"3-2\", \"3-4\", \"4-2\"] },\r\n"
				+ "            { \"id5-3\": [\"Struts2\", \"1-3\", \"1-4\", \"2-3\", \"3-1\", \"3-4\", \"4-4\"] },\r\n"
				+ "            { \"id5-4\": [\"Spring\", \"1-2\", \"1-4\", \"2-4\", \"3-2\", \"3-3\", \"4-2\"] },\r\n"
				+ "            { \"id5-5\": [\"SpringBoot\", \"1-1\", \"1-4\", \"2-3\", \"3-1\", \"3-3\", \"4-4\"] }\r\n"
				+ "        ]\r\n"
				+ "    }";
		return json;
	}
	
}
