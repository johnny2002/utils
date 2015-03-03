package com.ibm.gbsc.auth.vo;

/**
 * @author Johnny
 */
public class UserNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6735776404280900401L;

	/**
	 * 
	 */
	public UserNotFoundException() {
		super();
	}

	/**
	 * @param message
	 *            message
	 * @param cause
	 *            cause
	 */
	public UserNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 *            message
	 */
	public UserNotFoundException(String message) {
		super(message);
	}

}
