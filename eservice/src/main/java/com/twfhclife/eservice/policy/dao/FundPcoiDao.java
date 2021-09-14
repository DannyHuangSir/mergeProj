package com.twfhclife.eservice.policy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.policy.model.FundPcoiVo;

/**
 * 續期扣收 Dao.
 * 
 * @author all
 */
public interface FundPcoiDao {
	
	/**
	 * 續期扣收-查詢.
	 * 
	 * @param fundPcoiVo FundPcoiVo
	 * @return 回傳查詢結果
	 */
	List<FundPcoiVo> getFundPcoi(@Param("fundPcoiVo") FundPcoiVo fundPcoiVo);
}