package com.twfhclife.generic.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 檢核工具.
 * 
 * @author all
 */
public class ValidateUtil {

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
}