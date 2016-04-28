package com.techengage.registration.idm;

public class IdmRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 3L;

    public IdmRuntimeException(String message) {
	super(message);
    }

    public IdmRuntimeException(Throwable cause) {
	super(cause);
    }

    public IdmRuntimeException(String message, Throwable cause) {
	super(message, cause);
    }

    public IdmRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
    }

}
