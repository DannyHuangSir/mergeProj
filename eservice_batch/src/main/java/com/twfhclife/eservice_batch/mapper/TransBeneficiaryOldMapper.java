package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransBeneficiaryOldVo;

public interface TransBeneficiaryOldMapper {
	
	public List<TransBeneficiaryOldVo> getTransBeneficiaryOldList(@Param("transBeneficiaryOldVo") TransBeneficiaryOldVo transBeneficiaryOldVo);
}