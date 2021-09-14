package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransBeneficiaryVo;

public interface TransBeneficiaryMapper {
	
	public TransBeneficiaryVo findById(@Param("transBeneficiaryVo") TransBeneficiaryVo transBeneficiaryVo);
	
	public List<TransBeneficiaryVo> getTransBeneficiaryList(@Param("transBeneficiaryVo") TransBeneficiaryVo transBeneficiaryVo);
}