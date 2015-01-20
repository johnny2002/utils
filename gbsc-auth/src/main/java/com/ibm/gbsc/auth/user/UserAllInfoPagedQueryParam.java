/**
 * 
 */
package com.ibm.gbsc.auth.user;

import com.ibm.banking.framework.dto.PagedQueryParam;

/**
 * @author xushigang
 * 
 */
public class UserAllInfoPagedQueryParam extends PagedQueryParam {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5825480810924578562L;
	/**
	 * 
	 */

	private String userId;// 用户编号
	private String userName;// 用户姓名
	private String orgCode;// 组织编码
	private String roleId;// 角色编码
	private String permissionType;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(String permissionType) {
		this.permissionType = permissionType;
	}

}
