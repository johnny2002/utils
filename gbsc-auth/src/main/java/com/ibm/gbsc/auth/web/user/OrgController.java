/**
 *
 */
package com.ibm.gbsc.auth.web.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;

import com.google.gson.GsonBuilder;
import com.ibm.gbsc.auth.resource.Role;
import com.ibm.gbsc.auth.user.Organization;
import com.ibm.gbsc.auth.user.UserService;
import com.ibm.gbsc.gson.ZtreeNodeFieldNamingStrategy;
import com.ibm.gbsc.web.model.TreeNode;
import com.ibm.gbsc.web.springmvc.view.GsonView;

/**
 * @author fanjingxuan
 *
 */
@Controller
@RequestMapping("/auth/orgs")
public class OrgController {
	Logger log = LoggerFactory.getLogger(getClass());
	@Inject
	UserService userService;
	@Inject
	MessageSource messageSource;

	/**
	 * 查询出所有的组织结构，并以树的形式展现在页面上.
	 *
	 * @param model
	 *            model
	 * @return page
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String showOrgAll(Model model) {

		List<Organization> orgList = userService.getOrgTreeByLevel(1);
		GsonBuilder gb = new GsonBuilder();
		gb.setFieldNamingStrategy(new ZtreeNodeFieldNamingStrategy());
		model.addAttribute("orgTreeJson", gb.create().toJson(toTreeNodes(orgList)));
		model.addAttribute("orgList", orgList);
		return "/auth/user/orgTree.ftl";
	}

	private List<TreeNode> toTreeNodes(List<Organization> orgs) {
		ArrayList<TreeNode> nodes = new ArrayList<TreeNode>(orgs.size());
		for (Organization org : orgs) {
			TreeNode node = new TreeNode();
			nodes.add(node);
			node.setId(org.getCode());
			node.setName(org.getName());
			node.setCss(org.isVirtual() ? "virtualOrg" : "organization");
			node.setOpen(true);
			// node.setUrl("orgs/" + org.getCode() + "/roles");
			// node.setOnclick("return openOrgRoles('" + org.getCode() + "');");
			if (!(org.getChildren() == null || org.getChildren().isEmpty())) {
				node.setChildren(toTreeNodes(org.getChildren()));
			}
		}
		return nodes;
	}

	@RequestMapping(value = "/{orgCode}/roles", method = RequestMethod.GET)
	public String showOrgRoles(@PathVariable String orgCode, Model model) {
		Organization org = userService.getOrganization(orgCode, true, false, false);
		model.addAttribute("org", org);
		List<Role> roles = userService.getAllRoles();
		Map<String, String> rolesMap = new TreeMap<String, String>();
		for (Role role : roles) {
			rolesMap.put(role.getCode(), role.getName());
		}
		model.addAttribute("roles", rolesMap);
		return "/auth/user/orgRole.ftl";
	}

	@RequestMapping(value = "/{orgCode}", method = RequestMethod.PUT)
	public View saveOrgRoles(@PathVariable String orgCode, @ModelAttribute("org") Organization org, BindingResult bResult, Model model) {
		Organization oldOrg = userService.getOrganization(orgCode, true, false, false);
		oldOrg.setRoles(org.getRoles());
		userService.saveOrganzation(oldOrg);
		GsonView vw = new GsonView();
		vw.addAttribute("message", messageSource.getMessage("auth.org.savedOK", new Object[] { orgCode }, null));
		return vw;
	}

	/**
	 * @param orgCode
	 *            org code
	 * @param model
	 *            model
	 * @return page
	 */
	@RequestMapping(value = "/{orgCode}", method = RequestMethod.DELETE)
	public String delOrg(@PathVariable String orgCode, Model model) {
		userService.delOrganization(orgCode);
		return "success";
	}
}
