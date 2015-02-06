/**
 *
 */
package com.ibm.gbsc.auth.web.user;

import java.lang.reflect.Field;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.GsonBuilder;
import com.ibm.gbsc.auth.resource.Function;
import com.ibm.gbsc.auth.resource.FunctionService;
import com.ibm.gbsc.auth.resource.Role;
import com.ibm.gbsc.auth.user.UserService;

/**
 * @author fanjingxuan
 *
 */
@Controller
@RequestMapping("/auth")
@SessionAttributes({ "roleList", "functionList" })
public class RoleFunctionContoller {
	Logger log = LoggerFactory.getLogger(getClass());
	@Inject
	UserService userService;
	@Inject
	FunctionService functionService;

	/**
	 * @param fromWhere
	 *            fromWhere.
	 * @param model
	 *            model.
	 * @return string.
	 */
	@RequestMapping(value = "/roleFunction/roleFuncRes", method = RequestMethod.GET)
	public String index(@RequestParam(required = false) String fromWhere, Model model) {
		model.addAttribute("fromWhere", fromWhere);
		return "roleFuncRes.tile";
	}

	/**
	 * @param model
	 *            model.
	 * @return string.
	 */
	@RequestMapping(value = "/roleFunction/roles", method = RequestMethod.GET)
	public String showRoleAll(Model model) {

		List<Role> roleList = userService.getAllRoles();

		GsonBuilder bld = new GsonBuilder();
		bld.addSerializationExclusionStrategy(new ExclusionStrategy() {

			@Override
			public boolean shouldSkipField(FieldAttributes f) {

				return !f.getName().equals("name") && !f.getName().equals("code");
			}

			@Override
			public boolean shouldSkipClass(Class<?> clazz) {
				return false;
			}
		});

		bld.setFieldNamingStrategy(new FieldNamingStrategy() {

			@Override
			public String translateName(Field f) {
				if (f.getName().equals("code")) {
					return "id";
				}
				return f.getName();
			}

		});
		StringBuffer sb = new StringBuffer();
		sb.append("[{name:'角色列表',open:true,children:");
		sb.append(bld.create().toJson(roleList));
		sb.append("}]");
		model.addAttribute("zTreeJson", sb.toString());
		model.addAttribute("roleList", roleList);
		return "/rolefunction/roleList.jsp";
	}

	/**
	 * @param roleId
	 *            roleId.
	 * @param roleName
	 *            roleName.
	 * @param roleList
	 *            roleList.
	 * @param model
	 *            model.
	 * @return string.
	 */
	@RequestMapping(value = "/roleFunction/addOrUpdateRole", method = RequestMethod.POST)
	@ResponseBody
	public String addOrUpdateRole(@RequestParam(required = false) String roleId, @RequestParam String roleName,
	        @ModelAttribute("roleList") List<Role> roleList, Model model) {
		Role role = null;
		if (StringUtils.isBlank(roleId)) {
			role = new Role();
		} else {
			role = findRole(roleList, roleId);
			if (role == null) {
				return "notExist";
			}
		}
		role.setName(roleName);
		userService.updateRole(role);
		if (!roleList.contains(role)) {
			roleList.add(role);
		}
		return role.getCode();
	}

	/**
	 * @param roleId
	 *            roleId.
	 * @param roleList
	 *            roleList.
	 * @param model
	 *            model.
	 * @return string.
	 */
	@RequestMapping(value = "/roleFunction/delRole", method = RequestMethod.POST)
	@ResponseBody
	public String delRole(@RequestParam String roleId, @ModelAttribute("roleList") List<Role> roleList, Model model) {
		userService.delRole(roleId);
		Role role = findRole(roleList, roleId);
		if (role != null) {
			roleList.remove(role);
		}
		return "success";
	}

	/**
	 * @param functionList
	 *            functionList.
	 * @param funcId
	 *            funcId.
	 * @return function is system dictionary.
	 */
	private Function findFunction(List<Function> functionList, String funcId) {
		Function foundFunction = null;
		for (Function func : functionList) {
			if (funcId.equals(func.getId())) {
				foundFunction = func;
				break;
			} else if (func.getChildren() != null && !func.getChildren().isEmpty()) {
				foundFunction = findFunction(func.getChildren(), funcId);
				if (foundFunction != null) {
					break;
				}
			}
		}
		return foundFunction;
	}

	/**
	 * @param roleList
	 *            roleList.
	 * @param roleId
	 *            roleId.
	 * @return role is system dictionary.
	 */
	private Role findRole(List<Role> roleList, String roleId) {
		Role foundRole = null;
		for (Role role : roleList) {
			if (roleId.equals(role.getId())) {
				foundRole = role;
				break;
			}
		}
		return foundRole;
	}
}
