/**
 * 
 */
package com.ibm.gbsc.auth.resource;

import java.util.List;

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
	 * @param operationType operationType.
	 */
	void delRoleResByRoleId(String roleId, int operationType);

	/**
	 * 
	 * 增加roleId 某一资源权限.
	 * 
	 * @param roleId
	 *            role
	 * @param resId
	 *            resid
	 * @param optType
	 *            operation type
	 */
	void addRoleRes(String roleId, String resId, int optType);
}
