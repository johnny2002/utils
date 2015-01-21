/**
 *
 */
package com.ibm.gbsc.auth.user;

import com.ibm.gbsc.common.vo.PagedQueryParam;

/**
 * @author Johnny
 *
 */
public class UserPagedQueryParam extends PagedQueryParam {
	/**
	 *
	 */
	private static final long serialVersionUID = -277271766889896225L;
	private String name;
	private String orgName;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the organization
	 */
	public String getOrgName() {
		return orgName;
	}

	/**
	 * @param organization
	 *            the organization to set
	 */
	public void setOrgName(String organization) {
		this.orgName = organization;
	}
}
