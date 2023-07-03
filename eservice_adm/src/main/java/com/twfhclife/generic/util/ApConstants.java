package com.twfhclife.generic.util;

/**
 * 全域常數設定.
 * @author David
 * @version 1.0
 */
public interface ApConstants {

	final String NEW_LINE_SEPARATOR = "\n";
	
	final String KEYCLOAK_REALM = "twfhclife";
	
	final String SYSTEM_ID = "eservice_adm";

	final String SYSTEM_API_ID = "eservice_api";

	final String SYSTEM_ID_ESERVICE = "eservice";
	
	/** 系統錯誤訊息 */
	public static final String SYSTEM_ERROR = "系統發生錯誤";
	
	/**
	 * KEYCLOAK USER ID
	 */
	public static final String LOGIN_KEYCLOAK_USER_ID = "KEYCLOAK_USER_ID";
	
	/** 登入帳號. */
	public static final String LOGIN_USER_ID = "LOGIN_USER_ID";
	
	/** 登入顯示名稱. */
	public static final String LOGIN_SHOW_NAME = "LOGIN_SHOW_NAME";

	/** 登入名稱. */
	public static final String LOGIN_USER_NAME = "LOGIN_USER_NAME";

	/** 登入時間. */
	public static final String LOGIN_TIME = "LOGIN_TIME";

	/** 登入訊息. */
	public static final String LOGIN_MSG = "LOGIN_MSG";
	
	/** 登入UUID. */
	public static final String LOGIN_PROCESS_UUID = "LOGIN_PROCESS_UUID";
	
	/** 事件類型 */
	final String EVENT_TYPE_PARAMETER_CATEGORY_CODE = "EVENT_TYPE_JD";
	
	public static final String MENU_LIST = "MENU_LIST";
	
	public static final String DIV_AUTH_MAP = "DIV_AUTH_MAP";
	
	public static final String KEYCLOAK_USER = "KEYCLOAK_USER";
	
	/** 繳別 */
	public static final String TRANS_TYPE_PAYMODE = "PAYMODE";
	
	/** 年金給付方式 */
	public static final String TRANS_TYPE_ANNUITY_METHOD = "ANNUITY_METHOD";
	
	/** 變更紅利選擇權 */
	public static final String TRANS_TYPE_BONUS = "BONUS";
	
	/** 變更增值回饋分享金領取方式 */
	public static final String TRANS_TYPE_REWARD = "REWARD";
	
	/** 自動墊繳選擇權 */
	public static final String TRANS_TYPE_CUSHION = "CUSHION";
	
	/** 變更受益人 */
	public static final String TRANS_TYPE_BENEFICIARY = "BENEFICIARY";
	
	/** 展期 */
	public static final String TRANS_TYPE_RENEW = "RENEW";
	
	/** 減額 */
	public static final String TRANS_TYPE_REDUCE = "REDUCE";
	
	/** 申請減少保險金額 */
	public static final String TRANS_TYPE_REDUCE_POLICY = "REDUCE_POLICY";
	
	/** 變更保單聯絡資料 */
	public static final String TRANS_TYPE_CONTACT_INFO = "CONTACT_INFO";
	
	/** 死亡除戶 */
	public static final String TRANS_TYPE_DNS_ALLIANCE = "DNS_ALLIANCE";
	
	/** 設定停損停利通知 */
	public static final String TRANS_TYPE_FUND_NOTIFICATION = "FUND_NOTIFICATION";
	
	/** 變更收費管道 */
	public static final String TRANS_TYPE_PAYMETHOD = "PAYMETHOD";
	
	/** 變更信用卡效期 */
	public static final String TRANS_TYPE_CREDIT_CARD_DATE = "CREDIT_CARD_DATE";
	
	/** 解約 */
	public static final String TRANS_TYPE_CANCEL_CONTRACT = "CANCEL_CONTRACT";
	
	/** 紅利提領列印 */
	public static final String TRANS_TYPE_SURRENDER = "SURRENDER";
	
	/** 申請保單貸款 */
	public static final String TRANS_TYPE_LOAN = "LOAN";
	
	/** 基本資料變更 */
	public static final String TRANS_TYPE_CUST_INFO = "CUST_INFO";
	
