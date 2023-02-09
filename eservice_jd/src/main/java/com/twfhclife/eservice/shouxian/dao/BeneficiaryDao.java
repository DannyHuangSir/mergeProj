package com.twfhclife.eservice.shouxian.dao;

import com.twfhclife.eservice.web.model.BeneficiaryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BeneficiaryDao {
	
	public List<BeneficiaryVo> getBeneficiaryByPolicyNo(@Param("policyNo") String policyNo);

}
