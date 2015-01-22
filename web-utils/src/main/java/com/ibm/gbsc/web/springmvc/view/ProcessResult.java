/*
 * IBM Corporation.
 * Copyright (c) 2014 All Rights Reserved.
 */

package com.ibm.gbsc.web.springmvc.view;

/**
 * 类作用：
 *
 * @author Johnny@cn.ibm.com 使用说明：
 */
public class ProcessResult {
	private boolean success;
	private String message;

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 *
	 */
	public ProcessResult() {
	}

	/**
	 * @param success
	 * @param message
	 */
	public ProcessResult(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}

	/**
	 * @param success
	 *            the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