	/** 基本資料變更 */
	public static final String USERS_STATUS_ENABLED = "enable";
	
	/** 登入角色. */
	public static final String LOGIN_USER_ROLE_NAME = "LOGIN_USER_ROLE_NAME";
	
	/** 登入角色. 保服操作員*/
	public static final String ROLE_CODE_00 = "ROLE_CODE_00";
	
	/** 登入角色. 保服審核*/
	public static final String ROLE_CODE_01 = "ROLE_CODE_01";
	
	/** 登入角色. 保服覆核*/
	public static final String ROLE_CODE_02 = "ROLE_CODE_02";
	
	/** 異常件註記原因*/
	public static final String ABNORMAL_REASON = "ABNORMAL_REASON";
	
	/** 異常件註記原因. 十天內未寄回紙本文件*/
	public static final String REJECT_REASON_01 = "01";
	
	/** 已回收紙本*/
	public static final String STATUS_7 = "7";
	
	/** 已回收纸本*/
	public static final String FILE_RECEIVED = "1";
	
	/** 已發送聯盟*/
	public static final String SEND_ALLIANCE = "Y";
	
	/**異常件異常件通知主題 */
	public static final String ABNORMAL_REASON_SUB = "ABNORMAL_REASON_SUB";
	
	/**異常件異常件通知訊息 */
	public static final String ABNORMAL_REASON_MSG = "ABNORMAL_REASON_MSG";
	
	public static final String INS_CLAIM_COMPLETED = "INS_CLAIM_COMPLETED";
	
	/**保單理賠已完成主題 */
	public static final String INS_CLAIM_COMPLETED_SUB = "INS_CLAIM_COMPLETED_SUB";
	
	/**保單理賠已完成訊息 */
	public static final String INS_CLAIM_COMPLETED_MSG = "INS_CLAIM_COMPLETED_MSG";
	
	/** 申請項目 */
	public static final String APPLICATION_ITEMS = "APPLICATION_ITEMS";
	
	/** 畫面文案 */
	final String PAGE_WORDING = "PAGE_WORDING";
	
	/** 理賠聯盟鏈上傳申請應備文件 */
	public static final String INSURANCE_CLAIM_UPLOADFILE = "INSURANCE_CLAIM_UPLOADFILE";
	
	public static final String RELATION_ITEMS = "RELATION_ITEMS";
	
	public static final String SEND_COMPANY_ITEMS = "SEND_COMPANY_ITEMS";

	public static final String SEND_COMPANY_ITEMS_CONTACT = "SEND_COMPANY_ITEMS_CONTACT";

	public static final String PAYMENT_METHOD = "PAYMENT_METHOD";
	
	public static final String INSURANCE_ACCIDENT = "INSURANCE_ACCIDENT";
	
	public static final String ONLINE_CHANGE_STATUS = "ONLINE_CHANGE_STATUS";
	public static final String CONTENT_INFO_CHANGE_STATUS = "CONTENT_INFO_CHANGE_STATUS";

	public static final String FROM_COMPANY_L01 = "L01";
	
	public static final String MESSAGING_PARAMETER = "MessagingParameter";
	
	public static final String INSURANCE_CLAIM_TRANS_REMARK = "INSURANCE_CLAIM_TRANS_REMARK";
	
	public static final String INSURANCE_CLAIM_ABNORMAL_TRANS_REMARK = "INSURANCE_CLAIM_ABNORMAL_TRANS_REMARK";
	
	/** 保單理賠MAIL-已完成 */
	public static final String ELIFE_MAIL_005 = "ELIFE_MAIL-005";
	
	/** 保單理賠MAIL-異常件 */
	public static final String ELIFE_MAIL_006 = "ELIFE_MAIL-006";

	/**
	 *保單醫起通
	 * */
	final String MEDICAL_TREATMENT_PARAMETER_CODE = "MEDICAL_TREATMENT";

