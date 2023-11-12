package com.twfhclife.adm.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.twfhclife.adm.model.*;
import org.apache.ibatis.annotations.Param;

/**
 * 線上申請 Dao.
 * 
 * @author all
 */
public interface OnlineChangeDao {
	
	/**
	 * 取得線上申請查詢結果(分頁).
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請查詢結果
	 */
	List<Map<String, Object>> getOnlineChangeDetailForPageable(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得線上申請查詢結果(分頁).
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請查詢結果
	 */
	List<Map<String, Object>> getOnlineChangeDetail(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得變更保單聯絡資料查詢結果分頁).
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請查詢結果
	 */
	List<Map<String, Object>> getOnlineChangeCIDetail(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得死亡除戶查詢結果分頁).
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請查詢結果
	 */
	List<Map<String, Object>> getOnlineChangeDnsDetail(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得交易序號對應的保單號碼.
	 * 
	 * @param transVo TransVo
	 * @return 回傳交易序號對應的保單號碼
	 */
	List<String> getTransPolicyNoList(@Param("transNum") String transNum);
	
	/**
	 * 取得線上申請結果總筆數.
	 * 
	 * @param transVo TransVo
	 * @return 回傳總筆數
	 */
	int getOnlineChangeDetailTotal(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得變更保單聯絡資料結果總筆數
	 * 
	 * @param transVo TransVo
	 * @return 回傳總筆數
	 */
	int getOnlineChangeCIODetailTotal(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得線上申請結果總筆數
	 * 
	 * @param transVo TransVo
	 * @return 回傳總筆數
	 */
	int getOnlineChangeDnsDetailTotal(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得線上申請資料-繳別.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-繳別
	 */
	Map<String, Object> getTransPaymode(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得線上申請-保單理賠
	 * @param transVo
	 * @return Map<String, Object>
	 */
	Map<String, Object> getTransInsuranceClaim(@Param("transVo") TransVo transVo);
	/**
	 * 取得線上申請-保單醫療
	 * @param transVo
	 * @return Map<String, Object>
	 */
	Map<String, Object> getMedicalTreatmentClaim(@Param("transVo") TransVo transVo);
	/**
	 * 取得線上申請資料-年金給付方式.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-年金給付方式
	 */
	Map<String, Object> getTransAnnuityMethod(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得線上申請資料-變更紅利選擇權.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-變更紅利選擇權
	 */
	Map<String, Object> getTransBonus(@Param("transVo") TransVo transVo);

	/**
	 * 取得線上申請資料-變更增值回饋分享金領取方式.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-變更增值回饋分享金領取方式
	 */
	Map<String, Object> getTransReward(@Param("transVo") TransVo transVo);

	/**
	 * 取得線上申請資料-自動墊繳選擇權.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-自動墊繳選擇權
	 */
	Map<String, Object> getTransCushion(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得線上申請資料-變更受益人.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-變更受益人
	 */
	Map<String, Object> getTransBeneficiary(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得受益人明細資料.
	 * 
	 * @param transBeneficiaryId 受益人ID
	 * @return 回傳受益人明細資料
	 */
	List<Map<String, Object>> getTransBeneficiaryDetailList(@Param("transBeneficiaryId") BigDecimal transBeneficiaryId);
	
	/**
	 * 取得受益人明細資料(原申請).
	 * 
	 * @param transBeneficiaryId 受益人ID
	 * @return 回傳受益人明細資料人(原申請)
	 */
	List<Map<String, Object>> getTransBeneficiaryDetailOldList(@Param("transBeneficiaryId") BigDecimal transBeneficiaryId);
	
	/**
	 * 取得線上申請資料-展期.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-展期
	 */
	Map<String, Object> getTransRenew(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得線上申請資料-減額.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-減額
	 */
	Map<String, Object> getTransReduce(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得線上申請資料-申請減少保險金額.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-申請減少保險金額
	 */
	Map<String, Object> getTransReducePolicy(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得申請減少保險金額明細資料.
	 * 
	 * @param transReducePolicyId 明細ID
	 * @return 回傳申請減少保險金額明細資料
	 */
	List<Map<String, Object>> getTransReducePolicyDetailList(@Param("transReducePolicyId") BigDecimal transReducePolicyId);
	
	/**
	 * 取得申請減少保險金額明細資料(原申請).
	 * 
	 * @param transReducePolicyId 明細ID
	 * @return 回傳申請減少保險金額明細資料(原申請)
	 */
	List<Map<String, Object>> getTransReducePolicyDetailOldList(@Param("transReducePolicyId") BigDecimal transReducePolicyId);
	
	/**
	 * 取得線上申請資料-變更保單聯絡資料.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-變更保單聯絡資料
	 */
	Map<String, Object> getTransContactInfo(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得變更保單聯絡明細資料.
	 * 
	 * @param transContractId 明細ID
	 * @return 回傳變更保單聯絡明細資料
	 */
	List<Map<String, Object>> getTransContactInfoDetailList(@Param("transContactId") BigDecimal transContactId);
	
	/**
	 * 取得變更保單聯絡明細資料(原申請).
	 * 
	 * @param transContractId 明細ID
	 * @return 回傳變更保單聯絡明細資料(原申請)
	 */
	List<Map<String, Object>> getTransContactInfoDetailOldList(@Param("transContactId") BigDecimal transContactId);
         
	/**
	 * 取得線上申請資料-設定停損停利通知.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-設定停損停利通知
	 */
	Map<String, Object> getTransFundNotification(@Param("transVo") TransVo transVo);
        
	/**
	 * 取得設定停損停利通知明細資料.
	 * 
	 * @param transContractId 明細ID
	 * @return 回傳設定停損停利通知明細資料
	 */
	List<Map<String, Object>> getTransFundNotificationDetailList(@Param("transFundNotificationId") BigDecimal transFundNotificationId);
	
	/**
	 * 取得線上申請資料-變更收費管道.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-變更收費管道
	 */
	Map<String, Object> getTransPayMethod(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得線上申請資料-變更信用卡效期.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-變更信用卡效期
	 */
	Map<String, Object> getTransCreditCardInfo(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得線上申請資料-解約.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-解約
	 */
	Map<String, Object> getTransCancelContract(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得線上申請資料-紅利提領列印.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-紅利提領列印
	 */
	Map<String, Object> getTransSurrender(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得線上申請資料-申請保單貸款.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-申請保單貸款
	 */
	Map<String, Object> getTransLoan(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得線上申請資料-滿期.
	 * @param TransVo transVo
	 * @return 回傳線上申請資料-滿期
	 */
	Map<String, Object> getTransMaturity(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得線上申請資料-基本資料變更.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-基本資料變更
	 */
	Map<String, Object> getTransLoanCustInfo(@Param("transVo") TransVo transVo);

	/**
	 * 更新線上申請狀態.
	 * 
	 * @param transVo TransVo
	 * @return 回傳更新結果
	 */
	int updateTransStatus(@Param("transVo") TransVo transVo);

	/**
	 * 取得線上申請資料-補發保單.
	 * 
	 * @param TransVo transVo
	 * @return 回傳線上申請資料-補發保單
	 */
	Map<String, Object> getTransResendPolicy(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得線上申請資料-終止授權.
	 * 
	 * @param TransVo transVo
	 * @return 回傳線上申請資料-終止授權
	 */
	Map<String, Object> getTransCancelAuth(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得線上申請資料-保單價值列印.
	 * 
	 * @param TransVo transVo
	 * @return 回傳線上申請資料-保單價值列印
	 */
	Map<String, Object> getTransValuePrint(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得線上申請資料-旅平險.
	 * 
	 * @param TransVo transVo
	 * @return 回傳線上申請資料-旅平險
	 */
	Map<String, Object> getTransTravelPolicy(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得線上申請上傳檔案
	 * 
	 * @param TransVo transVo
	 * @return 回傳線上申請上傳檔案
	 */
	List<TransUploadFileVo> getAllUploadFile(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得線上申請資料-變更匯款帳號
	 * 
	 * @param transVo
	 * @return 回傳線上申請資料-變更匯款帳號
	 */
	Map<String, Object> getTransChangePayAccount(@Param("transVo") TransVo transVo);

	/**
	 * 查詢線上申請額外資料
	 * 
	 * @param transVo
	 * @return
	 */
	List<TransExtendAttrVo> getTransExtendsByTransNum(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得線上申請資料-變更風險屬性
	 * 
	 * @param transVo
	 * @return 回傳線上申請資料-變更風險屬性
	 */
	Map<String, Object> getTransRiskLevel(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得線上申請資料-保戶基本資料更新
	 * 
	 * @param transVo
	 * @return 回傳線上申請資料-保戶基本資料更新
	 */
	Map<String, Object> getTransPolicyHolderProfile(@Param("transVo") TransVo transVo);

	/**
	 * 取得線上申請資料-取得變更投資標的及配置比例資料
	 * 
	 * @param transVo
	 * @return 回傳線上申請資料-取得變更投資標的及配置比例資料
	 */
	Map<String, Object> getTransFundSwitch(@Param("transVo") TransVo transVo);
	
	/**
	 * 死亡除戶
	 * 
	 * @param transVo
	 * @return 回傳線上申請資料-死亡除戶資料
	 */
	Map<String, Object> getDnsAlliance(@Param("transVo") TransVo transVo);

	/**
	 * 取得線上申請資料-取得變更投資標的及配置比例轉出資料
	 * 
	 * @param transVo
	 * @return 回傳線上申請資料-取得變更投資標的及配置比例轉出資料
	 */
	List<Map<String, Object>> getTransFundSwitchOut(@Param("transVo") TransVo transVo);

	/**
	 * 取得線上申請資料-變更投資標的及配置比例轉入資料
	 * 
	 * @param transVo
	 * @return 回傳線上申請資料-變更投資標的及配置比例轉入資料
	 */
	List<Map<String, Object>> getTransFundSwitchIn(@Param("transVo") TransVo transVo);
	
	/**
	 * 更新是否收到紙本
	 *
	 * @param vo
	 * @param date
	 * @return
	 */
	public int updateInsuranceClaimFileReceived(@Param("vo") TransInsuranceClaimVo vo, @Param("date") Date date);
	
	/**
	 * 更新是否收到紙本
	 * @param vo
	 * @return
	 */
	public int updateICFileReceived(@Param("vo") TransInsuranceClaimVo vo);
	
	/**
	 * 更新是否傳送聯盟鏈
	 *
	 * @param vo
	 * @param date
	 * @return
	 */
	public int updateInsuranceClaimSendAlliance(@Param("vo") TransInsuranceClaimVo vo, @Param("date") Date date);
	
	/**
	 * 更新是否傳送聯盟鏈
	 * @param transNum
	 * @return
	 */
	List<TransInsuranceClaimVo> getTransInsuranceClaimByTransNum(@Param("transNum") String transNum);
	
	Float getInsuranceClaimSequence();
	
	/**
	 * 更新是否傳送聯盟鏈
	 * @param vo
	 * @return
	 */
	public int addInsuranceClaim(@Param("vo") TransInsuranceClaimVo vo);
	
	/**
	 * 更新是否傳送聯盟鏈
	 * @param claimSeqId
	 * @return
	 */
	List<TransInsuranceClaimFileDataVo> getFileDatas(@Param("claimSeqId") Float claimSeqId);
	
	/**
	 * 更新是否傳送聯盟鏈
	 * @param vo
	 * @return
	 */
	public int addInsuranceClaimFileData(@Param("vo") TransInsuranceClaimFileDataVo vo);
	
	/**
	 * 保單理賠申請明細
	 * 
	 * @param params
	 */
	List getFileDatasByClaimSeqId(@Param("claimSeqId") Float claimSeqId);
	
	/**
	 * 保單理賠申請明細
	 * 
	 * @param params
	 */
	List getFileDatasDetailByClaimSeqId(@Param("claimSeqId") Float claimSeqId);
	/**
	 * 保單醫療文件數據
	 *
	 * @param claimSeqId
	 */
	List getMedicalTreatmentFileDatasDetailByClaimSeqId(@Param("claimSeqId") Float claimSeqId);

	/**
	 * 添加狀態歷程
	 * @param vo
	 * @return
	 */
	int addTransStatusHistory(@Param("vo") TransStatusHistoryVo vo);
	
	/**
	 * 添加補件
	 * @param vo
	 * @return
	 */
	int addTransRFE(@Param("vo") TransRFEVo vo);
	
	/**
	 * 查詢狀態歷程
	 * @param vo
	 * @return
	 */
	List<TransStatusHistoryVo> getTransStatusHistoryList(@Param("vo") TransStatusHistoryVo vo);
	
	/**
	 * 查詢補件單歷程
	 * @param vo
	 * @return
	 */
	List<TransRFEVo> getTransRFEList(@Param("vo") TransRFEVo vo);
	
	/**
	 * 查詢使用者角色
	 * @param userId
	 * @return
	 */
	List<String> getRoleName(@Param("userId") String userId);
	
	/**
	 * 查詢補件
	 * @param vo
	 * @return
	 */
	List<TransInsuranceClaimFileDataVo> getTransInsCliamFileData(@Param("vo")TransRFEVo vo);
	
	/**
	 * 更新補件單歷程
	 * @param vo
	 * @return
	 */
    int updateTransRFEStatus(@Param("vo")TransRFEVo vo);
    
    int getCaseIDNum(@Param("transNum") String transNum);
    
    /**
	  * 檢查是否已進入黑名單
	 * @param vo
	 * @return
	 */
	public int checkIdNoExist(@Param("transNum") String transNum);
    
    /**
	  * 加入黑名單
	 * @param vo
	 * @return
	 */
	public int addBlackList(@Param("vo")TransStatusHistoryVo vo);
	
	
	 /**
	  * 查詢當前狀態歷程
	 * @param vo
	 * @return
	 */
	public TransStatusHistoryVo getTransStatusHis(@Param("vo")TransStatusHistoryVo vo);
	
	
	public String getInfoTOMail(@Param("transNum") String transNum);
	
	
	public List getInsClaimStatisticsReport(@Param("vo")InsClaimStatisticsVo vo,@Param("columnItem")String columnItem,@Param("status")List<String> status,@Param("fileReceivedList")List<String> fileReceivedList,@Param("sendAllianceList")List<String> sendAllianceList);
	
	public List getInsClaimDetailReport(@Param("vo")InsClaimStatisticsVo vo,@Param("columnItem")String columnItem,@Param("status")List<String> status,@Param("fileReceivedList")List<String> fileReceivedList,@Param("sendAllianceList")List<String> sendAllianceList);
	
	public List getContactInfoStatisticsReport(@Param("vo")ContactInfoReportVo vo,@Param("columnItem")String columnItem,@Param("status")List<String> status,@Param("companyList")List<String> companyList);
	
	public List getContactInfoDetailReport(@Param("vo")ContactInfoReportVo vo,@Param("columnItem")String columnItem,@Param("status")List<String> status,@Param("companyList")List<String> companyList);
	
	public String getMailByRocid(@Param("rocId") String rocId);
	
	public String getCIFromCompanyId(@Param("transNum") String transNum);
	
	/**
	 * 导出死亡除戶
	 * 
	 * @param transVo TransVo
	 * @return 回傳死亡除戶查詢結果
	 */
	List<Map<String, Object>> getDNS_CSV(@Param("transVo") TransVo transVo);
	
	
	public Map<String, Object> getDnsDetailInfo(@Param("transNum") String transNum);

	/**
	 * 獲取查詢條件
	 *   保單理賠申請統計報表
	 *
	 * @return
	 */
	List getMedicalTreatmentStatisticsReport(@Param("vo") MedicalTreatmentStatisticsVo var1, @Param("columnItem") String var2, @Param("status") List<String> var3, @Param("fileReceivedList") List<String> var4, @Param("sendAllianceList") List<String> var5);

	/**
	 * 獲取查詢條件
	 *   保單理賠明顯統計報表
	 *
	 * @return
	 */
	List getMedicalTreatmentDetailReport(@Param("vo") MedicalTreatmentStatisticsVo var1, @Param("columnItem") String var2, @Param("status") List<String> var3, @Param("fileReceivedList") List<String> var4, @Param("sendAllianceList") List<String> var5);

	//獲取保險公司明顯
	List<Hospital> getHospitalList(@Param("functionName")String medicalTreatmentParameterCode);

	//獲取醫院明顯
	List<HospitalInsuranceCompany> getHospitalInsuranceCompanyList(@Param("functionName") String medicalTreatmentParameterCode);

	//保單醫療   補件單歷程.
	List<MedicalTreatmentClaimFileDataVo> getTransMedicalTreatmentClaimFiledatas(@Param("vo") TransRFEVo tVo);

	/***
	 * 查詢申請明細-已持有投資標的轉換查詢
	 * @param transVo
	 * @return
	 */
	List<TransFundConversionVo>  getConversionDetail(@Param("transVo")TransVo transVo);
	/***
	 * 查詢申請保單的基本明細
	 * @param transVo
	 * @return
	 */
	Map<String, Object> getOnlineChangeDetailByTransNum(@Param("transNum")String transNum);

	List<Map<String, Object>> selectCompareInvestments(@Param("transVo") TransVo transVo);

	TransDepositDetailVo getAppliedTransDeposits(@Param("transNum") String transNum);

	Map<String, Object> getTransPaymentByTransNum(@Param("transNum") String transNum);

    List<Map<String, Object>> getTransChangePremium(@Param("transNum") String transNum);

	String   getTransApplyItemByTransNum(@Param("transNum")String transNum);

	//查詢醫療保單
	int getMedicalTreatmentCaseIDNum(@Param("transNum")String transNum);
	//醫療保單查詢是否存在黑名单
	int checkMedicalTreatmentIdNoExist(@Param("transNum")String transNum);
	//醫療保單加入黑名單
	int addMedicalTreatmentBlackList(@Param("vo")TransStatusHistoryVo vo);
	//查詢當前醫療保單的名稱\郵件
	String getInfoMedicalTreatmentTOMail(@Param("transNum")String transNum);
	//進行查詢醫療條數
	int getOnlineChangeMedicalTreatmentDetailTotal(@Param("transVo") TransVo transVo);
	//進行查詢醫療明顯
	List<Map<String, Object>> getOnlineChangeMedicalTreatmentDetail(@Param("transVo")TransVo transVo);
	//進行查詢Base64的數據
	MedicalTreatmentClaimFileDataVo getMedicalTreatmentDetailBase64(@Param("fdId") Float fdId)throws Exception;
	//通過編號查詢醫療保單類別
	String getMedicalTreatmentFromCompanyId(@Param("trans_num")String trans_num);
	//更新是否傳送聯盟鏈
	public int updateMedicalTreatmentSendAlliance(@Param("vo") TransMedicalTreatmentClaimVo vo, @Param("date") Date date);
	 //獲取當前保單需要推送聯盟的數據
	List<TransMedicalTreatmentClaimVo> getTransMedicalTreatmentByTransNum(@Param("transNum") String transNum);
	//編號
	Float getMedicalTreatmentClaimSequence();
	//添加MedicalTreatmentClaim數據,便於後續推送給聯盟
	int addMedicalTreatmentClaim(@Param("vo")TransMedicalTreatmentClaimVo voTemp);
	//查詢當前保單狀態碼
    String getTransMedicalTreatmentByAllianceStatus(@Param("transNum")String transNum)throws Exception;
	//獲取當前案件需要上傳影響系統條數
	int getTransMedicalTreatmentByCount(@Param("transNum")String transNum)throws Exception;
	//獲取當前案件已上傳影像系統成功條數
	int getTransMedicalTreatmentBySuccessCount(@Param("transNum")String transNum)throws Exception;
	//獲取當前的保單的caseId
	String getTransMedicalTreatmentByCaseId(@Param("transNum")String transNum)throws Exception;
	//進行回壓聯盟結束流程狀態
	void updateTarnsMedicalTreatmentClaimToAllianceStatus(@Param("vo")TransMedicalTreatmentClaimVo mvo)throws Exception;

	List<NotifyOfNewCaseMedicalVo> getUnionCourseListAllianceStatusMsg(@Param("notifyVo")NotifyOfNewCaseMedicalVo vo)throws Exception;

    List<TransMedicalTreatmentClaimMedicalInfoVo> getMedicalInfoByClaimId(@Param("claimSeqId") Double claimId);

	MedicalTreatmentClaimFileDataVo getMedicalInfoDetailBase64(@Param("fdId") Float fdId);
	
	/**
	 * 取得線上申請資料-變更保單聯絡資料.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-變更保單聯絡資料
	 */
	Map<String, Object> getTransElectronicFormMethod(@Param("transVo") TransVo transVo);
	
	Map<String, Object> getTransDeratePaidOffMethod(@Param("transVo") TransVo transVo);
	
	Map<String, Object> getTransRolloverPeriodicallyMethod(@Param("transVo") TransVo transVo);
	
	Map<String, Object> getTransContractRevocationMethod(@Param("transVo") TransVo transVo);

	SignRecord getNewSignStatus(@Param("transNum") String transNum);

    String getSignFileByFileId(@Param("signFileId") String signFileId);

	List<BxczSignApiLog> getSignApiRecordByTransNum(@Param("transNum") String transNum);

    int updatePolicyClaimApplyDate(@Param("transNum") String transNum, @Param("date") Date date);

	int updateMedicalTreatmentApplyDate(@Param("transNum") String transNum, @Param("date") Date date);

	TransInsuranceClaimFileDataVo getInsuranceClaimFile(@Param("id") Float id);
}
