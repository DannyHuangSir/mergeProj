package com.twfhclife.eservice.api.adm.domain;

/**
 * User authorization.
 * 
 * @author tcMarcus
 * @version 1.0
 */
public class UserAuth {

	/**
	 * 使用者代碼.
	 */
	private String userId;

	/**
	 * 使用者帳號.
	 */
	private String userName;

	/**
	 * 名.
	 */
	private String firstName;

	/**
	 * 姓.
	 */
	private String lastName;

	/**
	 * 功能.
	 */
	private Function funcionAuth;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Function getFuncionAuth() {
		return funcionAuth;
	}

	public void setFuncionAuth(Function funcionAuth) {
		this.funcionAuth = funcionAuth;
	}

}
