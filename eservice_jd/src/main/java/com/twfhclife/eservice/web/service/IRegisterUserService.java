package com.twfhclife.eservice.web.service;

import com.twfhclife.eservice.web.model.ParameterVo;
import com.twfhclife.eservice.web.model.RegisterQuestionVo;
import com.twfhclife.eservice.web.model.UsersVo;

import java.util.List;
import java.util.Map;

public interface IRegisterUserService {
	
	/**
	 * 用身份證號取得用戶資料
	 * 
	 * @param rocId 用戶證號
	 * @return UsersVo
	 */
	public UsersVo getUserByRocId(String rocId);

	/**
	 * 檢查身分證號碼是否已存在
	 * @param user
	 * @return boolean
	 */
	public String checkRegister(UsersVo user);
	
	public List<ParameterVo> getTerms();
	
	public boolean getPolicyByRocId(String policyNo, String rocId);
	
	public Map<String, String> registerUserData(UsersVo user);
	
	public UsersVo getUserByAccount(String account);
	
	public void updatePassword(String account, String newPassword) throws Exception;
	
	public UsersVo getUserByFbId(String fbId);
	
	public UsersVo getUserByCardSn(String cardSN);
	
	public boolean checkOldSystemUser(String rocId, String password);
	
	public List<UsersVo> getMailPhoneByRocid(String rocId);
	
	public UsersVo getMailPhoneByRocidPolicyNo(String rocId, String policyNo);
}
