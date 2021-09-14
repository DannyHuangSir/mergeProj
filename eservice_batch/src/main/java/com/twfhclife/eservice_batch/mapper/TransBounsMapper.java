package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransBounsVo;

public interface TransBounsMapper {
	
	public TransBounsVo findById(@Param("transBounsVo") TransBounsVo transBounsVo);
	
	public List<TransBounsVo> getTransBounsList(@Param("transBounsVo") TransBounsVo transBounsVo);
}