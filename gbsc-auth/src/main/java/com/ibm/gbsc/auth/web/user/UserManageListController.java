/**
 *
 */
package com.ibm.gbsc.auth.web.user;

import javax.inject.Inject;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ibm.gbsc.auth.user.User;
import com.ibm.gbsc.auth.user.UserPagedQueryParam;
import com.ibm.gbsc.auth.user.UserService;
import com.ibm.gbsc.common.vo.PagedQueryResult;
import com.ibm.gbsc.web.springmvc.controller.SearchListBaseController;

/**
 * @author fanjingxuan
 *
 */
// @Controller
@RequestMapping("/auth/userManage")
public class UserManageListController extends SearchListBaseController<User, UserPagedQueryParam> {
	@Inject
	UserService userService;

	@Override
	protected String getListViewName() {
		return "userList.tile";
	}

	@Override
	protected void refData(Model arg0) {

	}

	@Override
	protected PagedQueryResult<User> searchData(UserPagedQueryParam queryParam) {

		return userService.searchUser(queryParam);
	}

	@Override
	protected String getTableId() {
		return "userListTable";
	}
}
