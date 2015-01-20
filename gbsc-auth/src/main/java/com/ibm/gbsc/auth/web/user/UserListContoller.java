package com.ibm.gbsc.auth.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ibm.banking.framework.dto.PagedQueryResult;
import com.ibm.banking.framework.web.SearchListBaseController;
import com.ibm.gbsc.auth.user.User;
import com.ibm.gbsc.auth.user.UserPagedQueryParam;
import com.ibm.gbsc.auth.user.UserService;

/**
 * @author yangyz
 *
 */
@Controller
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
		// TODO Auto-generated method stub

	}

	@Override
	protected String getListViewName() {
		return "user.lookup.tile";
	}

	@Override
	protected String getTableId() {
		return "UserListTable";
	}
}
