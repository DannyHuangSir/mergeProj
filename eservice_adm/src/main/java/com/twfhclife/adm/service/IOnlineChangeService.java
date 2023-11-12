package com.twfhclife.adm.service;

import com.twfhclife.adm.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 報表查詢-線上申請查詢服務.
 * 
 * @author all
 */
public interface IOnlineChangeService {
	
	/**
	 * 取得線上申請查詢結果.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請查詢結果
	 */
	public List<Map<String, Object>> getOnlineChangeDetail(TransVo transVo, boolean isPageable);
	
	/**
	 * 取得線上申請結果總筆數
	 * 
	 * @param transVo TransVo
	 * @return 回傳總筆數
	 */
	public int getOnlineChangeDetailTotal(TransVo transVo);
	
	/**
	 * 取得變更保單聯絡資料結果總筆數
	 * 
	 * @param transVo TransVo
	 * @return 回傳總筆數
	 */
	public int getOnlineChangeCIODetailTotal(TransVo transVo);

	
	/**
	 * 取得死亡除戶結果總筆數
	 * 
	 * @param transVo TransVo
	 * @return 回傳總筆數
	 */
	public int getOnlineChangeDnsDetailTotal(TransVo transVo);


	/**
	 * 取得線上申請資料-繳別.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-繳別
	 */
	public Map<String, Object> getTransPaymode(TransVo transVo);
	
	/**
	 * 取得線上申請-保單理賠
	 * @param transVo
	 * @return Map<String, Object>
	 */
	public Map<String, Object> getTransInsuranceClaim(TransVo transVo);

	/**
	 * 取得線上申請資料-年金給付方式.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-年金給付方式
	 */
	public Map<String, Object> getTransAnnuityMethod(TransVo transVo);
	
	/**
	 * 取得線上申請資料-變更紅利選擇權.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-變更紅利選擇權
	 */
	public Map<String, Object> getTransBonus(TransVo transVo);
	
	/**
	 * 取得線上申請資料-變更增值回饋分享金領取方式.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-變更增值回饋分享金領取方式
	 */
	public Map<String, Object> getTransReward(TransVo transVo);

	/**
	 * 取得線上申請資料-自動墊繳選擇權.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-自動墊繳選擇權
	 */
	public Map<String, Object> getTransCushion(TransVo transVo);
	
	/**
	 * 取得線上申請資料-變更受益人.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-變更受益人
	 */
	public Map<String, Object> getTransBeneficiary(TransVo transVo);
	
	/**
	 * 取得線上申請資料-展期.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-展期
	 */
	public Map<String, Object> getTransRenew(TransVo transVo);
	
	/**
	 * 取得線上申請資料-減額.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-減額
	 */
	public Map<String, Object> getTransReduce(TransVo transVo);
	
	/**
	 * 取得線上申請資料-申請減少保險金額.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-申請減少保險金額
	 */
	public Map<String, Object> getTransReducePolicy(TransVo transVo);
	
	/**
	 * 取得線上申請資料-變更保單聯絡資料.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-變更保單聯絡資料
	 */	
	public Map<String, Object> getTransContactInfo(TransVo transVo);
	
	/**
	 * 取得線上申請資料-設定停損停利通知.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-設定停損停利通知
	 */
	public Map<String, Object> getTransFundNotification(TransVo transVo);
	
	/**
	 * 取得線上申請資料-變更收費管道.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-變更收費管道
	 */
	public Map<String, Object> getTransPayMethod(TransVo transVo);
	
	/**
	 * 取得線上申請資料-變更信用卡效期.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-變更信用卡效期
	 */
	public Map<String, Object> getTransCreditCardInfo(TransVo transVo);
	
	/**
	 * 取得線上申請資料-解約.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-解約
	 */
	public Map<String, Object> getTransCancelContract(TransVo transVo);
	
	/**
	 * 取得線上申請資料-紅利提領列印.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-紅利提領列印
	 */
	public Map<String, Object> getTransSurrender(TransVo transVo);
	
