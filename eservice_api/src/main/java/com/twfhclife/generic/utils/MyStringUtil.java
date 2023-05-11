package com.twfhclife.generic.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

/**
 * String Util $Id: StringUtil.java 2344 2013-11-11 06:53:06Z hp.david $
 * $Revision: 2344 $ $Since:1.0 $Author: hp.david $ $Date: 2013-08-09 17:48:41
 * +0800$
 */
public class MyStringUtil {

	/**
	 * 將DB字元(可能含有null)轉為字串
	 * 
	 * @param value
	 * @return
	 */
	public static String convertString(Object value) {
		String str = "";
		if (value != null) {
			String temp = value.toString().trim();
			if (temp.equalsIgnoreCase("null"))
				temp = "";
			str = temp;
		}
		return str;
	}

	/**
	 * 將DB字元(可能含有null)轉為數字
	 * 
	 * @param value
	 * @return
	 */
	public static int convertInt(Object value) {
		int index = 0;
		if (value != null) {
			String temp = value.toString();
			if (temp != null && !temp.trim().equalsIgnoreCase("null")) {
				if (Integer.parseInt(temp) > 0) {
					index = Integer.parseInt(temp);
				}
			}
		}
		return index;
	}
	
	/**
	 * 將DB字元(可能含有null)轉為數字
	 * 
	 * @param value
	 * @return
	 */
	public static long convertLong(Object value) {
		long index = 0;
		if (value != null) {
			String temp = value.toString();
			if (temp != null && !temp.trim().equalsIgnoreCase("null")) {
				if (Integer.parseInt(temp) > 0) {
					index = Long.parseLong(temp);
				}
			}
		}
		return index;
	}

	/**
	 * 填入參數至系統訊息中, 例:請輸入 {0}！
	 * 
	 * @param msg
	 * @param values
	 * @return
	 */
	public static String replaceSysMsg(String msg, String[] values) {
		for (int i = 0; i < values.length; i++) {
			msg = msg.replace("{" + i + "}", values[i]);
		}
		return msg;
	}

	/**
	 * 將double轉為String並加入千分號
	 * 
	 * @param data
	 * @return
	 */
	public static String inserComma(double data) {
		if (data == 0.0) {
			return "";
		} else {
			DecimalFormat df = new DecimalFormat();
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
			dfs.setGroupingSeparator(',');
			df.setDecimalFormatSymbols(dfs);
			return df.format(BigDecimal.valueOf(data));
		}
	}

