/**
 *
 */
package com.ibm.gbsc.auth.web.user;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.gbsc.auth.user.User;
import com.ibm.gbsc.auth.user.UserService;

/**
 * @author fanjingxuan
 */
@Controller
@RequestMapping("/auth")
public class UserController {
	Logger log = LoggerFactory.getLogger(getClass());
	@Inject
	UserService userService;
	@Inject
	MessageSource messageSource;

	@RequestMapping(value = "/users/{userCode}", method = RequestMethod.GET)
	public String getUserDetail(@PathVariable String userCode, Model model, HttpServletRequest request) {
		User user = userService.getUser(userCode);
		model.addAttribute("theUser", user);
		return "/auth/user/userdetail.ftl";
	}

	/**
	 * @param userCode
	 *            userCode.
	 * @param model
	 *            model.
	 * @param request
	 *            request.
	 * @return string.
	 */
	@RequestMapping(value = "/users/{userCode}", method = RequestMethod.PUT)
	@ResponseBody
	public String addOrUpdateUser(@PathVariable String userCode, @ModelAttribute User theUser, Model model, HttpServletRequest request) {
		log.debug("Save user: {}", request.getParameter("code"));
		userService.updateUser(theUser);
		return messageSource.getMessage("auth.user.savedOK", new Object[] { userCode }, "User Saved OK", null);
	}

	/**
	 * 打开new user page.
	 *
	 * @param model
	 *            model
	 * @param request
	 *            request.
	 * @return string.
	 */
	@RequestMapping(value = "/userManage/toAddUserPage", method = RequestMethod.GET)
	public String toAddUserPage(Model model, HttpServletRequest request) {
		User newUser = new User();
		model.addAttribute("type", "add");
		model.addAttribute("newUser", newUser);
		return "userManage.tile";
	}

	/**
	 * @return string.
	 */
	@RequestMapping(value = "/user/passwd", method = RequestMethod.GET)
	public String openChangePasswd() {
		return "/user/changePasswd.jsp";
	}

}
