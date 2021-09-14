package com.twfhclife.eservice.policy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.policy.model.FundValueVo;

/**
 * 淨值 Dao.
 * 
 * @author all
 */
public interface FundValueDao {
	
	/**
	 * 淨值-查詢.
	 * 
	 * @param fundValueVo FundValueVo
	 * @return 回傳查詢結果
	 */
	List<FundValueVo> getFundValue(@Param("fundValueVo") FundValueVo fundValueVo);
}