package com.twfhclife.eservice_batch.mapper;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.OnlineChangeInfoVo;

public interface OnlineChangeInfoMapper {
	
	public OnlineChangeInfoVo getOnlineChangeInfo(@Param("rocId") String rocId, @Param("policyNo") String policyNo);
	
	public OnlineChangeInfoVo getOnlineChangeInfoByTransNum(@Param("transNum") String transNum);
	
}