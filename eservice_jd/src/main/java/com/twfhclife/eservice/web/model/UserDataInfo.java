package com.twfhclife.eservice.web.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserDataInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String USER_DETAIL = "USER_DETAIL";
	
	public static final String LAST_LOGIN_TIME = "LAST_LOGIN_TIME";
	
	public static final String USER_ALL_POLICY_LIST = "USER_ALL_POLICY_LIST";
	
	public static final String USER_INVT_POLICY_LIST = "USER_INVT_POLICY_LIST";
	
	public static final String USER_BENEFIT_POLICY_LIST = "USER_BENEFIT_POLICY_LIST";
	
	public static final String USER_POLICY_NO_LIST = "USER_POLICY_NO_LIST";
	
	public static final String USER_PRODUCT_NAME_LIST = "USER_PRODUCT_NAME_LIST";
	
	public static final String USER_MAIN_INSURED_NAME_LIST = "USER_MAIN_INSURED_NAME_LIST";
	
	public static final String USER_DASHBOARD_DATA = "USER_DASHBOARD_DATA";
	
	/** 事件通知資料 */
	public static final String USER_NOTICE_BOARD_LIST = "USER_NOTICE_BOARD_LIST";
	
	/** 案件已完成資料 */
	public static final String USER_COMPLETE_LIST = "USER_COMPLETE_LIST";
	
	/** 案件申請中資料 */
	public static final String USER_PROCESSING_LIST = "USER_PROCESSING_LIST";
	
	/** 繳費通知資料 */
	public static final String USER_PAYMENT_REMINDER_LIST = "USER_PAYMENT_REMINDER_LIST";
	
	/** 資產與負債資料資料 */
	public static final String USER_POLICY_EXTRA = "USER_POLICY_EXTRA";
	
	/** 保障型的被保人資料 */
	public static final String USER_INSURED_PRODUCT_DATA = "USER_INSURED_PRODUCT_DATA";

	private Map<String, String> productNameMap = new HashMap<>();

	private Map<String, String> mainInsuredNameMap = new HashMap<>();

	private Map<String, String> mainInsuredNameBase64Map = new HashMap<>();

	public Map<String, String> getProductNameMap() {
		return productNameMap;
	}

	public void setProductNameMap(Map<String, String> productNameMap) {
		this.productNameMap = productNameMap;
	}

	public Map<String, String> getMainInsuredNameMap() {
		return mainInsuredNameMap;
	}

	public void setMainInsuredNameMap(Map<String, String> mainInsuredNameMap) {
		this.mainInsuredNameMap = mainInsuredNameMap;
	}

	public Map<String, String> getMainInsuredNameBase64Map() {
		return mainInsuredNameBase64Map;
	}

	public void setMainInsuredNameBase64Map(
			Map<String, String> mainInsuredNameBase64Map) {
		this.mainInsuredNameBase64Map = mainInsuredNameBase64Map;
	}
}