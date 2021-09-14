package com.twfhclife.eservice.api.adm.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.twfhclife.generic.model.Pagination;

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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}