/**
 *
 */
package com.ibm.gbsc.auth.web;

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
import com.ibm.gbsc.auth.model.Organization;
import com.ibm.gbsc.auth.model.Role;
import com.ibm.gbsc.auth.service.AuthService;
import com.ibm.gbsc.gson.ZtreeNodeFieldNamingStrategy;
import com.ibm.gbsc.web.springmvc.view.GsonView;
import com.ibm.gbsc.web.tree.TreeNode;
import com.ibm.gbsc.web.tree.TreeUtil;

/**
 * @author fanjingxuan
 *
 */
@Controller
@RequestMapping("/auth/orgs")
public class OrgController {
	Logger log = LoggerFactory.getLogger(getClass());
	@Inject
	AuthService authService;
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

		List<Organization> orgList = authService.getOrgTreeByLevel(1);
		GsonBuilder gb = new GsonBuilder();
		gb.setFieldNamingStrategy(new ZtreeNodeFieldNamingStrategy());
		TreeUtil.TreeNodePropertySetter<Organization> orgNodePropSetter = new TreeUtil.TreeNodePropertySetter<Organization>() {
			@Override
			public void setTreeNodeProperty(TreeNode node, Organization org) {
				node.setCss(org.isVirtual() ? "virtualOrg" : "organization");
			}
		};
		List<TreeNode> treeNodes = TreeUtil.toTreeNodes(orgList, null, orgNodePropSetter);
		model.addAttribute("orgTreeJson", gb.create().toJson(treeNodes));
		model.addAttribute("orgList", orgList);
		return "/auth/orgTree.ftl";
	}

	@RequestMapping(value = "/{orgCode}/roles", method = RequestMethod.GET)
	public String showOrgRoles(@PathVariable String orgCode, Model model) {
		Organization org = authService.getOrganization(orgCode, true, false, false);
		model.addAttribute("org", org);
		List<Role> roles = authService.getAllRoles();
		Map<String, String> rolesMap = new TreeMap<String, String>();
		for (Role role : roles) {
			rolesMap.put(role.getCode(), role.getName());
		}
		model.addAttribute("roles", rolesMap);
		return "/auth/orgRole.ftl";
	}

	@RequestMapping(value = "/{orgCode}", method = RequestMethod.PUT)
	public View saveOrgRoles(@PathVariable String orgCode, @ModelAttribute("org") Organization org, BindingResult bResult, Model model) {
		Organization oldOrg = authService.getOrganization(orgCode, true, false, false);
		oldOrg.setRoles(org.getRoles());
		authService.saveOrganzation(oldOrg);
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
		authService.delOrganization(orgCode);
		return "success";
	}
}
