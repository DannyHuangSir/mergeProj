package com.twfhclife.eservice.api.elife.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twfhclife.eservice.api.elife.domain.TransCtcLilipmResponse;
import com.twfhclife.eservice.api.elife.domain.TransCtcLilipmVo;

public interface TransCtcLilipmDao {
	
	List<TransCtcLilipmVo> getCtcLilipm(@Param("lipmId") String lipmId);
	
	List<TransCtcLilipmVo> getCtcLilipmDetail(@Param("policyNo") String policyNo);
	
	List<TransCtcLilipmVo> getRevokeByLilipmForLipmId (@Param("lipmId") String lipmId);
	
	List<TransCtcLilipmVo> getRevokeByLilipmForLipmInsuSeqNo(@Param("lipmInsuSeqNo") String lipmInsuSeqNo);
		 
	List<TransCtcLilipmVo> getRevokeByLilipmForSeqNoAndAginRecCode(@Param("lipmInsuSeqNo") String lipmInsuSeqNo);
	
	List<TransCtcLilipmVo> getOnlineTrialDetail(@Param("prodNo") String prodNo , @Param("lipmInsuSeqNo") String lipmInsuSeqNo);
}
