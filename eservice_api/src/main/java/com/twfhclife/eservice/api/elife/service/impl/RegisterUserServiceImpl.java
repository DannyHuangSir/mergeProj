package com.twfhclife.eservice.api.elife.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twfhclife.eservice.api.elife.dao.RegisterUserDao;
import com.twfhclife.eservice.api.elife.domain.RegisterUserVo;
import com.twfhclife.eservice.api.elife.service.IRegisterUserService;

@Service
public class RegisterUserServiceImpl implements IRegisterUserService{

	@Autowired
	private RegisterUserDao dao;
	
	@Override
	public List<RegisterUserVo> getUserMailPhoneByRocid(String rocId) {
		return dao.getUserMailPhoneByRocid(rocId);
	}

	@Override
	public RegisterUserVo getLilomsAmtByPolicyNo(String policyNo) {
		return dao.getLilomsAmtByPolicyNo(policyNo);
	}

	@Override
	public RegisterUserVo getUserMailPhoneByRocidAndPolicyNo(String rocId, String policyNo) {
		return dao.getUserMailPhoneByRocidAndPolicyNo(rocId, policyNo);
	}

	@Override
	public List<RegisterUserVo>  CheckLipmInsuNoByPolicyNo(String rocId, String policyNo) {
		return dao.CheckLipmInsuNoByPolicyNo(rocId, policyNo);
	}

}
