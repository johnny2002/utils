/*
 * IBM Corporation.
 * Copyright (c) 2014 All Rights Reserved.
 */

package com.ibm.gbsc.auth.web;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ibm.gbsc.auth.model.Role;
import com.ibm.gbsc.auth.service.AuthService;

/**
 * 类作用：
 *
 * @author Johnny@cn.ibm.com 使用说明：
 */
@Controller
@RequestMapping("/auth/roles")
public class RoleController {
	@Inject
	AuthService authService;

	@RequestMapping
	public String list(Model model) {
		List<Role> roles = authService.getAllRoles();
		model.addAttribute("roles", roles);
		return "/auth/roles.ftl";
	}

}
