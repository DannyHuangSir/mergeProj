package com.twfhclife.eservice_batch.mapper;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.EZAcquireVo;
import com.twfhclife.eservice_batch.model.TransEndorsementVo;

public interface TransEndorsementMapper {	
	
	public int insertTransEndorsement(@Param("vo") TransEndorsementVo vo);
	
	public String getPolicyHolderName(@Param("transNum") String transNum);
	
	public int insertTransEZ(@Param("vo") EZAcquireVo vo);
	
}