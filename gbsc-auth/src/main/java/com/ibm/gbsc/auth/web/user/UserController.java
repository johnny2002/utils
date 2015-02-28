/**
 *
 */
package com.ibm.gbsc.auth.web.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import org.springframework.web.servlet.ModelAndView;

import com.ibm.gbsc.auth.resource.Role;
import com.ibm.gbsc.auth.user.User;
import com.ibm.gbsc.auth.user.UserService;
import com.ibm.gbsc.auth.user.UserState;
import com.ibm.gbsc.web.springmvc.view.GsonView;

/**
 * @author fanjingxuan
 */
@Controller
@RequestMapping("/auth/users")
public class UserController {
	Logger log = LoggerFactory.getLogger(getClass());
	@Inject
	UserService userService;
	@Inject
	MessageSource messageSource;

	@RequestMapping(value = "/{userCode}", method = RequestMethod.GET)
	public String getUserDetail(@PathVariable String userCode, Model model, HttpServletRequest request) {
		User user;
		if ("new".equals(userCode)) {
			user = new User();
			model.addAttribute("_mode", "new");
		} else {
			user = userService.getUser(userCode);
		}
		model.addAttribute("theUser", user);
		refData(model);
		return "/auth/user/userdetail.ftl";
	}

	@RequestMapping(value = "/{userCode}", method = { RequestMethod.PUT, RequestMethod.POST })
	public ModelAndView updateUser(@PathVariable String userCode, @ModelAttribute("theUser") @Valid User theUser, BindingResult bResult,
	        Model model, HttpServletRequest request) {
		log.debug("Save user: {}", userCode);
		if (bResult.hasErrors()) {
			refData(model);
			return new ModelAndView("/auth/user/userdetail.ftl");
		}
		GsonView vw = new GsonView();
		if ("POST".equalsIgnoreCase(request.getMethod())) {
			userService.saveUser(theUser);
			vw.addAttribute("url", theUser.getCode() + ".htm");
		} else {
			userService.updateUser(theUser);
		}
		vw.addAttribute("message", messageSource.getMessage("auth.user.savedOK", new Object[] { theUser.getCode() }, null));

		return new ModelAndView(vw);
	}

	void refData(Model model) {
		HashMap<String, String> userStatesMap = new HashMap<String, String>();
		for (UserState sts : UserState.values()) {
			userStatesMap.put(sts.name(), messageSource.getMessage(UserState.class.getName() + "." + sts.name(), null, null));
		}
		model.addAttribute("UserStates", userStatesMap);
		List<Role> roles = userService.getAllRoles();
		Map<String, String> rolesMap = new TreeMap<String, String>();
		for (Role role : roles) {
			rolesMap.put(role.getCode(), role.getName());
		}
		model.addAttribute("roles", rolesMap);
	}

}
