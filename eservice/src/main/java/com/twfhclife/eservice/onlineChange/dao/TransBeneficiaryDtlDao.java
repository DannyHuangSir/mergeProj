package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransBeneficiaryDtlVo;

/**
 * 變更受益人明細 Dao.
 * 
 * @author all
 */
public interface TransBeneficiaryDtlDao {
	
	/**
	 * 變更受益人明細-查詢.
	 * 
	 * @param transBeneficiaryDtlVo TransBeneficiaryDtlVo
	 * @return 回傳查詢結果
	 */
	List<TransBeneficiaryDtlVo> getTransBeneficiaryDtl(@Param("transBeneficiaryDtlVo") TransBeneficiaryDtlVo transBeneficiaryDtlVo);
	
	/**
	 * 變更受益人明細-新增.
	 * 
	 * @param transBeneficiaryDtlVo TransBeneficiaryDtlVo
	 * @return 回傳影響筆數
	 */
	int insertTransBeneficiaryDtl(@Param("transBeneficiaryDtlVo") TransBeneficiaryDtlVo transBeneficiaryDtlVo);
}