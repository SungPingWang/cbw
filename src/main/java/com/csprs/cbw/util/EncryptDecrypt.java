package com.csprs.cbw.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

@SuppressWarnings("all")
public class EncryptDecrypt {

	public static String encryptAES(String input, String key, String transformation, String algorithm) throws Exception  {
		// 創建加密對象
		Cipher cipher = Cipher.getInstance(transformation);
		/* 創建加密規則
		 * 1 -> 表示key的字節
		 * 2 -> 表示加密的類型
		 * */
		SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), algorithm);
		/* 加密進行初始化
		 * 1 -> 模式:加密模式or解密模式
		 * 2 -> 加密的規則
		 * */ 
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		// 調用加密方法
		/* 調用加密方法
		* 1 -> 原文的字節數組
		* */ 
		byte[] bytes = cipher.doFinal(input.getBytes());
		// 打印字節會出現亂數是因為ascii是負數，正常的解析不出來，所以才會出現亂碼
		// System.out.println(new String(bytes));
		// 對數據進行Base64編碼
		String encode = Base64.getEncoder().encodeToString(bytes);
		// System.out.println(encode);
		return encode;
	}
	
	public static String decryptAES(String encryptDES, String key, String transformation, String algorithm) throws Exception  {
		// 因為token經過回傳有可能把+變成空格，所以要重新進行替換
		encryptDES = encryptDES.replaceAll(" ", "+");
		System.out.println("encryptDES: " + encryptDES);
		Cipher cipher = Cipher.getInstance(transformation);
		SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), algorithm);
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
		// 解密 傳入祕文
		byte[] decodeByBase64 = Base64.getDecoder().decode(encryptDES);
		byte[] decode = cipher.doFinal(decodeByBase64);
		return new String(decode);
	}
	
}
