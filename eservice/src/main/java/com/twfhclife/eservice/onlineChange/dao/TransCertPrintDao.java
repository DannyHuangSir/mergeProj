package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.onlineChange.model.TransCertPrintVo;

/**
 * 投保證明列印 Dao.
 * 
 * @author all
 */
public interface TransCertPrintDao {
	
	/**
	 * 投保證明列印-查詢.
	 * 
	 * @param transCertPrintVo TransCertPrintVo
	 * @return 回傳查詢結果
	 */
	List<TransCertPrintVo> getTransCertPrint(@Param("transCertPrintVo") TransCertPrintVo transCertPrintVo);
	
	/**
	 * 投保證明列印-新增.
	 * 
	 * @param transCertPrintVo TransCertPrintVo
	 * @return 回傳影響筆數
	 */
	int insertTransCertPrint(@Param("transCertPrintVo") TransCertPrintVo transCertPrintVo);
	
	/**
	 * 投保證明-保險中英文對照
	 * @param insuNo
	 * @return
	 */
	List<Map<String, Object>> queryBenefitDetail(@Param("insuNo") String insuNo);
	
	/**
	 * 投保證明-保單資訊
	 * @param insuNo
	 * @return
	 */
	List<Map<String, Object>> queryInsuDetail(@Param("insuNo") String insuNo);
}