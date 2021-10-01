package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.BlackListVo;
import com.twfhclife.eservice.onlineChange.model.TransInsuranceClaimFileDataVo;
import com.twfhclife.eservice.onlineChange.model.TransPolicyVo;
import com.twfhclife.eservice.onlineChange.model.TransRFEVo;
import com.twfhclife.eservice.onlineChange.model.TransStatusHistoryVo;
import com.twfhclife.eservice.onlineChange.model.TransVo;

public interface OnlineChangeDao {

	/**
	 * 取線上申請序號
	 * @param params
	 */
	public void getTransNum(Map<String,Object> params);
	
	/**
	 * 存入線上申請主表
	 * @param vo
	 * @return
	 */
	public int addTrans(@Param("vo") TransVo vo);
	
	/**
	 * 取的申請詳細的保單資料
	 * @param transNum
	 * @return
	 */
	public List<TransPolicyVo> getTransPolicy(@Param("transNum") String transNum);
	
	/**
	 * 我的申請項目
	 */
	public List<TransVo> getTransByUserId(@Param("userId") String userId, @Param("status") String status, 
			@Param("transType") String transType, @Param("policyNo") String policyNo, 
			@Param("startDate") String startDate, @Param("endDate") String endDate, 
			@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);
	
	/**
	 * 變更TRANS申請狀態
	 * @param transNum
	 * @param status
	 */
	public void updateTransStatus(@Param("transNum") String transNum, @Param("status") String status);
	
	/**
	 * 變更INSURANCE_CLAIM申請狀態
	 * @param transNum
	 * @param status
	 */
	public void updateInsClaimStatus(@Param("transNum") String transNum, @Param("status") String status);
	
	/**
	 * 補件
	 * @param transNum
	 * @param fileName
	 * @param transFile
	 * @param userId
	 */
	public void addTransFile(@Param("transNum")String transNum, @Param("fileName")String fileName, 
			@Param("transFile") byte[] transFile, @Param("userId") String userId);
	
	public String getUserIdByTransNum(@Param("transNum")String transNum, @Param("transType")String transType);
	
	/**
	 * 檢查是否進入進入黑名單
	 * @param blackListVo
	 */
	public String getBlackList(@Param("blackListVo")BlackListVo blackListVo);

	/**
	 * 檢查是否進入進入黑名單
	 * @param blackListVo
	 */
	public String getMedicalBlackList(@Param("blackListVo")BlackListVo blackListVo);

	/**
	 * 檢查保單理賠是否完成
	 */
	public int getPolicyClaimCompleted(@Param("rocId") String rocId);
	/**
	 * 檢查保單理賠是否完成
	 */
	public int getMedicalTreatmentClaimCompleted(@Param("rocId") String rocId);
	/**
	 * 添加狀態歷程
	 * @param vo
	 * @return
	 */
	int addTransStatusHistory(@Param("vo") TransStatusHistoryVo vo);
	
	/**
	 * 獲取年齡
	 */
	public String getAgeByPolicyNo(@Param("policyNo") String policyNo);
	
	/**
	 * 獲取申請時間
	 */
	public String getCreateDateByTransNum(@Param("transNum") String transNum);
	
	
	/**
	 * 檢查聯絡資料變更是否完成
	 */
	public int getContactInfoCompleted(@Param("rocId") String rocId);
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
	List<TransRFEVo> getTransRFEList(@Param("vo")TransRFEVo vo);
	/**
	 * 查詢補件
	 * @param vo
	 * @return
	 */
	List<TransInsuranceClaimFileDataVo> getTransInsCliamFileData(@Param("vo")TransRFEVo tVo);

	String getBirdateByPolicyNo(String policyNo);
}
