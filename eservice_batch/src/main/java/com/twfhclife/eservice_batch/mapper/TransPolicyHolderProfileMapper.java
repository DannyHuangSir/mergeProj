package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransPolicyHolderProfileVo;

public interface TransPolicyHolderProfileMapper {

	public TransPolicyHolderProfileVo findById(
			@Param("transPolicyHolderProfileVo") TransPolicyHolderProfileVo transPolicyHolderProfileVo);

	public List<TransPolicyHolderProfileVo> getPolicyHolderProfileList(
			@Param("transPolicyHolderProfileVo") TransPolicyHolderProfileVo transPolicyHolderProfileVo);

	public TransPolicyHolderProfileVo getOldJobByTransNum(@Param("transNum") String transNum);
}