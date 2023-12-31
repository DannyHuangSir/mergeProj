package com.twfhclife.jd.web.service;

import com.twfhclife.jd.web.model.ParameterVo;
import com.twfhclife.jd.web.model.UsersVo;

import java.util.List;
import java.util.Map;

public interface IRegisterUserService {
	

	public List<ParameterVo> getTerms();
	
	public Map<String, String> registerUserData(UsersVo user);
	
	public UsersVo getUserByAccount(String account);
	
	public String updatePassword(String account, String newPassword, String enable) throws Exception;
	
    int incLoginFailCount(String userId);

	int updateLoginSuccess(String userId);

	String changePassword(String account, String newPassword);
}
