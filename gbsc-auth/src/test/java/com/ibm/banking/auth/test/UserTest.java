package com.ibm.banking.auth.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import com.ibm.gbsc.auth.model.Function;
import com.ibm.gbsc.auth.model.Organization;
import com.ibm.gbsc.auth.model.Role;
import com.ibm.gbsc.auth.service.AuthService;
import com.ibm.gbsc.auth.vo.UserPagedQueryParam;
import com.ibm.gbsc.test.BaseTest;

public class UserTest extends BaseTest {
	@Inject
	AuthService auth;

	@Test
	public void testFunctionMain() {
		List<Function> func = auth.getAllFunctions();
		String json = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
			List<String> names = Arrays.asList(new String[] { "id", "name", "url", "portletUrl", "children" });

			@Override
			public boolean shouldSkipField(FieldAttributes f) {

				String name = f.getName();

				return !names.contains(name);
			}

			@Override
			public boolean shouldSkipClass(Class<?> clazz) {
				return false;
			}
		}).create().toJson(func);
		System.out.println(json);
	}

	@Test
	public void testGetNodes() {
		List<Organization> orgs = auth.getOrgTreeByLevel(1);
		for (Organization org : orgs.get(0).getChildren()) {
			System.out.println(org.getCode() + org.getName() + org.getNodeCode());
		}
	}

	@Test
	public void testA() {
		List<Function> ls = auth.getFunctionTree();
		GsonBuilder bld = new GsonBuilder();
		bld.addSerializationExclusionStrategy(new ExclusionStrategy() {

			@Override
			public boolean shouldSkipField(FieldAttributes f) {

				return !f.getName().equals("name") && !f.getName().equals("id") && !f.getName().equals("children");
			}

			@Override
			public boolean shouldSkipClass(Class<?> clazz) {
				return false;
			}
		});

		System.out.println(bld.create().toJson(ls));

	}

	@Test
	public void testGetResource() {
		List<Role> roles = new ArrayList<Role>();
		Role role = auth.getRole("RISK_INT_SYS_ADMIN");
		roles.add(role);
		// System.out.println(authService.getResource(roles, "1",1).size());
	}

	@Test
	public void testGetRoleFunctions() {
		auth.getFunctionsByRole(new Role("DEVP", ""));
	}

	@Test
	public void testSearchUser() {
		UserPagedQueryParam param = new UserPagedQueryParam();
		param.setName("周");
		param.setOrgName("风险");
		auth.searchUser(param);
	}
}
