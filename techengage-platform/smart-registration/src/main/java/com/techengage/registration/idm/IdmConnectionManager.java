package com.techengage.registration.idm;

import com.techengage.idm.dataobject.UserProfile;

public interface IdmConnectionManager {

    UserProfile registrationInitiation(UserProfile userProfile) throws IdmRuntimeException;

    Boolean registrationFinalize(UserProfile userProfile) throws IdmRuntimeException;

    UserProfile login(UserProfile userProfile) throws IdmRuntimeException;

    void rollBackIdmUserRegistration(String userLoginId) throws IdmRuntimeException;

}
