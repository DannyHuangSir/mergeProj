package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransCertPrintVo;

public interface TransCertPrintMapper {
	
	public TransCertPrintVo findById(@Param("transCertPrintVo") TransCertPrintVo transCertPrintVo);
	
	public List<TransCertPrintVo> getTransCertPrintList(@Param("transCertPrintVo") TransCertPrintVo transCertPrintVo);
}