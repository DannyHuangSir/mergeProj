package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransRewardVo;

public interface TransRewardMapper {
	
	public TransRewardVo findById(@Param("transRewardVo") TransRewardVo transRewardVo);
	
	public List<TransRewardVo> getTransRewardList(@Param("transRewardVo") TransRewardVo transRewardVo);
}