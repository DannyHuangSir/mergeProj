package com.twfhclife.generic.model;

import java.util.List;

import com.twfhclife.eservice.api.adm.model.RoleVo;
import com.twfhclife.eservice.api.adm.model.UserDepartmentVo;

public class UserVo {

	private String userId;
	private String email;
	private String mobile;
	private String enabled;
	private String firstName;
	private String lastName;
	private String realmId;
	private String username;
	private String createdTimestamp;
	private String roleId;
	private String roleDescription;
	private String roleName;
	private String client;
	private String password;
	private String rocId;
	private String fbId;
	private String moicaId;
	private String userType;
	private List<UserDepartmentVo> userDepartment;
	private List<RoleVo> userRole;

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

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
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

	public String getRealmId() {
		return realmId;
	}

	public void setRealmId(String realmId) {
		this.realmId = realmId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(String createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRocId() {
		return rocId;
	}

	public void setRocId(String rocId) {
		this.rocId = rocId;
	}

	public String getFbId() {
		return fbId;
	}

	public void setFbId(String fbId) {
		this.fbId = fbId;
	}

	public String getMoicaId() {
		return moicaId;
	}

	public void setMoicaId(String moicaId) {
		this.moicaId = moicaId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public List<UserDepartmentVo> getUserDepartment() {
		return userDepartment;
	}

	public void setUserDepartment(List<UserDepartmentVo> userDepartment) {
		this.userDepartment = userDepartment;
	}

	public List<RoleVo> getUserRole() {
		return userRole;
	}

	public void setUserRole(List<RoleVo> userRole) {
		this.userRole = userRole;
	}

}
