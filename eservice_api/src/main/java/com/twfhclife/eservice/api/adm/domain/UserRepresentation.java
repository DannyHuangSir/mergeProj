package com.twfhclife.eservice.api.adm.domain;

/**
 * UserRepresentation.
 * 
 * @author tcMarcus
 * @version 1.0
 */
public class UserRepresentation {

	private Long createdTimestamp;

	private String email;

	private boolean emailVerified;

	private boolean enabled;

	private String firstName;

	private String lastName;

	private String id;

	private String serviceAccountClientId;

	private String userName;

	public Long getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Long createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getServiceAccountClientId() {
		return serviceAccountClientId;
	}

	public void setServiceAccountClientId(String serviceAccountClientId) {
		this.serviceAccountClientId = serviceAccountClientId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