	/**
	 * 取得線上申請資料-申請保單貸款.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-申請保單貸款
	 */
	public Map<String, Object> getTransLoan(TransVo transVo);
	
	/**
	 * 取得線上申請資料-基本資料變更.
	 * 
	 * @param transVo TransVo
	 * @return 回傳線上申請資料-基本資料變更
	 */
	public Map<String, Object> getTransLoanCustInfo(TransVo transVo);
	
	/**
	 * 更新線上申請狀態.
	 * 
	 * @param transVo TransVo
	 * @return 回傳更新結果
	 */
	public int updateTransStatus(TransVo transVo);
	
	/**
	 * 取得線上申請資料-滿期
	 * @param TransVo transVo
	 * @return 回傳線上申請資料-滿期
	 */
	public Map<String, Object> getTransMaturity(TransVo transVo);
	
	/**
	 * 取得線上申請資料-補發保單
	 * @param TransVo transVo
	 * @return 回傳線上申請資料-補發保單
	 */
	public Map<String, Object> getTransResendPolicy(TransVo transVo);
	
	/**
	 * 取得線上申請資料-終止授權.
	 * @param TransVo transVo
	 * @return 回傳線上申請資料-終止授權
	 */
	public Map<String, Object> getTransCancelAuth(TransVo transVo);
	
	/**
	 * 取得線上申請資料-保單價值列印.
	 * 
	 * @param TransVo transVo
	 * @return 回傳線上申請資料-保單價值列印
	 */
	public Map<String, Object> getTransValuePrint(TransVo transVo);
	
	/**
	 * 取得線上申請資料-旅平險.
	 * 
	 * @param TransVo transVo
	 * @return 回傳線上申請資料-旅平險
	 */
	public Map<String, Object> getTransTravelPolicy(TransVo transVo);
	
	/**
	 * 取得
	 * 
	 * @param transVo
	 * @return
	 */
	public Optional<byte[]> getTransUploadZipFile(TransVo transVo);
	
	/**
	 * 取得線上申請資料-變更匯款帳號
	 * 
	 * @param transVo
	 * @return 回傳線上申請資料-變更匯款帳號
	 */
	public Map<String, Object> getTransChangePayAccount(TransVo transVo);

	/**
	 * 查詢線上申請額外資料
	 * 
	 * @param transVo
	 * @return
	 */
	public List<TransExtendAttrVo> getTransExtendsByTransNum(TransVo transVo);
	
	/**
	 * 取得線上申請資料-變更風險屬性
	 * 
	 * @param transVo
	 * @return 回傳線上申請資料-變更風險屬性
	 */
	public Map<String, Object> getTransRiskLevel(TransVo transVo);
	
	/**
	 * 取得線上申請資料-保戶基本資料更新
	 * 
	 * @param transVo
	 * @return 回傳線上申請資料-保戶基本資料更新
	 */
	public Map<String, Object> getTransPolicyHolderProfile(TransVo transVo);
	
	/**
	 * 取得線上申請資料-變更投資標的及配置比例資料
	 * 
	 * @param transVo
	 * @return 回傳線上申請資料-變更投資標的及配置比例資料
	 */
	public Map<String, Object> getTransFundSwitch(TransVo transVo);
	
	/**
	 * 死亡除戶
	 * 
	 * @param transVo
	 * @return 回傳線上申請資料-死亡除戶資料
	 */
	public Map<String, Object> getDnsAlliance(TransVo transVo);
	
	/**
	 * 更新是否收到紙本
	 * @param vo
	 * @return
	 */
	public int updateInsuranceClaimFileReceived(TransInsuranceClaimVo vo);
	
	/**
	 * 更新是否收到紙本
	 * @param vo
	 * @return
	 */
	public int updateICFileReceived(TransInsuranceClaimVo vo);
	
	/**
	 * 更新是否傳送聯盟鏈
	 * @param vo
	 * @return
	 */
	public int updateInsuranceClaimSendAlliance(TransInsuranceClaimVo vo);
	
