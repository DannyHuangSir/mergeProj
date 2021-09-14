package com.twfhclife.eservice_api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice_api.service.ICommLogService;
import com.twfhclife.generic.dao.CommLogDao;
import com.twfhclife.generic.domain.CommLogRequest;

@Service
public class CommLogServiceImpl implements ICommLogService {

	@Autowired
	private CommLogDao commLogDao;

	@Override
	public int addCommLog(CommLogRequest req) throws Exception {
		return commLogDao.addCommLog(req);
	}

}
