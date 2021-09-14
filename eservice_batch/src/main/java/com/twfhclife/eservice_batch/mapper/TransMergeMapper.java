package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransMergeVo;

public interface TransMergeMapper {
	
	public TransMergeVo findById(@Param("transMergeVo") TransMergeVo transMergeVo);
	
	public String getTodayNextTransMergeNum();
	
	public int insertTransMerge(@Param("transMergeVo") TransMergeVo transMergeVo);
	
	public List<TransMergeVo> getTransMergeList(@Param("transMergeVo") TransMergeVo transMergeVo);
}