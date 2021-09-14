package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransBeneficiaryOldVo;

/**
 * 變更受益人修改前 Dao.
 * 
 * @author all
 */
public interface TransBeneficiaryOldDao {
	
	/**
	 * 變更受益人修改前-查詢.
	 * 
	 * @param transBeneficiaryOldVo TransBeneficiaryOldVo
	 * @return 回傳查詢結果
	 */
	List<TransBeneficiaryOldVo> getTransBeneficiaryOld(@Param("transBeneficiaryOldVo") TransBeneficiaryOldVo transBeneficiaryOldVo);
	
	/**
	 * 變更受益人修改前-新增.
	 * 
	 * @param transBeneficiaryOldVo TransBeneficiaryOldVo
	 * @return 回傳影響筆數
	 */
	int insertTransBeneficiaryOld(@Param("transBeneficiaryOldVo") TransBeneficiaryOldVo transBeneficiaryOldVo);
}