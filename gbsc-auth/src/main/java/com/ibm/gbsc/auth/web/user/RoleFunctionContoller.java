/**
 * 
 */
package com.ibm.gbsc.auth.web.user;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import com.ibm.gbsc.auth.function.Function;
import com.ibm.gbsc.auth.function.FunctionService;
import com.ibm.gbsc.auth.user.Role;
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
	@Autowired
	UserService userService;
	@Autowired
	FunctionService functionService;

	/**
	 * @param fromWhere fromWhere.
	 * @param model model.
	 * @return string.
	 */
	@RequestMapping(value = "/roleFunction/roleFuncRes", method = RequestMethod.GET)
	public String index(@RequestParam(required = false) String fromWhere, Model model) {
		model.addAttribute("fromWhere", fromWhere);
		return "roleFuncRes.tile";
	}

	/**
	 * @param model model.
	 * @return string.
	 */
	@RequestMapping(value = "/roleFunction/roles", method = RequestMethod.GET)
	public String showRoleAll(Model model) {

		List<Role> roleList = userService.getAllRoles();

		GsonBuilder bld = new GsonBuilder();
		bld.addSerializationExclusionStrategy(new ExclusionStrategy() {

			public boolean shouldSkipField(FieldAttributes f) {

				return !f.getName().equals("name") && !f.getName().equals("authority");
			}

			public boolean shouldSkipClass(Class<?> clazz) {
				return false;
			}
		});

		bld.setFieldNamingStrategy(new FieldNamingStrategy() {

			public String translateName(Field f) {
				if (f.getName().equals("authority")) {
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
	 * @param roleId roleId.
	 * @param roleName roleName.
	 * @param roleList roleList.
	 * @param model model.
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
		return role.getAuthority();
	}

	/**
	 * @param roleId roleId.
	 * @param model model.
	 * @return string.
	 */
	@RequestMapping(value = "/roleFunction/checkRole", method = RequestMethod.POST)
	@ResponseBody
	public String checkRole(@RequestParam String roleId, Model model) {

		Role role = userService.getRole(roleId);
		if (role == null) {
			return "allow";
		}
		if (userService.haveRelationWithOrg(roleId)) {
			return "relationWithOrg";
		}
		if (userService.haveRelationWithUser(roleId)) {
			return "relationWithUser";
		}
		return "allow";
	}

	/**
	 * @param roleId roleId.
	 * @param roleList roleList.
	 * @param model model.
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
	 * @param allFuncList allFuncList.
	 * @param haveFunctions haveFunctions.
	 */
	private void compareChecked(List<Function> allFuncList, ArrayList<String> haveFunctions) {
		for (Function function : allFuncList) {
			if (haveFunctions.contains(function.getId())) {
				function.setChecked(true);
			} else {
				function.setChecked(false);
			}
			if (function.getChildren() != null || !function.getChildren().isEmpty()) {
				compareChecked(function.getChildren(), haveFunctions);
			}
		}
	}

	/**
	 * @param roleId roleId.
	 * @param roleList roleList.
	 * @param model model.
	 * @return string.
	 */
	@RequestMapping(value = "/roleFunction/roles/{roleId}", method = RequestMethod.GET)
	public String showRoleFunction(@PathVariable String roleId, @ModelAttribute("roleList") List<Role> roleList, Model model) {
		List<Function> functionList = functionService.getFunctionTree();
		Role role = findRole(roleList, roleId);
		List<Function> functions = role.getFunctions();
		ArrayList<String> haveFunctions = new ArrayList<String>();
		for (Function function : functions) {
			haveFunctions.add(function.getId());
		}
		compareChecked(functionList, haveFunctions);

		GsonBuilder bld = new GsonBuilder();
		bld.addSerializationExclusionStrategy(new ExclusionStrategy() {

			public boolean shouldSkipField(FieldAttributes f) {

				return !f.getName().equals("name") && !f.getName().equals("id") && !f.getName().equals("checked") && !f.getName().equals("children");
			}

			public boolean shouldSkipClass(Class<?> clazz) {
				return false;
			}
		});
		model.addAttribute("zFuncTreeJson", bld.create().toJson(functionList));
		model.addAttribute("functionList", functionList);
		model.addAttribute("role", role);
		return "/rolefunction/functionList.jsp";
	}

	/**
	 * @param roleId roleId.
	 * @param oldRoleId oldRoleId.
	 * @param functionIds functionIdsc.
	 * @param roleList roleList.
	 * @param functionList functionList.
	 * @param model model.
	 * @return string.
	 */
	@RequestMapping(value = "/roleFunction/cacheRole", method = RequestMethod.POST)
	public String cacheRole(@RequestParam String roleId, @RequestParam String oldRoleId, @RequestParam String functionIds,
			@ModelAttribute("roleList") List<Role> roleList, @ModelAttribute("functionList") List<Function> functionList, Model model) {
		Role oldRole = findRole(roleList, oldRoleId);

		if (oldRole != null) {
			List<Function> roleFunc = oldRole.getFunctions();
			if (roleFunc == null) {
				roleFunc = new ArrayList<Function>();
			}
			roleFunc.clear();
			String[] funcArr = functionIds.split(",");
			for (String functionId : funcArr) {
				Function func = this.findFunction(functionList, functionId);
				if (func != null && !roleFunc.contains(func)) {
					roleFunc.add(func);
				}
			}
		}

		GsonBuilder bld = new GsonBuilder();
		bld.addSerializationExclusionStrategy(new ExclusionStrategy() {

			public boolean shouldSkipField(FieldAttributes f) {

				return !f.getName().equals("name") && !f.getName().equals("id") && !f.getName().equals("children");
			}

			public boolean shouldSkipClass(Class<?> clazz) {
				return false;
			}
		});

		model.addAttribute("zFuncTreeJson", bld.create().toJson(functionList));
		model.addAttribute("functionList", functionList);
		Role currentRole = findRole(roleList, roleId);
		model.addAttribute("role", currentRole);
		return "/rolefunction/functionList.jsp";
	}

	/**
	 * @param roleId roleId.
	 * @param functionIds functionIds.
	 * @param roleList roleList.
	 * @param functionList functionList.
	 * @param model model.
	 * @param status status.
	 * @return string.
	 */
	@RequestMapping(value = "/roleFunction/savaAllRole", method = RequestMethod.POST)
	public String savaAllRole(@RequestParam String roleId, @RequestParam String functionIds, @ModelAttribute("roleList") List<Role> roleList,
			@ModelAttribute("functionList") List<Function> functionList, Model model, SessionStatus status) {
		Role currentRole = findRole(roleList, roleId);
		if (currentRole != null) {
			// TODO: 业务逻辑应该移到后台Service中去
			List<Function> roleFunc = currentRole.getFunctions();
			if (roleFunc == null) {
				roleFunc = new ArrayList<Function>();
			}
			roleFunc.clear();
			String[] funcArr = functionIds.split(",");
			for (String functionId : funcArr) {
				Function func = this.findFunction(functionList, functionId);
				while (func != null) {
					if (!roleFunc.contains(func)) {
						roleFunc.add(func);
					}
					func = func.getParent();
				}
			}
		}
		// for (Role role : roleList) {
		userService.updateRole(currentRole);
		// }
		status.setComplete();
		return "redirect:/auth/roleFunction/roleFuncRes.htm";
	}

	/**
	 * @param functionList functionList.
	 * @param funcId funcId.
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
	 * @param roleList roleList.
	 * @param roleId roleId.
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
