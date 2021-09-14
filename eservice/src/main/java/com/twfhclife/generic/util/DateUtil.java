package com.twfhclife.generic.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 日期工具.
 * 
 * @author all
 */
public class DateUtil {

	/** yyyy/MM/dd. */
	public final static String FORMAT_WEST_DATE = "yyyy/MM/dd";
	
	/** yyyyMMdd */
	public final static String FORMAT_DATE = "yyyyMMdd";

	/**
	 * 轉換日期格式.
	 * 
	 * @param date 轉換日期
	 * @param format 格式
	 * @return 回傳格式化日期字串
	 */
	public static String formatDateTime(Date date, String format) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat formater = new SimpleDateFormat(format);
		return formater.format(date);
	}

	/**
	 * 取得民國年月日.
	 * 
	 * @return 回傳民國年系統日(民國年格式 yyyyMMdd)
	 */
	public static String getRocDate(Date westDate) {
		if (westDate == null) {
			return "";
		}
		return westToTwDate(formatDateTime(westDate, FORMAT_WEST_DATE));
	}

	/**
	 * 西元年轉民國年
	 * 
	 * @param westDate YYYY/MM/DD
	 * @return 民國年 格式: YYY/MM/DD
	 */
	public static String westToTwDate(String westDate) {
		String twYear = Integer.parseInt(westDate.substring(0, 4)) - 1911 + "";
		return StringUtils.leftPad(twYear, 3, "0") + westDate.substring(4);
	}

	/**
	 * 根據商品開賣日, 取回年字串.
	 * 
	 * @param date 開賣日
	 * @return 回傳系統日至開賣日的民國年字串
	 */
	public static List<String> getYearOpitonByEffectDate(String effectDate) {
		String sysRocYear = getRocDate(new Date()).substring(0, 3);
		String effectDateYear = westToTwDate(effectDate).substring(0, 3);

		// 組出民國年(從系統日起算至開賣日的年)
		List<String> rocYearList = new ArrayList<>();
		for (int i = Integer.parseInt(effectDateYear); i <= Integer.parseInt(sysRocYear); i++) {
			String rocYear = (i < 100) ? "0" + i : "" + i;
			rocYearList.add(rocYear);
		}

		return rocYearList;
	}
}