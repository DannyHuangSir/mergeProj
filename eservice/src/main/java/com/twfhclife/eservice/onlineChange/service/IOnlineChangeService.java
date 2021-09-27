package com.twfhclife.eservice.onlineChange.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import com.twfhclife.eservice.onlineChange.model.BlackListVo;
import com.twfhclife.eservice.onlineChange.model.TableGetVo;
import com.twfhclife.eservice.onlineChange.model.TransStatusHistoryVo;
import com.twfhclife.eservice.onlineChange.model.TransVo;

public interface IOnlineChangeService {
	
	public List<TransVo> getTransByUserId(String userId, String status, String transType, 
			String policyNo, String startDate, String endDate, int pageNum, int pageSize);
	
	public TableGetVo getTransDetail(String transType, String transNum, String userId);
	
	public void cancelApplyTrans(String transNum, TransStatusHistoryVo hisVo);
	
	public void cancelInsClaim(String transNum);
	
	public void beAddedTrans(String transNum);
	
	public void addTransFile(String transNum, List<MultipartFile> uploadFiles, String userId) throws Exception;

	/**
	 * 取得批註單 byte[].
	 * 
	 * @return 回傳報表 byte[]
	 */
	public byte[] getEndorsementPDF(String transNum, String rocId);
	
	public String getUserIdByTransNum(@Param("transNum")String transNum, @Param("transType")String transType);
	
	public boolean checkFileSize(String transNum, List<MultipartFile> uploadFiles, String limitSizeStr);
	
	/**
	 * 檢查是否進入進入黑名單
	 */
	public Map<String, String> checkBackList(@Param("blackListVo")BlackListVo blackListVo);

	/**
	 * 查詢狀態歷程
	 * @param vo
	 * @return
	 */
	List<TransStatusHistoryVo> getTransStatusHistoryList(TransStatusHistoryVo vo);

	//進行取消 已持有醫療保單的轉換保單
        void cancelMedicalTreatmentApplyTrans(String transNum, TransStatusHistoryVo hisVo);

	//取消已持有投資標的轉換
	void cancelApplyTransConversion(String transNum, TransStatusHistoryVo hisVo);
}
