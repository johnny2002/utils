/**
 * 
 */
package com.ibm.gbsc.auth.web.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.ibm.gbsc.auth.user.Organization;
import com.ibm.gbsc.auth.user.User;
import com.ibm.gbsc.auth.user.UserService;

/**
 * @author fanjingxuan
 * 
 */
@Controller
@RequestMapping("/auth")
@SessionAttributes({ "rmUserSet", "theOrg", "userSet" })
public class OrgUserController {
	Logger log = LoggerFactory.getLogger(getClass());
	@Inject
	UserService userService;

	/**
	 * 访问时，加载所有的角色信息.
	 * 
	 * @param model model.
	 */
	@ModelAttribute
	protected void refData(Model model) {
		if (!model.containsAttribute("rmUserSet")) {
			Set<User> rmUserSet = new HashSet<User>();
			model.addAttribute("rmUserSet", rmUserSet);
		}
	}

	/**
	 * 展示当前选中的组织结构信息.
	 * 
	 * @param orgCode orgCode.
	 * @param model model.
	 * @return string.
	 */
	@RequestMapping(value = "/orguser/orgs/{orgCode}", method = RequestMethod.GET)
	public String showOrgUserDetail(@PathVariable String orgCode, Model model) {
		Organization theOrg = userService.getOrganization(orgCode);
		model.addAttribute("userSet", theOrg.getUsers());
		model.addAttribute("theOrg", theOrg);
		return "/orguser/orgUserResult.jsp";
	}
	/**
	 * 检测当前用户是否加入到哪个组织中.
	 * 
	 * @param userCode userCode.
	 * @param userSet userSet.
	 * @param model model.
	 * @return string.
	 */
	@RequestMapping(value = "/orguser/checkUser/{userCode}", method = RequestMethod.GET)
	@ResponseBody
	public String checkUserSet(@PathVariable String userCode, @ModelAttribute("userSet") Set<User> userSet, Model model) {
		User user = userService.getUser(userCode);
		if(null != user){
			if(user.getDepartments().size()>0){
				return "true";
			}else{
				return "false";
			}
		}else{
			return "error";
		}
	}
	/**
	 * 将选中的user放入userSet中.
	 * 
	 * @param userCode userCode.
	 * @param userSet userSet.
	 * @param model model.
	 * @return string.
	 */
	@RequestMapping(value = "/orguser/addUser/{userCode}", method = RequestMethod.GET)
	public String addToUserSet(@PathVariable String userCode, @ModelAttribute("userSet") Set<User> userSet, Model model) {
		User user = userService.getUser(userCode);
		if (null != user) {
			userSet.add(user);
		}
		return "/orguser/orgUserResult.jsp";
	}

	/**
	 * 在页面点击删除后，从userSet中删除用户.
	 * 
	 * @param userCode userCode.
	 * @param userSet userSet.
	 * @param model model.
	 * @param rmUserSet rmUserSet.
	 * @return string.
	 */
	@RequestMapping(value = "/orguser/delUser/{userCode}", method = RequestMethod.GET)
	public String delFromUserSet(@PathVariable String userCode, @ModelAttribute("userSet") Set<User> userSet,
			@ModelAttribute("rmUserSet") Set<User> rmUserSet, Model model) {
		User user = userService.getUser(userCode);

		if (null != user) {
			userSet.remove(user);
			rmUserSet.add(user);
		}

		return "/orguser/orgUserResult.jsp";
	}

	/**
	 * @param theOrg theOrg.
	 * @param userSet userSet.
	 * @param rmUserSet rmUserSet.
	 * @param status status.
	 * @return string.
	 */
	@RequestMapping(value = "/orguser/saveUser", method = RequestMethod.POST)
	public String saveUserChange(@ModelAttribute("theOrg") Organization theOrg, @ModelAttribute("userSet") Set<User> userSet,
			@ModelAttribute("rmUserSet") Set<User> rmUserSet, SessionStatus status) {
		List<Organization> depts = null;
		for (User user : userSet) {
			depts = user.getDepartments();
			if (!depts.contains(theOrg)) {
				depts.add(theOrg);
				userService.updateUser(user);
			}
		}

		for (User user : rmUserSet) {
			depts = user.getDepartments();
			if (depts.contains(theOrg)) {
				depts.remove(theOrg);
				userService.updateUser(user);
			}
		}
		status.setComplete();
		return "redirect:/auth/org/orgs.htm";
	}
}
