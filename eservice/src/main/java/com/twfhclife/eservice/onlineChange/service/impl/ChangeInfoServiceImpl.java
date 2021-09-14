package com.twfhclife.eservice.onlineChange.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.onlineChange.service.IChangeInfoService;
import com.twfhclife.eservice.web.dao.UsersDao;

@Service
public class ChangeInfoServiceImpl implements IChangeInfoService {
	
	@Autowired
	private UsersDao usersDao;
	
	@Override
	public void updateUserType(String userId, String userType){
		usersDao.updateUserType(userId, userType);
	}
}