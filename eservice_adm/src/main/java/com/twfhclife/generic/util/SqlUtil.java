package com.twfhclife.generic.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SqlUtil {

	private static final Logger logger = LogManager.getLogger(SqlUtil.class);
	
	public static boolean checkSqlInjection(String script) {
		if(script.toLowerCase().indexOf("insert") >= 0) {
			logger.info("[SQL] execute insert script:"+script);
			//return false;
		}
		if(script.toLowerCase().indexOf("delete") >= 0) {
			logger.info("[SQL] execute delete script:"+script);
			if(script.toLowerCase().indexOf("where") < 0) {
				return false;
			}
			//return false;
		}
		if(script.toLowerCase().indexOf("update") >= 0) {
			logger.info("[SQL] execute update script:"+script);
			if(script.toLowerCase().indexOf("where") < 0) {
				return false;
			}
			//return false;
		}
		if(script.toLowerCase().indexOf("truncate") >= 0) {
			return false;
		}
		if(script.toLowerCase().indexOf("drop") >= 0) {
			return false;
		}
		if(script.toLowerCase().indexOf("alter") >= 0) {
			return false;
		}
		if(script.toLowerCase().indexOf("user_entity") >= 0) {
			return false;
		}
		if(script.toLowerCase().startsWith("create")) {
			//return false;
		}
		if(!script.toLowerCase().startsWith("select ")) {
			//return false;
		}
		return true;
	}
}
