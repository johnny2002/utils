package com.ibm.gbsc.auth.web.user;

import java.lang.reflect.Field;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
 */
@Controller
@RequestMapping("/auth")
@SessionAttributes({ "orgList", "theOrg" })
public class OrgRoleController {
	Logger log = LoggerFactory.getLogger(getClass());
	@Inject
	UserService userService;

	/**
	 * 访问时，加载所有的角色信息.
	 *
	 * @param model
	 *            model
	 */
	@ModelAttribute
	protected void refData(Model model) {
		model.addAttribute("appRoles", userService.getAllRoles());
	}

	/**
	 * @param fromWhere
	 *            from where
	 * @param model
	 *            model
	 * @return page
	 */
	@RequestMapping(value = "/orgrole/userOrgRole", method = RequestMethod.GET)
	public String index(@RequestParam(required = false) String fromWhere, Model model) {
		model.addAttribute("fromWhere", fromWhere);
		return "userOrgRole.tile";
	}

	/**
	 * 查询出所有的组织结构，并以树的形式展现在页面上.
	 *
	 * @param model
	 *            model
	 * @return page
	 */
	@RequestMapping(value = "/orgrole/orgs", method = RequestMethod.GET)
	public String showOrgAll(Model model) {

		List<Organization> orgList = userService.getOrgTreeByLevel(1);

		GsonBuilder bld = new GsonBuilder();
		bld.addSerializationExclusionStrategy(new ExclusionStrategy() {

			@Override
			public boolean shouldSkipField(FieldAttributes f) {

				return !f.getName().equals("name") && !f.getName().equals("code") && !f.getName().equals("childOrgs")
				        && !f.getName().equals("level");
			}

			@Override
			public boolean shouldSkipClass(Class<?> clazz) {
				return false;
			}
		});

		bld.setFieldNamingStrategy(new FieldNamingStrategy() {

			@Override
			public String translateName(Field f) {
				if (f.getName().equals("childOrgs")) {
					return "children";
				}
				return f.getName();
			}

		});

		model.addAttribute("orgTreeJson", bld.create().toJson(orgList));
		model.addAttribute("orgList", orgList);
		return "/orgrole/orgRoleTree.jsp";
	}

	/**
	 * 展示当前选中的组织结构信息.
	 *
	 * @param orgCode
	 *            code
	 * @param orgList
	 *            list of org
	 * @param model
	 *            model
	 * @return page
	 */
	@RequestMapping(value = "/orgrole/orgs/{orgCode}", method = RequestMethod.GET)
	public String showOrgDetail(@PathVariable String orgCode, @ModelAttribute("orgList") List<Organization> orgList, Model model) {
		return gotoOrgDetail(orgCode, orgList, model);
	}

	/**
	 * @param orgCode
	 *            orgCode.
	 * @param orgList
	 *            orgList.
	 * @param model
	 *            model.
	 * @return string.
	 */
	private String gotoOrgDetail(String orgCode, List<Organization> orgList, Model model) {
		Organization theOrg = findOrg(orgList, orgCode);
		model.addAttribute("theOrg", theOrg);
		return "/orgrole/orgRoleDetail.jsp";
	}

	/**
	 * @param orgList
	 *            orgList.
	 * @param orgCode
	 *            orgCode.
	 * @return organization is system dictionary.
	 */
	private Organization findOrg(List<Organization> orgList, String orgCode) {
		Organization theOrg = null;
		for (Organization org : orgList) {
			if (orgCode.equals(org.getCode())) {
				theOrg = org;
				break;
			} else if (org.getChildren() != null && !org.getChildren().isEmpty()) {
				theOrg = findOrg(org.getChildren(), orgCode);
				if (theOrg != null) {
					break;
				}
			}
		}
		return theOrg;
	}

	/**
	 * @param orgCode
	 *            orgCode.
	 * @param theOrg
	 *            theOrg.
	 * @param orgList
	 *            orgList.
	 * @param model
	 *            model.
	 * @return string.
	 */
	@RequestMapping(value = "/orgrole/cacheOrg", method = RequestMethod.POST)
	public String cacheOrgChange(@RequestParam String orgCode, @ModelAttribute("theOrg") Organization theOrg,
	        @ModelAttribute("orgList") List<Organization> orgList, Model model) {
		log.debug("cacheOrg method");
		return gotoOrgDetail(orgCode, orgList, model);
	}

	/**
	 * @param theOrg
	 *            theOrg.
	 * @param orgList
	 *            orgList.
	 * @param status
	 *            status.
	 * @return string.
	 */
	@RequestMapping(value = "/orgrole/saveOrg", method = RequestMethod.POST)
	public String saveOrgChange(@ModelAttribute("theOrg") Organization theOrg, @ModelAttribute("orgList") List<Organization> orgList,
	        SessionStatus status) {

		userService.saveOrgTree(orgList);
		status.setComplete();
		return "redirect:/auth/orgrole/userOrgRole.htm";
	}

	/**
	 * @param model
	 *            model.
	 * @param status
	 *            status.
	 * @return string.
	 */
	@RequestMapping(value = "/orgrole/cancelOrg", method = RequestMethod.GET)
	public String cancelOrgChange(Model model, SessionStatus status) {
		status.setComplete();
		return "redirect:/";
	}
}
