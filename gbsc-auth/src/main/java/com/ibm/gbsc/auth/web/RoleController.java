/*
 * IBM Corporation.
 * Copyright (c) 2014 All Rights Reserved.
 */

package com.ibm.gbsc.auth.web;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.ibm.gbsc.auth.model.Function;
import com.ibm.gbsc.auth.model.Role;
import com.ibm.gbsc.auth.service.AuthService;
import com.ibm.gbsc.web.springmvc.view.GsonView;
import com.ibm.gbsc.web.tree.TreeNode;
import com.ibm.gbsc.web.tree.TreeUtil;

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
	@Inject
	MessageSource messageSource;

	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		List<Role> roles = authService.getAllRoles();
		model.addAttribute("roles", roles);
		return "/auth/roles.ftl";
	}

	@RequestMapping(value = "/{role}/functions", method = RequestMethod.GET)
	public String roleFunction(@PathVariable Role role, Model model) {
		List<Function> functionsByRole = authService.getFunctionsByRole(role);
		List<Function> functionTree = authService.getFunctionTree();
		List<TreeNode> funcTreeNodes = TreeUtil.toTreeNodes(functionTree, functionsByRole);
		model.addAttribute("role", role);
		// model.addAttribute("functionsByRole", functionsByRole);
		model.addAttribute("funcTreeNodes", new Gson().toJson(funcTreeNodes));
		return "/auth/roleFunction.ftl";
	}

	@RequestMapping(value = "/{role}/functions", method = RequestMethod.PUT)
	public ModelAndView saveRoleFunctions(@PathVariable Role role, @RequestParam Set<Function> funcs, Model model) {
		GsonView vw = new GsonView();

		authService.saveRoleFunctions(role, funcs);
		vw.addAttribute("message", messageSource.getMessage("auth.user.savedOK", new Object[] { role.getName() }, null));

		return new ModelAndView(vw);
	}

}
