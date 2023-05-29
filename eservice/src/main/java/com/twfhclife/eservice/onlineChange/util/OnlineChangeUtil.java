package com.twfhclife.eservice.onlineChange.util;

/**
 * 線上申請-全域參數設定(含條款)
 * 請依功能放好~
 * 
 * @author Cathy
 *
 */
public interface OnlineChangeUtil {

	/** 線上申請-申請狀態清單 */
	final String ONLINE_CHANGE_STATUS_PARAMETER_CATEGORY_CODE = "ONLINE_CHANGE_STATUS";
	
	/** 線上申請-申請項目清單 */
	final String ONLINE_CHANGE_PARAMETER_CATEGORY_CODE = "ONLINE_CHANGE";
	
	/* 狀態 Start */
	/** 線上申請-申請中狀態-申請中 */
	final String TRANS_STATUS_APPLYING = "-1";
	
	/** 線上申請-申請中狀態-處理中 */
	final String TRANS_STATUS_PROCESSING = "0";
	
	/** 線上申請-申請中狀態-已審核 */
	final String TRANS_STATUS_AUDITED = "1";
	
	/** 線上申請-已完成狀態-已完成 */
	final String TRANS_STATUS_COMPLETE = "2";
	
	/** 線上申請-已完成狀態-已撤銷 */
	final String TRANS_STATUS_CANCEL = "3";
	
	/** 線上申請-已完成狀態-待補件 */
	final String TRANS_STATUS_BEADDED = "4";
	
	/** 線上申請-申請中狀態-已上傳 */
	final String TRANS_STATUS_UPLOADED = "5";
	
	/** 線上申請-申請中狀態-已註記異常件 */
	final String TRANS_STATUS_ABNORMAL = "7";
	
	/** 線上申請-申請中狀態-已接受 */
	final String TRANS_STATUS_RECEIVED = "8";
	/* 狀態 End */

	/* 等待數位驗證 */
	final String TRANS_STATUS_WAIT_SIGN = "41";
	/* 數位驗證處理中 */
	final String TRANS_STATUS_PROCESS_SIGN = "42";
	/* 數位身份驗證失敗 */
	final String TRANS_STATUS_FAIL_VERIFY = "43";
	/* 數位簽署失敗 */
	final String TRANS_STATUS_FAIL_SIGN = "44";
	
	//目前沒用不知道做啥的
	//public static final String TRANS_TYPE_ANNUITY_METHOD = "TRANS_TYPE_ANNUITY_METHOD";
	
	/** 預計保單領取地點 */
	final String RECEIVE_POLICY_LOCATION_PARAMETER_CATEGORY_CODE = "RECEIVE_POLICY_LOCATION";
	
	/** 被保人與要保人關係 */
	final String INSURED_RELATION_PARAMETER_CATEGORY_CODE = "INSURED_RELATION";
	
	/** 受益人類型 */
	final String BENEFICIARY_TYPE_PARAMETER_CATEGORY_CODE = "BENEFICIARY_TYPE";
	
	/** 受益人與被保人關係 */
	final String BENEFICIARY_RELATION_PARAMETER_CATEGORY_CODE = "BENEFICIARY_RELATION";
	
	/** 受益人分配方式 */
	final String BENEFICIARY_ALLOCATE_TYPE_PARAMETER_CATEGORY_CODE = "BENEFICIARY_ALLOCATE_TYPE";
	
	/** 信用卡種類 */
	final String CARD_TYPE_PARAMETER_CATEGORY_CODE = "CARD_TYPE";
	
	/** 繳別種類 */
	final String PAYMODE_TYPE_PARAMETER_CATEGORY_CODE = "PAYMODE_TYPE";
	
	/** 繳費方式類型 */
	final String PAYMENT_METHOD_TYPE_PARAMETER_CATEGORY_CODE = "ONLINE_CHANGE_PAYMENT_METHOD";
	
	/** 年金給付方式類型 */
	final String ANNUITY_METHOD_TYPE_PARAMETER_CATEGORY_CODE = "ANNUITY_METHOD_TYPE";
	
	/**
	 * 減額/展期
	 */
	final String RENEW_REDUCE_TYPE_PARAMETER_CATEGORY_CODE = "RENEW_REDUCE_TYPE";
	
	/** 線上申請-紅利選擇權類型 */
	final String BONUS_TYPE_PARAMETER_CATEGORY_CODE = "BONUS_TYPE";
	
	/** 線上申請-增值回饋分享金領取方式 */
	final String REWARD_TYPE_PARAMETER_CATEGORY_CODE = "REWARD_TYPE";
	