	/**
	 * 更新是否傳送聯盟鏈
	 * @param vo
	 * @return
	 */
	public int addInsuranceClaim(TransInsuranceClaimVo vo);
	
	/**
	 * 查詢聯盟鏈歷程
	 * @param vo
	 * @return
	 */
	public List<UnionCourseVo> getUnionCourseList(UnionCourseVo vo);
	
	/**
	 * 添加狀態歷程
	 * @param vo
	 * @return
	 */
	public int addTransStatusHistory(TransStatusHistoryVo vo);
	
	/**
	 * 添加補件
	 * @param vo
	 * @return
	 */
	public int addTransRFE(TransRFEVo vo);
	
	/**
	 * 查詢狀態歷程
	 * @param vo
	 * @return
	 */
	public List<TransStatusHistoryVo> getTransStatusHistoryList(TransStatusHistoryVo vo);
	
	/**
	 * 查詢補件單歷程
	 * @param vo
	 * @return
	 */
	public List<TransRFEVo> getTransRFEList(TransRFEVo vo);
	
	/**
	 * 更新補件單歷程
	 * @param vo
	 * @return
	 */
	public int updateTransRFEStatus(TransRFEVo vo);

	
	/**
	 * 查詢使用者角色
	 * @param userId
	 * @return
	 */
	public String getRoleName(String userId);

	public int getCaseIDNum(String transNum);
	
	
	/**
	  * 加入黑名單
	 * @param vo
	 * @return
	 */
	public int addBlackList(TransStatusHistoryVo vo);
	
	/**
	 * 查詢當前狀態歷程
	 * @param vo
	 * @return
	 */
	public TransStatusHistoryVo getTransStatusHis(TransStatusHistoryVo vo);
	
	/**
	 * 發送郵件
	 * @param transNum
	 * @param parameterCode
	 * @param status
	 * @return
	 */
	public void sendMailTO(String transNum,String code,String status);
	
	/**
	 * 保單理賠申請統計報表
	 * @param InsClaimStatisticsVo
	 * @return lsit
	 */
	public List getInsClaimStatisticsReport(InsClaimStatisticsVo claimVo);
	
	/**
	 * 保單理賠申請統計報表
	 * @param InsClaimStatisticsVo
	 * @return lsit
	 */
	public List getInsClaimDetailReport(InsClaimStatisticsVo claimVo);

        public String converFileToBase64Str(String filePath);
	
	/**
	 * 資料變更統計報表
	 * @param InsClaimStatisticsVo
	 * @return lsit
	 */
	public List getContactInfoStatisticsReport(ContactInfoReportVo claimVo);
	
	/**
	 * 資料變更明细報表
	 * @param InsClaimStatisticsVo
	 * @return lsit
	 */
	public List getContactInfoDetailReport(ContactInfoReportVo claimVo);
	
	/**
	 * 聯絡資料變更-失败
	 * @param vo
	 * @return
	 */
	public int contactInfoFail(TransVo vo);
	
	/**
	 * 聯絡資料變更-已完成
	 * @param vo
	 * @return
	 */
	public int contactInfoComplete(TransVo vo);
	
	/**
	 * 导出死亡除戶
	 * 
	 * @param transVo TransVo
	 * @return 回傳死亡除戶查詢結果
	 */
	List<Map<String, Object>> getDNS_CSV(@Param("transVo") TransVo transVo);

	/***
	 * 查詢申請明細-已持有投資標的轉換查詢
	 * @param transVo
	 * @return
	 */
	Map<String, Object> getConversionDetail(TransVo transVo);

	Map<String, Object> getInvestmentDetail(TransVo transVo);

	Map<String, Object> getDepositDetail(TransVo transVo);

	Map<String, Object> getCashPaymentDetail(TransVo transVo);

	Map<String, Object> getTransChangePremiumDetail(TransVo transVo);

