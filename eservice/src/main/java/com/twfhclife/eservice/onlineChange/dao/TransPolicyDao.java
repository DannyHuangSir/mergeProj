package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.web.model.TransPolicyVo;

/**
 * 線上保單號碼申請 Dao.
 * 
 * @author all
 */
public interface TransPolicyDao {
	
	/**
	 * 取得交易序號對應的保單號碼.
	 * 
	 * @param transNum 交易序號
	 * @return 回傳交易序號對應的保單號碼
	 */
	List<String> getTransPolicyNoList(@Param("transNum") String transNum);
	
	/**
	 * 查詢現在還在申請處理中的保單號碼.
	 * 
	 * @param userId 使用者ID
	 * @param transType 申請項目
	 * @return 回傳還在申請處理中的保單碼
	 */
	List<String> getProgressPolicyNoList(@Param("userId") String userId, @Param("transType") String transType);
	
	/**
	 * 線上申請保單號碼-新增.
	 * 
	 * @param transPolicyVo TransPolicyVo
	 * @return 回傳影響筆數
	 */
	int insertTransPolicy(@Param("transPolicyVo") TransPolicyVo transPolicyVo);
}