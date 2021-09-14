package com.twfhclife.eservice.onlineChange.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.web.model.BeneficiaryVo;

public interface BeneficiaryDao {
	
	public List<BeneficiaryVo> getBeneficiaryByPolicyNo(@Param("policyNo") String policyNo);

}
