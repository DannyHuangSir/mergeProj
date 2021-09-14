package com.twfhclife.eservice.api.adm.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.api.adm.model.SystemEventVo;
import com.twfhclife.eservice.api.adm.service.ISystemEventService;
import com.twfhclife.generic.dao.adm.SystemEventDao;
import com.twfhclife.generic.dao.adm.UsersDao;
import com.twfhclife.generic.model.UsersVo;

@Service
public class SystemEventServiceImpl implements ISystemEventService {
	
	private static final Logger logger = LogManager.getLogger(SystemEventServiceImpl.class);

	@Autowired
	private SystemEventDao systemEventDao;
	
	@Autowired
	@Qualifier("apiUsersDao")
	private UsersDao userDao;
	
	@Value("${keycloak.elife-realm}")
	protected String ESERVICE_REALM;
	
	@Override
	public List<SystemEventVo> query(SystemEventVo systemEventVo) {
		return systemEventDao.query(systemEventVo);
	}
	
	@Override
	public int count(SystemEventVo systemEventVo) {
		return systemEventDao.count(systemEventVo);
	}
	
	@Override
	public void add(SystemEventVo systemEventVo) {
		String userId = systemEventVo.getExecUser();
		if(userId.startsWith("FBID")) {
			// UserId is null 
			String fbId = userId.substring(userId.indexOf("=")+1);
			logger.debug("BusinessEvent.add() userId is null, find userId by fbid: fbId="+fbId);
			UsersVo vo = getUserByFbId(fbId);
			systemEventVo.setExecUser(vo.getUserName());
			systemEventVo.setCreateUser(vo.getUserName());
		} else if(userId.startsWith("ICID")) {
			// UserId is null 
			String moicaId = userId.substring(userId.indexOf("=")+1);
			logger.debug("BusinessEvent.add() userId is null, find userId by moicaId: moicaId="+moicaId);
			UsersVo vo = getUserByMoicaId(moicaId);
			systemEventVo.setExecUser(vo.getUserName());
			systemEventVo.setCreateUser(vo.getUserName());
		}
		systemEventDao.add(systemEventVo);
	}
	
	@Override
	public void update(SystemEventVo systemEventVo) {
		systemEventDao.update(systemEventVo);
	}
	
	@Override
	public void delete(SystemEventVo systemEventVo) {
		systemEventDao.delete(systemEventVo);
	}
	
	public UsersVo getUserByFbId(String fbId) {
		UsersVo uservo = null;
		try {
			uservo = userDao.getUserByFbId(ESERVICE_REALM, fbId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uservo;
	}
	
	public UsersVo getUserByMoicaId(String moicaId) {
		UsersVo uservo = null;
		try {
			uservo = userDao.getUserByCardSn(ESERVICE_REALM, moicaId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uservo;
	}
}