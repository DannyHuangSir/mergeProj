package com.twfhclife.generic.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

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
	public static boolean isNullOrEmpty(String string) {
		return string == null || string.length() == 0;
	}
	
	public static boolean isNotNullOrEmpty(String string) {
		return string != null && string.length() > 0;
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
     * 底線轉駝峰法(默認小駝峰)
     *
     * @param line
     *            源字串
     * @param smallCamel
     *            大小駝峰,是否為小駝峰(駝峰，第一個字元是大寫還是小寫)
     * @return 轉換後的字串
     */
	public static String underline2Camel(String line, boolean... smallCamel) {
		if (line == null || "".equals(line)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
		Matcher matcher = pattern.matcher(line);
		// 匹配規則運算式
		while (matcher.find()) {
			String word = matcher.group();
			// 當是true 或則是空的情況
			if ((smallCamel.length == 0 || smallCamel[0]) && matcher.start() == 0) {
				sb.append(Character.toLowerCase(word.charAt(0)));
			} else {
				sb.append(Character.toUpperCase(word.charAt(0)));
			}

			int index = word.lastIndexOf('_');
			if (index > 0) {
				sb.append(word.substring(1, index).toLowerCase());
			} else {
				sb.append(word.substring(1).toLowerCase());
			}
		}
		return sb.toString();
	}

	/**
	 * 駝峰法轉底線
	 *
	 * @param line
	 *            源字串
	 * @return 轉換後的字串
	 */
	public static String camel2Underline(String line) {
		if (line == null || "".equals(line)) {
			return "";
		}
		line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
		StringBuffer sb = new StringBuffer();
		Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
		Matcher matcher = pattern.matcher(line);
		while (matcher.find()) {
			String word = matcher.group();
			sb.append(word.toUpperCase());
			sb.append(matcher.end() == line.length() ? "" : "_");
		}
		return sb.toString();
	}
	
	public static String getFormateStringFromDate(Date date, String formate) {
        String result = null;
        if (null != date && !StringUtils.isEmpty(formate)) {
            SimpleDateFormat sf = new SimpleDateFormat(formate);
            result = sf.format(date);
        }
        return result;
    }
	
	public static void main(String[] args) {
		String a = "MESSAGING_TEMPLATE_ID,SYSTEM_ID,MESSAGING_TEMPLATE_CODE,MESSAGING_TEMPLATE_NAME,STATUS,TRIGGER_TYPE,EVENT_TYPE,SEND_TYPE,SEND_TIME,CIRCLE_TYPE,CIRCLE_VALUE,RECEIVER_MODE,MESSAGING_SUBJECT,MESSAGING_CONTENT";
		for(String f : a.split("\\,")) {
			//System.out.println(f+" > "+underline2Camel(f));
			System.out.println(underline2Camel(f));
		}
		
		
	}
}