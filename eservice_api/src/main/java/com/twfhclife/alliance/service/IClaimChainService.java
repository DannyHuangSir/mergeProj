package com.twfhclife.alliance.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.alliance.domain.ClaimRequestVo;
import com.twfhclife.alliance.domain.ClaimResponseVo;
import com.twfhclife.alliance.domain.DnsRequestVo;
import com.twfhclife.alliance.domain.DnsResponseVo;
import com.twfhclife.alliance.model.InsuranceClaimFileDataVo;
import com.twfhclife.alliance.model.InsuranceClaimMapperVo;
import com.twfhclife.alliance.model.InsuranceClaimVo;
import com.twfhclife.alliance.model.NotifyOfNewCaseVo;

public interface IClaimChainService {
	
	/**
	 * 台銀件上傳聯盟後，取得回傳，更新INSURANCE_CLAIM,CASE_ID,CODE,MSG,STATUS
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updateInsuranceAfterUploadToAlliance(@Param("vo") InsuranceClaimMapperVo vo) throws Exception;
	
	/**
	 * 更新INSURANCE_CLAIM.MSG by CLAIMS_SEQ_ID
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updateInsuranceClaimMsg(@Param("vo") InsuranceClaimMapperVo vo) throws Exception;
	
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
	int addInsuranceCliam(InsuranceClaimMapperVo icvo) throws Exception;
	
	/**
	 * 依ncStatus(ex:0,1...etc.)傳入值取得資料
	 * @param ncStatus
	 * @return ArrayList<NotifyOfNewCaseVo>
	 * @throws Exception
	 */
	ArrayList<NotifyOfNewCaseVo> getNofifyOfNewCaseByNcStatus(String ncStatus) throws Exception;
	
	/**
	 * 取得未上傳聯盟的內部理賠件
	 * @return List<InsuranceClaimVo>
	 * @throws Exception
	 */
	List<InsuranceClaimMapperVo> getInsuranceClaimByNoCaseId() throws Exception;
	
	/**
	 * 內部申請件建立後，將呼叫完API101的回傳取得CaseId，更新回該筆記錄
	 * @param vo InsuranceClaimVo
	 * @return int
	 * @throws Exception
	 */
	int updateCaseIdByClaimSeqId(InsuranceClaimMapperVo vo) throws Exception;
	
	/**
	 * 內部申請件建立後取得檔案後，將呼叫完API101的回傳取得FileId，更新回該筆記錄
	 * @param vo InsuranceClaimFileDataVo
	 * @return
	 * @throws Exception
	 */
	int updateFileIdByFdId(InsuranceClaimFileDataVo vo) throws Exception;
	
	/**
	 * 依紙本註記，取得可上傳聯盟的案件
	 * @param fileReceived
	 * @return List<InsuranceClaimVo>
	 * @throws Exception
	 */
	List<InsuranceClaimMapperVo> getInsuranceClaimByFileReceived(String fileReceived) throws Exception;

	/**
	 * 台銀內部件，更新上傳紙本註記
	 * @param vo
	 * @return int
	 * @throws Exception
	 */
	int updateFileReceived(InsuranceClaimMapperVo vo) throws Exception;
	
	/**
	 * 更新TRANS_INSURANCE_CLAIM的是否已收到紙本欄位
	 * @param vo
	 * @return int
	 * @throws Exception
	 */
	int updateTransInsuranceClaimFileReceived(@Param("vo") InsuranceClaimMapperVo vo) throws Exception;
	
	/**
	 * 聯盟通知件，更新上傳紙本註記
	 * @param vo
	 * @return int
	 * @throws Exception
	 */
	int updateAllianceCaseFileReceived(InsuranceClaimVo vo) throws Exception;
	
	/**
	 * 取得聯盟通知件,下載未成功之檔案清單
	 * @param vo InsuranceClaimFileDataVo
	 * @return List<InsuranceClaimFileDataVo>
	 * @throws Exception
	 */
	List<InsuranceClaimFileDataVo> getFileDataToDownload(InsuranceClaimFileDataVo vo) throws Exception;
	
	/**
	 * 取得台銀理賠件,上傳未成功之檔案清單
	 * @param vo InsuranceClaimFileDataVo
	 * @return List<InsuranceClaimFileDataVo>
	 * @throws Exception
	 */
	List<InsuranceClaimFileDataVo> getFileDataToUpload(InsuranceClaimFileDataVo vo) throws Exception;
	
	/**
	 * 取得聯盟通知件，需要查詢是否收到紙本的件
	 * @return List<InsuranceClaimVo>
	 * @throws Exception
	 */
	List<InsuranceClaimMapperVo> getAllianceCaseByFileReceivedNotYet() throws Exception;
	
	/**
	 * 更新上傳/下載文件的(INSURANCE_CLAIM_FILEDATAS.FILE_STATUS),用以區分此筆記錄是否已完成(success)
	 * @param vo
	 * @return int
	 * @throws Exception
	 */
	int updateFileStatusByFileId(@Param("vo") InsuranceClaimFileDataVo vo) throws Exception;

	/**
	 * 取得未上傳台銀TRANS的聯盟通知件
	 * @return List<InsuranceClaimVo>
	 * @throws Exception
	 */
	List<InsuranceClaimMapperVo> getInsuranceClaimByHasNotifySeqIdNoTransNum() throws Exception;
	
	/**
	 * 更新聯盟通知，表示已上傳eservice.Trans
	 * @param vo InsuranceClaimMapperVo
	 * @return
	 * @throws Exception
	 */
	int updateInsuranceClaimTransNumByNotifySeqId(InsuranceClaimMapperVo vo) throws Exception;
	
	/**
	 * 更新NC_STATUS(ex:已取完件update NC_STATUS=1)
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updateNcStatusBySeqId(@Param("vo") NotifyOfNewCaseVo vo) throws Exception;
	
	/**
	 * 取得case id 
	 * @param claimSeqId
	 * @return InsuranceClaimMapperVo
	 * @throws Exception
	 */
	InsuranceClaimMapperVo getCaseIdBySeqId(@Param("claimSeqId") Float claimSeqId);
	
	List<InsuranceClaimFileDataVo> getInsuranceClaimFileDataByClaimsSequId(@Param("claimSeqId") Float claimSeqId);
	
	Map<String,String> getMailAndSMSInfo(String transNum);
	
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
	/**
	 * 新增理賠申請檔案FileData
	 * @param vo
	 * @return int
	 * @throws Exception
	 */
	int addInsuranceClaimFileData(InsuranceClaimFileDataVo vo) throws Exception;
	/**
	 * 更新文件的保单理赔的Base64的数据
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updateInsuranceClaimFileBase64(InsuranceClaimFileDataVo vo) throws Exception;

	/**
	 * 獲取保單理賠的文件編號
	 * @return
	 * @throws Exception
	 */
	Float getItransInsuranceFiledatasId() throws Exception;
}
