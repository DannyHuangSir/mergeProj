package com.twfhclife.eservice.onlineChange.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransBeneficiaryVo;

/**
 * 變更受益人主檔 Dao.
 * 
 * @author all
 */
public interface TransBeneficiaryDao {
	
	/**
	 * 變更受益人主檔-查詢.
	 * 
	 * @param transBeneficiaryVo TransBeneficiaryVo
	 * @return 回傳查詢結果
	 */
	List<TransBeneficiaryVo> getTransBeneficiary(@Param("transBeneficiaryVo") TransBeneficiaryVo transBeneficiaryVo);

	/**
	 * 取得下一筆序號.
	 * 
	 * @return 回傳序號
	 */
	BigDecimal getNextTransBeneficiaryId();
	
	/**
	 * 變更受益人主檔-新增.
	 * 
	 * @param transBeneficiaryVo TransBeneficiaryVo
	 * @return 回傳影響筆數
	 */
	int insertTransBeneficiary(@Param("transBeneficiaryVo") TransBeneficiaryVo transBeneficiaryVo);
}