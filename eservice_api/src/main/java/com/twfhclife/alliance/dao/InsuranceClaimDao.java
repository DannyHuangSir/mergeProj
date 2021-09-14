package com.twfhclife.alliance.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.alliance.model.InsuranceClaimFileDataVo;
import com.twfhclife.alliance.model.InsuranceClaimMapperVo;
import com.twfhclife.alliance.model.InsuranceClaimVo;
import com.twfhclife.generic.model.UserVo;


public interface InsuranceClaimDao {
	
	/**
	 * 台銀件上傳聯盟後，取得回傳，更新INSURANCE_CLAIM,CASE_ID,CODE,MSG,STATUS
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updateInsuranceAfterUploadToAlliance(@Param("vo") InsuranceClaimMapperVo vo) throws Exception;
	
	/**
	 * 更新INSURANCE_CLAIM.MSG
	 * @param vo
	 * @return int
	 * @throws Exception
	 */
	int updateInsuranceClaimMsg(@Param("vo") InsuranceClaimMapperVo vo) throws Exception;
	
	/**
	 * 以身份證字號取得ESERVICE中userId
	 * @param idNo
	 * @return String
	 * @throws Exception
	 */
	String getUserIdByIdNo(@Param("idNo") String idNo) throws Exception;
	
	/**
	 * 查詢是否為要保人/被保人
	 * @param idNo
	 * @return int
	 * @throws Exception
	 */
	int countPIPMByIdNo(@Param("idNo") String idNo) throws Exception;
	
	/**
	 * 取得INSURANCE_CLAIM_SEQ的next value
	 * @return Integer
	 * @throws Exception
	 */
	Float getInsuranceClaimSequence() throws Exception;

	/**
	 * 更新文件的保单理赔的Base64的数据
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updateInsuranceClaimFileBase64(@Param("vo") InsuranceClaimFileDataVo vo) throws Exception;

	/**
	 * 獲取保單理賠的文件編號
	 * @return
	 * @throws Exception
	 */
	Float getItransInsuranceClaimFiledatasId() throws Exception;

	/**
	 * 新增理賠申請
	 * @param vo
	 * @return int
	 * @throws Exception
	 */
	int addInsuranceClaim(@Param("vo") InsuranceClaimMapperVo vo ) throws Exception;
	
	/**
	 * 新增理賠申請檔案FileData
	 * @param vo
	 * @return int
	 * @throws Exception
	 */
	int addInsuranceClaimFileData(@Param("vo") InsuranceClaimFileDataVo vo) throws Exception;
	/**
	 * 取得可上傳但未上傳聯盟的內部理賠件
	 * @return List<InsuranceClaimVo>
	 * @throws Exception
	 */
	List<InsuranceClaimMapperVo> getInsuranceClaimByNoCaseId() throws Exception;
	
	/**
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<InsuranceClaimFileDataVo> getFileDatasByClaimSeqId(@Param("vo") InsuranceClaimMapperVo vo) throws Exception;
	
	/**
	 * 內部申請件建立後，將呼叫完API101的回傳取得CaseId，更新回該筆記錄
	 * @param vo InsuranceClaimVo
	 * @return int
	 * @throws Exception
	 */
	int updateCaseIdByClaimSeqId(@Param("vo") InsuranceClaimMapperVo vo) throws Exception;
	
	/**
	 * 內部申請件建立後取得檔案後，將呼叫完API101的回傳取得FileId，更新回該筆記錄
	 * @param vo InsuranceClaimFileDataVo
	 * @return
	 * @throws Exception
	 */
	int updateFileIdByFdId(@Param("vo") InsuranceClaimFileDataVo vo) throws Exception;
	
	/**
	 * 依紙本註記，取得可上傳聯盟的案件
	 * @param fileReceived
	 * @return List<InsuranceClaimVo>
	 * @throws Exception
	 */
	List<InsuranceClaimMapperVo> getInsuranceClaimByFileReceived(@Param("fileReceived") String fileReceived) throws Exception;
	
	/**
	 * 更新台銀內部件，，是否已收到紙本
	 * @param vo
	 * @return int
	 * @throws Exception
	 */
	int updateFileReceived(@Param("vo") InsuranceClaimMapperVo vo) throws Exception;
	
	/**
	 * 更新TRANS_INSURANCE_CLAIM的是否已收到紙本欄位
	 * @param vo
	 * @return int
	 * @throws Exception
	 */
	int updateTransInsuranceClaimFileReceived(@Param("vo") InsuranceClaimMapperVo vo) throws Exception;
	
	
	/**
	 * 更新聯盟件，是否已收到紙本
	 * @param vo
	 * @return int
	 * @throws Exception
	 */
	int updateAllianceCaseFileReceived(@Param("vo") InsuranceClaimVo vo) throws Exception;
	
	/**
	 * 取得聯盟通知件,下載未成功之檔案清單
	 * @param vo InsuranceClaimFileDataVo
	 * @return List<InsuranceClaimFileDataVo>
	 * @throws Exception
	 */
	List<InsuranceClaimFileDataVo> getFileDataToDownload(@Param("vo") InsuranceClaimFileDataVo vo) throws Exception;
	
	/**
	 * 取得台銀理賠件,上傳未成功之檔案清單
	 * @param vo InsuranceClaimFileDataVo
	 * @return List<InsuranceClaimFileDataVo>
	 * @throws Exception
	 */
	List<InsuranceClaimFileDataVo> getFileDataToUpload(@Param("vo") InsuranceClaimFileDataVo vo) throws Exception;
	
	/**
	 * 取得聯盟通知件，需要查詢是否收到紙本的件
	 * @return List<InsuranceClaimVo>
	 * @throws Exception
	 */
	List<InsuranceClaimMapperVo> getAllianceCaseByFileReceivedNotYet() throws Exception;
	
	/**
	 * 更新上傳/下載文件的(INSURANCE_CLAIM_FILEDATAS.FILE_STATUS),用以區分此筆記錄是否已完成
	 * @param vo
	 * @return int
	 * @throws Exception
	 */
	int updateFileStatusByFileId(@Param("vo") InsuranceClaimFileDataVo vo) throws Exception;
	/**
	 * 取得未上傳eservcie.Trans的聯盟通知件
	 * @return List<InsuranceClaimMapperVo>
	 * @throws Exception
	 */
	List<InsuranceClaimMapperVo> getInsuranceClaimByHasNotifySeqIdNoTransNum() throws Exception;
	
	/**
	 * 更新聯盟通知，表示已上傳eservice.Trans
	 * @param vo InsuranceClaimMapperVo
	 * @return int
	 * @throws Exception
	 */
	int updateInsuranceClaimTransNumByNotifySeqId(@Param("vo") InsuranceClaimMapperVo vo) throws Exception;
	
	/**
	 * 取得case id 
	 * @param claimSeqId
	 * @return InsuranceClaimMapperVo
	 * @throws Exception
	 */
	InsuranceClaimMapperVo getCaseIdBySeqId(@Param("claimSeqId") Float claimSeqId);
	
	/**
	 * 
	 * @param claimSeqId
	 * @return List<InsuranceClaimFileDataVo>
	 */
	List<InsuranceClaimFileDataVo> getInsuranceClaimFileDataByClaimsSequId(@Param("claimSeqId") Float claimSeqId);
	
	/**
	 *取得Mail info
	 */
	public String getInfoTOMail(@Param("transNum") String transNum);
	
	/**
	 * 取得Mail
	 */
	public UserVo getMailByRocid(@Param("rocId") String rocId);
	
}
