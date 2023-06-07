package com.twfhclife.eservice.api.elife.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.api.elife.domain.TransCtcLibnagVo;

public interface TransCtcLibnagDao {

	List<TransCtcLibnagVo> getRevokeByLibnagForBnagInsuSeqNo(@Param("bnagInsuSeqNo") String bnagInsuSeqNo);
	
	List<TransCtcLibnagVo> getBirthByPolicyNo(@Param("policyNo") String policyNo);
}