	/** 線上申請-是否自動墊繳 */
	final String CUSHION_TYPE_PARAMETER_CATEGORY_CODE = "CUSHION_TYPE";
	
	/** 線上申請-銀行名稱 */
	final String BANK_NAME_PARAMETER_CATEGORY_CODE = "BANK_NAME";
	
	/** 線上申請-銀行分行名稱 */
	final String BRANCHES_NAME_PARAMETER_CATEGORY_CODE = "BRANCHES_NAME";
	
	/** 線上申請-貸款條款 */
	final String LOANS_TERM_PARAMETER_CATEGORY_CODE = "LOANS_TERM";
	
	/** 線上申請-旅遊平安險條款1 */
	final String TRAVEL_POLICY_TERM1_PARAMETER_CATEGORY_CODE = "TRAVEL_POLICY_TERM1";
	
	/** 線上申請-旅遊平安險條款2-1 */
	final String TRAVEL_POLICY_TERM2_1_PARAMETER_CATEGORY_CODE = "TRAVEL_POLICY_TERM2_1";
	
	/** 線上申請-旅遊平安險條款2-2 */
	final String TRAVEL_POLICY_TERM2_2_PARAMETER_CATEGORY_CODE = "TRAVEL_POLICY_TERM2_2";
	
	/** 線上申請-旅遊平安險條款2-3 */
	final String TRAVEL_POLICY_TERM2_3_PARAMETER_CATEGORY_CODE = "TRAVEL_POLICY_TERM2_3";
	
	/** 線上申請-旅遊平安險條款2-4 */
	final String TRAVEL_POLICY_TERM2_4_PARAMETER_CATEGORY_CODE = "TRAVEL_POLICY_TERM2_4";
	
	/** 線上申請-旅遊平安險條款2-5 */
	final String TRAVEL_POLICY_TERM2_5_PARAMETER_CATEGORY_CODE = "TRAVEL_POLICY_TERM2_5";
	
	/** 線上申請-旅遊平安險條款2-6 */
	final String TRAVEL_POLICY_TERM2_6_PARAMETER_CATEGORY_CODE = "TRAVEL_POLICY_TERM2_6";
	
	/** 線上申請-旅遊平安險條款2-7 */
	final String TRAVEL_POLICY_TERM2_7_PARAMETER_CATEGORY_CODE = "TRAVEL_POLICY_TERM2_7";
	
	/** 線上申請-旅遊平安險條款3 */
	final String TRAVEL_POLICY_TERM3_PARAMETER_CATEGORY_CODE = "TRAVEL_POLICY_TERM3";
	
	/** 線上申請-旅遊平安險條款4 */
	final String TRAVEL_POLICY_TERM4_PARAMETER_CATEGORY_CODE = "TRAVEL_POLICY_TERM4";
	
	/** 業務員保全之照會公司 */
	final String BUSINESS_SECURITY_COMPANY = "BUSINESS_SECURITY_COMPANY";
	
	/** 線上申請-首頁類型 */
	final String ONLINE_CHANGE_HOME_PARAMETER_CATEGORY_CODE = "WORDING_3";
	final String ONLINE_CHANGE_HOME_PARAMETER_INVESTMENT_CATEGORY_CODE = "WORDING_INVESTMENT";

	/** 不屬可申請聯盟理賠的商品 */
	final String NOT_UNION_CLAIMS = "04";
	
	final String CUSTOMER_NAME = "被保人";
	
	final String FROM_COMPANY_L01 = "L01";
	
	/** 系統收件人MAIL地址 */
	final String CLAIM_CHAIN_ALERT_MAIL = "CLAIM_CHAIN_ALERT_MAIL";

	final String TWFHCLIFE_ADM = "twfhclife_adm";

	final String TWFHCLIFE_YQT_ADM = "twfhclife_yqt_adm";

	final String TWFHCLIFE_INVESTMENT_ADM = "twfhclife_investment_adm";

	final String TWFHCLIFE_DEPOSIT_ADM = "twfhclife_deposit_adm";
	
	final String TWFHCLIFE_ELECTRONIC_ADM = "twfhclife_electronic_adm";

	/** 系統收件人MAIL主題 */
	final String SYS_MAIL_SUB = "SYS_MAIL_SUB";
	
	/** 系統收件人MAIL內容 */
	final String SYS_MAIL_MSG = "SYS_MAIL_MSG";
	
	final String MAIL_INFO_TYPE_1 = "台銀首家件";
	
	final String MAIL_INFO_TYPE_2 = "聯盟件";
	
	/** 保單理賠MAIL */
	final String ELIFE_MAIL_005 = "ELIFE_MAIL-005";
	
