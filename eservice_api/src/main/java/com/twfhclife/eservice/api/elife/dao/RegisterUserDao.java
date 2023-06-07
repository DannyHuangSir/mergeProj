package com.twfhclife.eservice.api.elife.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.api.elife.domain.RegisterUserVo;

public interface RegisterUserDao {

	List<RegisterUserVo> getUserMailPhoneByRocid(@Param("rocId") String rocId);
	
	RegisterUserVo getLilomsAmtByPolicyNo(@Param("policyNo") String policyNo);
	
	RegisterUserVo getUserMailPhoneByRocidAndPolicyNo(@Param("rocId") String rocId , @Param("policyNo") String policyNo);
	
	List<RegisterUserVo>  CheckLipmInsuNoByPolicyNo(@Param("rocId") String rocId , @Param("policyNo") String policyNo);
	
}
