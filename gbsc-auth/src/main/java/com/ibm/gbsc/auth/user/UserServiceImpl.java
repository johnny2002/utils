package com.ibm.gbsc.auth.user;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.banking.framework.dao.HibernateDao;
import com.ibm.banking.framework.dto.PagedQueryResult;
import com.ibm.gbsc.auth.resource.RoleResource;

/**
 * @author Johnny
 */
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
	Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	HibernateDao dao;

	/** {@inheritDoc} */
	@Override
	@SuppressWarnings("unchecked")
	public List<Role> getAllRoles() {
		List<Role> roleList = dao.getSession().getNamedQuery("Role.getAll").list();
		for (Role role : roleList) {
			Hibernate.initialize(role.getFunctions());
			List<RoleResource> roleResources = role.getRoleResList();
			for (RoleResource roleResource : roleResources) {
				Hibernate.initialize(roleResource.getResource());
			}
		}
		return roleList;
	}

	/** {@inheritDoc} */
	@Override
	@SuppressWarnings("unchecked")
	public List<Role> getAppRoles(Integer appId) {
		return dao.getSession().getNamedQuery("Role.getByApp").setInteger("appId", appId).list();
	}

	/** {@inheritDoc} */
	@Override
	public User getUser(String loginId) {
		log.debug(loginId);
		User user = (User) dao.getSession().get(User.class, loginId);
		if (user == null) {
			throw new UserNotFoundException("指定的用户不存在：" + loginId);
		}
		Hibernate.initialize(user.getRoles());
		List<Organization> parents = user.getDepartments();
		Hibernate.initialize(parents);
		for (Organization org : parents) {
			Hibernate.initialize(org.getRoles());
			if (org.getParent() != null) {
				Hibernate.initialize(org.getParent());
			}
		}
		return user;
	}

	/** {@inheritDoc} */
	@Override
	public Role getRole(String auth) {
		Role role = (Role) dao.getSession().get(Role.class, auth);
		Hibernate.initialize(role.getFunctions());
		Hibernate.initialize(role.getRoleResList());
		return role;
	}

	/** {@inheritDoc} */
	@Override
	public PagedQueryResult<User> searchUser(UserPagedQueryParam param) {
		Criteria criteria = dao.getSession().createCriteria(User.class);
		if (StringUtils.isNotBlank(param.getName())) {
			criteria.add(Restrictions.ilike("displayName", param.getName().trim().toLowerCase(), MatchMode.ANYWHERE));
		}
		/*
		 * if (StringUtils.isNotBlank(param.getOrgName())){
		 * criteria.createAlias("org", "org");
		 * criteria.createAlias("org.parent", "porg");
		 * criteria.add(Restrictions.or(Restrictions.ilike("org.name",
		 * param.getOrgName().trim(), MatchMode.ANYWHERE),
		 * Restrictions.ilike("porg.name", param.getOrgName().trim(),
		 * MatchMode.ANYWHERE))); }
		 */
		criteria.addOrder(Order.asc("displayName"));
		PagedQueryResult<User> rst = dao.executePagingQuery(criteria, param);
		for (User user : rst.getDatas()) {
			if (user.getOrg() != null) {
				Hibernate.initialize(user.getOrg().getParent());
			}
		}
		return rst;
	}

	/** {@inheritDoc} */
	@Override
	@Transactional(readOnly = false)
	public void updateUser(User user) {
		log.info("saveUser saveUser saveUser saveUser");
		// dao.update(user);
		dao.getSession().merge(user);
	}

	/** {@inheritDoc} */
	@Override
	public List<Organization> getOrgTreeByLevel(int level) {
		Query queryOrgByLevelType = dao.getSession().getNamedQuery("Organization.getByLevel");
		queryOrgByLevelType.setInteger("level", level);
		@SuppressWarnings("unchecked")
		List<Organization> list = queryOrgByLevelType.list();
		initOrgList(list);
		return list;

	}

	/**
	 * @param orgs
	 *            orgs
	 */
	private void initOrgList(List<Organization> orgs) {
		for (Organization org : orgs) {
			Hibernate.initialize(org.getRoles());
			initOrgList(org.getChildOrgs());
		}

	}

	/** {@inheritDoc} */
	@Override
	public List<Organization> getOrganizationByLevelType(int level, String type) {
		Query queryOrgByLevelType = dao.getSession().getNamedQuery("Organization.getByLevelType");
		queryOrgByLevelType.setInteger("level", level);
		queryOrgByLevelType.setString("type", type);
		@SuppressWarnings("unchecked")
		List<Organization> list = queryOrgByLevelType.list();
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

			dao.getSession().merge(org);
		}
	}

	/** {@inheritDoc} */
	@Override
	public List<User> getUserByOrgCode(String orgCode) {
		Query queryUserByOrgCode = dao.getSession().getNamedQuery("User.getByOrgCode");
		queryUserByOrgCode.setString("code", orgCode);
		@SuppressWarnings("unchecked")
		List<User> users = queryUserByOrgCode.list();
		initUsers(users);
		return users;
	}

	/**
	 * @param users
	 *            users
	 */
	private void initUsers(List<User> users) {
		for (User user : users) {
			Hibernate.initialize(user.getDepartments());
		}
	}

	/** {@inheritDoc} */
	@Override
	public Organization getOrganization(String orgCode) {
		Organization org = (Organization) dao.getSession().get(Organization.class, orgCode);
		if (org == null) {
			org = (Organization) dao.getSession().createQuery("select o from Organization o where o.nodeCode = :nodeCode and o.level =2")
					.setParameter("nodeCode", orgCode).uniqueResult();
		}
		System.out.println(orgCode + "------------------");
		Hibernate.initialize(org.getRoles());
		Hibernate.initialize(org.getChildOrgs());
		Set<User> users = org.getUsers();
		initUsers(users);
		return org;
	}

	@Override
	public Organization getOrganizationLite(String orgCode) {
		Organization org = (Organization) dao.getSession().get(Organization.class, orgCode);
		if (org == null) {
			org = (Organization) dao.getSession().createQuery("select o from Organization o where o.nodeCode = :nodeCode and o.level =2")
					.setParameter("nodeCode", orgCode).uniqueResult();
		}
		return org;

	}

	/**
	 * @param users
	 *            users
	 */
	private void initUsers(Set<User> users) {
		for (User user : users) {
			Hibernate.initialize(user.getDepartments());
		}
	}

	/** {@inheritDoc} */
	@Override
	@Transactional(readOnly = false)
	public void updateOrganization(Organization org) {
		dao.getSession().merge(org);
	}

	/** {@inheritDoc} */
	@Override
	@Transactional(readOnly = false)
	public void delOrganization(String orgCode) {
		Organization org = (Organization) dao.getSession().get(Organization.class, orgCode);
		Organization parent = org.getParent();
		parent.getChildOrgs().remove(org);
		dao.getSession().delete(org);
	}

	/** {@inheritDoc} */
	@Override
	public PagedQueryResult<Role> searchRole(RolePagedQueryParam queryParam) {
		Criteria criteria = dao.getSession().createCriteria(Role.class);
		if (StringUtils.isNotBlank(queryParam.getRoleName())) {
			criteria.add(Restrictions.ilike("name", queryParam.getRoleName().trim().toLowerCase(), MatchMode.ANYWHERE));
		}

		criteria.addOrder(Order.asc("name"));
		PagedQueryResult<Role> rst = dao.executePagingQuery(criteria, queryParam);
		return rst;
	}

	/** {@inheritDoc} */
	@Override
	@Transactional(readOnly = false)
	public void updateRole(Role theRole) {
		Role role = (Role) dao.getSession().merge(theRole);
		theRole.setAuthority(role.getAuthority());
		// dao.getSessionFactory().getCache().evictEntityRegion(Role.class);
		// Hibernate.initialize(theRole.getFunctions());
	}

	/** {@inheritDoc} */
	@Override
	@Transactional(readOnly = false)
	public void delRole(String roleCode) {
		Session session = dao.getSession();

		Role role = (Role) session.load(Role.class, roleCode);
		session.delete(role);
	}

	/** {@inheritDoc} */
	@Override
	public boolean haveRelationWithOrg(String roleId) {
		BigDecimal count = (BigDecimal) dao.getSession().createSQLQuery("select count(*) from RI_NT_AUTH_ORG_ROLE t where t.ROLE_ID = :roleId")
				.setString("roleId", roleId).uniqueResult();
		return count.intValue() > 0;
	}

	/** {@inheritDoc} */
	@Override
	public boolean haveRelationWithUser(String roleId) {
		BigDecimal count = (BigDecimal) dao.getSession().createSQLQuery("select count(*) from RI_NT_AUTH_USER_ROLE t where t.ROLE_CODE = :roleId")
				.setString("roleId", roleId).uniqueResult();
		return count.intValue() > 0;
	}

	/** {@inheritDoc} */
	@Override
	public Set<String> getResource(LoginUser loginUser, String resourceType, int operType) {
		Collection<Role> roleSet = loginUser.getAuthorities();
		String[] roleIds = new String[roleSet.size()];
		int i = 0;
		for (Role rl : roleSet) {
			roleIds[i++] = rl.getAuthority();
		}
		@SuppressWarnings("unchecked")
		List<RoleResource> roleResList = dao.getSession().getNamedQuery("RoleResource.getByRoles").setParameterList("roles", roleIds)
				.setParameter("type", resourceType).setParameter("operationType", operType).list();
		Set<String> resSet = new HashSet<String>();
		for (RoleResource roleRes : roleResList) {
			resSet.add(roleRes.getResource().getResourceId());
		}
		// for (Role role : roleSet) {
		// role = (Role) dao.getSession().get(Role.class, role.getId());
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

	/** {@inheritDoc} */
	@Override
	@SuppressWarnings("unchecked")
	public List<User> getUserByOrgCodeAndRoleId(String[] orgCodes, String roleId) {
		// 查询特定组织中具有某种特定角色的用户
		Criteria userCriteria = dao.getSession().createCriteria(User.class);
		// 关联组织表
		userCriteria.createAlias("departments", "org");
		userCriteria.add(Restrictions.in("org.code", orgCodes));
		// 关联角色表
		userCriteria.createAlias("roles", "role");
		userCriteria.add(Restrictions.eq("role.authority", roleId));
		List<User> users = userCriteria.list();

		// 查询具有特定角色的组织下所有的用户
		Criteria organCriteria = dao.getSession().createCriteria(Organization.class);
		// 关联角色表
		organCriteria.createAlias("roles", "role");
		organCriteria.add(Restrictions.eq("role.authority", roleId));
		List<Organization> organs = organCriteria.list();

		// 两次查询结果做并集
		for (Organization organ : organs) {
			users.addAll(organ.getUsers());
		}

		return userCriteria.list();
	}

	/** {@inheritDoc} */
	@Override
	@Transactional(readOnly = false)
	public void updateUserPassword(String userCode, String newPasswd, String oldPassword) {
		Session session = dao.getSession();
		User user = (User) session.load(User.class, userCode);
		if (oldPassword.equals(user.getPassword())) {
			user.setPassword(newPasswd);
		} else {
			throw new CurrentPasswdIncorrectException();
		}
	}
}
