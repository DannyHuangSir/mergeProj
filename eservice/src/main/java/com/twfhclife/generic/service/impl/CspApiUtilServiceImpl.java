package com.twfhclife.generic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.onlineChange.util.OnlineChangeUtil;
import com.twfhclife.eservice.web.dao.ParameterDao;
import com.twfhclife.generic.service.ICspApiUtilService;
import com.twfhclife.generic.util.ApConstants;

@Service
public class CspApiUtilServiceImpl implements ICspApiUtilService{
	@Autowired
	private ParameterDao parameterDao;

	@Override
	public String getApiUrl(String apiMethod) {
		StringBuilder url = new StringBuilder();
		url.append(getURL());
		url.append(getApiMethod(apiMethod));
		return url.toString();
	}
	
	private String getURL() {
		String url = "";
		 url = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID_API,
				OnlineChangeUtil.CSP_URL);
		return url;
	}
	
	private String getApiMethod(String apiMethod) {
		apiMethod = parameterDao.getParameterValueByCode(ApConstants.SYSTEM_ID_API,
				apiMethod);
		return apiMethod;
	}
}
