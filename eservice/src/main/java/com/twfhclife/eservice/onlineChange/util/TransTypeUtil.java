package com.twfhclife.eservice.onlineChange.util;

/**
 * 線上申請項目類型
 * @author Cathy
 *
 */
public interface TransTypeUtil {
	
	/** 線上申請-繳別 */
	final String PAYMODE_PARAMETER_CODE = "PAYMODE";

	/** 線上申請-定期超額保費 */
	final String CHANGE_PREMIUM_CODE = "CHANGE_PREMIUM";

	/** 線上申請-年金給付方式 */
	final String ANNUITY_METHOD_PARAMETER_CODE = "ANNUITY_METHOD";
	
	/** 線上申請-紅利選擇權 */
	final String BONUS_PARAMETER_CODE = "BONUS";
	
	/** 線上申請-增值回饋分享金領取方式 */
	final String REWARD_PARAMETER_CODE = "REWARD";
	
	/** 線上申請-自動墊繳選擇權 */
	final String CUSHION_PARAMETER_CODE = "CUSHION";
	
	/** 線上申請-受益人 */
	final String BENEFICIARY_PARAMETER_CODE = "BENEFICIARY";
	
	/** 線上申請-展期 */
	final String RENEW_PARAMETER_CODE = "RENEW";
		
	/** 線上申請-減額 */
	final String REDUCE_PARAMETER_CODE = "REDUCE";
	
	/** 線上申請-展期定期保險/減額繳清保險 */
	final String RENEW_REDUCE_PARAMETER_CODE = "RENEW_REDUCE";
	
	/** 線上申請-申請減少保險金額 */
	final String REDUCE_POLICY_PARAMETER_CODE = "REDUCE_POLICY";
	
	/** 線上申請-聯絡資料變更 */
	final String CONTACT_INFO_PARAMETER_CODE = "CONTACT_INFO";
	
	/** 線上申請-設定停利停損通知 */
	final String FUND_NOTIFICATION_PARAMETER_CODE = "FUND_NOTIFICATION";

	/** 線上申請-收益分配或撥回資產分配方式 */
	final String CASH_PAYMENT_PARAMETER_CODE = "CASH_PAYMENT";
	
	/** 線上申請-變更收費管道 */
	final String PAYMETHOD_PARAMETER_CODE = "PAYMETHOD";
	
	/** 線上申請-變更信用卡有效年月 */
	final String CREDIT_CARD_DATE_PARAMETER_CODE = "CREDIT_CARD_DATE";
	
	/** 線上申請-解約 */
	final String CANCEL_CONTRACT_PARAMETER_CODE = "CANCEL_CONTRACT";
	
	/** 線上申請-紅利提領 */
	final String SURRENDER_PARAMETER_CODE = "SURRENDER";
	
	/** 線上申請-申請保單貸款 */
	final String LOAN_PARAMETER_CODE = "LOAN";
	
	/** 線上申請-申請保單貸款(已申請指定帳號) */
	final String LOAN_NEW_PARAMETER_CODE = "LOAN_NEW";
	
	/** 線上申請-旅遊平安險申請 */
	final String TRAVEL_POLICY_PARAMETER_CODE = "TRAVEL_POLICY";
	
	/** 線上申請-基本資料變更 */
	final String CUST_INFO_PARAMETER_CODE = "CUST_INFO";
	
	/** 線上申請- 保單價值列印 */
	final String VALUE_PRINT_PARAMETER_CODE = "VALUE_PRINT";
	
	/** 線上申請- 投保證明列印 */
	final String CERT_PRINT_PARAMETER_CODE = "CERTIFICATE_PRINT";
	
	/** 線上申請- 理賠申請書套印 */
	final String CLAIM_PARAMETER_CODE = "CLAIM";
	
	/**
	 * 線上申請-保單理賠
	 */
	final String INSURANCE_CLAIM_PARAMETER_CODE = "INSURANCE_CLAIM";
	
	/** 線上申請- 滿期套印 */
	final String MATURITY_PARAMETER_CODE = "MATURITY";
	
	/** 線上申請- 補發保單 */
	final String POLICY_RESEND_PARAMETER_CODE = "POLICY_RESEND";
	
	/** 線上申請- 匯款帳號變更 */
	final String CHANGE_PAY_ACCOUNT_PARAMETER_CODE = "CHANGE_PAY_ACCOUNT";
	
	/** 線上申請- 終止授權 */
	final String CANCEL_AUTH_ACCOUNT_PARAMETER_CODE = "CANCEL_AUTH";
	
	/** 線上申請- 變更風險屬性 */
	final String RISK_LEVEL_PARAMETER_CODE = "RISK_LEVEL";
	
	/** 線上申請- 保戶基本資料更新 */
	final String POLICY_HOLDER_PROFILE_PARAMETER_CODE = "POLICY_HOLDER_PROFILE";

	/** 線上申請- 保戶基本資料更新 */
	final String FUND_SWITCH_PARAMETER_CODE = "FUND_SWITCH";
	
	/**
	 * 線上申請-死亡除戶
	 */
	final String DNS_PARAMETER_CODE = "DNS_ALLIANCE";

	/**
	 *保單醫起通
	 * */
	final String MEDICAL_TREATMENT_PARAMETER_CODE = "MEDICAL_TREATMENT";

	/**
	 * 醫療異常件異常件通知訊息  組
	 */
	final String MEDICAL_ABNORMAL_REASON_MSG = "MEDICAL_ABNORMAL_REASON_MSG";

	/** 線上申請-未來保費投資標的與分配比例 */
	final String INVESTMENT_PARAMETER_CODE = "INVESTMENT";
	/** 線上申請-申請保單提領(贖回) */
	final String DEPOSIT_PARAMETER_CODE = "DEPOSIT";
	/** 線上申請-已持有投資標的轉換 */
	final String INVESTMENT_CONVERSION_CODE = "CONVERSION";
	/** 線上申請-未持有投資標的轉換 狀態 */
	final String INVESTMENT_STATUS_OUT = "OUT";
	/** 線上申請-未持有投資標的轉換 狀態 */
	final String INVESTMENT_STATUS_IN = "IN";

}