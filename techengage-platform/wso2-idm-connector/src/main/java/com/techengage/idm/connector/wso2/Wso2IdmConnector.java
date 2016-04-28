package com.techengage.idm.connector.wso2;

import java.util.List;

import com.techengage.idm.connector.Connector;
import com.techengage.idm.dataobject.UserProfile;
import com.techengage.idm.exception.IdmConnectorException;

public class Wso2IdmConnector implements Connector {

    private IdentityServerClient wso2Client;

    private void init() {
	wso2Client = wso2Client == null ? new IdentityServerClient() : wso2Client;
    }

    public UserProfile registrationInitiation(UserProfile userProfile) throws IdmConnectorException {

	init();

	List<Object> listReturn = wso2Client.createUser(userProfile.getUserName(), userProfile.getPassword());

	userProfile.setRegistrationSuccess((Boolean) listReturn.get(0));
	userProfile.setUserId((String) listReturn.get(1));

	return userProfile;
    }

    public UserProfile registrationFinalize(UserProfile userProfile) throws IdmConnectorException {

	init();

	List<Object> listReturn = wso2Client.updateUser(userProfile.getUserName(), userProfile.getFirstName(), userProfile.getLastName(),
		userProfile.getEmail(), userProfile.getFaceCode());
	System.out.println("listReturn ===== " + listReturn);
	userProfile.setRegistrationSuccess((Boolean) listReturn.get(0));

	// TO-Test rollback scenario ..
	// if (true) {
	// throw new IdmConnectorException(" IDM exception");
	// }
	return userProfile;
    }

    public void deleteUser(UserProfile userProfile) throws IdmConnectorException {
	init();
	wso2Client.deleteUser(userProfile.getUserName());
    }

    public UserProfile login(UserProfile userProfile) throws IdmConnectorException {
	init();
	List<Object> listReturn = wso2Client.login(userProfile.getUserName(), userProfile.getPassword());

	userProfile.setUserId((String) listReturn.get(0));
	userProfile.setUserName((String) listReturn.get(1));
	userProfile.setFaceCode((String) listReturn.get(2));
	userProfile.setFirstName((String) listReturn.get(3));
	userProfile.setLastName((String) listReturn.get(4));
	userProfile.setEmail((String) listReturn.get(5));

	return userProfile;
    }

}
