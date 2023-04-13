package com.twfhclife.generic.utils;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.twfhclife.eservice.api.elife.domain.TransHistoryListRequest;

/**
 * 檢核工具.
 * 
 * @author all
 */
public class ValidateUtil {
	
	static String reg = "(?:')|(?:--)|(///*(?:.|[//n//r])*?//*/)|"
			+ "(//b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)//b)";
	static	Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);

	/**
	 * 檢查密碼格式(8～20碼混合之數字及英文字母和符號(須區分大小寫)).
	 * 
	 * <pre>
	 * 1.最低要求8字元
	 * 2.最少符合下列四項中三項規則:
	 *   - 大寫英文字元
	 *   - 小寫英文字元
	 *   - 數字字元
	 *   - 符號字元
	 * </pre>
	 * @param pwd 密碼檢查
	 * @return
	 */
	public static boolean isPwd(String pwd) {
		if (StringUtils.isEmpty(pwd)) {
			return false;
		}
		if (pwd.length() < 8) {
			return false;
		}

		int matchCnt = 0;       // 符合次數
		int digitCnt = 0;       // 數字
		int upperLetterCnt = 0; // 大寫
		int lowerLetterCnt = 0; // 小寫
		int otherLetterCnt = 0; // 非字母及數字
		for (int i = 0; i < pwd.length(); i++) {
			if (Character.isDigit(pwd.charAt(i))) {
				digitCnt++;
			} else if (Character.isLetter(pwd.charAt(i))) {
				if (Character.isUpperCase(pwd.charAt(i))) {
					upperLetterCnt++;
				} else if (Character.isLowerCase(pwd.charAt(i))) {
					lowerLetterCnt++;
				}
			} else {
				otherLetterCnt++;
			}
		}

		if (digitCnt > 0) {
			matchCnt++;
		}
		if (upperLetterCnt > 0) {
			matchCnt++;
		}
		if (lowerLetterCnt > 0) {
			matchCnt++;
		}
		if (otherLetterCnt > 0) {
			matchCnt++;
		}
		return (matchCnt >= 3);
	}

	/**
	 * 判断字符串是否为空
	 * @param args
	 * @return
	 */
	public static boolean  notNULL(String  args){
		if (args!=null && !"".equals(args) ) {
			return  true;
		}
		return  false;
	}
	
	public static boolean TransHistoryListRequestIsValid(TransHistoryListRequest request) {
		if(StringUtils.isNotEmpty(request.getPolicyNo())) {
			if(!isValid(request.getPolicyNo())) {
				return false;
			}
		}
		if(StringUtils.isNotEmpty(request.getUserId())) {
			if(!isValid(request.getUserId())) {
				return false;
			}
		}
		if(StringUtils.isNotEmpty(request.getTransType())) {
			if(!isValid(request.getTransType())) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isValid(String str) {

		if (sqlPattern.matcher(str).find()) {
			return false;
		}
		return true;
	}
}