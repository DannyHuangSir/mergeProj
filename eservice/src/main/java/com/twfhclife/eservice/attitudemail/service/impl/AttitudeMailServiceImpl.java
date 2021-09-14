package com.twfhclife.eservice.attitudemail.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.attitudemail.dao.AttitudeMailDao;
import com.twfhclife.eservice.attitudemail.service.IAttitudeMailService;
import com.twfhclife.eservice.web.model.AttitudeMailVo;

@Service
public class AttitudeMailServiceImpl implements IAttitudeMailService {
	
	private static final Logger logger = LogManager.getLogger(AttitudeMailServiceImpl.class);
	
	@Autowired
	private AttitudeMailDao attitudeMailDao;

	@Override
	public boolean insertAttitudeMail(AttitudeMailVo attitudeMailVo) {
		if((attitudeMailVo.getAttitudeArea() != null && attitudeMailVo.getAttitudeArea().length() > 0)
				&& (attitudeMailVo.getAttitudeCity() != null && attitudeMailVo.getAttitudeCity().length() > 0)) {
			String zipCode = attitudeMailDao.getZipCode(attitudeMailVo);
			attitudeMailVo.setAttitudeZipCode(zipCode);
		}
		return attitudeMailDao.insertAttitudeMail(attitudeMailVo) > 0;
	}

}
