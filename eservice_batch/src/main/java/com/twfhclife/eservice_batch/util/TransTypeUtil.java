package com.twfhclife.eservice_batch.util;


/**
 * 線上申請項目類型
 * @author Cathy
 *
 */
public class TransTypeUtil {
	
	/** 線上申請-繳別 */
	public final static  String PAYMODE_PARAMETER_CODE = "PAYMODE";
	
	/** 線上申請-年金給付方式 */
	public final static  String ANNUITY_METHOD_PARAMETER_CODE = "ANNUITY_METHOD";
	
	/** 線上申請-紅利選擇權 */
	public final static  String BONUS_PARAMETER_CODE = "BONUS";
	
	/** 線上申請-增值回饋分享金領取方式 */
	public final static  String REWARD_PARAMETER_CODE = "REWARD";
	
	/** 線上申請-自動墊繳選擇權 */
	public final static  String CUSHION_PARAMETER_CODE = "CUSHION";
	
	/** 線上申請-受益人 */
	public final static  String BENEFICIARY_PARAMETER_CODE = "BENEFICIARY";
	
	/** 線上申請-展期 */
	public final static  String RENEW_PARAMETER_CODE = "RENEW";
		
	/** 線上申請-減額 */
	public final static  String REDUCE_PARAMETER_CODE = "REDUCE";
	
	/** 線上申請-展期定期保險/減額繳清保險 */
	public final static  String RENEW_REDUCE_PARAMETER_CODE = "RENEW_REDUCE";
	
	/** 線上申請-申請減少保險金額 */
	public final static  String REDUCE_POLICY_PARAMETER_CODE = "REDUCE_POLICY";
	
	/** 線上申請-聯絡資料變更 */
	public final static  String CONTACT_INFO_PARAMETER_CODE = "CONTACT_INFO";
	
	/** 線上申請-設定停利停損通知 */
	public final static  String FUND_NOTIFICATION_PARAMETER_CODE = "FUND_NOTIFICATION";
	
	/** 線上申請-變更收費管道 */
	public final static  String PAYMETHOD_PARAMETER_CODE = "PAYMETHOD";
	
	/** 線上申請-變更信用卡有效年月 */
	public final static  String CREDIT_CARD_DATE_PARAMETER_CODE = "CREDIT_CARD_DATE";
	
	/** 線上申請-解約 */
	public final static  String CANCEL_CONTRACT_PARAMETER_CODE = "CANCEL_CONTRACT";
	
	/** 線上申請-紅利提領 */
	public final static  String SURRENDER_PARAMETER_CODE = "SURRENDER";
	
	/** 線上申請-申請保單貸款 */
	public final static  String LOAN_PARAMETER_CODE = "LOAN";
	
	/** 線上申請-申請保單貸款 */
	public final static  String LOAN_NEW_PARAMETER_CODE = "LOAN_NEW";
	
	/** 線上申請-旅遊平安險申請 */
	public final static  String TRAVEL_POLICY_PARAMETER_CODE = "TRAVEL_POLICY";
	
	/** 線上申請-基本資料變更 */
	public final static  String CUST_INFO_PARAMETER_CODE = "CUST_INFO";
	
	/** 線上申請- 保單價值列印 */
	public final static  String VALUE_PRINT_PARAMETER_CODE = "VALUE_PRINT";
	
	/** 線上申請- 投保證明列印 */
	public final static  String CERT_PRINT_PARAMETER_CODE = "CERTIFICATE_PRINT";
	
	/** 線上申請- 理賠申請書套印 */
	public final static  String CLAIM_PARAMETER_CODE = "CLAIM";
	
	/** 線上申請- 滿期套印 */
	public final static  String MATURITY_PARAMETER_CODE = "MATURITY";
	
	/** 線上申請- 補發保單 */
	public final static  String POLICY_RESEND_PARAMETER_CODE = "POLICY_RESEND";
	
	/** 線上申請- 匯款帳號變更 */
	public final static  String CHANGE_PAY_ACCOUNT_PARAMETER_CODE = "CHANGE_PAY_ACCOUNT";
	
	/** 線上申請- 終止授權 */
	public final static  String CANCEL_AUTH_ACCOUNT_PARAMETER_CODE = "CANCEL_AUTH";
	
	/** 線上申請- 保戶基本資料更新 */
	public final static  String POLICY_HOLDER_PROFILE_PARAMETER_CODE = "POLICY_HOLDER_PROFILE";
	
	public static String getEZBusinessType(String transType){
		String type = "";// NB:承保科, PS:保全科, PA:保費科, CL:給付科
		switch(transType) {
		case PAYMODE_PARAMETER_CODE:
			type = "PS";
			break;
		case ANNUITY_METHOD_PARAMETER_CODE:
			type = "PS";
			break;
		case BONUS_PARAMETER_CODE:
			type = "PS";
			break;
		case REWARD_PARAMETER_CODE:
			type = "PS";
			break;
		case CUSHION_PARAMETER_CODE:
			type = "PS";
			break;
		case BENEFICIARY_PARAMETER_CODE:
			type = "PS";
			break;
		case RENEW_PARAMETER_CODE:
			type = "PS";
			break;
		case REDUCE_PARAMETER_CODE:
			type = "PS";
			break;
		case RENEW_REDUCE_PARAMETER_CODE:
			type = "PS";
			break;
		case REDUCE_POLICY_PARAMETER_CODE:
			type = "PS";
			break;
		case CONTACT_INFO_PARAMETER_CODE:
			type = "PS";
			break;
		case FUND_NOTIFICATION_PARAMETER_CODE:
			type = "PS";
			break;
		case VALUE_PRINT_PARAMETER_CODE:
			type = "PS";
			break;
		case CERT_PRINT_PARAMETER_CODE:
			type = "PS";
			break;			
		case POLICY_RESEND_PARAMETER_CODE:
			type = "PS";
			break;
			
		case PAYMETHOD_PARAMETER_CODE:
			type = "PA";
			break;
		case CREDIT_CARD_DATE_PARAMETER_CODE:
			type = "PA";
			break;
		case CANCEL_AUTH_ACCOUNT_PARAMETER_CODE:
			type = "PA";
			break;

		case SURRENDER_PARAMETER_CODE:
			type = "CL";
			break;
		case LOAN_PARAMETER_CODE:
			type = "CL";
			break;
		case CLAIM_PARAMETER_CODE: 
			type = "CL";
			break;
		case MATURITY_PARAMETER_CODE:
			type = "CL";
			break;
		case CHANGE_PAY_ACCOUNT_PARAMETER_CODE:
			type = "CL";
			break;
		case LOAN_NEW_PARAMETER_CODE:
			type = "CL";
			break;
			
		default :
			type = "PS";
			break;
		}
		return type;
	}
}