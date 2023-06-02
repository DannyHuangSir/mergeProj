package com.twfhclife.eservice.onlineChange.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.VerifyPolicyResult;
import com.twfhclife.eservice.policy.model.PolicyListVo;
import com.twfhclife.eservice.web.model.TransVo;

/**
 * 線上申請主檔服務.
 * 
 * @author all
 */
public interface ITransService {
	
	/**
	 * 線上申請-新增.
	 * 
	 * @param transVo TransVo
	 * @return 回傳影響筆數
	 */
	int insertTrans(@Param("transVo") TransVo transVo);

	/**
	 * 取線上申請序號.
	 */
	public String getTransNum();

	/**
	 * 線上申請保單驗證.
	 * 
	 * @param policyNo 保單號碼
	 * @param transType 申請類型
	 * @return 回傳驗證結果
	 */
	public VerifyPolicyResult verifyPolicyRule(String policyNo, String transType);

	/**
	 * 檢查是否已經申請.
	 * 
	 * @param policyNo 保單號碼
	 * @param transType 申請類型
	 * @param transStatus 申請狀態
	 * @return 回傳是否已經申請
	 */
	public boolean isTransApplyed(String policyNo, String transType, String transStatus);
	
	/**
	 * 處理保單過投保終期狀態鎖定.
	 * 
	 * @param policyList 保單清單資料
	 * @return 回傳處理過的保單清單資料
	 */
	public List<PolicyListVo> handlePolicyStatusExpiredLocked(List<PolicyListVo> policyList);
	
	/**
	 * 處理全域通用型的狀態鎖定，限定保單狀態不能申請(個別申請類型的限制，請寫在各自的服務內).
	 * 
	 * @param policyList 保單清單資料
	 * @param userId 使用者ID
	 * @param transType 申請類型
	 * @return 回傳處理過的保單清單資料
	 */
	public List<PolicyListVo> handleGlobalPolicyStatusLocked(List<PolicyListVo> policyList, String userId,
			String transType);
	
	/**
	 * 呼叫 SP verifyPolicyRule 驗證是否可以申請.
	 * 
	 * @param policyList 保單清單資料
	 * @param transType 申請類型
	 * @return 回傳處理過的保單清單資料
	 */
	public List<PolicyListVo> handleVerifyPolicyRuleStatusLocked(List<PolicyListVo> policyList, String transType);
	
	
	public Float getBatchIdSequence();

    Date getLastCompleteTime(String investmentConversionCode, String userId);

    int updateTransStatus(String transNum, String status);

	int checkTransInSignProcess(String transNum);
}