package com.twfhclife.eservice.onlineChange.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransContactInfoDtlVo;
import com.twfhclife.eservice.onlineChange.model.TransContactInfoFileDataVo;
import com.twfhclife.eservice.onlineChange.model.TransContactInfoOldVo;
import com.twfhclife.eservice.onlineChange.model.TransContactInfoVo;
import com.twfhclife.eservice.onlineChange.model.TransStatusHistoryVo;
import com.twfhclife.eservice.policy.model.PolicyListVo;

/**
 * 變更保單聯絡資料服務.
 * 
 * @author all
 */
public interface ITransContactInfoService {

	/**
	 * 處理保單狀態是否鎖定.
	 * 
	 * @param policyList 保單清單資料
	 */
	public void handlePolicyStatusLocked(List<PolicyListVo> policyList);
	
	/**
	 * 查看现行网络资料
	 * 
	 * @param policyNo 保單清單No
	 */
	public HashMap getUserCurrentNetworkData(String policyNo);
	
	/**
	 * 變更保單聯絡資料主檔-新增.
	 * 
	 * @param transContactInfoVo TransContactInfoVo
	 * @param transContactInfoDtlVo TransContactInfoDtlVo
	 * @return 回傳影響筆數
	 */
	public Map<String,Object> insertTransContactInfo(TransContactInfoVo transContactInfoVo,
			TransContactInfoDtlVo transContactInfoDtlVo, TransStatusHistoryVo hisVo);
	
	/**
	 * 取得變更後保單聯絡資料申請id.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳查詢結果
	 */
	public BigDecimal getTransContactInfoId(String transNum);
	
	/**
	 * 變更後保單聯絡資料-明細查詢.
	 * 
	 * @param contactInfoId 變更保單聯絡資料主檔id
	 * @return 回傳查詢結果
	 */
	public TransContactInfoDtlVo getTransContactInfoDtlDetail(BigDecimal contactInfoId);
	
	/**
	 * 變更前保單聯絡資料-明細查詢.
	 * 
	 * @param contactInfoId 變更保單聯絡資料主檔id
	 * @return 回傳查詢結果
	 */
	public TransContactInfoOldVo getTransContactInfoOldDetail(BigDecimal contactInfoId);
	
	/**
	 * 檢查聯絡資料變更是否完成
	 */
	public int getContactInfoCompleted(String rocId);
	
	public Map<String,Object> getSendMailInfo(String string);


	public Map<String,Object> getSendMailInfo();

	public Map<String,Object> getCIOUserDetailInfoOld(String roc_id);
	
	public Map<String,Object> getCIOUserDetailInfoNew(String roc_id);

	/**
	 * 获取某个保单序号对应的，批号所有的保单序号
	 * @param transNum  保单序号
	 * @return
	 */
	public List<TransContactInfoVo> getTransContactInfoTransNum(String transNum);

	/**
	 * 验证,取消申請是否可以进行取消此時STATUS=1
	 * @param transNum   单号
	 * @return   状态
	 */
	String getTransContactInfoTransNumCheck(String transNum);
	
	/**
	 * 依FILE_ID更新FILE_BASE64內容
	 * @param vo
	 * @return
	 */
	int updateFileBase64ByFileId(@Param("vo") TransContactInfoFileDataVo vo);

	/**
	 * 根据transNum  进行修改  caseId
	 * @param caseId
	 * @param transNum
	 * @return
	 */
	int updateTransContactInfoCaseId(String  caseId,  String  transNum);
	/**
	 * 通過Batch_ID獲取TransNum
	 * @param batchId
	 * @return
	 */
	List<String> getTransContactInfoTransNumByBatchId(Float  batchId);

	/**
	 * 验证,取消申請
	 *    如,已經推送至聯盟并且已經審核通過,則不能進行取消
	 *		SEND_ALLIANCE!=Y
	 */
	String transMedicalTreatmentClaimByCheck(String transNum);

	//進行查詢數據當前批次的保單號
    String getHistoryPolicyNo(String transNum);
}