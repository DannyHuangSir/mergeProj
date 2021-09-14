package com.twfhclife.eservice.api.adm.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.api.adm.model.BusinessEventVo;
import com.twfhclife.eservice.api.adm.service.IBusinessEventService;
import com.twfhclife.generic.dao.adm.BusinessEventDao;
import com.twfhclife.generic.dao.adm.UsersDao;
import com.twfhclife.generic.model.UsersVo;

@Service
public class BusinessEventServiceImpl implements IBusinessEventService {
	
	private static final Logger logger = LogManager.getLogger(BusinessEventServiceImpl.class);

	@Autowired
	private BusinessEventDao businessEventDao;
	
	@Autowired
	@Qualifier("apiUsersDao")
	private UsersDao userDao;
	
	@Value("${keycloak.elife-realm}")
	protected String ESERVICE_REALM;
	
	@Override
	public List<BusinessEventVo> query(BusinessEventVo businessEventVo) {
		return businessEventDao.query(businessEventVo);
	}
	
	@Override
	public int count(BusinessEventVo businessEventVo) {
		return businessEventDao.count(businessEventVo);
	}

	@Override
	public int getNextId() {
		return businessEventDao.getNextId();
	}
	
	@Override
	public int add(BusinessEventVo businessEventVo) {
		String userId = businessEventVo.getUserId();
		if(userId.startsWith("FBID")) {
			// UserId is null 
			String fbId = userId.substring(userId.indexOf("=")+1);
			logger.debug("BusinessEvent.add() userId is null, find userId by fbid: fbId="+fbId);
			UsersVo vo = getUserByFbId(fbId);
			businessEventVo.setUserId(vo.getUserName());
			businessEventVo.setCreateUser(vo.getUserName());
		} else if(userId.startsWith("ICID")) {
			// UserId is null 
			String moicaId = userId.substring(userId.indexOf("=")+1);
			logger.debug("BusinessEvent.add() userId is null, find userId by moicaId: moicaId="+moicaId);
			UsersVo vo = getUserByMoicaId(moicaId);
			businessEventVo.setUserId(vo.getUserName());
			businessEventVo.setCreateUser(vo.getUserName());
		}
		return businessEventDao.add(businessEventVo);
	}
	
	@Override
	public void update(BusinessEventVo businessEventVo) {
		businessEventDao.update(businessEventVo);
	}
	
	@Override
	public void delete(BusinessEventVo businessEventVo) {
		businessEventDao.delete(businessEventVo);
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