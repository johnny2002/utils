package com.ibm.gbsc.auth.web.user;

import org.springframework.beans.factory.annotation.Autowired;
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
// @Controller
@RequestMapping("/auth/user")
public class UserListContoller extends SearchListBaseController<User, UserPagedQueryParam> {

	@Autowired
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
		return "user.lookup.ftl";
	}

	@Override
	protected String getTableId() {
		return "UserListTable";
	}
}
