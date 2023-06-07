package com.twfhclife.eservice.onlineChange.dao;

import java.util.Date;
import java.util.List;

import com.twfhclife.eservice.policy.model.IndividualVo;
import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransRiskLevelVo;

/**
 * 變更風險屬性 Dao.
 * 
 * @author all
 */
public interface TransRiskLevelDao {
	
	/**
	 * 變更風險屬性-查詢.
	 * 
	 * @param TransRiskLevelVo transRiskLevelVo
	 * @return 回傳查詢結果
	 */
	List<TransRiskLevelVo> getTransRiskLevelList(@Param("transRiskLevelVo") TransRiskLevelVo transRiskLevelVo);
	
	/**
	 * 變更風險屬性-新增.
	 * 
	 * @param TransRiskLevelVo transRiskLevelVo
	 * @return 回傳影響筆數
	 */
	int insertTransRiskLevel(@Param("transRiskLevelVo") TransRiskLevelVo transRiskLevelVo);
	
	/**
	 * 取得投資風險屬性代碼.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳投資風險屬性
	 */
	String getRiskLevelCode(@Param("policyNo") String policyNo);

	/***
	 * 获取风险属性
	 * @param rocId
	 * @return
	 */
    String getRiskAttr(@Param("rocId") String rocId);

    Date getRecentApplyTime(@Param("userId") String userId);

    IndividualVo getIndividualVoByRocId(@Param("rocId") String userRocId);
    
	/**
	 * 變更風險屬性-查詢.
	 * 
	 * @param TransRiskLevelVo transRiskLevelVo
	 * @return 回傳查詢結果
	 */
	TransRiskLevelVo getTransRiskLevel(@Param("rocId") String rocId);
    
}