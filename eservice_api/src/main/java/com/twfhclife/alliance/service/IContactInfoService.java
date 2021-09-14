package com.twfhclife.alliance.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.alliance.domain.ClaimRequestVo;
import com.twfhclife.alliance.domain.ClaimResponseVo;
import com.twfhclife.alliance.domain.DnsRequestVo;
import com.twfhclife.alliance.domain.DnsResponseVo;
import com.twfhclife.alliance.model.ContactInfoFileDataVo;
import com.twfhclife.alliance.model.ContactInfoMapperVo;
import com.twfhclife.alliance.model.ContactInfoVo;
import com.twfhclife.alliance.model.NotifyOfNewCaseChangeVo;
import com.twfhclife.alliance.model.NotifyOfNewCaseVo;

public interface IContactInfoService {
	
	/**
	 * 取得未上傳聯盟的內部聯絡資料變更
	 * @return List<ContactInfoVo>
	 * @throws Exception
	 */
	List<ContactInfoMapperVo> getContactInfoByNoCaseId() throws Exception;
	
	/**
	 * 台銀件上傳聯盟後，取得回傳，更新
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updateContactAfterUploadToAlliance(@Param("vo") ContactInfoMapperVo vo) throws Exception;
	
	/**
	 * 更新CONTACT_INFO.MSG by SEQ_ID
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updateContactInfoMsg(@Param("vo") ContactInfoMapperVo vo) throws Exception;
	
	/**
	 * 以身份證字號取得ESERVICE中userId
	 * @param idNo
	 * @return
	 * @throws Exception
	 */
	String getUserIdByIdNo(String idNo) throws Exception;
	
	/**
	 * 查詢是否為要保人/被保人
	 * @param idNo
	 * @return
	 * @throws Exception
	 */
	int countPIPMByIdNo(@Param("idNo") String idNo) throws Exception;
	
	/**
	 * API-107 通知有新案件
	 * @param reqVo
	 * @return ClaimResponseVo
	 * @throws Exception
	 */
	ClaimResponseVo notifyOfNewCase(ClaimRequestVo reqVo) throws Exception;
	
	/**
	 * 儲存理賠申請
	 * @param icvo
	 * @return int
	 * @throws Exception
	 */
	int addContactInfo(ContactInfoMapperVo icvo) throws Exception;
	
	/**
	 * 依ncStatus(ex:0,1...etc.)傳入值取得資料
	 * @param ncStatus
	 * @return ArrayList<NotifyOfNewCaseChangeVo>
	 * @throws Exception
	 */
	ArrayList<NotifyOfNewCaseChangeVo> getNofifyOfNewCaseByNcStatus(String ncStatus) throws Exception;
	
	
	
	/**
	 * 內部申請件建立後，將呼叫完API201的回傳取得CaseId，更新回該筆記錄
	 * @param vo ContactInfoVo
	 * @return int
	 * @throws Exception
	 */
	int updateCaseIdByContactSeqId(ContactInfoMapperVo vo) throws Exception;
	
	/**
	 * 內部申請件建立後取得檔案後，將呼叫完API201的回傳取得FileId，更新回該筆記錄
	 * @param vo ContactInfoFileDataVo
	 * @return
	 * @throws Exception
	 */
	int updateFileIdByFdId(ContactInfoFileDataVo vo) throws Exception;
	
	/**
	 * 依紙本註記，取得可上傳聯盟的案件
	 * @param fileReceived
	 * @return List<ContactInfoMapperVo>
	 * @throws Exception
	 */
	List<ContactInfoMapperVo> getInsuranceClaimByFileReceived(String fileReceived) throws Exception;

	/**
	 * 台銀內部件，更新上傳紙本註記
	 * @param vo
	 * @return int
	 * @throws Exception
	 */
	int updateFileReceived(ContactInfoMapperVo vo) throws Exception;
	
	/**
	 * 聯盟通知件，更新上傳紙本註記
	 * @param vo
	 * @return int
	 * @throws Exception
	 */
	int updateAllianceCaseFileReceived(ContactInfoVo vo) throws Exception;
	
	/**
	 * 取得聯盟通知件,下載未成功之檔案清單
	 * @param vo ContactInfoFileDataVo
	 * @return List<ContactInfoFileDataVo>
	 * @throws Exception
	 */
	List<ContactInfoFileDataVo> getFileDataToDownload(ContactInfoFileDataVo vo) throws Exception;
	
