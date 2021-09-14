package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransPaymodeVo;

public interface TransPaymodeMapper {
	
	public TransPaymodeVo findById(@Param("transPaymodeVo") TransPaymodeVo transPaymodeVo);
	
	public List<TransPaymodeVo> getTransPaymodeList(@Param("transPaymodeVo") TransPaymodeVo transPaymodeVo);
	
	public TransPaymodeVo getActiveDate(@Param("policyNo") String policyNo);
}