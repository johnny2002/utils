package com.ibm.gbsc.auth.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.gbsc.auth.resource.RoleResource;
import com.ibm.gbsc.common.vo.PagedQueryResult;

/**
 * @author Johnny
 */
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
	Logger log = LoggerFactory.getLogger(getClass());
	@PersistenceContext
	EntityManager em;

	/** {@inheritDoc} */
	@Override
	@SuppressWarnings("unchecked")
	public List<Role> getAllRoles() {
		List<Role> roleList = em.createNamedQuery("Role.getAll").getResultList();
		return roleList;
	}

	/** {@inheritDoc} */
	@Override
	public User getUser(String code) {
		log.debug(code);
		User user = em.find(User.class, code);
		if (user == null) {
			throw new UserNotFoundException("指定的用户不存在：" + code);
		}
		user.getRoles().size();
		List<Organization> parents = user.getDepartments();
		parents.size();
		for (Organization org : parents) {
			org.getRoles().size();
			if (org.getParent() != null) {
				org.getParent();
			}
		}
		return user;
	}

	/** {@inheritDoc} */
	@Override
	public Role getRole(String auth) {
		Role role = em.find(Role.class, auth);
		return role;
	}

	/** {@inheritDoc} */
	@Override
	public PagedQueryResult<User> searchUser(UserPagedQueryParam param) {
		// Criteria criteria = dao.getSession().createCriteria(User.class);
		// if (StringUtils.isNotBlank(param.getName())) {
		// criteria.add(Restrictions.ilike("displayName",
		// param.getName().trim().toLowerCase(), MatchMode.ANYWHERE));
		// }
		// /*
		// * if (StringUtils.isNotBlank(param.getOrgName())){
		// * criteria.createAlias("org", "org");
		// * criteria.createAlias("org.parent", "porg");
		// * criteria.add(Restrictions.or(Restrictions.ilike("org.name",
		// * param.getOrgName().trim(), MatchMode.ANYWHERE),
		// * Restrictions.ilike("porg.name", param.getOrgName().trim(),
		// * MatchMode.ANYWHERE))); }
		// */
		// criteria.addOrder(Order.asc("displayName"));
		// PagedQueryResult<User> rst = dao.executePagingQuery(criteria, param);
		// for (User user : rst.getDatas()) {
		// if (user.getOrg() != null) {
		// Hibernate.initialize(user.getOrg().getParent());
		// }
		// }
		return null;
	}

	/** {@inheritDoc} */
	@Override
	@Transactional(readOnly = false)
	public void updateUser(User user) {
		log.info("saveUser {}", user.getCode());
		// 由于用户所属部门不在本功能中更新，所以此处还原用户部门信息，以免被清除
		User oldUser = em.find(User.class, user.getCode());
		user.setDepartments(oldUser.getDepartments());
		em.merge(user);
	}

	/** {@inheritDoc} */
	@Override
	public List<Organization> getOrgTreeByLevel(int level) {
		Query queryOrgByLevelType = em.createNamedQuery("Organization.getByLevel");
		queryOrgByLevelType.setParameter("level", level);
		@SuppressWarnings("unchecked")
		List<Organization> list = queryOrgByLevelType.getResultList();
		initOrgList(list);
		return list;

	}

	/**
	 * @param orgs
	 *            orgs
	 */
	private void initOrgList(List<Organization> orgs) {
		for (Organization org : orgs) {
			org.getRoles().size();
			initOrgList(org.getChildOrgs());
		}

	}

	/** {@inheritDoc} */
	@Override
	public List<Organization> getOrganizationByLevelType(int level, String type) {
		Query queryOrgByLevelType = em.createNamedQuery("Organization.getByLevelType");
		queryOrgByLevelType.setParameter("level", level);
		queryOrgByLevelType.setParameter("type", type);
		@SuppressWarnings("unchecked")
		List<Organization> list = queryOrgByLevelType.getResultList();
		return list;
	}

	/** {@inheritDoc} */
	@Override
	@Transactional(readOnly = false)
	public void saveOrgTree(List<Organization> orgList) {
		handleSaveOrgTree(orgList);
	}

	/**
	 * @param orgList
	 *            org list
	 */
	private void handleSaveOrgTree(List<Organization> orgList) {
		List<Organization> childOrgs = null;

		for (Organization org : orgList) {
			childOrgs = org.getChildOrgs();
			if (childOrgs != null && !childOrgs.isEmpty()) {

				// 递归保存
				handleSaveOrgTree(childOrgs);

				if (org.getRoles() == null) {
					org.setRoles(new HashSet<Role>());
				}
				// Set<Role> childRole = null;
				// // 把子组织结构的权限加到父组织机构上
				// for (Organization childOrg : childOrgs) {
				// childRole = childOrg.getRoles();
				// if (null != childRole && !childRole.isEmpty()) {
				// org.getRoles().addAll(childRole);
				// }
				// }
			}

			em.merge(org);
		}
	}

	/** {@inheritDoc} */
	@Override
	public List<User> getUserByOrgCode(String orgCode) {
		Query queryUserByOrgCode = em.createNamedQuery("User.getByOrgCode");
		queryUserByOrgCode.setParameter("code", orgCode);
		@SuppressWarnings("unchecked")
		List<User> users = queryUserByOrgCode.getResultList();
		initUsers(users);
		return users;
	}

	/**
	 * @param users
	 *            users
	 */
	private void initUsers(List<User> users) {
		for (User user : users) {
			user.getDepartments().size();
		}
	}

	/** {@inheritDoc} */
	@Override
	public Organization getOrganization(String orgCode) {
		Organization org = em.find(Organization.class, orgCode);
		org.getRoles().size();
		org.getChildOrgs().size();
		Set<User> users = org.getUsers();
		initUsers(users);
		return org;
	}

	@Override
	public Organization getOrganizationLite(String orgCode) {
		Organization org = em.find(Organization.class, orgCode);
		return org;

	}

	/**
	 * @param users
	 *            users
	 */
	private void initUsers(Set<User> users) {
		for (User user : users) {
			user.getDepartments().size();
		}
	}

	/** {@inheritDoc} */
	@Override
	@Transactional(readOnly = false)
	public void updateOrganization(Organization org) {
		em.merge(org);
	}

	/** {@inheritDoc} */
	@Override
	@Transactional(readOnly = false)
	public void delOrganization(String orgCode) {
		Organization org = em.find(Organization.class, orgCode);
		Organization parent = org.getParent();
		if (parent != null) {
			parent.getChildOrgs().remove(org);
		}
		em.remove(org);
	}

	/** {@inheritDoc} */
	@Override
	@Transactional(readOnly = false)
	public void updateRole(Role theRole) {
		em.merge(theRole);
	}

	/** {@inheritDoc} */
	@Override
	@Transactional(readOnly = false)
	public void delRole(String roleCode) {
		Role role = em.getReference(Role.class, roleCode);
		em.remove(role);
	}

	/** {@inheritDoc} */
	@Override
	public Set<String> getResource(LoginUser loginUser, String resourceType) {
		Collection<Role> roleSet = loginUser.getAuthorities();
		String[] roleIds = new String[roleSet.size()];
		int i = 0;
		for (Role rl : roleSet) {
			roleIds[i++] = rl.getAuthority();
		}
		List<RoleResource> roleResList = em.createNamedQuery("RoleResource.getByRoles", RoleResource.class).setParameter("roles", roleIds)
		        .setParameter("type", resourceType).getResultList();
		Set<String> resSet = new HashSet<String>();
		for (RoleResource roleRes : roleResList) {
			resSet.add(roleRes.getResource().getResourceId());
		}
		// for (Role role : roleSet) {
		// role = (Role) em.find((Role.class, role.getId());
		// Hibernate.initialize(role.getRoleResList());
		// roleResList = role.getRoleResList();
		// if (roleResList != null) {
		// for (RoleResource roleRes : roleResList) {
		// if (resourceType.equals(roleRes.getResource().getResourceType()) &&
		// roleRes.getOperationType() >= operType) {
		// resSet.add(roleRes.getResource().getResourceId());
		// }
		// }
		// }
		// }

		return resSet;
	}

	@Override
	@Transactional(readOnly = false)
	public void updateUserPassword(String userCode, String newPasswd, String oldPassword) {
		User user = em.find(User.class, userCode);
		if (oldPassword.equals(user.getPassword())) {
			user.setPassword(newPasswd);
		} else {
			throw new CurrentPasswdIncorrectException();
		}
	}
}
