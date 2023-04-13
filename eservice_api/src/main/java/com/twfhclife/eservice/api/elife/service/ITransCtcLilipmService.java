package com.twfhclife.eservice.api.elife.service;

import java.util.List;

import com.twfhclife.eservice.api.elife.domain.TransCtcLilipmVo;

public interface ITransCtcLilipmService {
	
	/**
	 * 取得CTC資料
	 * @param lipmId 身分證
	 * @return 
	 */
	public List<TransCtcLilipmVo> getCtcLilipm(String lipmId);
	
	public List<TransCtcLilipmVo> getCtcLilipmDetail(String policyNo); 
	
	public List<TransCtcLilipmVo> getRevokeByLilipmForLipmInsuSeqNo(String lipmInsuSeqNo);
	
	public List<TransCtcLilipmVo> getRevokeByLilipmForSeqNoAndAginRecCode(String lipmInsuSeqNo);
	
	public List<TransCtcLilipmVo> getOnlineTrialDetail(String lipmInsuSeqNo);
	
}
