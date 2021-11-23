package com.twfhclife.eservice_batch.util;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class StringUtil {
	
	public static String rpadBlank(String str, int len) {
		if (str == null) {
			str = "";
		}
		return rpad(str, len, " ");
	}
	
	public static String lpad(String str, int len, String addStr) {
		if (str == null) {
			str = "";
		}
		int realPadLen = 0;
		int wordLen = countDobuleSize(str);
		if (wordLen > len) {
			realPadLen = len - wordLen;
		} else {
			realPadLen = len;
		}
		return fillString(str, "L", realPadLen, addStr);
	}

	public static String rpad(String str, int len, String addStr) {
		int realPadLen = 0;
		int wordLen = countDobuleSize(str);//59
		int addrLen = str.length();//30
		if (wordLen > addrLen) {
			realPadLen = len - wordLen + addrLen;
		} else {
			realPadLen = len;
		}
		return fillString(str, "R", realPadLen, addStr);
	}

	/***
	 * modify: 2021/11/23 xianzhi
	 * 新rpad方法，修復漢字截取長度問題，
	 * addStr 不能爲漢字，否則長度計算會出錯
	 * @param str
	 * @param len
	 * @return
	 */
	public static String newRpadBlank(String str, int len) {
		if (str == null) {
			str = "";
		}
		return newRpad(str, len, " ");
	}

	public static String newRpad(String str, int len, String addStr) {
		int wordLen = countDobuleSize(str);
		int addrLen = str.length();
		if (wordLen > addrLen) {
			return getNewRpadLen(str, len, addStr);
		}
		return fillString(str, "R", len, addStr);
	}

	private static String getNewRpadLen(String str, int len, String addStr) {
		StringBuilder resultStr = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			String strTmp = str.substring(i, i + 1);
			if (strTmp.matches("[^\\x00-\\xff]")) {
				len--;
			}
			len--;
			if (len > 0) {
				resultStr.append(strTmp);
			}
			if (len == 0) {
				resultStr.append(strTmp);
				break;
			}
			if (len < 0) {
				resultStr.append(addStr);
				break;
			}
		}
		while (len > 0) {
			len--;
			resultStr.append(addStr);
		}
		return resultStr.toString();
	}

	public static String fillString(String str, String position, int len,
			String addStr) {
		if (addStr == null || addStr.length() == 0) {
			throw new IllegalArgumentException("addStr=" + addStr);
		}
		if (str == null) {
			str = "";
		}
		StringBuilder buffer = new StringBuilder(str);
		while (len > buffer.length()) {
			if ("L".equalsIgnoreCase(position)) {
				int sum = buffer.length() + addStr.length();
				if (sum > len) {
					addStr = addStr.substring(0, addStr.length() - (sum - len));
					buffer.insert(0, addStr);
				} else {
					buffer.insert(0, addStr);
				}
			} else {
				buffer.append(addStr);
			}
		}
		if (buffer.length() == len) {
			return buffer.toString();
		}
		return buffer.toString().substring(0, len);
	}

	public static int countDobuleSize(String str) {
		int cnt = 0;
		for (int i = 0; i < str.length(); i++) {
			String strTmp = str.substring(i, i + 1);
			if (strTmp.matches("[^\\x00-\\xff]")) {
				cnt++;
			}
			cnt++;
		}
		return cnt;
	}
	
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		
		String str = "台北市延平北路２段１０號２樓";
//		int cnt = 0;
//		for (int i = 0; i < str.length(); i++) {
//			String strTmp = str.substring(i, i + 1);
//			if (strTmp.matches("[^\\x00-\\xff]")) {
//				cnt++;
//			}
//			cnt++;
//		}
		System.out.println(StringUtil.rpadBlank(str, 100));
	}
}