package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransAnnuityMethodVo;

public interface TransAnnuityMethodMapper {
	
	public TransAnnuityMethodVo findById(@Param("transAnnuityMethodVo") TransAnnuityMethodVo transAnnuityMethodVo);
	
	public List<TransAnnuityMethodVo> getTransAnnuityMethodList(@Param("transAnnuityMethodVo") TransAnnuityMethodVo transAnnuityMethodVo);
}