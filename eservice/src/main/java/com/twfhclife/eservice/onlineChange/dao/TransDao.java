package com.twfhclife.eservice.onlineChange.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.web.model.TransVo;

/**
 * 線上申請 Dao.
 * 
 * @author all
 */
public interface TransDao {

	/**
	 * 取線上申請序號.
	 * 
	 * @param params
	 */
	void getTransNum(Map<String,Object> params);

	/**
	 * 線上申請保單驗證.
	 * 
	 * @param params
	 */
	void verifyPolicyRule(Map<String,Object> params);

	/**
	 * 檢查是否已經申請.
	 * 
	 * @param policyNo 保單號碼
	 * @param transType 申請類型
	 * @param transStatus 申請狀態
	 * @return 回傳是否已經申請
	 */
	int isTransApplyed(@Param("policyNo") String policyNo, @Param("transType") String transType, @Param("transStatus") String transStatus);
	
	/**
	 * 線上申請-新增.
	 * 
	 * @param transVo TransVo
	 * @return 回傳影響筆數
	 */
	int insertTrans(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得申請資料.
	 * 
	 * @param transNum 申請序號
	 * @return 回傳申請資料
	 */
	TransVo findByTransNum(@Param("transNum") String transNum);
	
	/**
	 * 取得使用者的線上申請記錄(申請中).
	 * 
	 * @param transVo TransVo
	 * @return 回傳使用者的線上申請記錄
	 */
	List<TransVo> getApplyProgressList(@Param("transVo") TransVo transVo);
	
	/**
	 * 取得使用者的線上申請記錄.
	 * 
	 * @param transVo TransVo
	 * @return 回傳使用者的線上申請記錄
	 */
	List<TransVo> getChangeHistoryList(@Param("transVo") TransVo transVo);
	
	/**
	 * 查看现行网络资料
	 */
	HashMap getUserCurrentNetworkData(@Param("policyNo")String policyNo);
	
	Float getBatchIdSequence();
	//通過保單編號獲取申請序號
	String isTransNum(@Param("policyNo")String policyNo);

    int updateSignTransStatus(@Param("transNum") String transNum, @Param("status")  String status);

    String getTransStatusByTransNum(@Param("transNum") String transNum);
	
	List<TransVo> getLastCompleteTime(@Param("transType") String transType, @Param("userId") String userId );
	
	List<TransVo> getProcessInvestment(@Param("userId") String userId, @Param("transTypes") List<String> transTypes);
}