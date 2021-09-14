package com.twfhclife.generic.utils;

/**
 * 全域常數設定.
 * @author David
 * @version 1.0
 */
public interface ApConstants {

	final String NEW_LINE_SEPARATOR = "\n";
	
	final String KEYCLOAK_REALM = "twfhclife";
	
	/** 系統別 */
	final String SYSTEM_ID = "eservice_api";
	
	final String SYSTEM_ID_AMD = "eservice_adm";
	
	/** 系統錯誤訊息 */
	public static final String SYSTEM_ERROR = "系統發生錯誤";
	
	public static final String INVALID_SECRET = "不合法的調用";
	
	public static final String REALM_TWFHCLIFE = "twfhclife";
	
	public static final String SECRET_HEADER = "secret";
	/** 聯盟件     */
	public static final String ALLIANCE_PIECE = "ALLIANCE_PIECE";

	/** 日程安排-系統屬性 */
    public static final String SYS_PRO_SCH = "SYS_PRO_SCH";
		
    /** 資源定位地址-系統屬性 */
	public static final String SYS_PRO_API_URL = "SYS_PRO_API_URL";
	
	/** 基本URL-系統屬性 */
	public static final String SYS_PRO_BASE_URL = "SYS_PRO_BASE_URL";
	
	public static final String INSURANCE_CLAIM = "INSURANCE_CLAIM";	
	
	/**異常件異常件通知主題 */
	public static final String ABNORMAL_REASON_SUB = "ABNORMAL_REASON_SUB";
	
	/**異常件異常件通知訊息 */
	public static final String ABNORMAL_REASON_MSG = "ABNORMAL_REASON_MSG";
	
    public static final String TWFHCLIFE_ADM = "TWFHCLIFE_ADM";
	
	public static final String ONLINE_CHANGE_STATUS = "ONLINE_CHANGE_STATUS";
	
	public static final String MESSAGING_PARAMETER = "MessagingParameter";
	
	public static final String INSURANCE_CLAIM_TRANS_REMARK = "INSURANCE_CLAIM_TRANS_REMARK";
	
	/** 保單理賠MAIL */
	public static final String ELIFE_MAIL_005 = "ELIFE_MAIL-005";
	
	/** 保單理賠SMS */
	public static final String ELIFE_SMS_005 = "ELIFE_SMS-005";
	
	/**
	 * 異常件EMAIL
	 */
	public static final String ELIFE_EMAIL_006 = "ELIFE_EMAIL-006";
	
	/**
	 * 異常件SMS
	 */
	public static final String ELIFE_SMS_006 = "ELIFE_SMS-006";
	
	/** 保全資料變更MAIL */
	public static final String ELIFE_MAIL_007 = "ELIFE_MAIL-007";
	
	/** 保全資料變更SMS */
	public static final String ELIFE_SMS_007 = "ELIFE_SMS-007";
	
	public static final String ELIFE_EMAIL_008 = "ELIFE_EMAIL-008";
	
	public static final String ELIFE_SMS_008 = "ELIFE_SMS-008";
	/** 邮件模板*/
	public static final String ELIFE_MAIL_008 = "ELIFE_MAIL-008";
	/** 線上申請-申請中狀態-已註記異常件 */
	public static final String TRANS_STATUS_ABNORMAL = "7";
	/**调用联盟API无法响应，发送邮件*/
	public static final String  API_NO_MAIL_012="API_NO_MAIL_012";
	/**调用联盟API无响应，超过最大次数*/
	public static final String  API_NO_RESPONSE_NUMBER="API_NO_RESPONSE_NUMBER";
	/**轉修eservice進件過程中有出程錯誤時通知後台管理人員*/
	public static final String TRANSFER_MAIL_013="TRANSFER_MAIL_013";
	/** 轉修eservice進件過程中【一般件(非異常件)會通知管理人員】发送邮件*/
	public static final String TRANSFER_MAIL_014="TRANSFER_MAIL_014";
	/**轉修eservice進件過程中【一般件(非異常件)會通知保戶】发送邮件*/
	public static final String TRANSFER_MAIL_015="TRANSFER_MAIL_015";
	/** 將轉收至本公司的案件【資料及影像成功存入本公司系統】，通知後台管理人員发送邮件*/
	public static final String TRANSFER_MAIL_016="TRANSFER_MAIL_016";
	/**將轉收至本公司的案件【資料及影像存入失败本公司系統】，通知後台管理人員发送邮件*/
	public static final String TRANSFER_MAIL_017="TRANSFER_MAIL_017";
	/**【轉修eservice進件-已註記異常件-處理】通知後台管理人員 发送邮件*/
	public static final String TRANSFER_MAIL_018="TRANSFER_MAIL_018";
	/**【轉修eservice進件-已註記異常件-處理】通知後台保戶人員 发送邮件*/
	public static final String TRANSFER_MAIL_019="TRANSFER_MAIL_019";
	/**客戶通知  轉收件通知  臺銀人壽保單理賠申請通知*/
	public static final String ELIFE_MAIL_023="ELIFE_MAIL_023";
	/**客戶通知	首家件通知  臺銀人壽保單理賠申請通知*/
	public static final String ELIFE_MAIL_024="ELIFE_MAIL_024";
	/**內部人員通知	首家件通知	理賠聯盟鏈首家理賠申請通知*/
	public static final String ELIFE_MAIL_025="ELIFE_MAIL_025";
	/**內部人員通知	轉收件通知	理賠聯盟鏈轉收申請通知*/
	public static final String ELIFE_MAIL_026="ELIFE_MAIL_026";
}
