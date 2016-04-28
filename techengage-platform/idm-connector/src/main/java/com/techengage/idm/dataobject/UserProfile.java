package com.techengage.idm.dataobject;

import java.io.Serializable;

public class UserProfile implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private String userName;
	private String userId; //For system use
	private String password;
	
	private String firstName;
	private String lastName;
	private String email;
	private String faceCode;
	
	private boolean registrationSuccess;
	private boolean loginSuccess;
	
	public UserProfile() {}
	
	public UserProfile(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFaceCode() {
		return faceCode;
	}
	public void setFaceCode(String faceCode) {
		this.faceCode = faceCode;
	}

	public boolean isRegistrationSuccess() {
		return registrationSuccess;
	}
	public void setRegistrationSuccess(boolean registrationSuccess) {
		this.registrationSuccess = registrationSuccess;
	}

	public boolean isLoginSuccess() {
		return loginSuccess;
	}
	public void setLoginSuccess(boolean loginSuccess) {
		this.loginSuccess = loginSuccess;
	}

	@Override
	public String toString() {
		return "UserProfile [userName=" + userName + ", userId=" + userId + ", password=" + password + ", firstName="
				+ firstName + ", lastName=" + lastName + ", email=" + email + ", faceCode=" + faceCode
				+ ", registrationSuccess=" + registrationSuccess + ", loginSuccess=" + loginSuccess + "]";
	}
	

}
