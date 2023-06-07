package com.twfhclife.eservice.api.elife.service;

import java.util.List;

import com.twfhclife.eservice.api.elife.domain.RegisterUserVo;

/**
 * 台銀人壽保單服務申請帳號
 */
public interface IRegisterUserService {

	public List<RegisterUserVo> getUserMailPhoneByRocid(String rocId);

	public RegisterUserVo getLilomsAmtByPolicyNo(String policyNo);
	
	public RegisterUserVo getUserMailPhoneByRocidAndPolicyNo(String rocId, String policyNo);
	
	public List<RegisterUserVo>  CheckLipmInsuNoByPolicyNo(String rocId, String policyNo);
}
