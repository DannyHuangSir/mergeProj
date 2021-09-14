package com.twfhclife.eservice_batch.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransContactInfoDtlVo;

public interface TransContactInfoDtlMapper {
	
	public TransContactInfoDtlVo findById(@Param("transContactInfoDtlVo") TransContactInfoDtlVo transContactInfoDtlVo);
	
	public List<TransContactInfoDtlVo> getTransContactInfoDtlList(@Param("transContactInfoDtlVo") TransContactInfoDtlVo transContactInfoDtlVo);

	/**
	 * 取得保單聯絡明細資料.
	 *
	 * @param transContactId 明細ID
	 * @return 回傳變更保單聯絡明細資料
	 */
	List<Map<String, Object>> getTransContactInfoDetailList(@Param("transContactId") String transContactId);

}