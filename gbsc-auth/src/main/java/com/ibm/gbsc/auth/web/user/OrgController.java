/**
 * 
 */
package com.ibm.gbsc.auth.web.user;

import java.lang.reflect.Field;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.GsonBuilder;
import com.ibm.gbsc.auth.user.Organization;
import com.ibm.gbsc.auth.user.UserService;

/**
 * @author fanjingxuan
 * 
 */
@Controller
@RequestMapping("/auth")
@SessionAttributes({ "orgList", "theOrg" })
public class OrgController {
	Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	UserService userService;

	/**
	 * 查询出所有的组织结构，并以树的形式展现在页面上.
	 * 
	 * @param model model
	 * @return page
	 */
	@RequestMapping(value = "/org/orgs", method = RequestMethod.GET)
	public String showOrgAll(Model model) {

		List<Organization> orgList = userService.getOrgTreeByLevel(1);

		GsonBuilder bld = new GsonBuilder();
		bld.addSerializationExclusionStrategy(new ExclusionStrategy() {

			public boolean shouldSkipField(FieldAttributes f) {

				return !f.getName().equals("name") && !f.getName().equals("code") && !f.getName().equals("childOrgs")&& !f.getName().equals("level");
			}

			public boolean shouldSkipClass(Class<?> clazz) {
				return false;
			}
		});

		bld.setFieldNamingStrategy(new FieldNamingStrategy() {

			public String translateName(Field f) {
				if (f.getName().equals("childOrgs")) {
					return "children";
				}
				return f.getName();
			}

		});
		model.addAttribute("orgTreeJson", bld.create().toJson(orgList));
		model.addAttribute("orgList", orgList);
		return "org.tile";
	}

	/**
	 * 展示当前选中的组织结构信息.
	 * 
	 * @param orgCode org code
	 * @param orgList org list
	 * @param model model
	 * @return page
	 */
	@RequestMapping(value = "/org/orgs/{orgCode}", method = RequestMethod.GET)
	public String showOrgDetail(@PathVariable String orgCode, @ModelAttribute("orgList") List<Organization> orgList, Model model) {
		return gotoOrgDetail(orgCode, orgList, model);
	}

	/**
	 * @param orgCode ord code
	 * @param orgList org list
	 * @param model model
	 * @return page
	 */
	private String gotoOrgDetail(String orgCode, List<Organization> orgList, Model model) {
		Organization theOrg = findOrg(orgList, orgCode);
		model.addAttribute("theOrg", theOrg);
		return "/org/orgDetail.jsp";
	}

	/**
	 * @param orgList org list
	 * @param orgCode org code
	 * @return the org.
	 */
	private Organization findOrg(List<Organization> orgList, String orgCode) {
		Organization theOrg = null;
		for (Organization org : orgList) {
			if (orgCode.equals(org.getCode())) {
				theOrg = org;
				break;
			} else if (org.getChildOrgs() != null && !org.getChildOrgs().isEmpty()) {
				theOrg = findOrg(org.getChildOrgs(), orgCode);
				if (theOrg != null) {
					break;
				}
			}
		}
		return theOrg;
	}

	/**
	 * @param theOrg org
	 * @param orgList org list
	 * @param status status
	 * @return page
	 */
	@RequestMapping(value = "/org/saveOrg", method = RequestMethod.POST)
	public String saveOrgChange(@ModelAttribute("theOrg") Organization theOrg, @ModelAttribute("orgList") List<Organization> orgList,
			SessionStatus status) {

		userService.updateOrganization(theOrg);
		status.setComplete();
		return "redirect:/auth/org/orgs.htm";
	}

	/**
	 * @param orgName org name
	 * @param parentOrgCode parent org code
	 * @param model model
	 * @return page
	 */
	@RequestMapping(value = "/org/initNewOrg")
	public String initNewOrg(@RequestParam String orgName, @RequestParam String parentOrgCode, Model model) {
		Organization newOrg = new Organization();
		Organization parentOrg = userService.getOrganization(parentOrgCode);
		newOrg.setName(orgName);
		newOrg.setCode(parentOrgCode + "000");
		newOrg.setNodeCode(parentOrg.getNodeCode());
		newOrg.setLevel(parentOrg.getLevel() + 1);
		newOrg.setParent(parentOrg);
		newOrg.setType(parentOrg.getType());
		model.addAttribute("theOrg", newOrg);
		return "/org/addOrg.jsp";
	}

	/**
	 * @param orgCode org code
	 * @param model model
	 * @return response
	 */
	@RequestMapping(value = "/org/checkOrg", method = RequestMethod.POST)
	@ResponseBody
	public String checkOrg(@RequestParam String orgCode, Model model) {

		Organization org = userService.getOrganization(orgCode);
		if (org == null) {
			return "allow";
		}
		if (org.getUsers() != null && !org.getUsers().isEmpty()) {
			return "haveUsers";
		}
		return "allow";
	}

	/**
	 * @param orgCode org code
	 * @param model model
	 * @return page
	 */
	@RequestMapping(value = "/org/delOrg", method = RequestMethod.POST)
	@ResponseBody
	public String delOrg(@RequestParam String orgCode, Model model) {
		userService.delOrganization(orgCode);
		return "success";
	}
}
