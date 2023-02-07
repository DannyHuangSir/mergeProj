package com.twfhclife.eservice.web.service;

import java.util.List;
import java.util.Map;

import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.RegisterQuestionVo;
import com.twfhclife.eservice.web.model.UsersVo;

public interface IRegisterUserService {

	/**
	 * 檢查身分證號碼是否已存在
	 * @param user
	 * @return boolean
	 */
	public String checkRegister(UsersVo user);
	
	public List<ParameterVo> getTerms();
	
	public boolean getPolicyByRocId(String policyNo, String rocId);
	
	public List<RegisterQuestionVo> getPolicyQues(String rocId, String policyNo);
	
	public Map<String, String> registerUserData(UsersVo user);
	
	public UsersVo getUserByAccount(String account);
	
	public void updatePassword(String account, String newPassword) throws Exception;
	
	public boolean checkOldSystemUser(String rocId, String password);
	
	public List<UsersVo> getMailPhoneByRocid(String rocId);
	
	public UsersVo getMailPhoneByRocidPolicyNo(String rocId, String policyNo);
}
