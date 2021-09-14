package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransContactInfoOldVo;

public interface TransContactInfoOldMapper {

	public List<TransContactInfoOldVo> getTransContactInfoOldList(@Param("transContactInfoOldVo") TransContactInfoOldVo transContactInfoOldVo);
	
	public TransContactInfoOldVo getTransContactInfoOld(@Param("transContactInfoOldVo") TransContactInfoOldVo transContactInfoOldVo);
}