/**
 *
 */
package com.ibm.gbsc.auth.web.user;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ibm.gbsc.auth.resource.Role;
import com.ibm.gbsc.auth.user.UserService;

/**
 * @author fanjingxuan
 *
 */
@Controller
@RequestMapping("/auth")
public class RoleController {
	@Inject
	UserService userService;

	/**
	 * @param model
	 *            model.
	 * @return string.
	 */
	@RequestMapping(value = "/toAddRolePage", method = RequestMethod.GET)
	public String toAddRolePage(Model model) {
		Role theRole = new Role();
		model.addAttribute("type", "add");
		model.addAttribute("theRole", theRole);
		return "roleManage.tile";
	}

	/**
	 * @param theRole
	 *            theRole.
	 * @param model
	 *            model.
	 * @return string.
	 */
	@RequestMapping(value = "/addOrUpdateRole", method = RequestMethod.POST)
	public String addOrUpdateRole(@ModelAttribute("theRole") Role theRole, Model model) {
		userService.updateRole(theRole);
		return "redirect:/roleManage/search.htm";
	}

	/**
	 * @param roleCode
	 *            roleCode.
	 * @param model
	 *            model.
	 * @return string.
	 */
	// @RequestMapping(value = "/delRole", method = RequestMethod.POST)
	// @ResponseBody
	// public String delRole(@RequestParam String roleCode,Model model) {
	// userService.delRole(roleCode);
	// return "success";
	// }

	/**
	 * @param roleCode
	 *            roleCode.
	 * @param model
	 *            model.
	 * @return string.
	 */
	@RequestMapping(value = "/update/{roleCode}", method = RequestMethod.GET)
	public String toModRolePage(@PathVariable String roleCode, Model model) {
		Role theRole = userService.getRole(roleCode);
		model.addAttribute("type", "update");
		model.addAttribute("theRole", theRole);
		return "roleManage.tile";
	}
}
