package com.twfhclife.eservice.onlineChange.service;

import java.util.List;

import com.twfhclife.eservice.onlineChange.model.OnlineTrialVo;
import com.twfhclife.eservice.onlineChange.model.TransOnlineTrialVo;

public interface ITransOnlineTrialService {

	public List<OnlineTrialVo> getPolicyDetail(String lipmId,String userId , String transType);
	
	public TransOnlineTrialVo getOnlineTrialDetail(String lipmInsuSeqNo);
}
