package com.ibm.gbsc.auth.web.user;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ibm.gbsc.auth.user.User;
import com.ibm.gbsc.auth.user.UserPagedQueryParam;
import com.ibm.gbsc.auth.user.UserService;
import com.ibm.gbsc.common.vo.PagedQueryResult;
import com.ibm.gbsc.web.springmvc.controller.SearchListBaseController;

/**
 * @author yangyz
 *
 */
@Controller
@RequestMapping("/auth/usersearch")
public class UserSearchContoller extends SearchListBaseController<User, UserPagedQueryParam> {

	@Inject
	UserService userService;

	@Override
	protected PagedQueryResult<User> searchData(UserPagedQueryParam queryParam) {

		return userService.searchUser(queryParam);
	}

	@Override
	protected void refData(Model model) {

	}

	@Override
	protected String getListViewName() {
		return "/auth/user/userSearchList.ftl";
	}

	@Override
	protected String getTableId() {
		return "tUser";
	}
}