package com.techengage.idm.connector.wso2.config;


public interface IServerConfig {
	
    	String WSO2_HOME = "C:/techngage/wso2";
    
	String SERVER_URL = "https://localhost:9443/services/";
	
	String adminId = "admin";
	String adminPasswd = "admin";
	
	String KEY_TRUSTSTORE = "javax.net.ssl.trustStore";
	String VALUE_TRUSTSTORE = WSO2_HOME + "/wso2is-5.1.0/repository/resources/security/wso2carbon.jks";
	
	String KEY_TRUSTSTORE_PASSWD = "javax.net.ssl.trustStorePassword";
	String VALUE_TRUSTSTORE_PASSWD = "wso2carbon";
	
	String AXIS_CTX_FILE_URI = WSO2_HOME + "/wso2is-5.1.0/repository/conf/axis2/axis2_client.xml";
	
	String AUTHEN_ADMIN = "AuthenticationAdmin";
	
	String CLAIM_URI_USER_ID =  "urn:scim:schemas:core:1.0:id";
	String CLAIM_URI_FACECD =  "http://wso2.org/claims/im";
		
	
	String CLAIM_URI_GIVEN_NAME = "http://wso2.org/claims/givenname";
	String CLAIM_URI_LAST_NAME = "http://wso2.org/claims/lastname";
	String CLAIM_URI_EMAIL = "http://wso2.org/claims/emailaddress";
		
	String ROLE_LOGIN = "loginOnly";
	
}
