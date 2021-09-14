package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransReducePolicyDtlVo;

/**
 * 減少保險金額明細 Dao.
 * 
 * @author all
 */
public interface TransReducePolicyDtlDao {
	
	/**
	 * 減少保險金額明細-查詢.
	 * 
	 * @param transReducePolicyDtlVo TransReducePolicyDtlVo
	 * @return 回傳查詢結果
	 */
	List<TransReducePolicyDtlVo> getTransReducePolicyDtl(@Param("transReducePolicyDtlVo") TransReducePolicyDtlVo transReducePolicyDtlVo);
	
	/**
	 * 減少保險金額明細-新增.
	 * 
	 * @param transReducePolicyDtlVo TransReducePolicyDtlVo
	 * @return 回傳影響筆數
	 */
	int insertTransReducePolicyDtl(@Param("transReducePolicyDtlVo") TransReducePolicyDtlVo transReducePolicyDtlVo);
}