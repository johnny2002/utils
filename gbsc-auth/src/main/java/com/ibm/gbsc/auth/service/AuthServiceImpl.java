/*
 * IBM Corporation.
 * Copyright (c) 2014 All Rights Reserved.
 */

package com.ibm.gbsc.auth.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

import com.ibm.gbsc.auth.model.Function;
import com.ibm.gbsc.auth.model.Organization;
import com.ibm.gbsc.auth.model.Resource;
import com.ibm.gbsc.auth.model.Role;
import com.ibm.gbsc.auth.model.User;
import com.ibm.gbsc.auth.vo.UserNotFoundException;
import com.ibm.gbsc.auth.vo.UserPagedQueryParam;
import com.ibm.gbsc.common.dao.JpaDao;
import com.ibm.gbsc.common.vo.PagedQueryResult;

/**
 * 类作用：
 *
 * @author Johnny@cn.ibm.com 使用说明：
 */
@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {
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
		List<Organization> depts = user.getDepartments();
		depts.size();
		for (Organization org : depts) {
			org.getRoles().size();
			if (org.getParent() != null) {
				org.getParent().getName();// eager-load
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
		List<Resource> resList = dao.executeQuery(roleQuery, true, false);
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
	public List<Function> getFunctionTree() {
		List<Function> funcs = em.createNamedQuery("Function.get1stLevel", Function.class).getResultList();
		initSubFuncs(funcs, true);
		return funcs;
	}

	@Override
	public List<Function> getAllFunctions() {
		List<Function> funcs = em.createNamedQuery("Function.getAll", Function.class).getResultList();
		for (Function func : funcs) {
			func.getRoles().size();
		}
		return funcs;
	}

	@Override
	public List<Function> getFunctionsByRole(Role role) {

		TypedQuery<Function> query = em.createNamedQuery("Function.byRole", Function.class).setParameter("role", role);
		List<Function> funcs = dao.executeReadonlyQuery(query);
		return funcs;
	}

	/**
	 * @param menus
	 *            menus
	 * @param recursive
	 *            recursively or not
	 */
	private void initSubFuncs(List<Function> menus, boolean recursive) {
		for (Function menu : menus) {
			if (menu.getChildren() != null) {
				menu.getRoles().size();
				if (recursive) {
					initSubFuncs(menu.getChildren(), recursive);
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibm.gbsc.auth.service.AuthService#saveRoleFunctions(com.ibm.gbsc.
	 * auth.model.Role, java.util.List)
	 */
	@Override
	@Transactional(readOnly = false)
	public void saveRoleFunctions(Role role, Collection<Function> funcs) {
		// 保存Role关联的Function变动情况
		List<Function> oFuncs = em.createNamedQuery("Function.byRole", Function.class).setParameter("role", role).getResultList();
		for (Function oFunc : oFuncs) {
			if (funcs.remove(oFunc)) {
				log.debug("{} function no need to change.", oFunc.getName());
			} else {
				oFunc.getRoles().remove(role);
			}
		}
		for (Function func : funcs) {
			Function oldFunc = em.find(Function.class, func.getCode());
			oldFunc.getRoles().add(role);
		}

	}
}
