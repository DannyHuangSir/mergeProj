package com.twfhclife.generic.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  隨機變數的方法
 *
 */
public class RandomUtils {

	private static String[] _ARRAY_NUMBER_ENGLISH = {
			"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
			"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z",
			"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
			"k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
			"u", "v", "w", "x", "y", "z",
			"0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

	/** 產生隨機變數字串, 預設總長度是6, 回傳值範例: 001234 or 012345 or 123456
     * @param digit 幾位數的隨機變數, 不足六位數, 前面補0
     * @return randomNumber
     * */
    public static String getRandomNum(int digit) {
        return getRandomNum(6, digit);
    }

    /** 產生隨機變數字串
     * @param length 回傳字串長度
     * @param digit 幾位數的隨機變數
     * @return randomNumber
     * */
    public static String getRandomNum(int length, int digit) {
		Random rd;
		try {
			rd = SecureRandom.getInstanceStrong();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		String rand = "";
		for (int i = 0; i < length; i++) {
			rand += "0";
		}
		String seed = "";
		for (int i = 0; i < digit; i++)
			seed = seed + 9;
		long a = rd.nextInt(Integer.parseInt(seed));
		rand = rand + String.valueOf(a);
		return rand.substring(rand.length() - length);
    }

    /** 產生隨機變數數值(int)
     * @return randomNumber(int)
     * */
    public static int nextInt() {
		Random rd;
		try {
			rd = SecureRandom.getInstanceStrong();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
        return rd.nextInt();
    }

    /** 產生隨機變數數值(int)
     * @param n 設定最大數值, 及回傳數值會介於0-n 之間
     * @return randomNumber(int)
     * */
    public static int nextInt(int n) {
		Random rd;
		try {
			rd = SecureRandom.getInstanceStrong();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
        return rd.nextInt(n);
    }
    
    /** 英數夾雜 */
	public static String getNumberEngRandomString() {
		return getNumberEngRandomString(10);
	}

	public static String getNumberEngRandomString(int length) {
		Random random;
		try {
			random = SecureRandom.getInstanceStrong();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int no = random.nextInt(81);
			sb.append(_ARRAY_NUMBER_ENGLISH[no]);
		}
		String value = sb.toString();
		if (isAllNum(value)) {
			value = "A" + value.substring(1);
		} else if (isAllEng(value)) {
			value = "9" + value.substring(1);
		}
		return value;
	}

	private static boolean isAllNum(String value) {
		String regex = "^[0-9]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}

	private static boolean isAllEng(String value) {
		String regex = "^[a-zA-Z]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
}
