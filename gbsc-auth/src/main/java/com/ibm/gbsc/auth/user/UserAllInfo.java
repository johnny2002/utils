package com.ibm.gbsc.auth.user;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ibm.banking.framework.dto.BaseVO;

/**
 * 该类是用户所有信息实体类
 * 
 * @author xushigang
 * 
 */
@Entity
@Table(name = "RI_NV_AUTH_USER_ALL_INFO")
public class UserAllInfo implements Serializable, BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String userId;// 用户编号
	private String userName;// 用户姓名
	private String orgCode;// 组织编码
	private String orgName;// 组织名称
	private String roleId;// 角色编码
	private String roleName;// 角色名称
	private String permissionId;// 权限编码
	private String permissionName;// 权限名称
	private PermissionType permissionType;// 权限类型0--大类；1--小类

	@Id
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

	@Id
	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	@Id
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Id
	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	@Enumerated(EnumType.ORDINAL)
	public PermissionType getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(PermissionType permissionType) {
		this.permissionType = permissionType;
	}

	@Transient
	public Serializable getId() {
		return null;
	}

}
