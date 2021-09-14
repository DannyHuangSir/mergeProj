package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransResendPolicyVo;

/**
 * 補發保單 Dao.
 * 
 * @author all
 */
public interface TransResendPolicyDao {
	
	/**
	 * 補發保單-查詢.
	 * 
	 * @param transResendPolicyVo TransResendPolicyVo
	 * @return 回傳查詢結果
	 */
	List<TransResendPolicyVo> getTransResendPolicy(@Param("transResendPolicyVo") TransResendPolicyVo transResendPolicyVo);
	
	/**
	 * 補發保單-新增.
	 * 
	 * @param transResendPolicyVo TransResendPolicyVo
	 * @return 回傳影響筆數
	 */
	int insertTransResendPolicy(@Param("transResendPolicyVo") TransResendPolicyVo transResendPolicyVo);

	List<TransResendPolicyVo> getTransPolicyResend(TransResendPolicyVo qryVo);
}