/**
 * 
 */
package com.ibm.gbsc.auth.web.user;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.ibm.gbsc.auth.resource.Resource;
import com.ibm.gbsc.auth.resource.ResourceService;
import com.ibm.gbsc.auth.resource.RoleResource;
import com.ibm.gbsc.auth.user.Role;
import com.ibm.gbsc.auth.user.UserService;

/**
 * @author fanjingxuan
 * 
 */
@Controller
@RequestMapping("/auth")
@SessionAttributes({ "roleList", "resList" })
public class RoleResourceContoller {
	Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	UserService userService;

	@Autowired
	ResourceService resourceService;

	/**
	 * @param model model.
	 * @return string.
	 */
	@RequestMapping(value = "/roleResource/roles", method = RequestMethod.GET)
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
		HashMap<String, Object> roleTree = new HashMap<String, Object>();
		roleTree.put("id", "root");
		roleTree.put("name", "角色列表");
		roleTree.put("open", true);
		roleTree.put("children", roleList);

		// StringBuffer sb = new StringBuffer();
		// sb.append("[{name:'角色列表',open:true,children:");
		// sb.append(bld.create().toJson(roleTree));
		// sb.append("}]");
		model.addAttribute("zTreeJson", bld.create().toJson(roleTree));
		model.addAttribute("roleList", roleList);
		return "/roleresource/roleList.jsp";
	}

	/**
	 * @param roleId roleId.
	 * @param operType operType.
	 * @param roleList roleList.
	 * @param model model.
	 * @return string.
	 */
	@RequestMapping(value = "/roleResource/roles/{roleId}", method = RequestMethod.GET)
	public String showRoleResource(@PathVariable String roleId, @RequestParam(required = false, defaultValue = "1") String operType,
			@ModelAttribute("roleList") List<Role> roleList, Model model) {
		List<Resource> resList = resourceService.getAllResource();
		Role role = findRole(roleList, roleId);
		List<RoleResource> roleResources = role.getRoleResList();
		ArrayList<String> haveResources = new ArrayList<String>();
		for (RoleResource roleResource : roleResources) {
			if (roleResource.getOperationType() == Integer.parseInt(operType)) {
				haveResources.add(roleResource.getResource().getResourceId());
			}
		}
		compareChecked(resList, haveResources);
		GsonBuilder bld = new GsonBuilder();
		bld.addSerializationExclusionStrategy(new ExclusionStrategy() {

			public boolean shouldSkipField(FieldAttributes f) {

				return !f.getName().equals("resourceId") && !f.getName().equals("checked") && !f.getName().equals("resourceName")
						&& !f.getName().equals("childRes");
			}

			public boolean shouldSkipClass(Class<?> clazz) {
				return false;
			}
		});

		bld.setFieldNamingStrategy(new FieldNamingStrategy() {

			public String translateName(Field f) {
				if (f.getName().equals("childRes")) {
					return "children";
				}
				if (f.getName().equals("resourceId")) {
					return "id";
				}
				if (f.getName().equals("resourceName")) {
					return "name";
				}
				return f.getName();
			}

		});
		model.addAttribute("zResTreeJson", bld.create().toJson(resList));
		model.addAttribute("resList", resList);

		model.addAttribute("role", role);
		return "/roleresource/resList.jsp";
	}

	/**
	 * @param roleId roleId.
	 * @param oldRoleId oldRoleId.
	 * @param resourceIds resourceIds.
	 * @param operType operType.
	 * @param roleList roleList.
	 * @param resList resList.
	 * @param model model.
	 * @return string.
	 */
	@RequestMapping(value = "/roleResource/cacheRoleRes", method = RequestMethod.POST)
	public String cacheRoleRes(@RequestParam String roleId, @RequestParam String oldRoleId, @RequestParam String resourceIds,
			@RequestParam String operType, @ModelAttribute("roleList") List<Role> roleList, @ModelAttribute("resList") List<Resource> resList,
			Model model) {
		Role oldRole = findRole(roleList, oldRoleId);

		if (oldRole != null) {
			List<RoleResource> roleResList = oldRole.getRoleResList();
			if (roleResList == null) {
				roleResList = new ArrayList<RoleResource>();
			}

			List<RoleResource> tempList = new ArrayList<RoleResource>();
			for (RoleResource roleRes : roleResList) {
				if (operType.equals(String.valueOf(roleRes.getOperationType()))) {
					tempList.add(roleRes);
				}
			}
			roleResList.removeAll(tempList);

			if (StringUtils.isNotBlank(resourceIds)) {
				String[] resArr = resourceIds.split(",");
				RoleResource roleRes = null;
				RoleResource tempRoleRes = null;
				int index = -1;
				for (String resourceId : resArr) {
					Resource res = this.findResource(resList, resourceId);
					if (res != null) {
						roleRes = new RoleResource();
						roleRes.setResource(res);
						roleRes.setRole(oldRole);
						roleRes.setOperationType(Integer.parseInt(operType));
						index = roleResList.indexOf(roleRes);
						if (index != -1) {
							tempRoleRes = roleResList.get(index);
							tempRoleRes.setOperationType(Integer.parseInt(operType));
						} else {
							roleResList.add(roleRes);
						}
					}
				}
			}
		}

		GsonBuilder bld = new GsonBuilder();
		bld.addSerializationExclusionStrategy(new ExclusionStrategy() {

			public boolean shouldSkipField(FieldAttributes f) {

				return !f.getName().equals("resourceId") && !f.getName().equals("resourceName") && !f.getName().equals("childRes");
			}

			public boolean shouldSkipClass(Class<?> clazz) {
				return false;
			}
		});

		bld.setFieldNamingStrategy(new FieldNamingStrategy() {

			public String translateName(Field f) {
				if (f.getName().equals("childRes")) {
					return "children";
				}
				if (f.getName().equals("resourceId")) {
					return "id";
				}
				if (f.getName().equals("resourceName")) {
					return "name";
				}
				return f.getName();
			}

		});

		model.addAttribute("zResTreeJson", bld.create().toJson(resList));
		Role currentRole = findRole(roleList, roleId);
		model.addAttribute("role", currentRole);
		return "/roleresource/resList.jsp";
	}

	/**
	 * @param roleId roleId.
	 * @param resourceIds resourceIds.
	 * @param oldOperType oldOperType.
	 * @param operType operType.
	 * @param roleList roleList.
	 * @param resList resList.
	 * @param model model.
	 */
	@RequestMapping(value = "/roleResource/cacheCurrentRole", method = RequestMethod.POST)
	@ResponseBody
	public void cacheCurrentRole(@RequestParam String roleId, @RequestParam String resourceIds, @RequestParam String oldOperType,
			@RequestParam String operType, @ModelAttribute("roleList") List<Role> roleList, @ModelAttribute("resList") List<Resource> resList,
			Model model) {
		// Role currentRole = findRole(roleList, roleId);
		//
		// if (currentRole != null) {
		// List<RoleResource> roleResList = currentRole.getRoleResList();
		// if (roleResList == null) {
		// roleResList = new ArrayList<RoleResource>();
		// }
		//
		// List<RoleResource> tempList = new ArrayList<RoleResource>();
		// for (RoleResource roleRes : roleResList) {
		// if (oldOperType.equals(String.valueOf(roleRes.getOperationType()))) {
		// tempList.add(roleRes);
		// }
		// }
		// roleResList.removeAll(tempList);

		// if (StringUtils.isNotBlank(resourceIds)) {
		// String[] resArr = resourceIds.split(",");
		// RoleResource roleRes = null;
		// RoleResource tempRoleRes = null;
		// int index = -1;
		// for (String resourceId : resArr) {
		// Resource res = this.findResource(resList, resourceId);
		// if (res != null) {
		// roleRes = new RoleResource();
		// roleRes.setResource(res);
		// roleRes.setRole(currentRole);
		// roleRes.setOperationType(Integer.parseInt(oldOperType));
		// index = roleResList.indexOf(roleRes);
		// if (index != -1) {
		// tempRoleRes = roleResList.get(index);
		// tempRoleRes.setOperationType(Integer.parseInt(oldOperType));
		// } else {
		// roleResList.add(roleRes);
		// }
		// }
		// }
		// }
		// }

	}

	/**
	 * @param roleId roleId.
	 * @param resourceIds resourceIds.
	 * @param operType operType.
	 * @param roleList roleList.
	 * @param resList resList.
	 * @param model model.
	 * @param status status.
	 * @return string.
	 */
	@RequestMapping(value = "/roleResource/savaRoleRes", method = RequestMethod.POST)
	public String savaRoleRes(@RequestParam String roleId, @RequestParam String resourceIds, @RequestParam String operType,
			@ModelAttribute("roleList") List<Role> roleList, @ModelAttribute("resList") List<Resource> resList, Model model, SessionStatus status) {
		Role currentRole = findRole(roleList, roleId);
		if (currentRole != null) {
			List<RoleResource> roleResList = currentRole.getRoleResList();
			if (roleResList == null) {
				roleResList = new ArrayList<RoleResource>();
			}

			List<RoleResource> tempList = new ArrayList<RoleResource>();
			for (RoleResource roleRes : roleResList) {
				if (operType.equals(String.valueOf(roleRes.getOperationType()))) {
					tempList.add(roleRes);
				}
			}
			roleResList.removeAll(tempList);

			if (StringUtils.isNotBlank(resourceIds)) {
				String[] resArr = resourceIds.split(",");
				RoleResource roleRes = null;
				RoleResource tempRoleRes = null;
				int index = -1;
				for (String resourceId : resArr) {
					Resource res = this.findResource(resList, resourceId);
					while (res != null) {
						roleRes = new RoleResource();
						roleRes.setResource(res);
						roleRes.setRole(currentRole);
						roleRes.setOperationType(Integer.parseInt(operType));
						index = roleResList.indexOf(roleRes);
						if (index != -1) {
							tempRoleRes = roleResList.get(index);
							tempRoleRes.setOperationType(Integer.parseInt(operType));
						} else {
							roleResList.add(roleRes);
						}
						res = res.getParentRes();
					}
				}
			}
		}
		// for (Role role : roleList) {
		// resourceService.delRoleResByRoleId(currentRole.getAuthority(),Integer.parseInt(operType));
		// }
		// for (Role role : roleList) {
		// initRoleResId(currentRole.getRoleResList());
		userService.updateRole(currentRole);
		// }
		status.setComplete();
		return "redirect:/auth/roleFunction/roleFuncRes.htm?fromWhere=roleRes";
	}

	/**
	 * @param allResList allResList.
	 * @param haveResources haveResources.
	 */
	private void compareChecked(List<Resource> allResList, ArrayList<String> haveResources) {
		for (Resource resource : allResList) {
			if (haveResources.contains(resource.getResourceId())) {
				resource.setChecked(true);
			} else {
				resource.setChecked(false);
			}
			if (resource.getChildRes() != null || !resource.getChildRes().isEmpty()) {
				compareChecked(resource.getChildRes(), haveResources);
			}
		}
	}

	/**
	 * @param resList resList.
	 * @param resourceId resourceId.
	 * @return resource is system dictionary.
	 */
	private Resource findResource(List<Resource> resList, String resourceId) {
		Resource theRes = null;
		for (Resource res : resList) {
			if (resourceId.equals(res.getResourceId())) {
				theRes = res;
				break;
			} else if (res.getChildRes() != null && !res.getChildRes().isEmpty()) {
				theRes = findResource(res.getChildRes(), resourceId);
				if (theRes != null) {
					break;
				}
			}
		}

		return theRes;
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