	/**
	 * Transform double to String(小數後面0位, 四捨五入)
	 * 
	 * @param money
	 * @return
	 */
	public static String double2String(double money) {
		BigDecimal bd = BigDecimal.valueOf(money);
		bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);// 小數後面0位, 四捨五入
		return bd.toString();
	}


	/**
	 * 將字串List轉為逗號分隔的字串, 如str1,str2,str3
	 * 
	 * @param arr
	 * @return
	 */
	public static String arrayToString(List<String> arr) {
		String result = "";
		int i = 0;
		for (String str : arr) {
			if (i > 0) {
				result += "," + str;
			} else {
				result += str;
			}
			i++;
		}
		return result;
	}


	/**
	 * 將資料(可能含有null)轉為空字串
	 * 
	 * @param inputStr
	 * @return
	 */
	public static String nullToString(Object inputObj) {
		String ret = "";
		try {
			if (inputObj != null) {
				ret = inputObj.toString().trim();
			}
		} catch (Exception e) {
			// do nothing.
		}
		return ret;
	}

	/**
	 * will return true if string is null or only contain white space(s)
	 * 
	 * @param string
	 * @return boolean
	 */
	public static boolean isNullOrEmpty(Object string) {
		return string == null || string.toString().length() == 0;
	}
	
	public static boolean isNotNullOrEmpty(Object string) {
		return string != null && string.toString().length() > 0;
	}

	/**
	 * 將DB物件(可能含有null)轉為字串
	 * 
	 * @param obj
	 * @return
	 */
	public static String objToStr(Object obj) {
		return obj == null ? "" : obj.toString().trim();
	}

	/**
	 * 將DB物件(可能含有null)轉為double
	 * 
	 * @param obj
	 * @return
	 */
	public static Double objToDouble(Object obj) {
		return obj == null ? 0 : Double.parseDouble(obj.toString().trim());
	}

	/**
	 * 將DB物件(可能含有null)轉為int
	 * 
	 * @param obj
	 * @return
	 */
	public static int objToInt(Object obj) {
		return obj == null ? 0 : Integer.parseInt(obj.toString().trim());
	}

	/**
	 * 取得字串長度
	 * 
	 * @param str
	 * @return
	 */
	public static int length(String str) {
		return ((str == null) ? 0 : str.length());
	}

	/**
	 * startsWith
	 * 
	 * @param str
	 * @param prefix
	 * @return
	 */
	public static boolean startsWith(String str, String prefix) {
		return startsWith(str, prefix, false);
	}

	/**
	 * startsWith
	 * 
	 * @param str
	 * @param prefix
	 * @param ignoreCase
	 * @return
	 */
	private static boolean startsWith(String str, String prefix, boolean ignoreCase) {
		if ((str == null) || (prefix == null)) {
			return ((str == null) && (prefix == null));
		}
		if (prefix.length() > str.length()) {
			return false;
		}
		return str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length());
	}

	/**
	 * endsWith
	 * 
	 * @param str
	 * @param suffix
	 * @return
	 */
	public static boolean endsWith(String str, String suffix) {
		return endsWith(str, suffix, false);
	}

	/**
	 * endsWith
	 * 
	 * @param str
	 * @param suffix
	 * @param ignoreCase
	 * @return
	 */
	private static boolean endsWith(String str, String suffix, boolean ignoreCase) {
		if ((str == null) || (suffix == null)) {
			return ((str == null) && (suffix == null));
		}
		if (suffix.length() > str.length()) {
			return false;
		}
		int strOffset = str.length() - suffix.length();
		return str.regionMatches(ignoreCase, strOffset, suffix, 0, suffix.length());
	}

	/**
	 * 去空白
	 * 
	 * @param str
	 * @return
	 */
	public static String trimAll(String str) {
		return trimAll(str, "");
	}

	/**
	 * 去空白
	 * 
	 * @param str
	 * @param suffix
	 * @return
	 */
	public static String trimAll(String str, String suffix) {
		if (null != str) {
			return str.replaceAll("\\s+", suffix);
		} else {
			return "";
		}
	}

	public static int getStringByteLength(String str) {
		int length = 0;
		if (str != null) {
			length = str.getBytes().length;
		}
		return length;
	}

	/**
	 * 左補填充字元.
	 * 
	 * @param str 判斷字串
	 * @param len 長度
	 * @param addStr 填充字元
	 * @return
	 */
	public static String lpad(String str, int len, String addStr) {
		int realPadLen = 0;
		int wordLen = countDobuleSize(str);
		if (wordLen > len) {
			realPadLen = len - wordLen;
		} else {
			realPadLen = len;
		}
		return fillString(str, "L", realPadLen, addStr);
	}

	/**
	 * 右補填充字元.
	 * 
	 * @param str 判斷字串
	 * @param len 長度
	 * @param addStr 填充字元
	 * @return
	 */
	public static String rpad(String str, int len, String addStr) {
		int realPadLen = 0;
		int wordLen = countDobuleSize(str);
		if (wordLen > str.length()) {
			realPadLen = len - wordLen + str.length();
		} else {
			realPadLen = len;
		}
		return fillString(str, "R", realPadLen, addStr);
	}

	/**
	 * @param str
	 * @param position
	 * @param len
	 * @param addStr
	 * @return
	 */
	private static String fillString(String str, String position, int len,
			String addStr) {
		if (addStr == null || addStr.length() == 0) {
			throw new IllegalArgumentException("addStr=" + addStr);
		}
		if (str == null) {
			str = "";
		}
		StringBuffer buffer = new StringBuffer(str);
		while (len > buffer.length()) {
			if (position.equalsIgnoreCase("L")) {
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

	/**
	 * 計算有包含中文字的字串長度.
	 * 
	 * @param str
	 * @return
	 */
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
}