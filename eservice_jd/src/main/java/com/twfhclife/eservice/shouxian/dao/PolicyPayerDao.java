package com.twfhclife.eservice.shouxian.dao;

import com.twfhclife.eservice.web.model.PolicyPayerVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 付款人 Dao.
 * 
 * @author all
 */
public interface PolicyPayerDao {
	
	/**
	 * 付款人-查詢.
	 * 
	 * @param policyPayerVo PolicyPayerVo
	 * @return 回傳查詢結果
	 */
	List<PolicyPayerVo> getPolicyPayerList(@Param("policyPayerVo") PolicyPayerVo policyPayerVo);
	
	/**
	 * 付款人-查詢.
	 * 
	 * @param policyNo 保單號碼
	 * @return 回傳查詢結果
	 */
	PolicyPayerVo getPolicyPayer(@Param("policyNo") String policyNo);
}

