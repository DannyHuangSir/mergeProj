package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransReducePolicyOldVo;

/**
 * 減少保險金額修改前 Dao.
 * 
 * @author all
 */
public interface TransReducePolicyOldDao {
	
	/**
	 * 減少保險金額修改前-查詢.
	 * 
	 * @param transReducePolicyOldVo TransReducePolicyOldVo
	 * @return 回傳查詢結果
	 */
	List<TransReducePolicyOldVo> getTransReducePolicyOld(@Param("transReducePolicyOldVo") TransReducePolicyOldVo transReducePolicyOldVo);
	
	/**
	 * 減少保險金額修改前-新增.
	 * 
	 * @param transReducePolicyOldVo TransReducePolicyOldVo
	 * @return 回傳影響筆數
	 */
	int insertTransReducePolicyOld(@Param("transReducePolicyOldVo") TransReducePolicyOldVo transReducePolicyOldVo);
}