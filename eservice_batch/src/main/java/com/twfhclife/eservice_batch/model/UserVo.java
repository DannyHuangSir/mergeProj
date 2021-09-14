package com.twfhclife.eservice_batch.model;

public class UserVo {

	private String userId;
	private String email;
	private String mobile;
	private String firstName;
	private String lastName;
	private String realm;
	private String username;
	private String rocId;
	private String lastChangPasswordDate;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRocId() {
		return rocId;
	}

	public void setRocId(String rocId) {
		this.rocId = rocId;
	}

	public String getLastChangPasswordDate() {
		return lastChangPasswordDate;
	}

	public void setLastChangPasswordDate(String lastChangPasswordDate) {
		this.lastChangPasswordDate = lastChangPasswordDate;
	}

}
