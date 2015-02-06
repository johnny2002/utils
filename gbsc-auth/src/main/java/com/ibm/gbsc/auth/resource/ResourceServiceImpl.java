/**
 *
 */
package com.ibm.gbsc.auth.resource;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.gbsc.common.dao.JpaDao;

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
	@Inject
	JpaDao jpaDao;

	@Override
	@SuppressWarnings("unchecked")
	public List<Resource> getAllResource() {
		List<Resource> resList = em.createNamedQuery("Resource.getAll").getResultList();
		initRes(resList);
		return resList;
	}

	@Override
	public List<Resource> getResourceByRoles(Collection<Role> roles) {
		StringBuilder jql = new StringBuilder(512).append("select r From Resource r, IN (r.roles) role where role.code in ('-1'");
		for (int i = 0; i < roles.size(); i++) {
			jql.append(", ?").append(i);
		}
		jql.append(")");
		TypedQuery<Resource> roleQuery = em.createQuery(jql.toString(), Resource.class);
		int i = 0;
		for (Role role : roles) {
			roleQuery.setParameter(i++, role.getCode());
		}
		List<Resource> resList = jpaDao.executeQuery(roleQuery, true, false);
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
			if (res.getChildren() != null) {
				res.getChildren().size();
				initRes(res.getChildren());
			}
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void delRoleResByRoleId(String roleId, int operationType) {
		em.createNamedQuery("RoleResource.delByRoleId").setParameter("roleId", roleId).setParameter("operationType", operationType)
		        .executeUpdate();
	}

}
