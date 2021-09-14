package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransReducePolicyVo;

public interface TransReducePolicyMapper {
	
	public TransReducePolicyVo findById(@Param("transReducePolicyVo") TransReducePolicyVo transReducePolicyVo);
	
	public List<TransReducePolicyVo> getTransReducePolicyList(@Param("transReducePolicyVo") TransReducePolicyVo transReducePolicyVo);
}