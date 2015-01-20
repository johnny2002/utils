/**
 *
 */
package com.ibm.gbsc.auth.resource;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.gbsc.auth.user.Role;

/**
 * @author fanjingxuan
 *
 */
@Service
@Transactional(readOnly = true)
public class ResourceServiceImpl implements ResourceService {
	Logger log = LoggerFactory.getLogger(this.getClass());

	@PersistenceContext
	EntityManager em;

	@Override
	@SuppressWarnings("unchecked")
	public List<Resource> getAllResource() {
		List<Resource> resList = em.createNamedQuery("Resource.getAll").getResultList();
		initRes(resList);
		return resList;
	}

	/**
	 * @param resList
	 *            resource list.
	 */
	private void initRes(List<Resource> resList) {
		for (Resource res : resList) {
			if (res == null) {
				continue;
			}
			if (res.getChildRes() != null) {
				res.getChildRes().size();
				initRes(res.getChildRes());
			}
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void delRoleResByRoleId(String roleId, int operationType) {
		em.createNamedQuery("RoleResource.delByRoleId").setParameter("roleId", roleId).setParameter("operationType", operationType)
		        .executeUpdate();
	}

	@Override
	@Transactional(readOnly = false)
	public void addRoleRes(String roleId, String resId, int optType) {

		Role role = em.getReference(Role.class, roleId);
		Resource resource = em.getReference(Resource.class, resId);
		RoleResource rr = new RoleResource();
		rr.setOperationType(optType);
		rr.setRole(role);
		rr.setResource(resource);
	}
}
