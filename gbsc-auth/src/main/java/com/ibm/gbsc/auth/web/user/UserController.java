/**
 *
 */
package com.ibm.gbsc.auth.web.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.support.RequestContext;

import com.ibm.gbsc.auth.user.CurrentPasswdIncorrectException;
import com.ibm.gbsc.auth.user.LoginUser;
import com.ibm.gbsc.auth.user.User;
import com.ibm.gbsc.auth.user.UserNotFoundException;
import com.ibm.gbsc.auth.user.UserService;
import com.ibm.gbsc.auth.user.UserState;

/**
 * @author fanjingxuan
 */
// @Controller
@RequestMapping("/auth")
public class UserController {
	@Autowired
	UserService userService;
	@Autowired
	MessageSource messageSource;
	@Autowired(required = false)
	org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
	@Value("%{systemWideSalt}")
	String salt;

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
		model.addAttribute("userStates", getUserStatusMap(request));
		return "userManage.tile";
	}

	/**
	 * @param newUser
	 *            newUser.
	 * @param oldPwd
	 *            oldPwd.
	 * @param model
	 *            model.
	 * @param status
	 *            status.
	 * @return string.
	 */
	@RequestMapping(value = "/userManage/addOrUpdateUser", method = RequestMethod.POST)
	public String addOrUpdateUser(@ModelAttribute("newUser") User newUser, @RequestParam String oldPwd, Model model, SessionStatus status) {
		String newPass = newUser.getPassword();
		if (passwordEncoder != null && newPass != null) {
			if (!newPass.equals(oldPwd)) {
				newUser.setPassword(passwordEncoder.encodePassword(newPass, salt));
			}
		}
		userService.updateUser(newUser);
		status.setComplete();
		return "redirect:/auth/userManage/search.htm";
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
	@RequestMapping(value = "/userManage/checkUser/{userCode}", method = RequestMethod.GET)
	@ResponseBody
	public String addOrUpdateUser(@PathVariable String userCode, Model model, HttpServletRequest request) {
		try {
			userService.getUser(userCode);
		} catch (UserNotFoundException e) {
			return "false";
		}
		return "true";
	}

	// @RequestMapping(value = "/userManage/delUser", method =
	// RequestMethod.POST)
	// @ResponseBody
	// public String delUser(@RequestParam String userCode, Model model) {
	// userService.delUser(userCode);
	// return "success";
	// }

	/**
	 * @param userCode
	 *            userCode.
	 * @param model
	 *            model.
	 * @param request
	 *            request.
	 * @return string.
	 */
	@RequestMapping(value = "/userManage/update/{userCode}", method = RequestMethod.GET)
	public String toModUserPage(@PathVariable String userCode, Model model, HttpServletRequest request) {
		User newUser = userService.getUser(userCode);
		model.addAttribute("type", "update");
		model.addAttribute("userStates", getUserStatusMap(request));
		model.addAttribute("newUser", newUser);
		return "userManage.tile";
	}

	/**
	 * @param request
	 *            request.
	 * @return user status map
	 */
	private Map<UserState, String> getUserStatusMap(HttpServletRequest request) {
		HashMap<UserState, String> sm = new HashMap<UserState, String>();
		for (UserState status : UserState.values()) {
			sm.put(status,
			        messageSource.getMessage("user.UserState." + status, null, status.name(), new RequestContext(request).getLocale()));
		}
		return sm;
	}

	/**
	 * @return string.
	 */
	@RequestMapping(value = "/user/passwd", method = RequestMethod.GET)
	public String openChangePasswd() {
		return "/user/changePasswd.jsp";
	}

	/**
	 * @param oldPwd
	 *            oldPwd.
	 * @param newPwd
	 *            newPwd.
	 * @param model
	 *            model.
	 * @return string.
	 */
	@RequestMapping(value = "/user/passwd", method = RequestMethod.POST)
	public String changePasswd(@RequestParam String oldPwd, @RequestParam String newPwd, Model model) {
		// validate the old passwd
		LoginUser user = (LoginUser) SecurityUtil.getCurrentUser();
		try {
			if (passwordEncoder != null) {
				newPwd = passwordEncoder.encodePassword(newPwd, salt);
				oldPwd = passwordEncoder.encodePassword(oldPwd, salt);
			}
			userService.updateUserPassword(user.getUser().getCode(), newPwd, oldPwd);
		} catch (CurrentPasswdIncorrectException e) {
			model.addAttribute("message", "你输入的旧密码不符，请重新输入！");
			return "/user/changePasswd.jsp";
		}
		return "/user/changePasswdOK.jsp";
	}
}
