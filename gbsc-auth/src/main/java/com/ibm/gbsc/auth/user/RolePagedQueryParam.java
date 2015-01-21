/**
 *
 */
package com.ibm.gbsc.auth.user;

import com.ibm.gbsc.common.vo.PagedQueryParam;

/**
 * @author fanjingxuan
 *
 */
public class RolePagedQueryParam extends PagedQueryParam {
	/**
	 *
	 */
	private static final long serialVersionUID = -277271766889896225L;

	private String roleName;

	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName
	 *            the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
