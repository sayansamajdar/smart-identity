package com.techengage.idm.connector;

import com.techengage.idm.dataobject.UserProfile;
import com.techengage.idm.exception.IdmConnectorException;

public interface Connector {
	
	UserProfile registrationInitiation(UserProfile userProfile) throws IdmConnectorException;
	UserProfile registrationFinalize(UserProfile userProfile) throws IdmConnectorException;
	
	void deleteUser(UserProfile userProfile) throws IdmConnectorException;
	
	UserProfile login(UserProfile userProfile) throws IdmConnectorException;

}
