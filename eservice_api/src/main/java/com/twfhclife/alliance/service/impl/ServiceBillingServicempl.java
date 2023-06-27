package com.twfhclife.alliance.service.impl;

import com.twfhclife.alliance.dao.ServiceBillingDao;
import com.twfhclife.alliance.domain.Spa402RequestVo;
import com.twfhclife.alliance.service.IServiceBillingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ServiceBillingServicempl implements IServiceBillingService {

	private static final Logger logger = LogManager.getLogger(ServiceBillingServicempl.class);

	@Autowired
	private ServiceBillingDao serviceBillingDao;
	@Override
	public int addServiceBillingReplay(Spa402RequestVo vo) {
		return serviceBillingDao.addServiceBillingReplay(vo);
	}

	@Override
	public Map<String, Object> getReplayStatusByIdNo(Long id) {
		return serviceBillingDao.getReplayStatusByIdNo(id);
	}
}
