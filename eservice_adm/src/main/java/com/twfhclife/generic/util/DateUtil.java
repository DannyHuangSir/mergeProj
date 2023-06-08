package com.twfhclife.generic.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	 * @param effectDate 開賣日
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

	/**
	 * 字符串轉換為時間類型的字符串
	 * @param dateFormat  時間類型的字符串  yyyyMMdd
	 * @param returnDateFormat  返回的時間類型的字符串  yyyy-MM-dd
	 * @param args  需要格式化的字符串 20201205
	 * @return  返回格式化之後的字符串 2020-12-05
	 */
	public  static String  getStringToDateString(String dateFormat,String  args,String returnDateFormat){
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
			Date parse = simpleDateFormat.parse(args);
			SimpleDateFormat format = new SimpleDateFormat(returnDateFormat);
			String formatStr = format.format(parse);
			return  formatStr;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return args;
	}
	/**
	 * 取得系統日距離投保日有多少天(含投保當天)
	 * @param date 保單投保日
	 * @return 
	 */
	public static int getDateInterval(String date) {
    	LocalDate a = LocalDate.parse(date , DateTimeFormatter.ISO_LOCAL_DATE);
    	LocalDate b = LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) , DateTimeFormatter.ISO_LOCAL_DATE);

    	Duration diff = Duration.between(a.atStartOfDay() , b.atStartOfDay());
    	long diffDays = diff.toDays()+1;
    	return Integer.valueOf(String.valueOf(diffDays));
	}

}