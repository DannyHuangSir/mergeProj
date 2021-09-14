package com.twfhclife.generic.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 列外處理工具.
 * 
 * @author alan
 */
public class ExceptionUtil {

	/**
	 * This is the constructor
	 */
	private ExceptionUtil() {
	}

	/**
	 * 取得錯誤訊息.
	 * 
	 * @param ex Throwable
	 * @return 回傳拋出異常資訊的字串
	 */
	public static String printStackTrace(Throwable ex) {
		String stackTrace = "";
		try (StringWriter swriter = new StringWriter(); PrintWriter pwriter = new PrintWriter(swriter);) {
			ex.printStackTrace(pwriter);
			stackTrace = swriter.toString();
		} catch (Exception e) {
		}
		return stackTrace;
	}
}
