package com.csprs.cbw.util;

public class Utils {
	
	public static boolean isNullEmptyString(String element) {
		boolean result = false;
		if(element == null || "".equals(element.trim())) {
			result = true;
		}
		return result;
	}
	
}