	/**
	 *保單醫起通醫療資料介接案件狀態 組
	 * */
	final String MEDICAL_INTERFACE_STATUS = "MEDICAL_INTERFACE_STATUS";
	/** 保單理賠醫起通上傳申請應備文件 */
	public String MEDICAL_TREATMENT_UPLOADFILE = "MEDICAL_TREATMENT_UPLOADFILE";
	/***
	 * 聯盟醫起通醫療文件類型組
	 **/
	final String MEDICAL_TREATMENT_FEDERATION_FILE_TYPE = "MEDICAL_TREATMENT_FEDERATION_FILE_TYPE";

	/**
	 * 醫療異常件異常件通知訊息  組
	 */
	final String MEDICAL_ABNORMAL_REASON_MSG = "MEDICAL_ABNORMAL_REASON_MSG";
	//更新案件提示信息
	final String MEDICAL_UPDATE_WINDOW_MSG = "MEDICAL_UPDATE_WINDOW_MSG";
	//更新案件提示信息,案件還未推送至聯盟
	final String MEDICAL_UPDATE_STATUS_WINDOW_MSG = "MEDICAL_UPDATE_STATUS_WINDOW_MSG";
	//醫療病例重新上傳提升信息
	final String MEDICAL_ITPR_WINDOW_MSG = "MEDICAL_ITPR_WINDOW_MSG";
	//禁止醫療病例重新上傳提示信息
	final String MEDICAL_NOT_ITPR_WINDOW_MSG = "MEDICAL_NOT_ITPR_WINDOW_MSG";
	//保險公司發送醫療資料重送
	public static final String MEDICAL_INTERFACE_STATUS_ITPR = "MEDICAL_INTERFACE_STATUS_ITPR";
	//保險公司(首家/轉收家)已確認，不申請此次醫療資料查調
	public static final String MEDICAL_INTERFACE_STATUS_ITPS_END = "MEDICAL_INTERFACE_STATUS_ITPS_END";
	//流程結束
	public static final String MEDICAL_INTERFACE_STATUS_PQHF_END = "MEDICAL_INTERFACE_STATUS_PQHF_END";
	//案件已取得醫療資料，平台CallBack給保險公司成功
	public static final String MEDICAL_INTERFACE_STATUS_HTPS_PTIS = "MEDICAL_INTERFACE_STATUS_HTPS_PTIS";
	//保險公司呼叫平台已成功取得醫療資料
	public static final String MEDICAL_INTERFACE_STATUS_ITPS = "MEDICAL_INTERFACE_STATUS_ITPS";
	//聯盟案件狀態未結束提示信息
	public static final String MEDICAL_NOT_FINISHED_WINDOW_MSG = "MEDICAL_NOT_FINISHED_WINDOW_MSG";
	//API 406 地址
	public static final String MEDICALALLIANCE_API406_URL = "medicalAlliance.api406.url";
	//聯盟案件更新失敗提示
	public static final String MEDICAL_NOT_APIALLIANCE_WINDOW_MSG = "MEDICAL_NOT_APIALLIANCE_WINDOW_MSG";
	//目前不可執行已完成-文件未上傳影響系統提示信息
	public static final String MEDICAL_NOT_FILE_WINDOW_MSG = "MEDICAL_NOT_FILE_WINDOW_MSG";
	//目前不可執行已完成-文件未存入DB提示信息
	public static final String MEDICAL_FILE_NOT_DB_WINDOW_MSG = "MEDICAL_FILE_NOT_DB_WINDOW_MSG";
	//目前不可執行重新上傳-文件未上傳影響系統提示信息
	public static final String MEDICAL_UPLOAD_NOT_FILE_WINDOW_MSG = "MEDICAL_UPLOAD_NOT_FILE_WINDOW_MSG";
	//目前不可執行重新上傳-文件未存入DB提示信息
	public static final String MEDICAL_UPLOAD_FILE_NOT_DB_WINDOW_MSG = "MEDICAL_UPLOAD_FILE_NOT_DB_WINDOW_MSG";


	/** 線上申請-未持有投資標的轉換 狀態 */
	final String INVESTMENT_STATUS_OUT = "OUT";
	/** 線上申請-未持有投資標的轉換 狀態 */
	final String INVESTMENT_STATUS_IN = "IN";
	public static final String LOGIN_VALIDATE_CODE = "LOGIN_VALIDATE_CODE";
}
