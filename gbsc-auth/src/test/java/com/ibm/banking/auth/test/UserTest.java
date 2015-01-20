package com.ibm.banking.auth.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import com.ibm.gbsc.auth.function.Function;
import com.ibm.gbsc.auth.function.FunctionService;
import com.ibm.gbsc.auth.function.FunctionType;
import com.ibm.gbsc.auth.resource.ResourceService;
import com.ibm.gbsc.auth.user.Organization;
import com.ibm.gbsc.auth.user.Role;
import com.ibm.gbsc.auth.user.User;
import com.ibm.gbsc.auth.user.UserService;

@ContextConfiguration(locations = { "classpath:conf/banking-auth-dao.xml", "classpath:conf/banking-auth-res-test.xml" })
@TransactionConfiguration(defaultRollback = false)
public class UserTest extends AbstractTransactionalTestNGSpringContextTests {
	@Autowired
	ResourceService resourceService;
	@Autowired
	UserService userService;
	@Autowired
	FunctionService fs;
	@Autowired
	SessionFactory sessionFactory;

	// @Test
	public void testNewUser() {
		Session session = sessionFactory.getCurrentSession();
		User user = new User();
		user.setCode("1");
		user.setDisplayName("Johnny Zhou");
		Set<Role> roles = new HashSet<Role>();
		user.setRoles(roles);
		Role r1 = new Role("Admin", "irmp");
		session.persist(r1);
		roles.add(r1);
		r1 = new Role("User", "irmp");
		session.persist(r1);
		r1 = new Role("User2", "irmp");
		session.persist(r1);
		roles.add(r1);

		session.persist(user);

	}

	@Test
	// (dependsOnMethods="testNewUser")
	public void testGetUser() {
		Session session = sessionFactory.getCurrentSession();
		User user = (User) session.get(User.class, "1");
		System.out.println(user.getCode());
		System.out.println(user.getRoles());
	}

	// @Test(dependsOnMethods="testNewUser")
	public void testUpdateUser() {
		Session session = sessionFactory.getCurrentSession();
		User user = new User();
		user.setCode("1");
		user.setDisplayName("Johnny6");
		Set<Role> roles = new HashSet<Role>();
		user.setRoles(roles);
		Role r1 = (Role) session.load(Role.class, "Admin");
		roles.add(r1);
		r1 = (Role) session.load(Role.class, "User");

		roles.add(r1);

		session.persist(r1);
		Object obj = session.merge(user);
	}

	@Test
	public void testFunction() {
		Session session = sessionFactory.getCurrentSession();
		session.get(Function.class, "10");
	}

	@Test
	public void testFunctionMain() {
		List<Function> func = fs.getFunctionsByType(FunctionType.MAIN, false);
		String json = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
			List<String> names = Arrays.asList(new String[] { "id", "name", "url", "portletUrl", "children" });

			public boolean shouldSkipField(FieldAttributes f) {

				String name = f.getName();

				return !names.contains(name);
			}

			public boolean shouldSkipClass(Class<?> clazz) {
				return false;
			}
		}).create().toJson(func);
		System.out.println(json);
	}

	@Test
	public void testGetNodes() {
		List<Organization> orgs = userService.getOrgTreeByLevel(1);
		for (Organization org : orgs.get(0).getChildOrgs()) {
			System.out.println(org.getCode() + org.getName() + org.getNodeCode());
		}
	}

	@Test
	public void testGetUserByRole() {
		String hql = "select user from User user inner join user.roles role where role.authority = 'RISK_INT_SYS_ADMIN'";
		List<User> list = sessionFactory.getCurrentSession().createQuery(hql).list();
		for (User user : list) {
			System.out.println(user.getCode());
		}
	}

	@Test
	public void testA() {
		List<Function> ls = fs.getFunctionTree();
		GsonBuilder bld = new GsonBuilder();
		bld.addSerializationExclusionStrategy(new ExclusionStrategy() {

			public boolean shouldSkipField(FieldAttributes f) {

				return !f.getName().equals("name") && !f.getName().equals("id") && !f.getName().equals("children");
			}

			public boolean shouldSkipClass(Class<?> clazz) {
				return false;
			}
		});

		System.out.println(bld.create().toJson(ls));

	}

	@Test
	public void testHaveRelationWithOrg() {
		System.out.println(userService.haveRelationWithOrg("RISK_INT_DATA_ADUIT"));
	}

	@Test
	public void testGetResource() {
		List<Role> roles = new ArrayList<Role>();
		Role role = userService.getRole("RISK_INT_SYS_ADMIN");
		roles.add(role);
		// System.out.println(userService.getResource(roles, "1",1).size());
	}

	@Test
	public void testDelRoleResByRoleId() {
		// resourceService.delRoleResByRoleId("RISK_INT_DATA_AGENCY");
	}

	@Test
	public void testDelRoleResByRoles() {
		Session session = sessionFactory.getCurrentSession();
		List list = session.getNamedQuery("RoleResource.getByRoles").setParameterList("roles", new String[] { "RISK_INT_SYS_ADMIN" }).list();
		for (Object obj : list) {
			System.out.println(obj.toString());
		}
	}
}
