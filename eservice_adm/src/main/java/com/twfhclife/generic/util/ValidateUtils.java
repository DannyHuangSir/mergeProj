package com.twfhclife.generic.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class ValidateUtils {

	/**
	 * 檢查 Email 合法性.
	 *
	 * @param pInput 要檢查的字串
	 * @return boolean 檢查結果
	 */
	public static boolean isEmail(String pInput) {
		if (StringUtils.isEmpty(pInput)) {
			return false;
		}

		String regEx = "([\\w[_-][\\.]]+@+[\\w[_-]]+\\.+[A-Za-z]{2,3})|([\\" + "w[_-][\\.]]+@+[\\w[_-]]+\\.+[\\w[_-]]+\\.+[A-Za-z]{2,3})|"
				+ "([\\w[_-][\\.]]+@+[\\w[_-]]+\\.+[\\w[_-]]+\\.+[\\w[_-]]+" + "\\.+[A-Za-z]{2,3})";

		Pattern p = Pattern.compile(regEx);
		Matcher matcher = p.matcher(pInput.trim());

		return matcher.matches();
	}
	
	/**
	 * 檢查 行動電話號碼 合法性.
	 *
	 * @param mobile the mobile
	 * @return boolean 檢查結果
	 */
	public static boolean isMobilePhoneNumber(String mobile) {
		if (StringUtils.isEmpty(mobile)) {
			return false;
		}
		if (mobile.length() != 10) {
			return false;
		}
		return mobile.matches("09[0-9]{8}");
	}
	
	public static boolean isCorrectCronTabTime(String cronExp) {
		String[] array = cronExp.split(" ");
		if(array.length != 5) {
			return false;
		} else {
			int index = 0;
			while(index < array.length) {
				if("*".equals(array[index])) {
					index++;
					continue;
				}
				String arrayValue = array[index];
				String[] values = arrayValue.split("-");
				switch(index) {
					case 0:
						if(arrayValue.matches("[0-9]{1,2}") || arrayValue.matches("[0-9]{1,2}-[0-9]{2}")) {
							for(String value: values) {
								int intVal = Integer.parseInt(value);
								if(intVal > 59 || intVal < 0) {
									return false;
								}
							}
							break;
						}
					case 1:
						if(arrayValue.matches("[0-9]{1,2}") || arrayValue.matches("[0-9]{1,2}-[0-9]{1,2}")) {
							for(String value: values) {
								int intVal = Integer.parseInt(value);
								if(intVal > 23 || intVal < 0) {
									return false;
								}
							}
							break;
						}
					case 2: 
						if(arrayValue.matches("[0-9]{1,2}") || arrayValue.matches("[0-9]{1,2}-[0-9]{1,2}")) {
							for(String value: values) {
								int intVal = Integer.parseInt(value);
								if(intVal > 31 || intVal < 1) {
									return false;
								}
							}
							break;
						}
					case 3:
						if(arrayValue.matches("[0-9]{1,2}") || arrayValue.matches("[0-9]{1,2}-[0-9]{1,2}")) {
							for(String value: values) {
								int intVal = Integer.parseInt(value);
								if(intVal > 12 || intVal < 1) {
									return false;
								}
							}
							break;
						}
					case 4:
						if(arrayValue.matches("[1-7]{1}") || arrayValue.matches("[1-7]{1}-[1-7]{1}")) {
							break;
						}
					default: return false;
				}
				index++;
			}
			return true;
		}
	}
}