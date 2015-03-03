/**
 *
 */
package com.ibm.gbsc.auth.resource;

import java.util.Collection;
import java.util.List;

import com.ibm.gbsc.auth.model.Resource;
import com.ibm.gbsc.auth.model.Role;

/**
 * @author fanjingxuan
 *
 */
public interface ResourceService {
	/**
	 * 获取全部的资源.
	 *
	 * @return resources.
	 */
	List<Resource> getAllResource();

	/**
	 * 根据roleId删除角色和资源的关系.
	 *
	 * @param roleId
	 *            role
	 * @param operationType
	 *            operationType.
	 */
	void delRoleResByRoleId(String roleId, int operationType);

	/**
	 * @param roles
	 * @return
	 */
	List<Resource> getResourceByRoles(Collection<Role> roles);

}
