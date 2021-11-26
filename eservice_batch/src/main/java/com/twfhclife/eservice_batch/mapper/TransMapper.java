package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import com.twfhclife.eservice_batch.model.TransStatusHistoryVo;
import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransVo;

public interface TransMapper {
	
	public TransVo findById(@Param("transVo") TransVo transVo);
	
	public List<TransVo> getTransList(@Param("transVo") TransVo transVo);
	
	public int updateTrans(@Param("transVo") TransVo transVo);
	
	public int updateTransMerge(@Param("transVo") TransVo transVo);

    int addTransStatusHistory(@Param("transStatusHistoryVo")   TransStatusHistoryVo transStatusHistoryVo);

    String getTransNumsByMergeNum(@Param("transNum") String transNum);
}