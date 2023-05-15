package com.twfhclife.jd.util;

/**
 * 全域常數設定.
 * @author David
 * @version 1.0
 */
public interface ApConstants {

	final String SUCCESS = "success";

	final String NEW_LINE_SEPARATOR = "\n";
	
	final String KEYCLOAK_REALM = "elife_jdzq";
	
	/** 系統別 */
	final String SYSTEM_ID = "eservice_jd";
	
	/** 系統別 */
	final String SYSTEM_ID_ADM = "eservice_adm";

	final String SYSTEM_ID_JD = "eservice_jd";

	/** 事件類型 */
	final String EVENT_TYPE_PARAMETER_CATEGORY_CODE = "EVENT_TYPE";

	/** 保單狀態對應 */
	final String POLICY_STATUS_PARAMETER_CATEGORY_CODE = "POLICY_STATUS";
	
	/** 交易類別 */
	final String TRANSACTION_PARAMETER_CATEGORY_CODE = "TRANSACTION_CODE";
	
	/** 系統參數 */
	final String SYSTEM_PARAMETER = "SYSTEM_PARAMETER";
	
	/** 系統常數 */
	final String SYSTEM_CONSTANTS = "SYSTEM_CONSTANTS";
	
	/** 畫面文案 */
	final String PAGE_WORDING = "PAGE_WORDING";
	
	/** 註冊條款 */
	final String REGISTER_TERM_PARAMETER_CATEGORY_CODE = "REGISTER_TERM";
	
	/** 註冊問題 */
	final String REGISTER_QUESTION_PARAMETER_CATEGORY_CODE = "REGISTER_QUESTION";
	
	/** 登入帳號. */
	public static final String LOGIN_USER_ID = "LOGIN_USER_ID";

	/** 登入名稱. */
	public static final String LOGIN_USER_NAME = "LOGIN_USER_NAME";
	
	/** 登入角色. */
	public static final String LOGIN_USER_ROLE_NAME = "LOGIN_USER_ROLE_NAME";

	/** 登入時間. */
	public static final String LOGIN_TIME = "LOGIN_TIME";

	/** 登出基準時間. */
	public static final String LOGUT_TIME_OUT = "LOGUT_TIME_OUT";

	/** 登出基準時間. */
	public static final long LOGUT_TIME_DURATION_SECOND = 300;
	
	/** 閒置超時登出秒數. */
	public static final String TIMEOUT_SECONDS = "TIMEOUT_SECONDS";
	
	/** 登入訊息. */
	public static final String LOGIN_MSG = "LOGIN_MSG";

	/** 圖形驗證碼. */
	public static final String LOGIN_VALIDATE_CODE = "LOGIN_VALIDATE_CODE";
	
	/** 畫面文檔 */
	public static final String PAGE_WORDING_CATEGORY_CODE = "PAGE_WORDING";
	
	/** 系統錯誤訊息 */
	public static final String SYSTEM_ERROR = "系統發生錯誤";
	
	/** 系統參數錯誤訊息 */
	public static final String SYSTEM_ERROR_PARAMETER = "取得系統參數發生錯誤!";
	
	public static final String KEYCLOAK_USER = "KEYCLOAK_USER";
	
	/** 會員類型-一般會員 */
	public static final String USER_TYPE_MEMBER = "member";
	
	/** 會員類型-保戶會員 */
	public static final String USER_TYPE_NORMAL = "normal";
	
	/** 註冊條款 */
	public static final String USER_TERM_REGISTER = "TERM_REGISTER";
	
	/** 保單借貸款條款 */
	public static final String USER_TERM_LOAN = "TERM_LOAN";
	
	/** 旅平險條款 */
	public static final String USER_TERM_TRAVEL_POLICY = "TERM_TRAVEL_POLICY";
	
	public static final String SYSTEM_MSG_PARAMETER = "SYSTEM_MSG_PARAMETER";
	
	public static final String TRANS_CHANGE_ACCT_VO = "transChangeAccountVo";
	
	public static final String BONUS_MODE = "bounsMode";
	
