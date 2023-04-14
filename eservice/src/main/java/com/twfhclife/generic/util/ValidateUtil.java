package com.twfhclife.generic.util;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.twfhclife.eservice.web.controller.RegisterUserController;

import com.twfhclife.eservice.onlineChange.model.TransInvestmentVo;

/**
 * 檢核工具.
 * 
 * @author all
 */
public class ValidateUtil {

	private static final Logger logger = LogManager.getLogger(RegisterUserController.class);

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
	 * 20221004 by 203990
	 * 因應稽核追加密碼判別
	 * 檢查密碼格式(請勿輸入連續3碼重覆或連續性之密碼).
	 * @param pwd 密碼檢查-2
	 * @return
	 */
	public static boolean isPwdUc(String pwd) {
		String letterChku = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String letterChkl = "abcdefghijlkmnopqrstuvwxyz";
		String digitChk = "0123456789";
		
		int continueCnt = 0;       // 連續性
		String continueBefore = "";
		String continueBeforeSub = "";
		int repeatCnt = 0;         // 重覆性
		char repeatBefore = ' ';
		for (int i = 0; i < pwd.length(); i++) {
			// 判別是否有連續3碼連續
			continueBefore += pwd.charAt(i);
			if (continueCnt >= 2) {
				continueBeforeSub = continueBefore.substring(continueBefore.length()-3);
				if (letterChku.contains(continueBeforeSub) || letterChkl.contains(continueBeforeSub) || digitChk.contains(continueBeforeSub)) {
					return false;
				}
			}
			continueCnt++;

			// 判別是否有連續3碼重覆
			if (repeatBefore != pwd.charAt(i)) {
				repeatBefore = pwd.charAt(i);
				repeatCnt = 0;
			} else {
				repeatCnt++;
				if (repeatCnt >= 2) {
					return false;
				}
			}
		}

		return true;
	}
	

    /**
	 * 20221006 by 203990
	 * 全形的資料, 新增時就會寫入? 調整後端接收到的資料寫入變半形
    * 全形轉半形
    * @param input String.
    * @return 半形字串
    */
    public static String ToDBC(String input) {
	    char c[] = input.toCharArray();

	    for ( int i = 0 ; i < c.length; i ++ ) {
		    if (c[i] == '\u3000' ) {
		    	c[i] = ' ' ;
		    } else if (c[i] > '\uFF00' && c[i] < '\uFF5F' ) {
		    	c[i] = (char)(c[i] - 65248 );		
		    }
	    }
	    String returnString = new String(c);

	    return returnString;
    }

	public static boolean TransInvestmentIsValid(TransInvestmentVo transInvestmentVo) {
		if(StringUtils.isNotEmpty(transInvestmentVo.getPolicyNo())) {
			if(!isValid(transInvestmentVo.getPolicyNo())) {
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