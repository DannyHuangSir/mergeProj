package com.twfhclife.eservice.onlineChange.util;

/**
 * 
 * 不可使用保單時的回傳訊息
 * 
 * @author Cathy
 *
 */
public interface OnlineChangMsgUtil {
	
	/** 線上申請-保單已失效(超過失效日期) */
	final String POLICY_EXPIREDATE_MSG = "此張保單已過投保終期，故無法使用。";

	/** 線上申請-非投資型保單 */
	final String POLICY_N_INVESTMENT_MSG = "此張保單非投資型保單，故無法使用。";
	
	/** 線上申請-繳費方式非信用卡付款 */
	final String POLICY_N_CDCARD_PAY_MSG = "此張保單非信用卡繳款類型，故無法使用。";
	
	/** 線上申請-無舊受益人資料 */
	final String POLICY_N_BENEFICIARY_MSG = "此張保單沒有受益人，故無法更改。";
	
	/** 線上申請-無聯絡資料 */
	final String POLICY_N_CONTACT_MSG = "此張保單沒有聯絡資料，故無法更改。";
	
	/** 線上申請-無風險屬性 */
	final String POLICY_N_PORTFOLIO_MSG = "您尚未設定投資風險屬性，請至「變更風險屬性」完成設定後，於次一工作日再進行此項申請";
	
	/** 線上申請-可貸金額不足 */
	final String POLICY_LOAN_MSG = "可貸款金額不足，無法進行此項申請";
	
	/** 線上申請-已申請 */
	final String POLICY_HIST_TRANS_MSG = "前次申請尚在處理中，暫無法進行此項申請<br/>處理狀態可以至我的申請紀錄查詢。";
	
	/** 線上申請-保單已停效 
	final String POLICY_MESSAGE_ALERT_ = ""; */
	
	/** 戶名、證號、帳戶號碼有一空白則不能申請 */
	final String CHANGE_ACCOUNT_INFO_NOT_ALLOW_MSG = "此張保單匯款資訊不完整，故不允許變更。";
	
	/** 線上申請-契約狀況不允許變更 */
	final String POLICY_STATUS_NOT_ALLOW_MSG = "此張保單契約狀況不允許變更。";

	/** 線上申請-契約狀況-非首次申請(第二次申請,不可在申請非相同保單) */
	final String POLICY_STATUS_MANY_TIMES_MSG = "此張保單與申請中的保單不一致不允許申請。";

	/** 線上申請-繳別T不允許變更 */
	final String POLICY_PAYMODE_T_NOT_ALLOW_MSG = "此張保單的繳別不允許變更。";
	
	/** 線上申請-紅利選擇權繳別T不允許變更 */
	final String BONUS_PAYMODE_T_NOT_ALLOW_MSG = "你選擇的保單繳費方式為躉繳，不提供申請變更紅利選擇權";
	
	/** 線上申請-自動墊繳選擇權繳別T不允許變更 */
	final String CUSION_PAYMODE_T_NOT_ALLOW_MSG = "因保單繳費方式為躉繳，不提供自動墊繳選擇權變更";
	
	/** 線上申請-保單貸款註記不允許變更 */
	final String POLICY_LOAN_NOT_ALLOW_MSG = "此張保單現有保單貸款，故不允許變更。";
	
	/** 線上申請-非年金險不可變更年金給付方式 */
	final String POLICY_ANNU_NOT_ALLOW_MSG = "此張保單非年金險不可變更年金給付方式。";
	
	/** 線上申請-契約狀況不允許變更 */
	final String INSURED_NOT_SAME_MSG = "要保人與被保人為不同人不允許變更。";
	
	/** 線上申請-沒有投資型保單不提供變更 */
	final String NO_VUL_POLICY_MSG = "查無投資型保單可進行設定。";

	/** 線上申請-此商品不具保單價值，故無法申請保單價值列印 */
	final String NO_POLICY_VUL_NOT_APPLY_MSG = "此商品不具保單價值，故無法申請保單價值列印。";
	
	/** 線上申請-此張保單契約狀況不允許申請保價金列印 */
	final String POLICY_NOT_APPLY_VALUE_PRINT_MSG = "此張保單契約狀況不允許申請保價金列印。";
	
	/** 線上申請-此險種為投資型商品，請參考帳戶價值 */
	final String VUL_REF_ACCOUNT_VALUE = "此險種為投資型商品，請參考帳戶價值。";

	/**理賠聯盟鏈險種*/
	final String INSURANCE_CLAIM_PLAN = "此張保單不可申辦保單理賠。";
	
	final String INSURANCE_CLAIM_APPLYING = "有申請中的保單理賠,則不可再申請。";

	final String BACK_LIST_MSG = "已進入黑名單,不可再申請。";
	
	final String INSURANCE_CLAIM_NOT_SAME = "登入者和被保人不是同一人。";
	
	final String CONTACT_INFO_APPLYING = "有申請中的聯絡資料變更,則不可再申請。";

	/**
	 * 保單醫起通
	 * */
	final String MEDICAL_TREATMENT_CLAIM_APPLYING = "有申請中的保單醫起通,則不可再申請。";

	final String MEDICAL_TREATMENT_CLAIM_PLAN = "此張保單不可申辦保單醫起通。";

	/**
	 *投資型保單商品發送保戶人員參數-申請通知
	 * */
	final String INVESTMENT_POLICY_APPLY_TITLE = "變更申請通知";
	final String INVESTMENT_POLICY_APPLY_CAPACITY = "本公司已收受您的線上辦理已持有投資標的轉換申請案，本公司將為您辦理。";
	final String INVESTMENT_POLICY_APPLY_CAPACITY1 = "本公司已收受您的線上辦理未來保費投資標的與分配比例申請案，本公司將為您辦理。";
	final String INVESTMENT_POLICY_APPLY_CAPACITY3 = "本公司已收受您的線上辦理收益分配或撥回資產分配方式申請案，本公司將為您辦理。";
	final String INVESTMENT_POLICY_APPLY_CAPACITY4 = "本公司已收受您的線上辦理定期超額保費變更申請案，本公司將為您辦理。";
	final String INVESTMENT_POLICY_APPLY_CAPACITY5 = "本公司已收受您的線上辦理保單提領(贖回)變更申請案，本公司將為您辦理。";
	final String INVESTMENT_POLICY_APPLY_CANCEL_TITLE = "取消申請通知";
	final String INVESTMENT_POLICY_APPLY_CANCEL_CAPACITY = "您已經取消已持有投資標的轉換申請案。";


}
