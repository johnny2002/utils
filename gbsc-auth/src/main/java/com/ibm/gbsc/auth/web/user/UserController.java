/**
 *
 */
package com.ibm.gbsc.auth.web.user;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.gbsc.auth.user.User;
import com.ibm.gbsc.auth.user.UserService;
import com.ibm.gbsc.auth.user.UserState;

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
		if ("new".equals(userCode)) {
			return toAddUserPage(model, request);
		}
		User user = userService.getUser(userCode);
		model.addAttribute("theUser", user);
		return "/auth/user/userdetail.ftl";
	}

	@RequestMapping(value = "/users/{userCode}", method = { RequestMethod.PUT, RequestMethod.POST })
	public String updateUser(@PathVariable String userCode, @ModelAttribute("theUser") @Valid User theUser, BindingResult bResult,
	        Model model, HttpServletRequest request) {
		log.debug("Save user: {}", userCode);
		if (bResult.hasErrors()) {
			// model.addAttribute("theUser", theUser);
			return "/auth/user/userdetail.ftl";
		}
		if ("POST".equalsIgnoreCase(request.getMethod())) {
			userService.saveUser(theUser);
		} else {
			userService.updateUser(theUser);
		}
		model.addAttribute("MSG_KEY", "auth.user.savedOK");
		model.addAttribute("MSG_PARAM", new Object[] { userCode });

		return "/auth/json-message.ftl";
	}

	void refData(Model model) {
		model.addAttribute("UserStates", UserState.values());
		model.addAttribute("");
	}

	@ResponseBody
	public String addUser(@PathVariable String userCode, @ModelAttribute User theUser, Model model, HttpServletRequest request) {
		log.debug("Save user: {}", userCode);

		userService.updateUser(theUser);
		return messageSource.getMessage("auth.user.savedOK", new Object[] { userCode }, "User Saved OK", null);
	}

	String toAddUserPage(Model model, HttpServletRequest request) {
		User newUser = new User();
		model.addAttribute("_mode", "add");
		model.addAttribute("theUser", newUser);
		return "/auth/user/userdetail.ftl";
	}

	/**
	 * @return string.
	 */
	@RequestMapping(value = "/user/passwd", method = RequestMethod.GET)
	public String openChangePasswd() {
		return "/user/changePasswd.jsp";
	}

}
