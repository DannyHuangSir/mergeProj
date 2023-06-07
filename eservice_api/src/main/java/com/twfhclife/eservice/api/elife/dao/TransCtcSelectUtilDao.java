package com.twfhclife.eservice.api.elife.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.api.elife.domain.LicohilVo;
import com.twfhclife.eservice.api.elife.domain.PolicyDetailRequest;
import com.twfhclife.eservice.api.elife.domain.PolicyDetailVo;
import com.twfhclife.eservice.api.elife.domain.TransCtcSelectDataAddCodeVo;
import com.twfhclife.eservice.api.elife.domain.TransCtcSelectDataVo;
import com.twfhclife.eservice.api.elife.domain.TransCtcSelectDetailVo;

public interface TransCtcSelectUtilDao {
	
	List<TransCtcSelectDataVo> getTransCtcSelectDataByLipmId(@Param("lipmId") String lipmId);
	
	List<TransCtcSelectDetailVo> getTransCtcSelectDetailByLipmInsuSeqNo(@Param("lipmInsuSeqNo") String lipmInsuSeqNo);
	
	List<TransCtcSelectDataAddCodeVo> getTransCtcSelectDataAddCode(@Param("lipmId") String lipmId);
	
	List<PolicyDetailVo> getPolicyDataByRocId(@Param("request") PolicyDetailRequest request);
	
	List<LicohilVo> getLicohiByPolicyNo(@Param("request") PolicyDetailRequest request);
	
	List<LicohilVo>  getLilipmByPolicyNo(@Param("request") PolicyDetailRequest request);
}
