package com.twfhclife.eservice.onlineChange.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransReducePolicyVo;

/**
 * 減少保險金額主檔 Dao.
 * 
 * @author all
 */
public interface TransReducePolicyDao {
	
	/**
	 * 減少保險金額主檔-查詢.
	 * 
	 * @param transReducePolicyVo TransReducePolicyVo
	 * @return 回傳查詢結果
	 */
	List<TransReducePolicyVo> getTransReducePolicy(@Param("transReducePolicyVo") TransReducePolicyVo transReducePolicyVo);
	
	/**
	 * 減少保險金額主檔-新增.
	 * 
	 * @param transReducePolicyVo TransReducePolicyVo
	 * @return 回傳影響筆數
	 */
	int insertTransReducePolicy(@Param("transReducePolicyVo") TransReducePolicyVo transReducePolicyVo);
	
	/**
	 * 取得下一筆序號.
	 * 
	 * @return 回傳序號
	 */
	BigDecimal getNextTransReducePolicyId();
}