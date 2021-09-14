package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransCushionVo;

public interface TransCushionMapper {
	
	public TransCushionVo findById(@Param("transCushionVo") TransCushionVo transCushionVo);
	
	public List<TransCushionVo> getTransCushionList(@Param("transCushionVo") TransCushionVo transCushionVo);
}