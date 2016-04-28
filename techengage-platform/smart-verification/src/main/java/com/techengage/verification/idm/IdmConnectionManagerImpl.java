package com.techengage.verification.idm;

import com.techengage.idm.connector.Connector;
import com.techengage.idm.connector.wso2.Wso2IdmConnector;
import com.techengage.idm.dataobject.UserProfile;
import com.techengage.idm.exception.IdmConnectorException;

public class IdmConnectionManagerImpl implements IdmConnectionManager {

    private Connector connector = new Wso2IdmConnector();

    private UserProfile storedProfileData;

    /**
     * Args sequence : userName, passwd, firstName, lastName, email return : userName
     */
    public UserProfile registrationInitiation(UserProfile userProfile) throws IdmRuntimeException {

	try {
	    userProfile = connector.registrationInitiation(userProfile);

	    storedProfileData = userProfile;
	} catch (IdmConnectorException e) {
	    throw new IdmRuntimeException("Registration initiation failed!!", e);
	} catch (Exception e) {
	    throw new IdmRuntimeException("Registration initiation failed!!", e);
	}
	return userProfile;
    }

    /**
     * Args sequence : userName, personId(face code) returns : Success or Failure flag
     */
    public Boolean registrationFinalize(UserProfile userProfile) throws IdmRuntimeException {
	boolean registrationSuccess = false;
	try {

	    if (userProfile != null && storedProfileData != null) {

		userProfile.setFirstName(storedProfileData.getFirstName());
		userProfile.setLastName(storedProfileData.getLastName());
		userProfile.setEmail(storedProfileData.getEmail());

		System.out.println(" Face registration process complete.. ");
		userProfile = connector.registrationFinalize(userProfile);
		registrationSuccess = userProfile.isRegistrationSuccess();

	    } else {
		throw new IdmConnectorException("Registration finalization failed!!");
	    }
	} catch (IdmConnectorException e) {
	    rollBackIdmUserRegistration(userProfile.getUserName());
	    e.printStackTrace();
	    throw new IdmRuntimeException("Registration finalization failed!!", e);
	} catch (Exception e) {
	    rollBackIdmUserRegistration(userProfile.getUserName());
	    e.printStackTrace();
	    throw new IdmRuntimeException("Registration finalization failed!!", e);
	}
	return registrationSuccess;
    }

    public void rollBackIdmUserRegistration(String userLoginId) throws IdmRuntimeException {
	try {
	    UserProfile userProfile = new UserProfile(userLoginId, null);
	    connector.deleteUser(userProfile);
	} catch (IdmConnectorException e1) {
	    e1.printStackTrace();
	}
    }

    /**
     * Args: userLoginId, password returns : personId(face code)
     */
    public UserProfile login(UserProfile userProfile) throws IdmRuntimeException {
	String faceCode = null;
	try {
	    userProfile = connector.login(userProfile);
	    faceCode = userProfile.getFaceCode();
	    userProfile.setLoginSuccess(faceCode != null);
	} catch (IdmConnectorException e) {
	    e.printStackTrace();
	    throw new IdmRuntimeException("IDM_LOGIN_FAILED", e);
	}
	return userProfile;
    }

}