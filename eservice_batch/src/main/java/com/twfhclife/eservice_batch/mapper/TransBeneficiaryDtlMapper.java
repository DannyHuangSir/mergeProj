package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransBeneficiaryDtlVo;

public interface TransBeneficiaryDtlMapper {
	
	public TransBeneficiaryDtlVo findById(@Param("transBeneficiaryDtlVo") TransBeneficiaryDtlVo transBeneficiaryDtlVo);
	
	public List<TransBeneficiaryDtlVo> getTransBeneficiaryDtlList(@Param("transBeneficiaryDtlVo") TransBeneficiaryDtlVo transBeneficiaryDtlVo);
}