/**
 *
 */
package com.ibm.gbsc.auth.user;

/**
 * user state defination.
 * 
 * @author Johnny
 *
 */
public enum UserState {
	/**
	 * 正常.
	 */
	NORMAL,
	/**
	 * 注销.
	 */
	CLOSED,
	/**
	 * 锁定.
	 */
	LOCKED
}
