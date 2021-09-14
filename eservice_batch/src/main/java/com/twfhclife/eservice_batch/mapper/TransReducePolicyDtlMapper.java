package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransReducePolicyDtlVo;

public interface TransReducePolicyDtlMapper {
	
	public TransReducePolicyDtlVo findById(@Param("transReducePolicyDtlVo") TransReducePolicyDtlVo transReducePolicyDtlVo);
	
	public List<TransReducePolicyDtlVo> getTransReducePolicyDtlList(@Param("transReducePolicyDtlVo") TransReducePolicyDtlVo transReducePolicyDtlVo);
}