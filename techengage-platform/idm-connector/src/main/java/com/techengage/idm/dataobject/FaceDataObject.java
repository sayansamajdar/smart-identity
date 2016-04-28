package com.techengage.idm.dataobject;

import java.io.Serializable;

public class FaceDataObject implements Serializable {
	
	private static final long serialVersionUID = 2L;
	
	private String userName;
	private String userId; //For system use
	private String password;	
	
	private String email;
	private String faceCode;
	
	private boolean registrationSuccess;
	private boolean loginSuccess;
	
	public FaceDataObject () {}
	
	public FaceDataObject (String userName, String faceCode) {
		this.userName = userName;
		this.faceCode = faceCode;
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
	
	
}