	/** 保單理賠SMS */
	final String ELIFE_SMS_005 = "ELIFE_SMS-005";
	/** 理賠醫起通SMS */
	final String MEDICAL_SMS_037 = "MEDICAL_SMS-037";

        final String ELIFE_SMS_006 = "ELIFE_SMS-006";
	
	/** 變更保單聯絡資料MAIL */
	final String ELIFE_MAIL_007 = "ELIFE_MAIL-007";
	
	/** 變更保單聯絡資料SMS */
	final String ELIFE_SMS_007 = "ELIFE_SMS-007";
	/** 保戶前台線上申請送出成功後通知後台管理人員*/
	final String ELIFE_MAIL_009 = "ELIFE_MAIL_009";
	/** 保戶前台線上申請送出成功後通知保户同意轉送聯盟鏈*/
	final String ELIFE_MAIL_010 = "ELIFE_MAIL_010";
	/** 保戶前台線上申請送出成功後通知保户不同意轉送聯盟鏈*/
	final String ELIFE_MAIL_011 = "ELIFE_MAIL_011";
	/**保戶前台線上申請文件上传最大值，单位kb*/
	final String UPLOAD_MAX_NUMBER = "UPLOAD_MAX_NUMBER";
	/**客戶通知  轉收件通知  臺銀人壽保單理賠申請通知*/
	public static final String ELIFE_MAIL_023="ELIFE_MAIL_023";
	/**客戶通知	首家件通知  臺銀人壽保單理賠申請通知*/
	public static final String ELIFE_MAIL_024="ELIFE_MAIL_024";
	/**客戶通知	首家件通知  理賠醫起保單理賠申請通知*/
	public static final String MEDICAL_MAIL_036="MEDICAL_MAIL_036";
	/**內部人員通知	首家件通知	理賠聯盟鏈首家理賠申請通知*/
	public static final String ELIFE_MAIL_025="ELIFE_MAIL_025";
	/**內部人員通知	首家件通知	理賠醫起通首家理賠申請通知*/
	public static final String MEDICAL_MAIL_035="MEDICAL_MAIL_035";
	/**內部人員通知	轉收件通知	理賠聯盟鏈轉收申請通知*/
	public static final String ELIFE_MAIL_026="ELIFE_MAIL_026";
	/**已持有投資標的轉換-備註條款*/
	public static final String INVESTMENT_TRANSFORMATION_REMARKS="INVESTMENT_TRANSFORMATION_REMARKS";
	/**已持有投資標的轉換-同意條款*/
	public static final String INVESTMENT_TRANSFORMATION_CONSENT="INVESTMENT_TRANSFORMATION_CONSENT";
	/**已持有投資標的轉換-比例備註條款*/
	public static final String INVESTMENT_TRANSFORMATION_PROPORTION="INVESTMENT_TRANSFORMATION_PROPORTION";
	/**未來保費投資標的與分配比例-備註條款*/
	public static final String INVESTMENT_DISTRIBUTION_REMARKS="INVESTMENT_DISTRIBUTION_REMARKS";
	/**未來保費投資標的與分配比例-同意條款*/
	public static final String INVESTMENT_DISTRIBUTION_CONSENT="INVESTMENT_DISTRIBUTION_CONSENT";
	/**未來保費投資標的與分配比例-備註*/
	public static final String INVESTMENT_DISTRIBUTION_REMARK1="INVESTMENT_DISTRIBUTION_REMARK1";
	/**未來保費投資標的與分配比例-我要瞭解基金內容*/
	public static final String INVESTMENT_DISTRIBUTION_URI="INVESTMENT_DISTRIBUTION_URI";
	/**未來保費投資標的與分配比例-數量 */
	public static final String INVESTMENT_LIMIT="INVESTMENT_LIMIT";
	/**設定停損停利通知-備註*/
	public static final String NOTIFICATION_REMARKS="NOTIFICATION_REMARK1";
	/**臺幣  欲轉出基金與比例,限制單位數小值*/
	public static final String NTD_UNIT_MIN="NTD_UNIT_MIN";
	/**人民幣  欲轉出基金與比例,限制單位數小值*/
	public static final String USD_UNIT_MIN="USD_UNIT_MIN";
	/**臺幣  欲轉出基金與比例,限制比例小值*/
	public static final String NTD_PROPORTION_MIN="NTD_PROPORTION_MIN";
	/**美元   欲轉出基金與比例,限制比例小值*/
	public static final String USD_PROPORTION_MIN="USD_PROPORTION_MIN";
	/**投資型保單商品-內部人員通知	接受申請/申請完成/取消申請*/
	public static final String ELIFE_MAIL_027="ELIFE_MAIL_027";
	/**投資型保單商品-保戶人員通知	接受申請/申請完成/取消申請*/
	public static final String ELIFE_MAIL_028="ELIFE_MAIL_028";
	/** 電子表單申辦服務-通知保戶人員*/
	public static final String ELIFE_MAIL_029 = "ELIFE_MAIL_029";
	/** 電子表單申辦服務-通知管理員*/
	public static final String ELIFE_MAIL_030 = "ELIFE_MAIL_030";
	/** 減額繳清保險-通知管理員*/
	public static final String ELIFE_MAIL_032 = "ELIFE_MAIL_032";
	/** 減額繳清保險-通知保戶人員*/
	public static final String ELIFE_MAIL_033 = "ELIFE_MAIL_033";
	/** 展期定期保險-通知管理員*/
	public static final String ELIFE_MAIL_034 = "ELIFE_MAIL_034";
	/** 展期定期保險-通知保戶人員*/
	public static final String ELIFE_MAIL_035 = "ELIFE_MAIL_035";
	
