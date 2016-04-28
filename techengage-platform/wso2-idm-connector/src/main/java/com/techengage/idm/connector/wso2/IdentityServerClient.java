package com.techengage.idm.connector.wso2;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.transport.http.HTTPConstants;
import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;
import org.wso2.carbon.authenticator.stub.LogoutAuthenticationExceptionException;
//import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;
import org.wso2.carbon.um.ws.api.WSRealmBuilder;
import org.wso2.carbon.user.core.UserRealm;
import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.UserStoreManager;

import com.techengage.idm.connector.wso2.config.IServerConfig;

public class IdentityServerClient {

    // ONE TIME TASKS WE NEED TO DO BEFORE EXECUTING THIS PROGRAM.

    // TASK - 1 , CREATE a LoginOnly role from IS UI Console
    // ===========================================================
    // 0. Login as admin/admin
    // 1. Go to Users and Roles
    // 2. Click on Roles
    // 3. Add New Role
    // 4. Role Name : loginOnly [please use this name, since it's referred
    // within the code below]
    // 5. Click Next
    // 6. Select only the 'Login' permission
    // 7. Click Next
    // 8. No need to select any users
    // 9. Click Finish

    // TASK - 2 , CREATE a custom claim from IS UI Console
    // ===========================================================
    // 0. Login as admin/admin
    // 1. Go to Claim Management
    // 2. Click on http://wso2.org/claims
    // 3. Click on 'Add New Claim Mapping'
    // 3.1 Display Name : Business Phone
    // 3.2 Description : Business Phone
    // 3.3 Claim Uri : http://wso2.org/claims/businessphone
    // 3.4 Mapped Attribute : http://wso2.org/claims/businessphone
    // 3.5 Support by default : Checked
    // 3.6 The rest can be kept blank

    // private final static String SERVER_URL = "https://localhost:9443/services/";

    private static AuthenticationAdminStub authstub = null;
    private static ConfigurationContext configContext = null;
    private static String cookie = null;
    // private static UserRealm realm;

    private void init() throws AxisFault {
	System.setProperty(IServerConfig.KEY_TRUSTSTORE, IServerConfig.VALUE_TRUSTSTORE);
	System.setProperty(IServerConfig.KEY_TRUSTSTORE_PASSWD, IServerConfig.VALUE_TRUSTSTORE_PASSWD);

	System.out.println("... init ....");
	configContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(IServerConfig.AXIS_CTX_FILE_URI);

	System.out.println(" Stub created ...");
    }

    public IdentityServerClient() {
	try {
	    init();
	} catch (AxisFault e) {
	    e.printStackTrace();
	}
    }

    // +++++ ======================================================

    public List<Object> login(String userName, String passwd) {
	boolean isValidUser = false;
	List<Object> listReturn = new ArrayList<Object>(3);
	try {
	    AuthenticationAdminStub authstub0 = new AuthenticationAdminStub(configContext, IServerConfig.SERVER_URL + IServerConfig.AUTHEN_ADMIN);
	    // Authenticates as a user having rights to add users.
	    isValidUser = authstub0.login(userName, passwd, null);
	    authstub0.logout();

	    if (isValidUser) {
		authstub0.login(IServerConfig.adminId, IServerConfig.adminPasswd, null);
		cookie = (String) authstub0._getServiceClient().getServiceContext().getProperty(HTTPConstants.COOKIE_STRING);

		UserRealm realm1 = WSRealmBuilder.createWSRealm(IServerConfig.SERVER_URL, cookie, configContext);
		UserStoreManager storeManager = realm1.getUserStoreManager();
		try {
		    // storeManager.addUser(userName, passwd, null, null, null);
		    String uid = storeManager.getUserClaimValue(userName, IServerConfig.CLAIM_URI_USER_ID, null);
		    String faceCode = storeManager.getUserClaimValue(userName, IServerConfig.CLAIM_URI_FACECD, null);

		    String firstName = storeManager.getUserClaimValue(userName, IServerConfig.CLAIM_URI_GIVEN_NAME, null);
		    String lastName = storeManager.getUserClaimValue(userName, IServerConfig.CLAIM_URI_LAST_NAME, null);
		    String email = storeManager.getUserClaimValue(userName, IServerConfig.CLAIM_URI_EMAIL, null);

		    listReturn.add(uid);
		    listReturn.add(userName);
		    listReturn.add(faceCode);
		    listReturn.add(firstName);
		    listReturn.add(lastName);
		    listReturn.add(email);

		} catch (UserStoreException e) {
		    System.out.println("Unable to retrieve user login info. :" + e);
		}
		authstub0.logout();
	    }
	    System.out.println(isValidUser ? " Login Successful " : "Login Failed!!");

	} catch (Exception e) {
	    e.printStackTrace();
	}
	return listReturn;
    }

