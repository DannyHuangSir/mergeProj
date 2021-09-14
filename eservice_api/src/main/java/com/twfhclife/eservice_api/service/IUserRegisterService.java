package com.twfhclife.eservice_api.service;

import java.util.List;
import java.util.Map;

import com.twfhclife.eservice.api.adm.model.ParameterCategoryVo;
import com.twfhclife.generic.domain.ResetPwdRequest;
import com.twfhclife.generic.model.UserVo;

/**
 * 使用者註冊服務類別
 * 
 * @author YLW
 *
 */
public interface IUserRegisterService {
	
	/**
	 * 新增使用者帳戶
	 * @param user
	 * @return
	 */
	public Map<String, String> createEserviceUser(UserVo user);

	/**
	 * 
	 * @param user
	 * @return
	 */
	public int deleteEserviceUser(UserVo user);
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	public List<ParameterCategoryVo> updateEserviceUser(UserVo user);
	
	/**
	 * 重設密碼
	 * @param username
	 * @param password
	 * @return
	 */
	public String resetEservicePassword(ResetPwdRequest req);
	
	
}
