package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransBankInfoVo;

public interface TransBankInfoMapper {
	
	public TransBankInfoVo findById(@Param("transBankInfoVo") TransBankInfoVo transBankInfoVo);
	
	public List<TransBankInfoVo> getTransBankInfoList(@Param("transBankInfoVo") TransBankInfoVo transBankInfoVo);
}