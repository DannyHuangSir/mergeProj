package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransContactInfoVo;

public interface TransContactInfoMapper {
	
	public TransContactInfoVo findById(@Param("transContactInfoVo") TransContactInfoVo transContactInfoVo);
	
	public List<TransContactInfoVo> getTransContactInfoList(@Param("transContactInfoVo") TransContactInfoVo transContactInfoVo);
}