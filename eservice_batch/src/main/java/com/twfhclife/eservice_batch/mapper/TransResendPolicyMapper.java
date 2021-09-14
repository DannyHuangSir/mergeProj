package com.twfhclife.eservice_batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice_batch.model.TransResendPolicyVo;

public interface TransResendPolicyMapper {
	
	public TransResendPolicyVo findById(@Param("transResendPolicyVo") TransResendPolicyVo transResendPolicyVo);
	
	public List<TransResendPolicyVo> getTransResendPolicyList(@Param("transResendPolicyVo") TransResendPolicyVo transResendPolicyVo);
}