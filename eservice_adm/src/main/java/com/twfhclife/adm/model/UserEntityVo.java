package com.twfhclife.adm.model;

import com.twfhclife.generic.model.Pagination;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

public class UserEntityVo extends Pagination {
	
	/**  */
	private String id;
	/**  */
	private String email;
	/**  */
	private String emailConstraint;
	/**  */
	private Boolean emailVerified;
	/**  */
	private Boolean enabled;
	/**  */
	private String federationLink;
	/**  */
	private String firstName;
	/**  */
	private String lastName;
	/**  */
	private String realmId;
	/**  */
	private String username;
	/**  */
	private BigDecimal createdTimestamp;
	/**  */
	private String serviceAccountClientLink;
	/**  */
	private BigDecimal notBefore;
	
	private String systemId;
	
	private String rocId;
	
	private String department;
	
	private String jobTitle;
	
	private String roleName;

	/**
	 * 通路别
	 */
	private String parentDep;

	private String branchId;

	private String userId;
	//系統賬號狀態
	private String status;

	private String salespersonId;

	private String roleId;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public void setSalespersonId(String salespersonId) {
		this.salespersonId = salespersonId;
	}

	public String getSalespersonId() {
		return salespersonId;
	}

	public String getStatus() {
		return status;
	}



	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setParentDep(String parentDep) {
		this.parentDep = parentDep;
	}

	public String getParentDep() {
		return parentDep;
	}

	public String getRegisterNum() {
		return registerNum;
	}

	/**
	 * 登录字号
	 */
	private String registerNum;



	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmailConstraint() {
		return this.emailConstraint;
	}
	
	public void setEmailConstraint(String emailConstraint) {
		this.emailConstraint = emailConstraint;
	}
	
	public Boolean getEmailVerified() {
		return this.emailVerified;
	}
	
	public void setEmailVerified(Boolean emailVerified) {
		this.emailVerified = emailVerified;
	}
	
	public Boolean getEnabled() {
		return this.enabled;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public String getFederationLink() {
		return this.federationLink;
	}
	
	public void setFederationLink(String federationLink) {
		this.federationLink = federationLink;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getRealmId() {
		return this.realmId;
	}
	
	public void setRealmId(String realmId) {
		this.realmId = realmId;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public BigDecimal getCreatedTimestamp() {
		return this.createdTimestamp;
	}
	
	public void setCreatedTimestamp(BigDecimal createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	
	public String getServiceAccountClientLink() {
		return this.serviceAccountClientLink;
	}
	
	public void setServiceAccountClientLink(String serviceAccountClientLink) {
		this.serviceAccountClientLink = serviceAccountClientLink;
	}
	
	public BigDecimal getNotBefore() {
		return this.notBefore;
	}
	
	public void setNotBefore(BigDecimal notBefore) {
		this.notBefore = notBefore;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getRocId() {
		return rocId;
	}

	public void setRocId(String rocId) {
		this.rocId = rocId;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}