package com.twfhclife.eservice.api.elife.service;

import com.twfhclife.eservice.api.elife.domain.TransCsp002DataRequest;
import com.twfhclife.eservice.api.elife.domain.CspData;
import com.twfhclife.eservice.api.elife.domain.TransCspApiUtilRequest;
import com.twfhclife.eservice.api.elife.domain.TransOnlineTrialVo;

public interface ITransCspApiUtilService {
	
	public CspData getCsp001Detail(TransCspApiUtilRequest reuqest);

	public TransOnlineTrialVo getCsp0021Detail (TransCsp002DataRequest reuqest);
}
