package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransPolicyVo;

public interface TransPolicyMapper {
	
	public TransPolicyVo findById(@Param("transPolicyVo") TransPolicyVo transPolicyVo);
	
	public List<TransPolicyVo> getTransPolicyList(@Param("transPolicyVo") TransPolicyVo transPolicyVo);
}