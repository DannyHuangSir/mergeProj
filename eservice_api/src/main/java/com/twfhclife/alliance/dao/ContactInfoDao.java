package com.twfhclife.alliance.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.alliance.model.ContactInfoMapperVo;
import com.twfhclife.alliance.model.ContactInfoFileDataVo;
import com.twfhclife.alliance.model.ContactInfoVo;
import com.twfhclife.generic.model.UserVo;


public interface ContactInfoDao {
	

	
	/**
	 * 台銀件上傳聯盟後，取得回傳，更新CONTACT_INFO,CASE_ID,CODE,MSG,STATUS
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updateContactAfterUploadToAlliance(@Param("vo") ContactInfoMapperVo vo) throws Exception;
	
	/**
	 * 更新CONTACT_INFO.MSG
	 * @param vo
	 * @return int
	 * @throws Exception
	 */
	int updateContactInfoMsg(@Param("vo") ContactInfoMapperVo vo) throws Exception;
	
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
	 * 取得CONTACT_INFO_SEQ的next value
	 * @return Integer
	 * @throws Exception
	 */
	Float getContactInfoSequence() throws Exception;

	/**
	 * 新增理賠申請
	 * @param vo
	 * @return int
	 * @throws Exception
	 */
	int addContactInfo(@Param("vo") ContactInfoMapperVo vo ) throws Exception;
	
	/**
	 * 新增理賠申請檔案FileData
	 * @param vo
	 * @return int
	 * @throws Exception
	 */
	int addContactInfoFileData(@Param("vo") ContactInfoFileDataVo vo) throws Exception;
	/**
	 * 获取檔案FileData 的id编号
	 * @return Float
	 * @throws Exception
	 */
	Float  selectontactInfoFiledatasSeq()throws  Exception;
	/**
	 * 修改FileData文件的数据
	 * @param vo
	 * @return int
	 * @throws Exception
	 */
	int updateContactInfoFileData(@Param("vo") ContactInfoFileDataVo vo) throws Exception;

	/**
	 * 取得可上傳但未上傳聯盟的內部理賠件
	 * @return List<ContactInfoVo>
	 * @throws Exception
	 */
	List<ContactInfoMapperVo> getContactInfoByNoCaseId() throws Exception;
	
	/**
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<ContactInfoFileDataVo> getFileDatasByContactSeqId(@Param("vo") ContactInfoMapperVo vo) throws Exception;

	/**
	 * 通过多个SEQID进行查询出多条数据
	 * @param list
	 * @return
	 * @throws Exception
	 */
	List<ContactInfoFileDataVo> getFileDatasByContactSeqIdList(@Param("list") List  list) throws Exception;

	/**
	 * 內部申請件建立後，將呼叫完API101的回傳取得CaseId，更新回該筆記錄
	 * @param vo ContactInfoVo
	 * @return int
	 * @throws Exception
	 */
	int updateCaseIdByContactSeqId(@Param("vo") ContactInfoMapperVo vo) throws Exception;
	
	/**
	 * 內部申請件建立後取得檔案後，將呼叫完API101的回傳取得FileId，更新回該筆記錄
	 * @param vo ContactInfoFileDataVo
	 * @return
	 * @throws Exception
	 */
	int updateFileIdByFdId(@Param("vo") ContactInfoFileDataVo vo) throws Exception;
	
	/**
	 * 依紙本註記，取得可上傳聯盟的案件
	 * @param fileReceived
	 * @return List<ContactInfoVo>
	 * @throws Exception
	 */
	List<ContactInfoMapperVo> getInsuranceClaimByFileReceived(@Param("fileReceived") String fileReceived) throws Exception;
	
	/**
	 * 更新台銀內部件，，是否已收到紙本
	 * @param vo
	 * @return int
	 * @throws Exception
	 */
	int updateFileReceived(@Param("vo") ContactInfoMapperVo vo) throws Exception;
	
	/**
	 * 更新聯盟件，是否已收到紙本
	 * @param vo
	 * @return int
	 * @throws Exception
	 */
	int updateAllianceCaseFileReceived(@Param("vo") ContactInfoVo vo) throws Exception;
	
