package com.ibm.banking.auth.test;

import java.util.ArrayList;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ibm.gbsc.auth.resource.ResourceService;
import com.ibm.gbsc.auth.resource.Role;
import com.ibm.gbsc.test.BaseTest;

public class ResourceTest extends BaseTest {
	@Inject
	ResourceService res;

	@Test
	public void testGetRes() {
		ArrayList<Role> roles = new ArrayList<Role>();
		Role role = new Role();
		role.setCode("DD");
		roles.add(role);
		role = new Role();
		role.setCode("DD");
		roles.add(role);
		role = new Role();
		role.setCode("DD");
		roles.add(role);
		role = new Role();
		role.setCode("DD");
		roles.add(role);
		res.getResourceByRoles(roles);
	}
}
