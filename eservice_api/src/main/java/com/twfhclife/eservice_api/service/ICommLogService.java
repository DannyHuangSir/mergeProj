package com.twfhclife.eservice_api.service;

import com.twfhclife.generic.domain.CommLogRequest;

public interface ICommLogService {

	public int addCommLog(CommLogRequest req) throws Exception;
}
