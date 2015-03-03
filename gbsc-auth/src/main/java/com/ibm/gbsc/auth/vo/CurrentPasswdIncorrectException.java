package com.ibm.gbsc.auth.vo;

/**
 * 用户当前的密码输入错误.
 * @author Johnny
 *
 */
public class CurrentPasswdIncorrectException extends RuntimeException {

	/**
	 * sn.
	 */
	private static final long serialVersionUID = 2828548808566781178L;

	/**
	 * default constructor.
	 */
	public CurrentPasswdIncorrectException() {
	}


	/**
	 * @param message message
	 * @param rootCause cause.
	 */
	public CurrentPasswdIncorrectException(String message, Throwable rootCause) {
		super(message, rootCause);
	}

}