    public List<Object> createUser(String userName, String password) {

	String uid = null;
	List<Object> listReturn = new ArrayList<Object>(2);
	AuthenticationAdminStub authstub1 = null;
	try {
	    // init();

	    authstub1 = new AuthenticationAdminStub(configContext, IServerConfig.SERVER_URL + IServerConfig.AUTHEN_ADMIN);

	    // Authenticates as a user having rights to add users.
	    if (authstub1.login(IServerConfig.adminId, IServerConfig.adminPasswd, null)) {
		cookie = (String) authstub1._getServiceClient().getServiceContext().getProperty(HTTPConstants.COOKIE_STRING);

		UserRealm realm1 = WSRealmBuilder.createWSRealm(IServerConfig.SERVER_URL, cookie, configContext);
		UserStoreManager storeManager = realm1.getUserStoreManager();
		boolean registnFlag = false;

		try {
		    storeManager.addUser(userName, password, null, null, null);
		    uid = storeManager.getUserClaimValue(userName, IServerConfig.CLAIM_URI_USER_ID, null);
		    registnFlag = true;
		    System.out.println("The use added successfully to the system, uid=" + uid);
		} catch (UserStoreException e) {
		    System.out.println("The use Not added, may be duplicate user name. :" + e);
		}
		listReturn.add(registnFlag);
		listReturn.add(uid);
	    }

	} catch (UserStoreException e) {
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		authstub1.logout();
		authstub1.cleanup();
	    } catch (RemoteException e) {
		e.printStackTrace();
	    } catch (LogoutAuthenticationExceptionException e) {
		e.printStackTrace();
	    }
	}
	return listReturn;
    }

    public List<Object> updateUser(String userName, String firstName, String lastName, String email, String faceCode) {

	String uid = null;
	List<Object> listReturn = new ArrayList<Object>(2);
	AuthenticationAdminStub authstub1 = null;
	try {
	    // init();

	    authstub1 = new AuthenticationAdminStub(configContext, IServerConfig.SERVER_URL + IServerConfig.AUTHEN_ADMIN);

	    // Authenticates as a user having rights to add users.
	    if (authstub1.login(IServerConfig.adminId, IServerConfig.adminPasswd, null)) {
		cookie = (String) authstub1._getServiceClient().getServiceContext().getProperty(HTTPConstants.COOKIE_STRING);

		UserRealm realm1 = WSRealmBuilder.createWSRealm(IServerConfig.SERVER_URL, cookie, configContext);
		UserStoreManager storeManager = realm1.getUserStoreManager();
		boolean registnFlag = false;

		if (storeManager.isExistingUser(userName)) {

		    Map<String, String> claims = new HashMap<String, String>(5);

		    claims.put(IServerConfig.CLAIM_URI_FACECD, faceCode);
		    claims.put(IServerConfig.CLAIM_URI_GIVEN_NAME, firstName);
		    claims.put(IServerConfig.CLAIM_URI_LAST_NAME, lastName);
		    claims.put(IServerConfig.CLAIM_URI_EMAIL, email);

		    storeManager.updateRoleListOfUser(userName, null, new String[] { IServerConfig.ROLE_LOGIN });

		    // Here we pass null for the profile - so it will use the
		    // default profile.
		    storeManager.setUserClaimValues(userName, claims, null);
		    // storeManager.addUser(userName, password, new String[] { "loginOnly" }, claims, null);

		    uid = storeManager.getUserClaimValue(userName, IServerConfig.CLAIM_URI_USER_ID, null);
		    registnFlag = true;
		    System.out.println("The use updated successfully to the system, uid=" + uid);

		} else {
		    registnFlag = false;
		    System.out.println(" User does not exist");
		}
		listReturn.add(registnFlag);
		listReturn.add(uid);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		authstub1.logout();
		authstub1.cleanup();
	    } catch (RemoteException e) {
		e.printStackTrace();
	    } catch (LogoutAuthenticationExceptionException e) {
		e.printStackTrace();
	    }
	}
	return listReturn;
    }

    public void deleteUser(String userName) {

	AuthenticationAdminStub authstub1 = null;
	try {
	    // init();
	    authstub1 = new AuthenticationAdminStub(configContext, IServerConfig.SERVER_URL + IServerConfig.AUTHEN_ADMIN);

	    // Authenticates as a user having rights to add users.
	    if (authstub1.login(IServerConfig.adminId, IServerConfig.adminPasswd, null)) {
		cookie = (String) authstub1._getServiceClient().getServiceContext().getProperty(HTTPConstants.COOKIE_STRING);

		UserRealm realm1 = WSRealmBuilder.createWSRealm(IServerConfig.SERVER_URL, cookie, configContext);
		UserStoreManager storeManager = realm1.getUserStoreManager();

		try {
		    storeManager.deleteUser(userName);

		} catch (UserStoreException e) {
		    System.out.println("The use Not deleted :" + e);
		}
	    }
	} catch (UserStoreException e) {
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		authstub1.logout();
		authstub1.cleanup();
	    } catch (RemoteException e) {
		e.printStackTrace();
	    } catch (LogoutAuthenticationExceptionException e) {
		e.printStackTrace();
	    }
	}
    }

    // +++++ =======================================================

    // ********************************************************************
    // Stand-alone testing
    // ********************************************************************

    private static String newUser = "prabath2";
    private final static String APP_ID = "myapp";
    private final static String SERVER_URL1 = "https://localhost:9443/services/";

    public List<Object> createUser(String userName, String password, String firstName, String lastName, String email, String faceCode) {

	// System.setProperty("javax.net.ssl.trustStore", "D:/programs_no-del/wso2is-5.1.0/repository/resources/security/wso2carbon.jks");
	// System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
	String uid = null;
	List<Object> listReturn = new ArrayList<Object>(2);
	try {
	    init();

	    // configContext =
	    // ConfigurationContextFactory.createConfigurationContextFromFileSystem("D:/programs_no-del/wso2is-5.1.0/repository/conf/axis2/axis2_client.xml");

	    AuthenticationAdminStub authstub1 = new AuthenticationAdminStub(configContext, IServerConfig.SERVER_URL + "AuthenticationAdmin");

	    // Authenticates as a user having rights to add users.
	    if (authstub1.login(IServerConfig.adminId, IServerConfig.adminPasswd, null)) {
		cookie = (String) authstub1._getServiceClient().getServiceContext().getProperty(HTTPConstants.COOKIE_STRING);

		UserRealm realm1 = WSRealmBuilder.createWSRealm(IServerConfig.SERVER_URL, cookie, configContext);
		UserStoreManager storeManager = realm1.getUserStoreManager();
		boolean registnFlag = false;

		if (!storeManager.isExistingUser(userName)) {
		    // Let's the this user to APP_ID role we just created.

		    // First let's create claims for users.
		    // If you are using a claim that does not exist in default
		    // IS instance,
		    Map<String, String> claims = new HashMap<String, String>(5);

		    // TASK-1 and TASK-2 should be completed by now.
		    // Here I am using an already existing claim

		    claims.put("http://wso2.org/claims/im", faceCode);
		    claims.put("http://wso2.org/claims/givenname", firstName);
		    claims.put("http://wso2.org/claims/lastname", lastName);
		    claims.put("http://wso2.org/claims/emailaddress", email);

		    // Here we pass null for the profile - so it will use the
		    // default profile.
		    storeManager.addUser(userName, password, new String[] { "loginOnly" }, claims, null);
		    uid = storeManager.getUserClaimValue(userName, "urn:scim:schemas:core:1.0:id", null);
		    registnFlag = true;
		    System.out.println("The use added successfully to the system, uid=" + uid);

		} else {
		    registnFlag = false;
		    uid = storeManager.getUserClaimValue(userName, "urn:scim:schemas:core:1.0:id", null);
		    String faceCode1 = storeManager.getUserClaimValue(userName, "http://wso2.org/claims/im", null);
		    System.out.println("The user trying to add - already there in the system. Uid=" + uid + " | FaceCode=" + faceCode1);
		}
		listReturn.add(registnFlag);
		listReturn.add(uid);

		authstub1.logout();
		authstub1.cleanup();
		System.out.println("Cleaned up");
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return listReturn;
    }

    public static void methodOriginal() {

	System.setProperty("javax.net.ssl.trustStore", "D:/programs_no-del/wso2is-5.1.0/repository/resources/security/wso2carbon.jks");
	System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");

	try {
	    configContext = ConfigurationContextFactory
		    .createConfigurationContextFromFileSystem("D:/programs_no-del/wso2is-5.1.0/repository/conf/axis2/axis2_client.xml");

	    authstub = new AuthenticationAdminStub(configContext, SERVER_URL1 + "AuthenticationAdmin");

	    // Authenticates as a user having rights to add users.
	    if (authstub.login("admin", "admin", null)) {
		cookie = (String) authstub._getServiceClient().getServiceContext().getProperty(HTTPConstants.COOKIE_STRING);

		UserRealm realm = WSRealmBuilder.createWSRealm(SERVER_URL1, cookie, configContext);
		UserStoreManager storeManager = realm.getUserStoreManager();

		// Add a new role - with no users - with APP_ID as the role name

		if (!storeManager.isExistingRole(APP_ID)) {

		    storeManager.addRole(APP_ID, null, null);
		    System.out.println("The role added successfully to the system");
		} else {
		    System.out.println("The role trying to add - already there in the system");
		}

		if (!storeManager.isExistingUser(newUser)) {
		    // Let's the this user to APP_ID role we just created.

		    // First let's create claims for users.
		    // If you are using a claim that does not exist in default
		    // IS instance,
		    Map<String, String> claims = new HashMap<String, String>();

		    // TASK-1 and TASK-2 should be completed by now.
		    // Here I am using an already existing claim
		    claims.put("http://wso2.org/claims/businessphone", "0112842302");

		    // Here we pass null for the profile - so it will use the
		    // default profile.
		    storeManager.addUser(newUser, "password", new String[] { APP_ID, "loginOnly" }, null // claims
			    , null);
		    System.out.println("The use added successfully to the system");
		} else {
		    System.out.println("The user trying to add - already there in the system");
		}

		// Now let's see the given user [newUser] belongs to the role
		// APP_ID.
		String[] userRoles = storeManager.getRoleListOfUser(newUser);
		boolean found = false;

		if (userRoles != null) {
		    for (int i = 0; i < userRoles.length; i++) {
			if (APP_ID.equals(userRoles[i])) {
			    found = true;
			    System.out.println("The user is in the required role");
			    break;
			}
		    }
		}

		if (!found) {
		    System.out.println("The user is NOT in the required role");
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

	IdentityServerClient client = new IdentityServerClient();
	// client.methodOriginal();

	// client.login("bijayd", "bd1234");

	client.createUser("amitsen1", "amitsen1");

	// client.updateUser("amitsen", "Amit", "Sen", "asen@gmail.com", "facecd-amit-111");

	// amitsen
	// client.deleteUser("amitsen");

	System.out.println("--Done--");

    }

}