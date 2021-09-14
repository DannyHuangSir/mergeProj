package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.policy.model.FundSwitchVo;

/**
 * 變更投資標的及配置比例 Dao.
 * 
 * @author all
 */
public interface TransFundSwitchDao {
	
	/**
	 * 變更投資標的及配置比例轉出-查詢.
	 * 
	 * @param String transNum
	 * @return 回傳查詢結果
	 */
	List<FundSwitchVo> getTransFundSwitchOut(@Param("transNum") String transNum);

	/**
	 * 變更投資標的及配置比例轉入-查詢.
	 * 
	 * @param String transNum
	 * @return 回傳查詢結果
	 */
	List<FundSwitchVo> getTransFundSwitchIn(@Param("transNum") String transNum);
	
	/**
	 * 變更投資標的及配置比例轉出-新增.
	 * 
	 * @param transFundSwitchVo TransFundSwitchVo
	 * @return 回傳影響筆數
	 */
	int insertTransFundSwitchOut(@Param("fundSwitchVo") FundSwitchVo fundSwitchVo);
	
	/**
	 * 變更投資標的及配置比例轉入-新增.
	 * 
	 * @param transFundSwitchVo TransFundSwitchVo
	 * @return 回傳影響筆數
	 */
	int insertTransFundSwitchIn(@Param("fundSwitchVo") FundSwitchVo fundSwitchVo);

	List<FundSwitchVo> getOptionFundList(String policyType);
}