	/**
	 * 取得台銀理賠件,上傳未成功之檔案清單
	 * @param vo ContactInfoFileDataVo
	 * @return List<ContactInfoFileDataVo>
	 * @throws Exception
	 */
	List<ContactInfoFileDataVo> getFileDataToUpload(ContactInfoFileDataVo vo) throws Exception;
	
	/**
	 * 取得聯盟通知件，需要查詢是否收到紙本的件
	 * @return List<ContactInfoMapperVo>
	 * @throws Exception
	 */
	List<ContactInfoMapperVo> getAllianceCaseByFileReceivedNotYet() throws Exception;
	
	/**
	 * 更新上傳/下載文件的(CONTATCT_INFO_FILEDATAS.FILE_STATUS),用以區分此筆記錄是否已完成(success)
	 * @param vo
	 * @return int
	 * @throws Exception
	 */
	int updateFileStatusByFileId(@Param("vo") ContactInfoFileDataVo vo) throws Exception;
	
	/**
	 * 取得未上傳台銀TRANS的聯盟通知件
	 * @return List<ContactInfoMapperVo>
	 * @throws Exception
	 */
	List<ContactInfoMapperVo> getContactInfoByHasNotifySeqIdNoBatchId() throws Exception;
	
	/**
	 * 更新聯盟通知，表示已上傳eservice.Trans
	 * @param vo ContactInfoMapperVo
	 * @return
	 * @throws Exception
	 */
	int updateContactInfoTransNumByNotifySeqId(ContactInfoMapperVo vo) throws Exception;
	
	/**
	 * 更新NC_STATUS(ex:已取完件update NC_STATUS=1)
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updateNcStatusBySeqId(@Param("vo") NotifyOfNewCaseChangeVo vo) throws Exception;
	
	/**
	 * 取得case id 
	 * @param claimSeqId
	 * @return ContactInfoMapperVo
	 * @throws Exception
	 */
	ContactInfoMapperVo getCaseIdBySeqId(@Param("claimSeqId") Float claimSeqId);
	
	List<ContactInfoFileDataVo> getContactInfoFileDataByClaimsSequId(@Param("claimSeqId") Float claimSeqId);
	
//	Map<String,String> getMailAndSMSInfo(String transNum);
	
	/**
	 * API-205 通知有新案件
	 * @param reqVo
	 * @return ClaimResponseVo
	 * @throws Exception
	 */
	ClaimResponseVo newCaseNotified(ClaimRequestVo reqVo) throws Exception;
	
	
	/**
	 * DNS-491  通知有新除戶案件
	 * @param reqVo DnsRequestVo
	 * @return DnsResponseVo
	 * @throws Exception
	 */
	DnsResponseVo notifyOfNewCaseDns(DnsRequestVo reqVo) throws Exception;
	
	List<ContactInfoMapperVo> getContactInfoByHasNotifySeqIdAndBatchId() throws Exception;
	
	/**
	 * 以BATCH_ID取出TRANS.STATUS尚未完結的筆數(已完結為2,6,7)
	 * @param batchId
	 * @return int
	 */
	int getCompletedContactInfoByBatchId(@Param("batchId") Float batchId);
	
	int updateStatusByBatchId(@Param("vo") ContactInfoMapperVo vo) throws Exception;
	
	/**
	 * update STATUS by CASE_ID
	 * @param vo
	 * @return int
	 * @throws Exception
	 */
	int updateStatusByCaseId(@Param("vo")ContactInfoMapperVo vo) throws Exception;
	
	/**
	 * 取得臺銀變更資料
	 * @return List<ContactInfoMapperVo>
	 * @throws Exception
	 */
	List<ContactInfoMapperVo> getTransContactInfoByForTask() throws Exception;

	/**
	 * 成功发送邮件
	 * @param policyNumber  保單編號
	 */
	public   void  forwardingCaseImageSuccessMail(String policyNumber);
	/***
	 *模仿将DB数据写出文档
	 * 当前用于测试API 204 的文件数据
	 * */
  //  List<ContactInfoFileDataVo> getFileDataToDownloadSuccess();
	/**
	 * 根据transNum  进行修改  caseId
	 * @param caseId
	 * @param transNum
	 * @return
	 */
	int updateTransContactInfoCaseId(String caseId, String transNum);

	/**
	 * 以BATCH_ID取出TRANS.STATUS不是已完成的筆數(已完成為2)
	 * @param batchId
	 * @return int
	 */
	int getCompletedContactInfoByBatchIdStatus(Float batchId);

	/**
	 * 以BATCH_ID取出TRANS.STATUS=7的筆數
	 * @param batchId
	 * @return Integer
	 */
	Integer getAbnormlContactInfoCountByBatchIdStatus(Float batchId);
}
