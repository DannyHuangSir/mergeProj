package com.twfhclife.eservice.api.elife.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.api.elife.domain.TransCtcLilipiVo;

public interface TransCtcLilipiDao {

	List<TransCtcLilipiVo> getLipiDataByPolicyNo(@Param("policyNo") String policyNo);
	
}
