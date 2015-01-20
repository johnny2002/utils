/**
 * 
 */
package com.ibm.gbsc.auth.web.user;

import java.io.UnsupportedEncodingException;

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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.ibm.gbsc.auth.user.Role;
import com.ibm.gbsc.auth.user.User;
import com.ibm.gbsc.auth.user.UserService;

/**
 * @author cui xx
 * 
 */
@Controller
@RequestMapping("/auth")
@SessionAttributes({ "theUser" })
public class UserEditController {
	/**
	 * 
	 */
	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	UserService userService;

	/**
	 * 用户列表.
	 * 
	 * @param model
	 *            model
	 * @return page
	 */
	@RequestMapping(value = "/user/users", method = RequestMethod.GET)
	public String index(Model model) {
		// model.addAttribute("theUser", null);
		log.debug("/user/users");
		return "userDetail.tile";
		// return "/user/userEdit.jsp";
	}

	/**
	 * @param loadNew loadNew.
	 * @param model model.
	 * @param status status.
	 * @return string.
	 */
	@RequestMapping(value = "/user/userRole", method = RequestMethod.GET)
	public String userRole(@RequestParam(required = false) String loadNew, Model model, SessionStatus status) {
		if (StringUtils.isNotBlank(loadNew) && "true".equals(loadNew)) {
			model.addAttribute("theUser", new User());
			model.addAttribute("loadNew", true);
		}
		return "/user/userEdit.jsp";
	}

	/**
	 * @param code code.
	 * @param model model.
	 * @return string.
	 */
	@RequestMapping(value = "/user/users/{code}", method = RequestMethod.GET)
	public String showUserDetail(@PathVariable String code, Model model) {
		User user = userService.getUser(code);
		model.addAttribute("theUser", user);

		log.debug(user.getRoles().size() + "");

		for (Role r : user.getRoles()) {
			log.debug(r.getName());
		}
		// refData(model);

		// return "userDetail.tile";
		// return "/user/userEdit.jsp";
		return "redirect:/auth/orgrole/userOrgRole.htm?fromWhere=userRole";
	}

	/**
	 * @param model model.
	 */
	@ModelAttribute
	public void refData(Model model) {

		model.addAttribute("appRoles", userService.getAllRoles());

	}

	/**
	 * @param user user.
	 * @return string.
	 */
	@RequestMapping(value = "/user/saveuser", method = RequestMethod.POST)
	public String saveUserDetail(@ModelAttribute(value = "theUser") User user) {
		if (log.isDebugEnabled()) {
			log.debug("/user/saveuser");
			// log.debug(user.toString());
			// log.debug("/user/saveuser user roles size:"+user.getRoles().size()+"");
			// for(Role r : user.getRoles()){
			// log.debug(r.getName());
			// }
		}
		userService.updateUser(user);
		try {
			return "redirect:/auth/user/users/" + java.net.URLEncoder.encode(user.getCode(), "utf-8") + ".htm";
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
			return null;
		}
	}

}