	public static final String ELIFE_MAIL_036 = "ELIFE_MAIL_036";
	/** 契約撤銷-通知保戶人員 */
	public static final String ELIFE_MAIL_037 = "ELIFE_MAIL_037";
	
	/** 契約撤銷-通知管理員 */
	public static final String ELIFE_MAIL_038 = "ELIFE_MAIL_038";
	/**
	 * 停泊賬號信息數據
	 */
	public static final String DEPOSIT_UU_STOP_ACCOUNT = "DEPOSIT_UU_STOP_ACCOUNT";
	public static final String DEPOSIT_US_STOP_ACCOUNT = "DEPOSIT_US_STOP_ACCOUNT";
	/**投資標商品顯示賬戶*/
	public static final String SHOW_ACCOUNT_INVT_NOS="SHOW_ACCOUNT_INVT_NOS";

	public static final String ATTRIBUTE_REMARK1 = "ATTRIBUTE_REMARK1";
	public static final String ATTRIBUTE_SUCCESS1 = "ATTRIBUTE_SUCCESS1";

	public static final String CASH_PAYMENT_REMARK1 = "CASH_PAYMENT_REMARK1";
	public static final String CASH_PAYMENT_SUCCESS1 = "CASH_PAYMENT_SUCCESS1";

	public static final String CHANGE_PREMIUM_REMARK1 = "CHANGE_PREMIUM_REMARK1";
	public static final String CHANGE_PREMIUM_SUCCESS1 = "CHANGE_PREMIUM_SUCCESS1";
	
	/**
	 * 查詢CTC API功能
	 */
	//CTC API URL
	public static final String CTC_URL= "CTC_URL";
	//查詢登入者擁有保單
	public static final String CTC_SELECT_DATA= "CTC_SELECT_DATA";
	//查詢保單明細
	public static final String CTC_SELECT_DETAIL= "CTC_SELECT_DETAIL";
	//查詢申請電子表單狀態
	public static final String CTC_LIPMDA= "CTC_LIPMDA";
	//查詢減額、展期保單	
	public static final String CTC_LILIPM= "CTC_LILIPM";
	//減額、展期保單明細
	public static final String CTC_LILIPM_DETAIL= "CTC_LILIPM_DETAIL";
	//查詢撤銷保單明細資料
	public static final String CTC_REVOKE_BY_LIPRPA= "REVOKE_BY_LIPRPA";
	//查詢保戶轉帳紀錄
	public static final String CTC_REVOKE_BY_LINEAC= "REVOKE_BY_LINEAC";
	//查詢受理單位代碼
	public static final String CTC_REVOKE_BY_LILIPM= "REVOKE_BY_LILIPM";
	//查詢保單業務員姓名
	public static final String CTC_REVOKE_BY_LIBNAG= "REVOKE_BY_LIBNAG";
	//查詢經攬單位代理人
	public static final String CTC_REVOKE_BY_LILIPM_AGIN_REC_CODE= "REVOKE_BY_LILIPM_AGIN_REC_CODE";
	//查詢保單試算狀態
	public static final String CTC_ONLINE_TRIAL_DETAIL= "ONLINE_TRIAL_DETAIL";
	//查詢繳清、展期保單資料
	public static final String CTC_SELECT_DATA_ADD_CODE = "SELECT_DATA_ADD_CODE";
	/**
	 * 查詢CSP API功能
	 */
	//CSP API URL
	public static final String CSP_URL= "CSP_URL";
	//展期定期及繳額繳清CSP
	public static final String CSP_001_DETAIL= "CSP_001_DETAIL";
	//線上試算CSP
	public static final String CSP_002_DETAIL= "CSP_002_DETAIL";
}
