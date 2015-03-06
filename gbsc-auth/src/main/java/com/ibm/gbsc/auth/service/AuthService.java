/*
 * IBM Corporation.
 * Copyright (c) 2014 All Rights Reserved.
 */

package com.ibm.gbsc.auth.service;

import java.util.Collection;
import java.util.List;

import com.ibm.gbsc.auth.model.Function;
import com.ibm.gbsc.auth.model.Organization;
import com.ibm.gbsc.auth.model.Resource;
import com.ibm.gbsc.auth.model.Role;
import com.ibm.gbsc.auth.model.User;
import com.ibm.gbsc.auth.vo.UserPagedQueryParam;
import com.ibm.gbsc.common.vo.PagedQueryResult;

/**
 * 类作用：
 *
 * @author Johnny@cn.ibm.com 使用说明：
 */
public interface AuthService {
	/**
	 * 主键取role.
	 *
	 * @param auth
	 *            auth
	 * @return role
	 */
	Role getRole(String auth);

	/**
	 * @return all roles in the system
	 */
	List<Role> getAllRoles();

	/**
	 * @param code
	 *            user code
	 * @return user
	 */
	User getUser(String code);

	/**
	 * @param param
	 *            param
	 * @return user with paging.
	 */
	PagedQueryResult<User> searchUser(UserPagedQueryParam param);

	/**
	 * 保存用户，同时保存用户所属的角色列表(主要用途).
	 *
	 * @param user
	 *            user
	 */
	void updateUser(User user);

	/**
	 * 新增用户
	 *
	 * @param user
	 *            user
	 */
	void saveUser(User user);

	/**
	 * 按层级查找机构，同时取得所有的下级机构树.
	 *
	 * @param level
	 *            level
	 * @return 机构树，及角色
	 */
	List<Organization> getOrgTreeByLevel(int level);

	/**
	 * 根据组织结构的code查询该组织.
	 *
	 *
	 * @param orgCode
	 *            机构代码
	 * @param loadRoles
	 *            是否要加载roles
	 * @param loadUsers
	 *            是否要加载users;
	 * @param loadChildren
	 *            是否要加载children;
	 * @return
	 */
	Organization getOrganization(String orgCode, boolean loadRoles, boolean loadUsers, boolean loadChildren);

	/**
	 * 根据组织结构的code删除该组织.
	 *
	 * @param orgCode
	 *            org code
	 */
	void delOrganization(String orgCode);

	/**
	 * @param oldOrg
	 */
	void saveOrganzation(Organization org);

	/**
	 * @param roles
	 * @return
	 */
	List<Resource> getResourceByRoles(Collection<Role> roles);

	/**
	 * @return
	 */
	List<Function> getFunctionTree();

	/**
	 * @return
	 */
	List<Function> getAllFunctions();

	/**
	 * @param role
	 * @return
	 */
	List<Function> getFunctionsByRole(Role role);

	/**
	 * @param role
	 * @param funcs
	 */
	void saveRoleFunctions(Role role, Collection<Function> funcs);

}
