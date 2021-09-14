package com.twfhclife.eservice.api.adm.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.twfhclife.generic.model.Pagination;

public class KeycloakRoleVo extends Pagination {
	private String id;
	private String clientRealmConstraint;
	private String clientRole;
	private String description;
	private String name;
	private String realmId;
	private String client;
	private String realm;
	private String scopeParamRequired;
	
	private String roleId;
	private String roleName;
	private String parentRoleId;
	private String departmentId;
	private String departmentName;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	
	private String systemId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClientRealmConstraint() {
		return clientRealmConstraint;
	}

	public void setClientRealmConstraint(String clientRealmConstraint) {
		this.clientRealmConstraint = clientRealmConstraint;
	}

	public String getClientRole() {
		return clientRole;
	}

	public void setClientRole(String clientRole) {
		this.clientRole = clientRole;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRealmId() {
		return realmId;
	}

	public void setRealmId(String realmId) {
		this.realmId = realmId;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getScopeParamRequired() {
		return scopeParamRequired;
	}

	public void setScopeParamRequired(String scopeParamRequired) {
		this.scopeParamRequired = scopeParamRequired;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getParentRoleId() {
		return parentRoleId;
	}

	public void setParentRoleId(String parentRoleId) {
		this.parentRoleId = parentRoleId;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
