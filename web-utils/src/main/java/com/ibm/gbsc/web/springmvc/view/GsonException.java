package com.ibm.gbsc.web.springmvc.view;

import java.io.Serializable;

/**
 * @author yangyz
 *
 */
public class GsonException implements Serializable {

	private static final long serialVersionUID = 3475690128786390445L;

	private String errMsg;

	/**
	 * @param errMsg
	 *            errMsg.
	 */
	public GsonException(String errMsg) {
		this.errMsg = errMsg;
	}

	/**
	 * @return string.
	 */
	public String getErrMsg() {
		return errMsg;
	}

	/**
	 * @param errMsg
	 *            is to set value.
	 */
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
}
