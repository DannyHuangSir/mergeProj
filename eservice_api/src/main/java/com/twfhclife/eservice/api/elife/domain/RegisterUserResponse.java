package com.twfhclife.eservice.api.elife.domain;

import java.util.List;

public class RegisterUserResponse {
	
	private RegisterUserVo registerUserVo;
	
	private List<RegisterUserVo> registerUserList;

	public List<RegisterUserVo> getRegisterUserList() {
		return registerUserList;
	}

	public void setRegisterUserList(List<RegisterUserVo> registerUserList) {
		this.registerUserList = registerUserList;
	}

	public RegisterUserVo getRegisterUserVo() {
		return registerUserVo;
	}

	public void setRegisterUserVo(RegisterUserVo registerUserVo) {
		this.registerUserVo = registerUserVo;
	}	
	
}