	/**
	 * 獲取查詢條件
	 *   保單理賠申請統計報表
	 *
	 * @return
	 */
	List getMedicalTreatmentStatisticsReport(MedicalTreatmentStatisticsVo var1);
	/**
	 * 獲取查詢條件
	 *   保單理賠明顯明顯統計報表
	 *
	 * @return
	 */
	List getMedicalTreatmentDetailReport(MedicalTreatmentStatisticsVo var1);
	/**
	 * 取得線上申請-保單醫療理賠
	 * @param transVo
	 * @return Map<String, Object>
	 */
	public Map<String, Object>  getMedicalTreatmentClaim(TransVo transVo);
	//獲取醫院明顯
	List<Hospital> getHospitalList(String medicalTreatmentParameterCode);
	//獲取保險公司明顯
	List<HospitalInsuranceCompany> getHospitalInsuranceCompanyList(String medicalTreatmentParameterCode);
	//醫療保險公司附件按鈕歷程
	List<TransRFEVo> getMedicalTreatmentTransRFEList(TransRFEVo vo);
	//查詢醫療保單是否是異常案件
	int getMedicalTreatmentCaseIDNum(String transNum);
	//醫療保單查詢是否存在黑名单
	int checkMedicalTreatmentIdNoExist(String transNum);
	//醫療保單加入黑名單
	int addMedicalTreatmentBlackList(TransStatusHistoryVo vo);
	//當前保單標記為異常件的時候,進行發生郵件
	void sendMedicalTreatmentMailTO(String transNum, String rejectReason, String status);
	//進行查詢醫療條數
	int getOnlineChangeMedicalTreatmentDetailTotal(TransVo transVo);
	//獲取文件的大小與數據
	MedicalTreatmentClaimFileDataVo getMedicalTreatmentDetailBase64FileSize(Float fdId)throws Exception;
	//更新醫療保單是否推送聯盟
    int updateMedicalTreatmentSendAlliance(TransMedicalTreatmentClaimVo vo);
	//醫療復審核通過,進行推送給TransMedicalTreatment
	int addMedicalTreatmentClaim(TransMedicalTreatmentClaimVo vo);
	//是否已開啓傳送公會聯盟鏈-覆核人員審核
	int updateOrAddMedicalTreatment(TransMedicalTreatmentClaimVo vo);
	//查詢當前保單的聯盟狀態
    String getTransMedicalTreatmentByAllianceStatus(String transNum)throws Exception;
	// 獲取當前案件需要上傳影響系統條數
    int getTransMedicalTreatmentByCount(String transNum)throws Exception;
	// 獲取當前案件已上傳影像系統成功條數
	int getTransMedicalTreatmentBySuccessCount(String transNum)throws Exception;
	//獲取當前的保單的caseId
    String getTransMedicalTreatmentByCaseId(String transNum)throws Exception;
	//進行回壓聯盟結束流程狀態
	void updateTarnsMedicalTreatmentClaimToAllianceStatus(TransMedicalTreatmentClaimVo mvo)throws Exception;

    List<NotifyOfNewCaseMedicalVo> getUnionCourseListAllianceStatusMsg(NotifyOfNewCaseMedicalVo vo)throws Exception;

	List<TransMedicalTreatmentClaimMedicalInfoVo> getMedicalInfo(Double claimId);

	MedicalTreatmentClaimFileDataVo getMedicalInfoBase64FileSize(Float fdId) throws Exception;
	
	public Map<String , Object> getTransElectronicFormMethod(TransVo transVo) throws Exception;
	
	public Map<String , Object> getTransDeratePaidOffMethod(TransVo transVo) throws Exception;
	
	public Map<String , Object> getTransRolloverPeriodicallyMethod(TransVo transVo) throws Exception;
	
	public Map<String , Object> getTransContractRevocationMethod(TransVo transVo) throws Exception;

    SignRecord getNewSignStatus(String transNum);

    byte[] getSignPdf(String signFileId);

	List<BxczSignApiLog> getSignHistoryList(TransVo vo);

	int updatePolicyClaimApplyDate(String transNum);

	int updateMedicalTreatmentApplyDate(String transNum);

    TransInsuranceClaimFileDataVo getInsuranceClaimFile(Float id);
}
