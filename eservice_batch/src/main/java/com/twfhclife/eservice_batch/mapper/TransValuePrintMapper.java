package com.twfhclife.eservice_batch.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransPolicyVo;
import com.twfhclife.eservice_batch.model.TransValuePrintVo;

public interface TransValuePrintMapper {
	
	public TransValuePrintVo findById(@Param("transValuePrintVo") TransValuePrintVo transValuePrintVo);
	
	public List<TransValuePrintVo> getTransValuePrintList(@Param("transValuePrintVo") TransValuePrintVo transValuePrintVo);
	
	public Map<String, Object> getTransValuePrintInfoData(@Param("transPolicyVo") TransPolicyVo transPolicyVo);
	
	public String getProductNameByCode(@Param("productCode") String productCode);
}