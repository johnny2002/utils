package com.ibm.gbsc.auth.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.gbsc.auth.resource.Role;
import com.ibm.gbsc.common.dao.JpaDao;
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
	@Inject
	JpaDao dao;

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
	public PagedQueryResult<User> searchUser(UserPagedQueryParam queryParam) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> userCtQuery = cb.createQuery(User.class);
		Root<User> userRoot = userCtQuery.from(User.class);
		// userRoot.alias("u");
		List<Predicate> criteList = new ArrayList<Predicate>();
		if (StringUtils.isNotBlank(queryParam.getName())) {
			criteList.add(cb.like(userRoot.<String> get("fullName"), "%" + queryParam.getName().trim().toUpperCase() + "%"));
		}
		if (StringUtils.isNotBlank(queryParam.getOrgName())) {
			Join<User, Organization> dep = userRoot.join("departments");
			criteList.add(cb.like(dep.<String> get("name"), "%" + queryParam.getOrgName().toUpperCase() + "%"));
		}
		Predicate[] predicates = criteList.toArray(new Predicate[criteList.size()]);
		userCtQuery.orderBy(cb.asc(userRoot.<String> get("fullName")));

		userCtQuery.where(predicates);

		PagedQueryResult<User> result = dao.executePagedQuery(em, User.class, queryParam, userCtQuery, predicates);
		for (User user : result.getDatas()) {
			user.getDepartments().size();
		}
		return result;

		// return rst;

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
		TypedQuery<Organization> queryOrgByLevelType = em.createNamedQuery("Organization.getByLevel", Organization.class);
		queryOrgByLevelType.setParameter("level", level);
		List<Organization> list = dao.executeQuery(queryOrgByLevelType, true, false);
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
			initOrgList(org.getChildren());
		}

	}

	/** {@inheritDoc} */
	@Override
	public List<Organization> getOrganizationByLevelType(int level, String type) {
		TypedQuery<Organization> queryOrgByLevelType = em.createNamedQuery("Organization.getByLevelType", Organization.class);
		queryOrgByLevelType.setParameter("level", level);
		queryOrgByLevelType.setParameter("type", type);
		List<Organization> list = dao.executeReadonlyQuery(queryOrgByLevelType);
		return list;
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
	private void initUsers(Collection<User> users) {
		for (User user : users) {
			user.getDepartments().size();
		}
	}

	/** {@inheritDoc} */
	@Override
	public Organization getOrganization(String orgCode, boolean loadRoles, boolean loadUsers, boolean loadChildren) {
		Organization org = em.find(Organization.class, orgCode);
		if (loadRoles) {
			org.getRoles().size();
		}
		if (loadChildren) {
			org.getChildren().size();
		}

		if (loadUsers) {
			Collection<User> users = org.getUsers();
			initUsers(users);
		}
		return org;
	}

	@Override
	public Organization getOrganizationLite(String orgCode) {
		Organization org = em.find(Organization.class, orgCode);
		return org;

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
			parent.getChildren().remove(org);
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

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.ibm.gbsc.auth.user.UserService#saveUser(com.ibm.gbsc.auth.user.User)
	 */
	@Override
	@Transactional(readOnly = false)
	public void saveUser(User user) {
		em.persist(user);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.ibm.gbsc.auth.user.UserService#saveOrganzation(com.ibm.gbsc.auth.
	 * user.Organization)
	 */
	@Override
	@Transactional(readOnly = false)
	public void saveOrganzation(Organization org) {
		em.merge(org);

	}
}
