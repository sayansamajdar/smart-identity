package com.techengage.idm.exception;

public class IdmConnectorException extends RuntimeException {

	private static final long serialVersionUID = 3L;

	public IdmConnectorException(String message){
		super(message);
	}

	public IdmConnectorException(Throwable cause){
		super(cause);
	}

	public IdmConnectorException(String message, Throwable cause){
		super(message, cause);
	}

	public IdmConnectorException(String message, Throwable cause, boolean enableSuppression, 
			boolean writableStackTrace)	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