	public static final String ANNUITY_PAY_METHOD = "annuityMethod";
	
	public static final String CUSHION_MODE = "cushionMode";
	
	public static final String TRAVEL_POLICY = "travelPolicy";

	public static final boolean isNotice = true;
	
	public static final String INSURANCEC_CLAIM_BLACKLIST_ALERT01 = "INSURANCEC_CLAIM_BLACKLIST_ALERT01";
	
	/**理賠聯盟鏈險種*/
	public static final String INSURANCE_CLAIM_PLAN = "INSURANCE_CLAIM_PLAN";
	
	/** 申請項目 */
	public static final String APPLICATION_ITEMS = "APPLICATION_ITEMS";
	
	/** 理賠聯盟鏈上傳申請應備文件 */
	public static final String INSURANCE_CLAIM_UPLOADFILE = "INSURANCE_CLAIM_UPLOADFILE";
	
	public static final String RELATION_ITEMS = "RELATION_ITEMS";
	
	public static final String SEND_COMPANY_ITEMS = "SEND_COMPANY_ITEMS";

	public static final String SEND_COMPANY_ITEMS_CONTACT = "SEND_COMPANY_ITEMS_CONTACT";

	public static final String PAYMENT_METHOD = "PAYMENT_METHOD";

	public static final String INSURANCE_ACCIDENT = "INSURANCE_ACCIDENT";
	
	public static final String INS_CLAIM_POLICY_2 = "ESERVICE_ONLINECHANGE_ODM_URL";
	
	public static final String INSURANCE_CLAIM = "INSURANCE_CLAIM";
	
	public static final String ONLINE_CHANGE_STATUS = "ONLINE_CHANGE_STATUS";
	
	public static final String MESSAGING_PARAMETER = "MessagingParameter";
	
	public static final String INSURANCE_CLAIM_TRANS_REMARK = "INSURANCE_CLAIM_TRANS_REMARK";
	
	public static final String CONTACT_INFO_TRANS_REMARK = "CONTACT_INFO_TRANS_REMARK";

	/**
	 *  保單理賠醫起通請閱讀並同意以下條款
	 * */
	public static final String MEDICAL_TREATMENT_CONTENT = "MEDICAL_TREATMENT_CONTENT";

	/** 保單理賠醫起通上傳申請應備文件 */
	public static final String MEDICAL_TREATMENT_UPLOADFILE = "MEDICAL_TREATMENT_UPLOADFILE";

	/** 保單理賠醫起通  畫面文案 */
	final String MEDICAL_TREATMENT_PAGE = "MEDICAL_TREATMENT_PAGE";
	/** 保單理賠醫起通  醫院集合 */
	public static final String MEDICAL_COMPANY_ITEMS = "MEDICAL_COMPANY_ITEMS";
	/**保單理賠醫起通  保單產品  */
	public static final String MEDICAL_TREATMENT_ITEMS = "MEDICAL_TREATMENT_ITEMS";

	/**
	 * 醫療保單查詢驗證年齡
	 */
	public static final String ESERVICE_MEDICAL_ONLINECHANGE_ODM_URL = "ESERVICE_MEDICAL_ONLINECHANGE_ODM_URL";
	/**
	 * 醫療保單黑名單提示信息
	 */
	public static final String MEDICAL_BLACKLIST_ALERT01 = "MEDICAL_BLACKLIST_ALERT01";
	//生日提示信息
	public static final String MEDICAL_BIRDATE_WINDOW_MSG = "MEDICAL_BIRDATE_WINDOW_MSG";

	public static final String INVESTMENT_TRANS_REMARK = "INVESTMENT_TRANS_REMARK";

	public static final String DEPOSIT_TRANS_REMARK = "DEPOSIT_TRANS_REMARK";

	public static final String PROCESS_INVESTMENT_LIST = "PROCESS_INVESTMENT_LIST";
	public static final String WORDING_NA201 = "WORDING_NA201";

	public static final String WORDING_NA202 = "WORDING_NA202";
	public static final  String CRON_JOB = "CRON_JOB";
}