	/**
	 * 取得聯盟通知件,下載未成功之檔案清單
	 * @param vo ContactInfoFileDataVo
	 * @return List<ContactInfoFileDataVo>
	 * @throws Exception
	 */
	List<ContactInfoFileDataVo> getFileDataToDownload(@Param("vo") ContactInfoFileDataVo vo) throws Exception;
	
	/**
	 * 取得台銀理賠件,上傳未成功之檔案清單
	 * @param vo ContactInfoFileDataVo
	 * @return List<ContactInfoFileDataVo>
	 * @throws Exception
	 */
	List<ContactInfoFileDataVo> getFileDataToUpload(@Param("vo") ContactInfoFileDataVo vo) throws Exception;
	
	/**
	 * 取得聯盟通知件，需要查詢是否收到紙本的件
	 * @throws Exception
	 */
	List<ContactInfoMapperVo> getAllianceCaseByFileReceivedNotYet() throws Exception;
	
	/**
	 * 更新上傳/下載文件的(CONTACT_INFO_FILEDATAS.FILE_STATUS),用以區分此筆記錄是否已完成
	 * @param vo
	 * @return int
	 * @throws Exception
	 */
	int updateFileStatusByFileId(@Param("vo") ContactInfoFileDataVo vo) throws Exception;
	
	/**
	 * 取得未上傳eservcie.Trans的聯盟通知件
	 * @return List<ContactInfoMapperVo>
	 * @throws Exception
	 */
	List<ContactInfoMapperVo> getContactInfoByHasNotifySeqIdNoBatchId() throws Exception;
	
	/**
	 * 更新聯盟通知，表示已上傳eservice.Trans
	 * @param vo ContactInfoMapperVo
	 * @return int
	 * @throws Exception
	 */
	int updateContactInfoTransNumByNotifySeqId(@Param("vo") ContactInfoMapperVo vo) throws Exception;
	
	/**
	 * 取得case id 
	 * @param claimSeqId
	 * @return ContactInfoMapperVo
	 * @throws Exception
	 */
	ContactInfoMapperVo getCaseIdBySeqId(@Param("claimSeqId") Float claimSeqId);
	
	/**
	 * 
	 * @param claimSeqId
	 * @return List<ContactInfoFileDataVo>
	 */
	List<ContactInfoFileDataVo> getContactInfoFileDataByClaimsSequId(@Param("seqId") Float seqId);

	/**
	 *
	 * @param claimSeqId
	 * @return List<ContactInfoFileDataVo>
	 */
	List<ContactInfoFileDataVo> getTransContactInfoFileDataByClaimsSequId(@Param("seqId") Float seqId);


//	/**
//	 *取得Mail info
//	 */
//	public String getInfoTOMail(@Param("transNum") String transNum);
	
	/**
	 * 取得Mail
	 */
	public UserVo getMailByRocid(@Param("rocId") String rocId);
	
	List<ContactInfoMapperVo>  getContactInfoByHasNotifySeqIdAndBatchId()  throws Exception;
	
	/**
	 * 以BATCH_ID取出TRANS.STATUS尚未完結的筆數(已完結為2,6,7)
	 * @param batchId
	 * @return
	 */
	int getCompletedContactInfoByBatchId(@Param("batchId") Float batchId);
	
	int updateStatusByBatchId(@Param("vo")ContactInfoMapperVo vo) throws Exception;
	
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
	 * 以BATCH_ID取出TRANS.STATUS不是已完成的筆數(已完成為2)
	 * @param batchId
	 * @return int
	 */
	int getCompletedContactInfoByBatchIdStatus(@Param("batchId") Float batchId);
	
	/**
	 * 以BATCH_ID取出TRANS.STATUS=7的筆數
	 * @param batchId
	 * @return Integer
	 */
	Integer getAbnormlContactInfoCountByBatchIdStatus(@Param("batchId") Float batchId);

	/***
	 *模仿将DB数据写出文档
	 * 当前用于测试API 204 的文件数据
	 * */
   // List<ContactInfoFileDataVo> getFileDataToDownloadSuccess();


}
